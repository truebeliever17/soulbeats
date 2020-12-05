require("dotenv").config();
const express = require("express");

const app = express();

app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header(
        "Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept"
    );
    res.header(
        "Access-Control-Allow-Methods",
        "PUT, POST, GET, DELETE, OPTIONS"
    );
    next();
});

app.use(express.json());

app.get("/", (req, res) => {
    res.json({
        message: "✌",
    });
});

const server = app.listen(process.env.PORT || 5000, function () {
    const port = server.address().port;
    console.log("Server is running on port", port);
});

const users = require("./routes/user");
app.use("/users", users);

const albums = require("./routes/album");
app.use("/albums", albums);
