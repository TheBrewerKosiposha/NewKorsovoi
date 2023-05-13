const mysql = require('mysql2');

const connectionPool = mysql.createPool({
    connectionLimit: 10,
    host: '192.168.116.170',
    user: 'root',
    password: '1234567890',
    database: 'bd',
})

module.exports = connectionPool;

//192.168.0.118;192.168.1.66