# Online Bookstore Management System

## Framework Settings

- Java 17
- Spring Boot 2.7.18
- Maven
- H2 database
- Favorite IDE (STS, Eclipse, Intellij)

## How To Run

- Run the project through the following command lines

```
mvn spring-boot:run
```

- The application is configured to run on port 9000. If you want to change it, please go to application.properties file
  in the project and change property "server.port" to the suitable port number
    - To open API documentation, please hit in the browser this url : http://localhost:9000/swagger-ui/index.html
- H2 database will be used for saving books , users and requests for booking
- To open H2 database :
    - Go to the browser and hit the url : http://localhost:9000/h2-console
    - Then connect using the default configuration in the browser
    - You can use the editor to run any query to know what are the current instruments in the system

## API Definition

- There are admin APIs and user APIs
    - For admin APIs :
        - [GET] /admins/books : retrieve all available books in the system
        - [GET] /admins/users : get all users in the system
        - [POST] /admins/books : create new book in the system with request body object, example :
            - {
              "name":"a book for dummies",
              "authorName" :"dummy author",
              "price" :10,
              "numberOfDaysForBorrow":1,
              "inStock" : 1,
              "category": "Category dummy"
              }
        - [PUT] /admins/books/{bookId} : update the book details
            - bookId : path parameter with value book id
            - request body object contain all new book details to be updated, example :
                - {
                  "name": "a book for dummies",
                  "authorName": "dummy author",
                  "price": 10,
                  "numberOfDaysForBorrow": 3,
                  "inStock": 1,
                  "category": "Category dummy"
                  }
      - [GET] /admins/requests : get all requests to borrow books in the system. This api will help to know the request
        id to be used in approve/reject api
      - [PUT] /admins/requests/{requestId}/approve : approve book request to allow user to borrow the book
        - [PUT] /admins/requests/{requestId}/reject : reject book request initiated from a user
        - [DELETE] /admins/books/{bookId} : delete the provided book id in the path url
    - For Users APIs:
        - [GET] /users/{userId}/books : retrieve all books in the system
            - userId : is to track the user browsing history to be used in the suggested books api
        - [GET] /users/{userId}/books/categories : retrieve all books in the system grouped by categories
            - userId : is to track the user browsing history to be used in the suggested books api
        - [GET] : /users/{userId}/books/suggest : retrieve all suggested books to the provided user id according to the
          frequency of checking books
            - for example : if we have book retrieved by user apis 3 times and other book retrieved 2 times, then this
              api will return the book with 3 times first then the book with 2 times
        - [GET] /users/{userId}/books/{bookId} : retrieve more book details about the provided book id for the provided
          user id
        - [POST] /user : register new user to the system with request body object, example :
            - {
              "name":"User",
              "password" :"123"
              }
        - [POST] /users/{userId}/books/{bookId}/borrow : request to borrow the provided book id for the provided user id
        - [PUT] /users/{userId}/books/{bookId}/return : return back the borrowed book id for the provided user id
    - Attached in the repo postman collection "Online Bookstore.postman_collection.json" contain all needed apis

## Use cases

- If you want to borrow a book, you can hit api "/users/1/books/1/borrow", it will generate a book request with
  id 1 (you can run api "/admins/requests" to know all current requests)
    - If there is no "in-stock" value for this book, user cannot request to borrow it
- Then you can run the admin api for approving "/admins/requests/1/approve" or rejecting "
  /admins/requests/1/reject"
- If you approve the request, the user can borrow the book then the "in-stock" property for this book will be
  decreased by one
- When the user hit return api "/users/1/books/1/return", the book "in-stock" property will be increased by one
- Admin can add/update/delete books by apis
    - For delete, admin cannot delete book that is already borrowed. It needs to be returned to allow user to
      delete it

## Database Structure

- You can find database tables in file src/main/resources/schema.sql
- There are initial data in the system : 2 books and 2 users

## Unit Tests

- I just added unit test for service classes only as they only contain the main logic.
- For Controller classes, there is no extra logic to be tested. So I neglect to add tests for controller methods

## Assumptions

- Stock-level in the book, is configured as normal number with no business cases or validations as it was not clear for
  me from the document
- In-stock property in the book, it will have a maximum value in the system "1" as I understood that there is only one
  copy for any book in the system
    - I put the maximum value as environment variable in application.properties with key "max.number.of.book.copies", if
      you want to adjust it you can modify this value
- For the return date for any book, I provide a property called "numberOfDaysForBorrow" which decide the allowed number
  of days to borrow the book
    - If the user returned the book after this configured number of days, we will return different message to the user
      to inform him to pay more fees for delay
- The suggested books api, depend on how many times the user hit any api that retrieve details about any book.
    - Book details apis : "/users/{userId}/books" , "/users/{userId}/books/categories" , "
      /users/{userId}/books/{bookId} "
    - When book retrieved, I increment the browsing history field in table "user_browsing_history" to track how many
      times a book is reviewed by a user
    - Then we return suggested books ordered by browsing history descending