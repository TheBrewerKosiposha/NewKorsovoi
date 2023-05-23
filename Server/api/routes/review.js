const express = require('express')
const router = express.Router()

// import file
const database = require("../../config")

router.get("/", (request, response) => {
    var productId = request.query.productId;
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
        productId,
        parseInt(page_size),
        parseInt(page)
    ];

    const query = "SELECT user.name, DATE_FORMAT(review.review_date, '%d/%m/%Y') As date,review.rate, review.feedback FROM Review JOIN Product JOIN User ON review.product_id = product.id AND review.user_id = user.id WHERE product_id = ? LIMIT ? OFFSET ?"
    database.query(query, args, (error, reviews) => {
        if(error) throw error;

        const avgQuery = 'SELECT AVG(rate) AS averageRate FROM review WHERE product_id = ?'

        database.query(avgQuery, productId, (err, avrg) => {
            if(err) throw err;
            response.status(200).json({
                "page": offset + 1,
                "error" : false,
                "avrg_review" : avrg[0]['averageRate'],
                "review" : reviews
            })
        });
    })
});

router.get("/full", (request, response) => {
    const query = "SELECT * FROM review";
    database.query(query, (error, results) => {
      if (error) {
        console.log(error);
        response.status(500).send("Internal Server Error");
      } else {
        const table = `
        <li><a href="/">Назад</a></li>
          <table>
            <thead>
              <tr>
                <th>Review ID</th>
                <th>User ID</th>
                <th>Product ID</th>
                <th>Feedback</th>
                <th>Rate</th>
                <th>Review Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              ${results.map(result => {
                return `
                  <tr>
                    <td>${result.review_id}</td>
                    <td>${result.user_id}</td>
                    <td>${result.product_id}</td>
                    <td>${result.feedback}</td>
                    <td>${result.rate}</td>
                    <td>${result.review_date}</td>
                    <td>
                      <form method="post" action="/review/delete">
                        <input type="hidden" name="review_id" value="${result.review_id}">
                        <button type="submit">Delete</button>
                      </form>
                      <form method="get" action="/review/edit">
                      <input type="hidden" name="review_id" value="${result.review_id}">
                      <input type="hidden" name="role" value="editor">
                      <button type="submit">Edit</button>
                    </form>
                  </td>
                </tr>
                <tr id="edit-${result.review_id}" style="display:none">
                  <form method="post" action="/review/edit">
                    <td>${result.review_id}</td>
                    <td>${result.user_id}</td>
                    <td>${result.product_id}</td>
                    <td><input type="text" name="feedback" value="${result.feedback}"></td>
                    <td><input type="number" name="rate" value="${result.rate}"></td>
                    <td>${result.review_date}</td>
                    <td>
                      <input type="hidden" name="review_id" value="${result.review_id}">
                      <button type="submit">Save</button>
                      <button type="button" onclick="hideEditForm(${result.review_id})">Cancel</button>
                    </td>
                  </form>
                    </td>
                  </tr>
                `;
              }).join("")}
            </tbody>
          </table>
        `;
        response.send(table);
      }
    });
  });
  
  router.post("/delete", (request, response) => {
    const review_id = request.body.review_id;
    const query = `DELETE FROM review WHERE review_id=${review_id}`;
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.status(500).send("Internal Server Error");
      } else {
        response.redirect("/review//full");
      }
    });
  });
  

  router.post("/edit", (request, response) => {
    const { review_id, feedback, rate } = request.body;
    const query = `UPDATE review SET feedback='${feedback}', rate=${rate} WHERE review_id=${review_id}`;
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.status(500).send("Internal Server Error");
      } else {
        response.redirect("/review/full");
      }
    });
  });
  
  router.get("/edit", (request, response) => {
    const { review_id } = request.query;
    const query = `SELECT * FROM review WHERE review_id=${review_id}`;
    database.query(query, (error, result) => {
      if (error) {
        console.log(error);
        response.status(500).send("Internal Server Error");
      } else {
        const editForm = `
        <li><a href="/review/full">Назад</a></li>
          <script>
            function hideEditForm(review_id) {
              document.getElementById("edit-" + review_id).style.display = "none";
            }
          </script>
          <h1>Edit Review</h1>
          <form method="post" action="/review/edit">
            <input type="hidden" name="review_id" value="${result[0].review_id}">
            <label>Feedback:</label>
            <input type="text" name="feedback" value="${result[0].feedback}">
            <br>
            <label>Rate:</label>
            <input type="number" name="rate" value="${result[0].rate}">
            <br>
            <button type="submit">Save</button>
            <button type="button" onclick="/review/full">Cancel</button>
          </form>`
        ;
        response.send(editForm);
      }
    });
  });


// Add Review about Product
router.post("/add", (request, response) => {
    const userId = request.body.userId
    const productId = request.body.productId
    const feedback = request.body.feedback
    const rate = request.body.rate

    const query = "INSERT INTO review(user_Id, product_Id, feedback, rate ,review_date) VALUES(?, ?, ?, ?,NOW())"
   
    const args = [userId, productId, feedback,rate]

    database.query(query, args, (error, result) => {
        if (error) {
            if (error.code === 'ER_DUP_ENTRY') {
                response.status(500).send("Deplicate Entry")
            } else {
                throw error;
            }
        } else {
            response.status(200).send("Review is Added")
        }
    });
});


module.exports = router