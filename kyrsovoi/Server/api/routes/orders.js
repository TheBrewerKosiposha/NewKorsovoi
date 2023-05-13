const express = require('express')
const router = express.Router()
const bodyParser = require("body-parser");
// import file
const database = require("../../config")

const util = require('../../utils/mail');
const util2 = require('../../utils/encrypt');

// Order a product
router.post("/add", (request, response) => {
    var status = request.body.status
    const name_on_card = request.body.name_on_card
    var card_number = request.body.card_number
    const expiration_date = request.body.expiration_date
    const userId = request.body.userId
    const productId = request.body.productId
    var order_number;

    card_number = util2.encrypt(card_number)
     
    const queryCategory = 'SELECT category FROM product WHERE id = ?'
    database.query(queryCategory,productId, (error, result) => {
        if(error) throw error;

        result = result[0]["category"]
       
        if(result == "measuring"){
            order_number = '55' + util.getRandomInt(100000, 999999)
        }else if(result == "metalCutting"){
            order_number = '66' + util.getRandomInt(100000, 999999)
        }else if(result == "woodworking"){
            order_number = '77' + util.getRandomInt(100000, 999999)
        }else if(result == "household"){
            order_number = '88' + util.getRandomInt(100000, 999999)
        }

        if(typeof status == 'undefined' && status == null){
            status = "shipped";
        }
   
        const query = "INSERT INTO Ordering(order_number, order_date ,status,name_on_card, card_number,expiration_date,user_id, product_id) VALUES(?,NOW(),?,?,?,?,?,?)"
        const args = [order_number,status, name_on_card, card_number, expiration_date, userId, productId]

        database.query(query, args, (error, result) => {
            if (error) {
                if (error.code === 'ER_DUP_ENTRY') {
                    response.status(500).send("Deplicate Entry")
                } else {
                    throw error;
                }
            } else {
                response.status(200).send("You ordered a product")
            }
        });

        
    })
});

router.get("/allrouter", (request, response) => {
  const query = "SELECT * FROM bd.ordering";
  database.query(query, (error, result) => {
    if (error) {
      console.log(error);
      response.sendStatus(500); // internal server error
    } else {
      let results = `
      <form method="GET" action="/">
      <td>
        <button type="submit">Назад</button>
      </td>
    </form>
        <form method="POST" action="/orders/addOrder">
          <td><input type="text" name="orderNumber"></td>
          <td><input type="date" name="orderDate"></td>
          <td><input type="text" name="status"></td>
          <td><input type="text" name="nameOnCard"></td>
          <td><input type="text" name="cardNumber"></td>
          <td><input type="date" name="expirationDate"></td>
          <td><input type="number" name="userId"></td>
          <td><input type="number" name="productId"></td>
          <td>
            <button type="submit" name="addOrder">Add Order</button>
          </td>
        </form>
        <table>
          <thead>
            <tr>
              <th>Ordering ID</th>
              <th>Order Number</th>
              <th>Order Date</th>
              <th>Status</th>
              <th>Name on Card</th>
              <th>Card Number</th>
              <th>Expiration Date</th>
              <th>User ID</th>
              <th>Product ID</th>
              <th>Delete</th>
              <th>Change Status</th> // добавили новый столбец
            </tr>
          </thead>
          <tbody>`;
      result.forEach((order) => {
        results += `
          <tr>
            <td>${order.ordering_id}</td>
            <td>${order.order_number}</td>
            <td>${order.order_date}</td>
            <td>${order.status}</td>
            <td>${order.name_on_card}</td>
            <td>${order.card_number}</td>
            <td>${order.expiration_date}</td>
            <td>${order.user_id}</td>
            <td>${order.product_id}</td>
            <td>
              <form method="POST" action="/orders/deleteOrder">
                <button type="submit" name="orderId" value="${order.ordering_id}">Delete</button> 
              </form>
            </td>
            <td>
              <form method="POST" action="/orders/changeStatus">
                <input type="hidden" name="orderId" value="${order.ordering_id}">
                <label for="status">status: ${order.status}</label>
                 <select name="status">
                  <option value="В пути">В пути</option>
                  <option value="Прибыл">Прибыл</option>
                  <option value="Собираем">Собираем</option>
             </select>
             <br><br>
                <button type="submit" name="changeStatus">Save</button>
              </form>
            </td>
          </tr>`;
      });
      results += `
          </tbody>
        </table>`;
      
      response.send(results);
    }
  });
});
  
  router.post("/deleteOrder", bodyParser.urlencoded({ extended: false }), (request, response) => {
    const orderId = request.body.orderId; // fix: should be request.body.orderId instead of request.body.ordering_id
    const query = `DELETE FROM bd.ordering WHERE ordering_id = '${orderId}'`;
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.sendStatus(500); // internal server error
      } else {
        console.log(`Order with ID ${orderId} deleted`);
        response.redirect("/orders/allrouter"); // перенаправляем на страницу, где отображаются все заказы
      }
    });
  });
  router.post("/changeStatus", (request, response) => {
    const orderId = request.body.orderId;
    const newStatus = request.body.status;
    const query = `UPDATE bd.ordering SET status = '${newStatus}' WHERE ordering_id = ${orderId}`;
    
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.sendStatus(500); // internal server error
      } else {
        response.send(`
        <h1>Status update.</h1>
        <ul>
            <li><a href="/orders/allrouter">Назад</a></li>
        </ul>
    `);
      }
    });
  });

  router.post("/addOrder", (request, response) => {
    const {
      orderNumber,
      orderDate,
      status,
      nameOnCard,
      cardNumber,
      expirationDate,
      userId,
      productId
    } = request.body;
  
    const query = `INSERT INTO bd.ordering (order_number, order_date, status, name_on_card, card_number, expiration_date, user_id, product_id) VALUES ('${orderNumber}', '${orderDate}', '${status}', '${nameOnCard}', '${cardNumber}', '${expirationDate}', '${userId}', '${productId}');`;
  
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.sendStatus(500); // internal server error
      } else {
        console.log('New order added to the database:', result);
        response.redirect("/orders/allrouter");
      }
    });
  });


