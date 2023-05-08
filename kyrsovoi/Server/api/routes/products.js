const express = require('express')
const router = express.Router()

// Deal with file
const fileSystem = require('fs');

// Upload and store images
const multer = require('multer')

const storage = multer.diskStorage({
    // Place of picture
    destination: (request, file, callback) => {
        callback(null, 'storage_product/');
    },
    filename: (request, file, callback) => {
        const avatarName = Date.now() + file.originalname;
        callback(null, avatarName);
    }
});

const uploadImage = multer({
    storage: storage,
});


// import file
const database = require("../../config")
const checkAuth = require('../../middleware/check_auth');

router.get("/all", (request, response) => {
  var page = request.query.page;
  var page_size = request.query.page_size;

  if (page == null || page < 1) {
    page = 1;
  }

  if (page_size == null) {
    page_size = 20;
  }

  // OFFSET starts from zero
  page = page - 1;
  // OFFSET * LIMIT
  page = page * page_size;

  const args = [parseInt(page_size), parseInt(page)];

  const query = "SELECT * FROM product LIMIT ? OFFSET ?";
  database.query(query, args, (error, result) => {
    if (error) throw error;

      let tableRows = "";
      result.forEach((product) => {
        tableRows += `
          <tr>
            <td>${product.id}</td>
            <td>${product.product_name}</td>
            <td>${product.quantity}</td>
            <td>${product.category}</td>
            <td>${product.price}</td>
            <td>
              <form method="POST" action="/products/deletes/${product.id}" >
                <button type="submit">Delete</button>
              </form>
            </td>
          </tr>
        `;
      });

      const html = `
        <html>
          <head>
            <title>All Products</title>
            <style>
              table {
                border-collapse: collapse;
                width: 100%;
              }
              th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
              }
              th {
                background-color: #f2f2f2;
              }
            </style>
          </head>
          <body>
          <form action="/products/addnew" method="POST">
          <input type="text" name="productName" placeholder="Название продукта"/>
          <input type="text" name="productPrice" placeholder="Цена продукта"/>
          <input type="text" name="productQuantity" placeholder="Количество продукта"/>  
          <input type="text" name="productSupplier" placeholder="Поставщик продукта"/>
          <input type="text" name="productCategory" placeholder="Категория продукта"/>
          <input type="text" name="productImage" placeholder="Изображение продукта"/>
          <button type="submit">Добавить продукт</button>
        </form>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Имя</th>
                  <th>Количество</th>
                  <th>Категория</th>
                  <th>Цена</th>
                </tr>
              </thead>
              <tbody>
                ${tableRows}
              </tbody>
            </table>
           
          </body>
        </html>
      `;

      response.status(200).send(html);
  });
});

router.post("/deletes/:id", (request, response) => {
  const productId = request.params.id;

  const query = "DELETE FROM product WHERE id = ?";

  database.query(query, [productId], (error, result) => {
    if (error) throw error;
    response.send("Product deleted successfully.");
  });
});

router.post("/addnew", (request, response) => {
  const productName = request.body.productName;
  const productPrice = request.body.productPrice;
  const productQuantity = request.body.productQuantity;
  const productSupplier = request.body.productSupplier;
  const productCategory = request.body.productCategory;
  const productImage = request.body.productImage;

  const values = [productName, productPrice, productQuantity, productSupplier, productCategory, productImage];
  const query = "INSERT INTO product(product_name, price, quantity, supplier, category, image) VALUES(?, ?, ?, ?, ?,?)"
  database.query(query, values, (error, result) =>{
    if (error) throw error;
    response.redirect("/products/all");
  });
});




// Get products by category
router.get("/", (request, response) => {
    const user_id = request.query.userId;
    const category = request.query.category
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
    page = offset * page_size; // 20

    const args = [
        user_id,
        user_id,
        category,
        parseInt(page_size),
        parseInt(page)
    ];

    //const query = "SELECT * FROM product WHERE category = ? LIMIT ? OFFSET ?";
    const query = `SELECT product.id,
    product.product_name,
    product.price,
    product.quantity,
    product.supplier,
    product.image,
    product.category,
    (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM favorite WHERE favorite.user_id = ? AND favorite.product_id = product.id) as isFavourite,
    (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM cart WHERE cart.user_id = ? AND cart.product_id = product.id) as isInCart
    FROM product 
    WHERE category = ? 
    LIMIT ? OFFSET ?`;

    database.query(query, args, (error, result) => {
        if(error) throw error
        response.status(200).json({
            "page": offset + 1,  //2
            "error" : false,
            "products" : result
        })
    });
}); 

