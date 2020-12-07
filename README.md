# Soulbeats

Soulbeats is a music streaming platform, just like Spotify, Apple Music. It is a demo project for a DBMS course, so the project is quite raw. The main goal was to connect the database to the user interface.

P.S. If you are going to use the project's code for production, please note that there are security problems. For example, with the `GET /users` query, you can get data from all users, even if you are not on the system and do not have administrator rights.

## 🛠 Tech stack

🧱 **TL;DR: MS SQL Server, Node.js, Android (Java)**

The project uses as few and as simple things as possible:

- [Node.js](https://nodejs.org/) – Server-side, API
    - [express](https://expressjs.com/) – Network stuff
    - [mssql](https://www.npmjs.com/package/mssql) – Connect and manage database
    - [express-validator](https://express-validator.github.io/docs/) – Validate fields
    - [dotenv](https://github.com/motdotla/dotenv) – Store and load environment variables
    - [jsonwebtoken](https://www.npmjs.com/package/jsonwebtoken) – Tokens

- [Android](https://developer.android.com/) – Client-side, Mobile
    - [Retrofit](https://square.github.io/retrofit/) – Make requests
    - [Material Design](https://material.io/develop/android) – UI components

- [vas3k.club](https://github.com/vas3k/vas3k.club) – Markdown Template

## 🔮 Installation

> In progress...
