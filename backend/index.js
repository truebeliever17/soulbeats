const express = require('express');
const bodyParser = require('body-parser');

require('dotenv').config();

const app = express();

app.use(function (req, res, next) {  
    res.header("Access-Control-Allow-Origin", "*");  
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");  
    res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');  
    next();
});

app.use(bodyParser.urlencoded({extended: true}));

app.get('/', (req, res) => {
    res.json({
        'message': '✌'
    });
});

const server = app.listen(process.env.PORT || 5000, function() {
    const port = server.address().port;
    console.log("Server is running on port", port);
});


