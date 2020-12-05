const sql = require('mssql');
const config = {
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    server: process.env.DB_HOST || "localhost",
    database: process.env.DB_DATABASE
};

const poolPromise = new sql.ConnectionPool(config)
    .connect()
    .then(pool => {
        console.log("Connected to MSSQL.");
        return pool;
    }).catch(err => console.log('Database Connection Failed! Bad Config: ', err));

module.exports = {
    sql, poolPromise
};