router.get("/", (request, response) => {
    const productId = request.body.id

    var order_number;

    const queryCategory = 'SELECT category FROM product WHERE id = ?'
    database.query(queryCategory,productId, (error, result) => {
        if(error) throw error;

        result = result[0]["category"]

        console.log(result)
       
        if(result === "measuring"){
            console.log('hello')
            order_number = 55 + getRandomInt(100000, 999999)
        }else if(result == "metalCutting"){
            order_number = 66 + getRandomInt(100000, 999999)
        }else if(result == "woodworking"){
            order_number = 77 + getRandomInt(100000, 999999)
        }else if(result == "household"){
            order_number = 88 + getRandomInt(100000, 999999)
        }

        response.status(200).json({
            "category" : result
        })
    });

    function getRandomInt(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min; 
    }
});




// Get Orders
router.get("/get", (request, response) => {
    var userId = request.query.userId;
    var page = request.query.page;
    var page_size = request.query.page_size;

    if(page == null || page < 1){
        page = 1;
    }
 
    if(page_size == null){
        page_size = 20;
    }

    // OFFSET starts from zero
    const offset = page - 1;
    // OFFSET * LIMIT
    page = offset * page_size;

    const args = [
        userId,
        parseInt(page_size),
        parseInt(page)
    ];

    const query = `SELECT DISTINCT Ordering.order_number,
                          DATE_FORMAT(Ordering.order_date, '%d/%m/%Y') As order_date, 
                          Ordering.status,Product.product_name,
                          Product.price,
                          Product.id,
                          User.name,
                          Shipping.address
                          FROM Ordering JOIN Product JOIN User JOIN Shipping 
                          ON Ordering.product_id = product.id AND Ordering.user_id = user.id AND Ordering.product_id = Shipping.product_id
                          WHERE Ordering.user_id = ? 
                          LIMIT ? OFFSET ?`

    database.query(query, args, (error, orders) => {
        if(error) throw error;
        response.status(200).json({
            "page": offset + 1,
            "error" : false,
            "orders" : orders
        })
       
    })
});

module.exports = router