const express = require("express");
const { check, validationResult } = require("express-validator");
const auth = require("../middleware/auth");
const router = express.Router();
const { sql, poolPromise } = require("../connection/db");

router.get("/", async (req, res) => {
    try {
        const pool = await poolPromise;
        await pool
            .request()
            .query("select * from album", function (err, response) {
                if (err) {
                    console.log(err);
                    return res.status(500).json({
                        message: "Some internal error happened",
                    });
                }
                res.status(200).json(response.recordset);
            });
    } catch (err) {
        console.log(err);
        res.status(500).json({ message: "Some internal error happened" });
    }
});

router.post("/", async (req, res) => {
    const { show_only_main_info } = req.body;
    const query = show_only_main_info
        ? `select album.image_id, album.album_name, "user".display_name from album join "user" on album.artist_id = "user".user_id where is_private = 0`
        : `select * from album`;

    try {
        const pool = await poolPromise;
        await pool.request().query(query, function (err, response) {
            if (err) {
                console.log(err);
                return res.status(500).json({
                    message: "Some internal error happened",
                });
            }
            res.status(200).json(response.recordset);
        });
    } catch (err) {
        console.log(err);
        res.status(500).json({ message: "Some internal error happened" });
    }
});

router.get("/my", auth, async (req, res) => {
    try {
        const pool = await poolPromise;
        const userId = req.user_id;
        await pool
            .request()
            .query(
                `select album.image_id, album.album_name, "user".display_name from album join "user" on album.artist_id = "user".user_id where is_private = 0 and album.artist_id = ${userId}`,
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

router.post(
    "/create",
    [
        auth,
        check("album_name", "Please enter a valid album name").not().isEmpty(),
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({
                errors: errors.array(),
            });
        }

        let { album_name, image_id, description, is_private } = req.body;
        if (description != null) {
            description = `'${description}'`;
        }

        const user_id = req.user_id;

        try {
            const pool = await poolPromise;
            await pool.request().query(
                `exec createalbum @album_name='${album_name}', 
                                        @artist_id=${user_id}, 
                                        @image_id=${image_id}, 
                                        @description=${description}, 
                                        @is_private=${is_private}`,
                function (err, response) {
                    if (err) {
                        console.log(err);
                        return res.status(500).json({
                            message: "Some internal error happened",
                        });
                    }
                    res.status(200).json({
                        message: "Album successfully created!",
                    });
                }
            );
        } catch (err) {
            res.status(500).json({ message: "Some internal error happened" });
        }
    }
);

module.exports = router;
