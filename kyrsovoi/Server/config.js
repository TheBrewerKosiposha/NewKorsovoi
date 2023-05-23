const mysql = require('mysql2');

const connectionPool = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: '1234567890',
    database: 'bd',
})

module.exports = connectionPool;

