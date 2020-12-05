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

module.exports = router;
