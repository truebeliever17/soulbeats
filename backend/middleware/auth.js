const jwt = require("jsonwebtoken");

module.exports = function (req, res, next) {
    const token = req.header("token");
    if (!token)
        return res.status(401).json({ message: "Authorization required!" });

    try {
        const decoded = jwt.verify(
            token,
            process.env.SECRET_KEY || "something"
        );
        req.user_id = decoded.user_id;
        next();
    } catch (e) {
        console.error(e);
        res.status(500).send({ message: "Invalid Token" });
    }
};
