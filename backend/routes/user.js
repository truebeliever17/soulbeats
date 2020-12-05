const express = require('express');
const router = express.Router();
const {sql, poolPromise} = require("../connection/db");
router.get('/', async (req, res) => {
    try {
        const pool = await poolPromise;
        const result = await pool
            .request()
            .query('select * from "user"', function(err, userset) {
                if (err) {
                    console.log(err);
                } else {
                    let data = userset.recordset;
                    res.json(data);
                }
            });
    } catch(err) {
        res.status(500);
        res.send(err.message);
    }
});

module.exports = router;