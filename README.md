# E-Wallet-Service:
The service helps in creating a user with e-wallet with some default balance, add money to the wallet, send money to other users, maintain transaction details and send an email notification whenever a transaction happens.

# Prerequisites:
- Java 1.8 or above
- Kafka server
- Redis server
- MySQL server

# Installation:
- Clone this repo:
```bash
> gh repo clone sanjay235/E-Wallet-Service
> cd E-Wallet-Service
```
- Install Maven, MySQL(any Database of your choice), IntelliJ(any IDE of your choice).
- Create database tables provide in the `resources/sql` folder.
- Update the dependencies given in the `pom.xml` using Maven.
- Run it as spring boot application in the IDE.

# Sample API end points:
```
Create user will automatically create an e-wallet,
End point = localhost:7000/user
HTTP method = POST
JSON body = {
    "userId": "sanjay35",
    "emailId": "sanjayxyz@gmail.com",
    "name": "Sanjay",
    "age": 24
}
```
```
Get user details with given userId,
End point = localhost:7000/user?userId=Sanjay123
HTTP method = GET
```
```
Add money to the user's wallet,
End point = localhost:5000/add-money
HTTP method = PUT
JSON body = {
    "receiver": "sanjay35",
    "amount": 23
}
```
```
Send money to other user's wallet,
End point = localhost:5000/send-money
HTTP method = PUT
JSON body = {
    "sender": "sanjay35",
    "receiver": "nikki23",
    "amount": 35,
    "purpose": "This is a festiv gift!"
}
```

# Contribute:
* Feel free to fork, add a star and share if you like it.
* Corrections & Contributions are always welcome! 😃