// Search for products
router.get("/search", (request, response) => {
    const user_id = request.query.userId;
    const keyword = request.query.q.toLowerCase();
    var page = request.query.page;
    var page_size = request.query.page_size;

    if(page == null || page < 1){
        page = 1;
    }
 
    if(page_size == null){
        page_size = 20;
    }

    // OFFSET starts from zero
    page = page - 1;
    // OFFSET * LIMIT
    page = page * page_size;

    const searchQuery = '%' + keyword + '%';

    const args = [
        user_id,
        user_id,
        searchQuery,
        searchQuery,
        parseInt(page_size),
        parseInt(page)
    ];

    //const query = "SELECT * FROM product WHERE product_name LIKE ? OR category LIKE ? LIMIT ? OFFSET ?";

    const query = `SELECT product.id,
    product.product_name,
    product.price,
    product.quantity,
    product.supplier,
    product.image,
    product.category,
    (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM favorite WHERE favorite.user_id = ? AND favorite.product_id = product.id) as isFavourite,
    (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM cart WHERE cart.user_id = ? AND cart.product_id = product.id) as isInCart
    FROM product 
    WHERE product_name LIKE ? OR category LIKE ?
    LIMIT ? OFFSET ?`;


    database.query(query, args, (error, result) => {
        if(error) throw error
        response.status(200).json({
            "page": page + 1,
            "error" : false,
            "products" : result
        })
    });
}); 

// Insert Product

router.get('/insert', (req, res) => {
  res.send(`
  <form method="POST" action="/products/insert" enctype="multipart/form-data">
    <label>Id:</label>
    <input type="number" name="id"><br><br>
    <label>Name:</label>
    <input type="text" name="name"><br><br>
    <label>Price:</label>
    <input type="number" name="price"><br><br>
    <label>Quantity:</label>
    <input type="number" name="quantity"><br><br>
    <label>Supplier:</label>
    <input type="text" name="supplier"><br><br>
    <label for="text">Category:</label>
    <select name="category">
      <option value="measuring">measuring</option>
      <option value="metalCutting">metalCutting</option>
      <option value="woodworking">woodworking</option>
      <option value="household">household</option>
    </select><br><br>
    <label for="text">Profile Image:</label>
    <input type="file" name="image"><br><br>
    <button type="submit"  >Add</button>
  </form>
`);
}); 

router.post("/insert", checkAuth, uploadImage.single('image'), (request, response) => {
  const name = request.body.name
  const price = request.body.price
  const quantity = request.body.quantity
  const supplier = request.body.supplier
  const category = request.body.category
  
  const file = request.file;
  var filePath = ""
  if(file != null){
      filePath = file.path
  }
 
  const query = "INSERT INTO product(product_name, price, quantity, supplier, category, image) VALUES(?, ?, ?, ?, ?,?)"
      
  const args = [name, price, quantity, supplier, category, filePath]

      database.query(query, args, (error, result) => {
          if (error) throw error
          response.status(200).send("Product Inserted")
      });
}); 

// Delete Product
router.delete("/:id", (request, response) => {
    const id = request.params.id;
    const query = "DELETE FROM product WHERE id = ?"
    const args = [id]

    database.query(query, args, (error, result) => {
        if(error) throw error
        response.status(200).send("Product is deleted")
    });
});

// Update image of product
router.put("/update", uploadImage.single('image'), (request, response) => {
    const id = request.body.id;
    
    const file = request.file;
    var filePath = ""
    if(file != null){
        filePath = file.path
    }

    const selectQuery = "SELECT image FROM product WHERE id = ?"
    database.query(selectQuery, id, (error, result) => {

        console.log(result)
        if(error) throw error
        try {
            // Get value from key image
            var image = result[0]['image'];
            // Delete old image 
            fileSystem.unlinkSync(image);
        } catch (err) {
            console.error("Can't find file in storage/pictures Path");
        }
    });

    const query = "UPDATE product SET image = ? WHERE id = ?"  
    
    const args = [filePath,id]

    database.query(query, args, (error, result) => {
        if(error) throw error

        if(result['affectedRows']  == 1){
            response.status(200).send("Product Image is updated")
        }else{
            response.status(500).send("Invalid Update")
        }
    });

});

module.exports = router