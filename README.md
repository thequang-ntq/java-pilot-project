# Java Pilot Project

<div style="display: flex;">
  <div style="flex: 1;">
    <a href="https://github.com/thequang-ntq">My Github</a>
  </div>
</div>

## Table of Contents

- [Java Pilot Project](#java-pilot-project)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
    - [Database](#database)
    - [Backend](#backend)
    - [Frontend](#frontend)
  - [Technology \& Knowledge](#technology--knowledge)
  - [API testing with Postman](#api-testing-with-postman)
    - [Base URL](#base-url)
    - [Postman Test Flow](#postman-test-flow)
      - [Step 1 - Run project and verify DB connection](#step-1---run-project-and-verify-db-connection)
      - [Step 2 - Test Register](#step-2---test-register)
      - [Step 3 - Test Login and get accessToken + refreshToken](#step-3---test-login-and-get-accesstoken--refreshtoken)
      - [Step 4 - Refresh and get a new accessToken](#step-4---refresh-and-get-a-new-accesstoken)
      - [Step 5 - Logout and remove both tokens (requires Access Token)](#step-5---logout-and-remove-both-tokens-requires-access-token)
      - [Step 6 - Configure Authorization in Postman](#step-6---configure-authorization-in-postman)
      - [Step 7 - Test Public APIs (no token required)](#step-7---test-public-apis-no-token-required)
      - [Step 8 - Test Brand CRUD (Admin token required)](#step-8---test-brand-crud-admin-token-required)
      - [Step 9 - Test Product CRUD (Admin token required)](#step-9---test-product-crud-admin-token-required)
      - [Step 10 - Test Cart APIs (User token required)](#step-10---test-cart-apis-user-token-required)
      - [Step 11 - Test Order APIs (User token required)](#step-11---test-order-apis-user-token-required)
      - [Step 12 - Test Admin Order APIs (Admin token required)](#step-12---test-admin-order-apis-admin-token-required)
      - [Step 13 - Test login by Google Account](#step-13---test-login-by-google-account)
    - [Import Postman Collection](#import-postman-collection)
  - [Works](#works)
    - [2026-04-24](#2026-04-24)
    - [2026-04-25](#2026-04-25)
    - [2026-04-27](#2026-04-27)
    - [2026-04-28](#2026-04-28)
    - [2026-04-29](#2026-04-29)
    - [2026-04-30](#2026-04-30)
    - [2026-05-01](#2026-05-01)
    - [2026-05-02](#2026-05-02)
    - [2026-05-03](#2026-05-03)
    - [2026-05-04](#2026-05-04)
    - [2026-05-05](#2026-05-05)
    - [2026-05-06](#2026-05-06)
    - [2026-05-07](#2026-05-07)
  - [Fix Bugs](#fix-bugs)
    - [2026-05-02](#2026-05-02-1)
    - [2026-05-04](#2026-05-04-1)
    - [2026-05-06](#2026-05-06-1)
  - [Time Tracking](#time-tracking)
  - [Future Work](#future-work)

## Features

Fullstack Website

### Database

MySql: pilot_project_db (CHARSET utf8mb4, COLLATE utf8mb4_0900_ai_ci, (accent-insensitive, case-insensitive))

- Tables:
  - account
  - brand
  - product
  - order
  - order_detail
  - refresh_token
- Indexes:
  - account:
    - idx_account_account_name
  - brand:
    - idx_brand_brand_name
    - idx_brand_is_deleted
  - product:
    - idx_product_product_name
    - idx_product_brand_id
    - idx_product_price
    - idx_product_is_deleted
  - order:
    - idx_order_account_id
    - idx_order_status
    - idx_order_order_time
    - idx_order_finish_time
  - refresh_token:
    - idx_refresh_token_account_id
    - idx_refresh_token_token

### Backend

Java Spring Boot (IDE: Intellij): pilot-project-backend (Spring Web, Spring Data JPA, MySQL Driver, Spring Security, Spring Boot DevTools, Lombok, Validation, Maven). Use:

- entity/: Map DB tables to java object
  - AccountEntity
  - BrandEntity
  - OrderDetailEntity
  - OrderDetailId
  - OrderEntity
  - ProductEntity
  - RefreshTokenEntity
- dao(repository)/: communicate/react with DB (CRUD, query)
  - AccountDao
  - BrandDao
  - OrderDao
  - OrderDetailDao
  - ProductDao
  - RefreshTokenDao
- common/: constants variables and common functions
  - constants/Constants
  - util/:
    - CommonUtil
    - FileHelper
- model(DTO)/: object transmit data between Controller and Service
  - request/: DTO object to server from client (login, register, add/update brand, product)
    - LoginRequest
    - RegisterRequest
    - BrandRequest
    - ProductRequest
    - RefreshTokenRequest
  - response/: DTO object from server to client-side
    - LoginResponse
    - BrandResponse
    - ProductResponse
    - OrderResponse
    - OrderDetailResponse
    - PageResponse
    - ResponseDataModel
- security/: JWT Token auth, ADMIN/USER role, Custom User Detail
  - CustomUserDetailsService
  - JwtTokenProvider
  - JWTAuthenticationFilter
  - OAuth2FailureHandler
  - OAuth2SuccessHandler
  - SecurityConfig
- config/: CORS, MainApplication, ServletInitializer, ExceptionHandler
  - ServletInitializer
  - WebConfig
  - PilotProjectBackendApplication
- service/: Handle business logic, method related redirect to controller / client
  - impl/: implements methods
    - AccountServiceImpl
    - BrandServiceImpl
    - OrderServiceImpl
    - ProductServiceImpl
  - AccountService
  - BrandService
  - OrderService
  - ProductService
- controller/: get request from React, return Response
  - AuthController
  - BrandController
  - ProductController
  - OrderController
- exception/: Handle exception at Controller
  - GlobalExceptionHandler: exception handler for error when mapping request <-> object in Controller, after Spring Security.

### Frontend

React (IDE: VS Code): pilot-project-frontend

- App.css
- App.jsx
- index.css
- main.jsx
  - components/
    - common/
    - layout/
      - Header/
        - Header.jsx
        - Header.css
      - MainLayout/
        - MainLayout.jsx
        - MainLayout.css
  - pages/:
    - brands/
    - cart/
    - history/
    - home/
    - login/
    - logout/
    - not-found/
    - orders/
    - products/
    - register/
  - services/:
    - auth-api
    - auth_storage
    - axios-instance
    - brands-api
    - orders-api
    - products-api

## Technology & Knowledge

- Frontend:
  - JS: Named/Default export, Function, OOP, Array, Destructuring, Spread Operator, Array map() method
  - React: JSX, Component, Fragment, Props, Props.children, Truthy/Falsy values, useState, useEffect, Forwarding Props, Default Props, One-Way Binding / Two-Way Binding, Import CSS, CSS Module, Styled Components, useRef, Axios (axios instance, interceptors, handling error, loading), Routing (Routes, Route, Link, NavLink, Nested Routing)
- Backend:
  - CORS Policy (urlPattern: URL API that FE can connect, for example: /a --> all child URL after /a can access - /a/b, /a/c...)
  - MVC
  - Pagination, LIMIT A OFFSET B.
  - Spring Boot
  - BCrypt for Hash password
  - JWT Token for authorization
- Naming Convention:
  - project name: kebab-case
  - MySQL:
    - database name: snake_case, for example: contact_management
    - table names: snake_case, for example: foo_bar
    - constraint name: [type of key]_[table 1's name that have key]_[table 2 name that table 1 references to]
    - column names: snake_case
    - SELECT, FROM,... clause: CONSTANT_CASE
    - Always have ";" at the end of a statement.
  - Java:
    - Classes: PascalCase, a noun
    - Interfaces: PascalCase, capabilities / behaviors
    - Function: camelCase, a verb
    - Variable: camelCase, short
    - Constant variable: CONSTANT_CASE, all UPPERCASE
    - Package: all lowercase, should be just 1 word, and chaining, for example: com.ntq.demo (chaining packages).
    - Controller files: PascalCase, end with Controller
    - Filter files: PascalCase, end with Filter (example: CORSFilter)
  - React:
    - Component name in code, export: PascalCase;
    - JSX file name: PascalCase
    - JS File: kebab-case
    - State variable: start with is, has, should, for example: isActive, setIsActive ([is + name, set + Is + name])
    - Event handler functions: use "handle" prefix.
    - CSS classes (className,...): kebab-case
    - Constants: CONSTANT_CASE
    - Utils functions: camelCase
  - URL request parameter convention: kebab-case
- Commit conventions: -https://viblo.asia/p/dat-ten-commit-message-sao-cho-tinh-nghia-anh-em-chac-chan-ben-lau-OeVKBM605kW

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

## API testing with Postman

### Base URL

http://localhost:8080/api

### Postman Test Flow

#### Step 1 - Run project and verify DB connection

Run `PilotProjectBackendApplication`. If the console has no error, it is OK:

`Started PilotProjectBackendApplication in 3.xxx seconds`

#### Step 2 - Test Register

`POST http://localhost:8080/api/auth/register`

Body -> raw -> JSON:

```json
{
  "accountName": "testuser",
  "password": "123456",
  "confirmPassword": "123456"
}
```

Expected result:

```json
{
  "responseCode": 200,
  "responseMsg": "Register successful",
  "data": null
}
```

#### Step 3 - Test Login and get accessToken + refreshToken

`POST http://localhost:8080/api/auth/login`

Body -> raw -> JSON:

```json
{
  "accountName": "testuser",
  "password": "123456"
}
```

Expected result:

```json
{
  "responseCode": 200,
  "responseMsg": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "...",
    "accountName": "testuser",
    "role": "USER"
  }
}
```

Copy the token values for the next steps.

#### Step 4 - Refresh and get a new accessToken

`POST http://localhost:8080/api/auth/refresh`

Body -> raw -> JSON:

```json
{
  "refreshToken": "<refreshToken>"
}
```

Expected result:

```json
{
  "responseCode": 200,
  "responseMsg": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "...",
    "accountName": "testuser",
    "role": "USER"
  }
}
```

#### Step 5 - Logout and remove both tokens (requires Access Token)

`POST http://localhost:8080/api/auth/logout`

Header:

`Authorization: Bearer <accessToken>`

Expected result:

```json
{
  "responseCode": 200,
  "responseMsg": "Logout successful"
}
```

#### Step 6 - Configure Authorization in Postman

There are 2 ways:

- Per request:
  - Authorization tab
  - Type: Bearer Token
  - Token: paste your token
- By Collection (recommended):
  - Create collection `Pilot Project`
  - Collection -> Authorization -> Bearer Token
  - All requests in that collection inherit the token

#### Step 7 - Test Public APIs (no token required)

- `GET http://localhost:8080/api/brands?page=1&keyword=`
- `GET http://localhost:8080/api/brands/1`
- `GET http://localhost:8080/api/products?page=1&keyword=&priceFrom=&priceTo=`
- `GET http://localhost:8080/api/products/1`

#### Step 8 - Test Brand CRUD (Admin token required)

Login with an admin account first and get admin token.

- Add brand:
  - `POST http://localhost:8080/api/admin/brands`
  - Body -> form-data:
    - `brandName`: Apple Test
    - `description`: Test description
    - `logoFiles`: choose image file
- Update brand:
  - `PUT http://localhost:8080/api/admin/brands`
  - Body -> form-data:
    - `brandId`: 1
    - `brandName`: Apple Updated
    - `description`: Updated description
- Delete brand:
  - `DELETE http://localhost:8080/api/admin/brands/1`

#### Step 9 - Test Product CRUD (Admin token required)

- Add product:
  - `POST http://localhost:8080/api/admin/products`
  - Body -> form-data:
    - `productName`: iPhone Test
    - `quantity`: 100
    - `price`: 25000000
    - `brandId`: 1
    - `saleDate`: 2026-04-29
    - `description`: Test product
    - `imageFiles`: choose image file
- Update product:
  - `PUT http://localhost:8080/api/admin/products`
  - Body -> form-data:
    - `productId`: 1
    - `productName`: iPhone Updated
    - `quantity`: 50
    - `price`: 20000000
    - `brandId`: 1
    - `saleDate`: 2026-04-29
- Delete product:
  - `DELETE http://localhost:8080/api/admin/products/1`

#### Step 10 - Test Cart APIs (User token required)

- Add to cart:
  - `POST http://localhost:8080/api/cart/products/1`
- Get cart:
  - `GET http://localhost:8080/api/cart`
- Update quantity:
  - `PUT http://localhost:8080/api/cart/products/1?quantity=3`
- Remove one product from cart:
  - `DELETE http://localhost:8080/api/cart/products/1`
- Clear cart:
  - `DELETE http://localhost:8080/api/cart`
- Confirm order:
  - `POST http://localhost:8080/api/cart/confirm`

#### Step 11 - Test Order APIs (User token required)

- Order history:
  - `GET http://localhost:8080/api/orders?page=1&dateFrom=2026-05-01&dateTo=2026-05-07&status=NEW`
- Order detail:
  - `GET http://localhost:8080/api/orders/1?page=1`

#### Step 12 - Test Admin Order APIs (Admin token required)

- Get all orders:
  - `GET http://localhost:8080/api/admin/orders?page=1&dateFrom=2026-05-01&dateTo=2026-05-07&status=NEW`
- Get order detail:
  - `GET http://localhost:8080/api/admin/orders/1?page=1`
- Update order status:
  - `PUT http://localhost:8080/api/admin/orders/1/status?status=IN_PROGRESS`

#### Step 13 - Test login by Google Account

- Run app, then follow this URL: http://localhost:8080/oauth2/authorize/google
- Login to your Google Account
- Browser show JSON:

```json
{
  "responseCode": 200,
  "responseMsg": "OAuth2 login successful",
  "data": {
    "accessToken": "eyJhbGci...",
    "refreshToken": "550e8400...",
    "accountName": "user@gmail.com",
    "role": "USER"
  }
}
```

- Copy accessToken and refreshToken
- Use it for the others URL

### Import Postman Collection

- Import this JSON file into your Postman: postman/JavaPilotProject.postman_collection.json

## Works

### 2026-04-24

1. Create database pilot_project_db

### 2026-04-25

1. Database pilot_project_db: Create tables, indexes, insert sample data for tables.
2. Initialize pilot-project-backend (Spring Boot)

### 2026-04-27

1. Entity (5 main files, 1 support file)
2. Rename status enum name in `order` table to match variable names in Java
3. DAO/Repository (5 main files)

### 2026-04-28

1. common (Constants, CommonUtil, FileHelper)
2. Edit Configurations:
   - Spring Boot: Shorten command line - JAR manifest
3. model / dto (11 main files)
4. resources/config/: application.properties, datasource.properties

### 2026-04-29

1. security (3 main files)
2. config (3 main files)
3. service (8 main files)

### 2026-04-30

1. controller (4 main files)
2. Test Postman (Cannot test due to errors)

### 2026-05-01

1. Using generic class <T> for services and ResponseDataModel

### 2026-05-02

1. Using ResponseDataModel<?> in service interfaces.
2. Fix bugs
3. Add @Valid so that jakarta.validation can work in Controller
4. Fix bugs, add config/GlobalExceptionHandler
5. Using Bcrypt instead of md5
6. Add JWT_SECRET in Run/Edit Configurations/Modify Options/Environment variables/JWT_SECRET=...
7. Logout use client logout localStorage.removeItem("...")?
8. Change Page Response of confirm order to last page of order history list
9. Test Postman

### 2026-05-03

1. confirmOrder return just status code and message, no data list
2. Add User Detail from Spring Security between get JWT Token payload value and set Security Context, so that can load from DB for each request instead of just read from JWT Token (CustomUserDetailsService)

### 2026-05-04

1. Change PilotProjectBackendApplication position (to config/) and add annotations to this file
2. Add LOGGER for GlobalExceptionHandler
3. Add exception folder and change GlobalExceptionHandler into exception folder
4. Default Spring Security: SecurityConfig -> add unauthorized check (401) and access denied check (403)

### 2026-05-05

1. Work with React: Create project, folder structure.
2. Do: main.jsx, app.jsx, MainLayout, Header, HomePage, NotFoundPage
3. Create service/ folder in React
4. Add refresh token for JWT Token features into DB/Backend:
   4.1. Database: Add refresh_token table, 2 idx related to this table
   4.2. Backend: RefreshTokenEntity, RefreshTokenDao, RefreshTokenRequest added. Constants, JwtTokenProvider, LoginResponse, AccountService, AccountServiceImpl, AuthController modified.

### 2026-05-06

1. Add OAuth to verify Google Account into DB/Backend.
   1.1. Database: Add 2 field `google_id` and `auth_type` to `account` table.
   1.2. Add dependency oauth2-client into pom.xml
   1.3. Add 2 new Columns into AccountEntity
   1.4. Add 1 new Function into AccountDao
   1.5. Add OAuth2 Google Configuration into application.properties
   1.6. Create: security/OAuth2SuccessHandler.java and security/OAuth2FailureHandler.java -> Handle after Google authenticate success/failure
   1.7. Add OAuth2 in SecurityConfig
   1.8. Get Google Client ID and Secret
   1.9. Test
2. Change to logging.file.name

### 2026-05-07

1. Rotate refresh token
2. OrderTime, FinishTime from input search to: Pick fromDate, toDate and search by OrderTime
3. accessToken time down to 10 minutes
4. brands and products: admin can watch all, even deleted, but cannot modify when deleted.
5. stateless --> need to fix bug that not have token but can call API

## Fix Bugs

### 2026-05-02

1. Using generated security password: 6dbe7ff8-c396-44e2-a32b-6f9ab6051c3e This generated password is for development use only. Your security configuration must be updated before running your application in production -> Cannot access API because Spring generate default user information in UserDetails object -> block all request, must login through default Spring form.
   --> Change PilotProjectBackendApplication (main) to com.ntq.demo (outside packages)
   --> Disable Spring Security default user, Authentication Manager suppress auto-generated password warning
2. @Valid in Controller make 403 Forbidden Error, because Spring Security not know MethodArgumentNotValidException
   --> Add config/GlobalExceptionHandler, use @RestControllerAdvice and @ExceptionHandler

### 2026-05-04

1. Fix default Spring Security to add unauthorized check

### 2026-05-06

1. Change OAuth2 using from Redirect to JSON
2. Fix: ERROR 9020 --- [pilot-project-backend] [http-nio-8080-exec-4] c.n.demo.security.OAuth2FailureHandler : OAuth2 authentication failed: [invalid_token_response] An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: 401 Unauthorized on POST request for "https://www.googleapis.com/oauth2/v4/token": "{<EOL> "error": "invalid_client",<EOL> "error_description": "The provided client secret is invalid."<EOL>}" ; org.springframework.security.oauth2.core.OAuth2AuthenticationException: [invalid_token_response] An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: 401 Unauthorized on POST request for "https://www.googleapis.com/oauth2/v4/token": "{<EOL> "error": "invalid_client",<EOL> "error_description": "The provided client secret is invalid."<EOL>}"
   --> Invalid client, recheck client id and client secret, and their environment variable names.

## Time Tracking

| Date                     | Task              | Notes              |
| ------------------------ | ----------------- | ------------------ |
| 2026-04-24               | Database          | MySQL              |
| 2026-04-25               | Database, Backend | MySQL, Spring Boot |
| 2026-04-27 -> 2026-05-04 | Backend           | Spring Boot        |
| 2026-05-05 -> 2026-05-06 | Backend, Frontend | Spring Boot, React |

## Future Work

- [ ] Update app structure, optimize and clean code.
- [ ] UI : Design the UI better, cleaner.
