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
      - [Step 2 - Test Login and get accessToken + refreshToken](#step-2---test-login-and-get-accesstoken--refreshtoken)
      - [Step 3 - Refresh and get a new accessToken and rotate refresh token](#step-3---refresh-and-get-a-new-accesstoken-and-rotate-refresh-token)
      - [Step 4 - Logout and remove both tokens (requires Access Token)](#step-4---logout-and-remove-both-tokens-requires-access-token)
      - [Step 5 - Configure Authorization in Postman](#step-5---configure-authorization-in-postman)
      - [Step 6 - Test Public APIs (no token required)](#step-6---test-public-apis-no-token-required)
      - [Step 7 - Test Brand CRUD (Admin token required)](#step-7---test-brand-crud-admin-token-required)
      - [Step 8 - Test Product CRUD (Admin token required)](#step-8---test-product-crud-admin-token-required)
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
    - [2026-05-08](#2026-05-08)
    - [2026-05-10](#2026-05-10)
    - [2026-05-11](#2026-05-11)
    - [2026-05-12](#2026-05-12)
    - [2026-05-13](#2026-05-13)
    - [2026-05-14](#2026-05-14)
    - [2026-05-15](#2026-05-15)
    - [2026-05-16](#2026-05-16)
  - [Fix Bugs](#fix-bugs)
    - [2026-05-02](#2026-05-02-1)
    - [2026-05-04](#2026-05-04-1)
    - [2026-05-06](#2026-05-06-1)
    - [2026-05-07](#2026-05-07-1)
  - [Time Tracking](#time-tracking)
  - [Future Work](#future-work)

## Features

Fullstack Website

### Database

MySql: pilot_project_db (CHARSET utf8mb4, COLLATE utf8mb4_0900_ai_ci, (accent-insensitive, case-insensitive))

- Tables:
  - users
  - brands
  - products
  - refresh_tokens
- Indexes:
  - users:
    - idx_users_username
  - brand:
    - idx_brands_brand_name
  - product:
    - idx_products_product_name
    - idx_products_brand_id
    - idx_products_price
  - refresh_token:
    - idx_refresh_tokens_user_id
    - idx_refresh_tokens_token

### Backend

Java Spring Boot (IDE: Intellij): pilot-project-backend (Spring Web, Spring Data JPA, MySQL Driver, Spring Security, Spring Boot DevTools, Lombok, Validation, Maven, JWT with refresh token, Bcrypt). Use:

- enums/: Enum for entity field
  - Role
- entity/: Map DB tables to java object
  - UserEntity
  - BrandEntity
  - ProductEntity
  - RefreshTokenEntity
- repository/: communicate/react with DB (CRUD, query)
  - UserRepository
  - BrandRepository
  - ProductRepository
  - RefreshTokenRepository
- common/: constants variables and common functions
  - constants/Constants
  - util/:
    - CommonUtil
    - FileHelper
- dto/: object transmit data between Controller and Service
  - request/: DTO object to server from client (login, register, add/update brand, product)
    - LoginRequest
    - BrandRequest
    - ProductRequest
    - RefreshTokenRequest
  - response/: DTO object from server to client-side
    - LoginResponse
    - BrandResponse
    - ProductResponse
- mapper/: Map between entity and DTO
  - BrandMapper
  - ProductMapper
- model/:
  - PageResponse
  - ResponseDataModel
- security/: JWT Token auth, ADMIN/USER role, Custom User Detail
  - CustomUserDetailsService
  - JwtTokenProvider
  - JWTAuthenticationFilter
  - SecurityConfig
- config/: CORS, MainApplication, ServletInitializer, ExceptionHandler
  - ServletInitializer
  - WebConfig
  - PilotProjectBackendApplication
- service/: Handle business logic, method related redirect to controller / client
  - impl/: implements methods
    - UserServiceImpl
    - BrandServiceImpl
    - ProductServiceImpl
  - UserService
  - BrandService
  - ProductService
- controller/: get request from React, return Response
  - AuthController
  - BrandController
  - ProductController
- exception/: Handle exception at Controller
  - GlobalExceptionHandler: exception handler for error when mapping request <-> object in Controller, after Spring Security

### Frontend

React (IDE: VS Code): pilot-project-frontend

- App.css
- App.jsx
- index.css
- main.jsx
  - assets/
  - components/
    - context/
      - auth-context.js: createContext
      - AuthContext.jsx: shared auth state across app, every components can get current user of app
      - use-auth.js: useContext for AuthContext
    - common/
      - Pagination.css
      - Pagination.jsx
    - layout/
      - Header/
        - Header.jsx
        - Header.css
      - MainLayout/
        - MainLayout.jsx
        - MainLayout.css
      - Footer/
        - Footer.jsx
        - Footer.css
    - routes/
      - RouteGuard.jsx: authentication and authorization for going to the routes
  - data/
    - admin-mock-data.js: template data and services for Admin Brands & Products
  - pages/:
    - brands/
      - BrandsPage
      - BrandFormPage
    - dashboard/
    - login/
    - logout/
    - products/
      - ProductsPage
      - ProductFormPage
  - services/:
    - auth-api
    - auth_storage
    - axios-instance
    - brands-api
    - products-api
  - utils/:
    - constants.js
    - utils.js

## Technology & Knowledge

- Database:
  - Primary key, Foreign key
  - Index
  - DDL, SQL Constraint, DML, DQL, 3NF
  - ACID
  - Transactions

- Frontend:
  - JS: Named/Default export, Function, OOP, Array, Destructuring, Spread Operator, Array map() method
  - React: JSX, Component, Fragment, Props, Props.children, Truthy/Falsy values, useState, useEffect, Forwarding Props, Default Props, One-Way Binding / Two-Way Binding, Import CSS, CSS Module, Styled Components, useRef, Axios (axios instance, interceptors, handling error, loading), Routing (Routes, Route, Link, NavLink, Nested Routing), useMemo, useContext...
- Backend:
  - CORS Policy (urlPattern: URL API that FE can connect, for example: /a --> all child URL after /a can access - /a/b, /a/c...)
  - MVC
  - Pagination, LIMIT A OFFSET B.
  - Spring Boot
  - RESTful API, Spring Framework
  - BCrypt for Hash password
  - JWT Token for authorization with refresh token
  - OAuth2 (Not added yet, just learning)
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

#### Step 2 - Test Login and get accessToken + refreshToken

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

#### Step 3 - Refresh and get a new accessToken and rotate refresh token

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

#### Step 4 - Logout and remove both tokens (requires Access Token)

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

#### Step 5 - Configure Authorization in Postman

There are 2 ways:

- Per request:
  - Authorization tab
  - Type: Bearer Token
  - Token: paste your token
- By Collection (recommended):
  - Create collection `Pilot Project`
  - Collection -> Authorization -> Bearer Token
  - All requests in that collection inherit the token

#### Step 6 - Test Public APIs (no token required)

- `GET http://localhost:8080/api/brands?page=1&keyword=`
- `GET http://localhost:8080/api/brands/1`
- `GET http://localhost:8080/api/products?page=1&keyword=&priceFrom=&priceTo=`
- `GET http://localhost:8080/api/products/1`

#### Step 7 - Test Brand CRUD (Admin token required)

Login with an admin account first and get admin token.

- Add brand:
  - `POST http://localhost:8080/api/brands`
  - Body -> form-data:
    - `brandName`: Apple Test
    - `description`: Test description
    - `logoFiles`: choose image file
- Update brand:
  - `PUT http://localhost:8080/api/brands/1`
  - Body -> form-data:
    - `brandId`: 1
    - `brandName`: Apple Updated
    - `description`: Updated description
- Delete brand:
  - `DELETE http://localhost:8080/api/brands/1`

#### Step 8 - Test Product CRUD (Admin token required)

- Add product:
  - `POST http://localhost:8080/api/products`
  - Body -> form-data:
    - `productName`: iPhone Test
    - `quantity`: 100
    - `price`: 25000000
    - `brandId`: 1
    - `saleDate`: 2026-04-29
    - `description`: Test product
    - `imageFiles`: choose image file
- Update product:
  - `PUT http://localhost:8080/api/products/1`
  - Body -> form-data:
    - `productId`: 1
    - `productName`: iPhone Updated
    - `quantity`: 50
    - `price`: 20000000
    - `brandId`: 1
    - `saleDate`: 2026-04-29
- Delete product:
  - `DELETE http://localhost:8080/api/products/1`

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
6. Rename google_id and auth_type to provider_id and provider
7. Refactor code according to that, add mappers/ and enums/, change dao/ to repository/, add dto/, change request DTO and response DTO to dto/, model: PageResponse and ResponseDataModel. Enum in Account Entity and Order Entity go to enums/
8. Write mapper/ files to map Entity -> DTO using in servicesImpl. Manual mapper class according to services.
   8.1. BrandMapper and Product Mapper: use in getList, getListForAdmin, Add, Update in BrandServiceImpl and ProductServiceImpl
   8.2. OrderMapper and OrderDetailMapper: use in PRIVATE HELPER in OrderServiceImpl

### 2026-05-08

1. Research Responsive Web Design
2. 4 Breakpoints:
   2.1. Mobile: 0-479.98px
   2.2. Small Tablet: 480-767.98px
   2.3. Tablet: 768px-1023.98px
   2.4. Desktop: 1024px
3. Common CSS outside, different CSS in respective breakpoints
4. HTML Structure:

```HTML
  <body>

    <header class="header">
      <div class="container">
        <div class="header-wrapper">
          <a href="/" class="logo">Lite</a>
          <nav class="nav">
            <ul class="nav-list">
              <li>
                  <a href="#">Home</a>
              </li>
              <li>
                  <a href="#">Shop</a>
              </li>
              <li>
                  <a href="#">About</a>
              </li>
            </ul>
          </nav>
          <div class="header-actions">
            <button class="search-btn">Search</button>
            <button class="cart-btn">Cart</button>
          </div>
        </div>
      </div>
    </header>

    <main>
      <section class="{section-name} section">
        <div class="container"> ->max-width, margin-auto
          <div class="section-wrapper"> -> layout, alignment, spacing
            <div class="section-content">
            </div>
            <div class="section-image">
            </div>
          </div>
        </div>
      </section>

      <section class="categories section">
        <div class="container">
          <div class="section-heading">
          </div>
          <div class="section-body">
          </div>
        </div>
      </section>

      <section class="products section">
          ...
      </section>

      <section class="banner section">
          ...
      </section>
    </main>

    <footer class="footer">
      <div class="container">
        <div class="footer-grid">
          <div class="footer-column">
              ...
          </div>
          <div class="footer-column">
              ...
          </div>
          <div class="footer-column">
              ...
          </div>
        </div>
      </div>
    </footer>

  </body>
```

5. clamp -> font-size, line-height, small spacing, gap, padding, margin... (no need breakpoint). Breakpoint for: grid-template-columns, flex-direction, sidebar, navbar, drawer...
6. em -> internal component padding
7. % -> width layout, max-width, image
8. Focus on UX
9. gap -> rem

### 2026-05-10

1. section (outer block) -> position, font-size, font-weight, width, background, big spacing, semantic meaning: padding-block: clamp(4rem, 8vw, 7rem);
2. container (width manager) -> max-width, margin-auto, horizontal padding: width: min(1200px, calc(100% - 2rem));margin-inline: auto;
3. wrapper (section layout) -> display flex/grid, alignment, gap, justify, layout direction
4. menu-btn (fixed width), mobile-menu, mobile-actions, X fixed translate and rotate
5. grid-template-rows
6. padding-block = padding-top + padding-bottom, padding-inline = padding-left + padding-right
7. Do Header component

### 2026-05-11

1. Do Footer component
2. object-fit: cover -> crops the unwanted part. object-position: center -> center the remaining. Or we can crop the image.
3. Do: MainLayout, HomePage

### 2026-05-12

1. Complete: HomePage, NotFoundPage, BrandsPage
2. Add scrollToTop in App.jsx to scroll to top page when navigate to another route
3. Do: ProductsPage
4. components/context/AuthContext.jsx: get current user (data in LocalStorage) across all components in app
5. components/context/auth-context.js: createContext, and use-auth.js: export function useAuth().
6. Update App.jsx to have AuthProvider in AuthContext
7. Header update: Read authState, shows user dropdown when logged in, refactor code

### 2026-05-13

1. Update: Header.jsx - onLogoutClick show modal, set show logout, manage LogoutModal state
2. Add: LogoutModal
3. Add: LoginPage. Validation, fake auth, success message from signup, eye toggle, filter input.
4. Add: SignupPage. Validation with trim/collapse whitespace, eye toggle, duplicate check, filter input.
5. Add: utils/utils.js -> add util functions, sanitize, filterInput.
6. Split Pagination to a specific component
7. Add: Navigate replace previous route after signup/login/logout success, or after from NotFoundPage to another page - {replace: true}.
8. Block route by auth grand: <Navigate to="/..." replace>
9. RouteGuard -> Define which role and which type of user (Guest, User, Admin) that we need to access the routes, to simplify, this file contains everything about authentication and authorization for going to the routes. (IMPORTANT)
10. Update route in App.jsx
11. Update Route in: NotFoundPage, LoginPage
12. Update Header.jsx: NavLink for Admin

### 2026-05-14

1. Add admin-mock-data.js, brands and products template data and services for Admin Brands and Admin Products
2. Add: AdminBrandsPage, AddEditBrandPage. State between 2 pages: List -> AddEdit is current page when go to Edit. AddEdit -> List is goToLastPage and goToPage. Both need window.history.replaceState({}, ""); in use effect after using it.
3. Fix add but image not have (NoBrandImage) in AdminBrandsPage
4. In database, re-structure database, delete Order and OrderDetail, unused index, unused column: isDeleted, auth_type, auth_id, email...
5. In Backend Spring Boot, @OneToMany in List<ProductEntity> products in BrandEntity.java, has: cascade = CascadeType.ALL, orphanRemoval = true -> Delete brand, so delete all products in that brand too. https://www.baeldung.com/jpa-cascade-remove-vs-orphanremoval
6. In Backend Spring Boot, Change /api/admin/... to /api/..., soft delete -> hard delete

### 2026-05-15

1. Route (Redo) & App.jsx (Route, redo)
2. Change page links in import (App.jsx, BrandFormPage, BrandsPage, Header)
3. Change Footer, Header component UI: Change display type in @media queries, others keep unchanged, @media queries for smallest number of code lines. First desktop, mobile/tablet... display: none, then go to @media queries, display: flex..., and go to general code parts -> mobile and update UI. Delete Header and Footer, merge them with MainLayout.
4. LoginPage
5. http://localhost:8080/images/brand/4f131286-07c0-4099-9abb-997c1ecb0b6f.jpg -> Image in Backend
6. AccessToken and RefreshToken in local storage.
7. Do in React:
   7.1. services/: auth-api, auth-storage, axios-instance, brands-api, products-api
   7.2. Know about responseCode, transactional, when to refresh
8. Update responseCode in Backend

### 2026-05-16

1. Do interceptors in axios-instance: Business error, HTTP Status 401
2. Do: AuthContext, LoginPage.
3. Refactor DB and BE: change table names, column names in DB -> also change in BE; delete logos and images logic when delete brands and products; add cors in SecurityConfig.
   3.1. users, brands, products, refresh_tokens
   3.2. users -> user_id, username,...; refresh_tokens -> user_id
   3.3. constraint -> according to table names and column names
   3.4. index -> according to table names and column names
   3.5. Spring Boot -> change entity, repository functions, service names according to DB; add cors in Spring Security
4. Do: LogoutModal.

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

### 2026-05-07

1. Stateless mix bug while using OAuth2 and JWT --> Delete OAuth2

## Time Tracking

| Date                     | Task                  | Notes                  |
| ------------------------ | --------------------- | ---------------------- |
| 2026-04-24               | Database              | MySQL                  |
| 2026-04-25               | Database, Backend     | MySQL, Spring Boot     |
| 2026-04-27 -> 2026-05-04 | Backend               | Spring Boot            |
| 2026-05-05 -> 2026-05-06 | Backend, Frontend     | Spring Boot, React     |
| 2026-05-07 -> 2026-05-16 | DB, Backend, Frontend | DB, Spring Boot, React |

## Future Work

- [ ] Update app structure, optimize and clean code.
- [ ] UI : Design the UI better, cleaner.
