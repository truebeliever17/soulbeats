const express = require("express");
const { check, validationResult } = require("express-validator/check");
const auth = require("../middleware/auth");
const jwt = require("jsonwebtoken");
const router = express.Router();
const { sql, poolPromise } = require("../connection/db");

router.get("/", async (req, res) => {
    try {
        const pool = await poolPromise;
        const result = await pool
            .request()
            .query('select * from "user"', function (err, response) {
                if (err) {
                    console.log(err);
                    return res.status(500).json({
                        message: "Some internal error happened",
                    });
                }
                res.response(200).json(response.recordset);
            });
    } catch (err) {
        res.status(500);
        console.log(err.message);
        res.send("Some internal error happened");
    }
});

router.post(
    "/signup",
    [
        check("username", "Please enter a valid username").not().isEmpty(),
        check("display_name", "Please enter a valid name").not().isEmpty(),
        check("email", "Please enter a valid email").isEmail(),
        check("password", "Please enter a valid password").isLength({
            min: 1,
        }),
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({
                errors: errors.array(),
            });
        }

        const { username, display_name, email, password } = req.body;
        try {
            const pool = await poolPromise;
            await pool
                .request()
                .query(
                    `exec finduser @username='${username}', @email='${email}'`,
                    function (err, queryRes) {
                        if (err) {
                            console.log(err);
                            return res.status(500).json({
                                message: "Some internal error happened",
                            });
                        }
                        if (queryRes.recordset.length == 0) {
                            pool.request().query(
                                `exec registeruser @username='${username}', @display_name='${display_name}', @email='${email}', @password='${password}'`,
                                function (err, response) {
                                    if (err) {
                                        console.log(err);
                                        return res.status(500).json({
                                            message:
                                                "Some internal error happened",
                                        });
                                    }
                                    const payload = response.recordset[0];
                                    jwt.sign(
                                        payload,
                                        process.env.SECRET_KEY || "something",
                                        {
                                            expiresIn: "30d",
                                        },
                                        (err, token) => {
                                            if (err) {
                                                console.log(err);
                                                return res.status(500).json({
                                                    message:
                                                        "Some internal error happened",
                                                });
                                            }
                                            res.status(200).json({
                                                token,
                                                payload,
                                                message:
                                                    "User successfully added!",
                                            });
                                        }
                                    );
                                }
                            );
                        } else {
                            return res.status(400).json({
                                message: "Username or email is already taken",
                            });
                        }
                    }
                );
        } catch (err) {
            res.status(500);
            console.log(err.message);
            res.send("Some internal error happened");
        }
    }
);

router.post(
    "/login",
    [
        check("username", "Please enter a valid username").not().isEmpty(),
        check("password", "Please enter a valid password").isLength({
            min: 1,
        }),
    ],
    async (req, res) => {
        const errors = validationResult(req);

        if (!errors.isEmpty()) {
            return res.status(400).json({
                errors: errors.array(),
            });
        }

        const { username, password } = req.body;
        try {
            const pool = await poolPromise;
            await pool
                .request()
                .query(
                    `exec signin @username=${username}, @password=${password}`,
                    function (err, response) {
                        if (err) {
                            console.log(err);
                            return res.status(500).json({
                                message: "Some internal error happened",
                            });
                        }
                        if (response.recordset.length == 0) {
                            return res.status(400).json({
                                message: "User or password is incorrect",
                            });
                        }

                        const payload = response.recordset[0];
                        jwt.sign(
                            payload,
                            process.env.SECRET_KEY || "something",
                            {
                                expiresIn: "30d",
                            },
                            (err, token) => {
                                if (err) {
                                    console.log(err);
                                    return res.status(500).json({
                                        message: "Some internal error happened",
                                    });
                                }
                                res.status(200).json({
                                    token,
                                    payload,
                                    message: "Logged in successfully",
                                });
                            }
                        );
                    }
                );
        } catch (err) {
            console.log(err.message);
            res.status(500).send("Some internal error happened");
        }
    }
);

router.get("/me", auth, async (req, res) => {
    try {
        const pool = await poolPromise;
        const userId = req.user_id;
        await pool
            .request()
            .query(
                `exec finduserbyid @user_id = ${userId}`,
                function (err, response) {
                    if (err) {
                        console.log(err);
                        return res.status(500).json({
                            message: "Some internal error happened",
                        });
                    }
                    if (response.recordset.length == 0) {
                        return res.status(400).json({
                            message: "Token not found",
                        });
                    }
                    res.status(200).json(response.recordset);
                }
            );
    } catch (err) {
        console.log(err.message);
        res.status(500).send("Some internal error happened");
    }
});

module.exports = router;
