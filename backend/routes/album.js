const express = require("express");
const { check, validationResult } = require("express-validator/check");
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
