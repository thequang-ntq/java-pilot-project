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
    - [2026-05-17](#2026-05-17)
    - [2026-05-18](#2026-05-18)
    - [2026-05-19](#2026-05-19)
    - [2026-05-20](#2026-05-20)
    - [2026-05-21](#2026-05-21)
    - [2026-05-22](#2026-05-22)
    - [2026-05-23](#2026-05-23)
    - [2026-05-24](#2026-05-24)
    - [2026-05-25](#2026-05-25)
    - [2026-05-26](#2026-05-26)
    - [2026-05-27](#2026-05-27)
    - [2026-05-28](#2026-05-28)
    - [2026-05-29](#2026-05-29)
    - [2026-05-30](#2026-05-30)
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
      - MainLayout/
        - MainLayout.jsx
        - MainLayout.css
    - routes/
      - RouteGuard.jsx: authentication and authorization for going to the routes
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
- .env
- index.html

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

### 2026-05-17

1. Do: BrandsPage, update BrandsPage css, create utils/constants.js, add success / error message, response
2. Add: Loading spinner in MainLayout
3. Do: LoginPage, update LoginPage css
4. BrandsPage:
   4.1. useEffect handle state - goToLastPage: How to update total pages then load list with new total pages -> fetch brands then setPage (page changes so fetch brands again)
   4.2. confirmDelete -> update total pages then load list (setPage to make page changes)
   4.3. How to check if image path URL not load (onError={(e) => {
   e.target.src = NoBrandImage;
   }})
   4.4. margin: 0 auto; for .table-logo to center image

### 2026-05-18

1. Do BrandFormPage & BrandsPage. Things to do:
   1.1. Return fetch brands and return response, throw err to fix add problem: Come back from Form Page to List Page -> Empty
   1.2. Error -> fetch brands not return Promise -> Cannot read state -> Add not success (need "return")
   1.3. Error -> return wrong page after add/edit
   1.4. Error -> delete if page
   1.5. Error -> return wrong page when go back (add goToPage for Go Back, no replace)
   1.6. Responsive: Table columns (hide name & logo cols, layout fixed width, column spacing), pagination, modal fit content
   1.7. NoBrandImage cannot be as an image in edit -> logoPreview (not null, not empty)
2. Do Pagination (5 pages + Prev Next in Desktop + Tablet, 3 pages + Prev Next in Mobile + Small tablet)
3. Do ProductFormPage & ProductsPage (The same as 2 Brand pages):
   3.1. Detail model (Copy to brand):
   - modal: overflow: hidden, width + max-height
   - detail-row: display flex, flex-row, align-items: center
   - model-body: overflow-x,y: auto;
   - Show: 7 in desktop -> 4 in tablet -> 3 in small tablet -> 2 in mobile

### 2026-05-19

1. ProductFormPage:
   1.1. Promise.all
   1.2. Rest parameter (...content)
   1.3. CSS
   1.4. Problems: If brand too much -> How do we handle (Instead of get all brands, we can get limit dropdown 20 brands, and in this field, we can input characters to search brands, max top 20 brands can have in dropdown) !
   1.5. Show records (UI) in ProductsPage !
   1.6. Price step=1000 because money unit is VND
   1.7. CSS: Brand padding, open arrow in select tag
   1.8. CSS: select tag not selected, input date not selected - className add condition, input[type="date"] - className and condition
2. DashboardPage:
   2.1. --space-md for alert/success and other elements
3. UI in BrandsPage and ProductsPage:
   3.1. Limit dropdown 10 brands, and in this field, we can input characters to search brands, max top 10 brands can have in dropdown when search or list all (ProductFormPage) -> OK
   -> Add functions in ProductFormPage
   -> Change CSS in ProductFormPage.css
   3.2. Show records (in Pagination) -> OK
   -> Add totalElements, pageSize, fetch in ProductsPage, BrandsPage (Create new 2 useState), responsive display column in pagination for mobile & small tablets.
   -> Add isSearching, fetch in ProductsPage, BrandsPage -> If appliedSearch or 1 of 3 object of appliedFilters not empty
   -> State + resize listener for window.innerWidth in Pagination, logic pagination in pagination
   3.3. Select list dropdown in ProductsPage is overflow-x and overflow-y in mobile -> OK
   -> Change CSS: overflow-y: auto in .brand-dropdown, and no overflow in .field
   3.4. More data to products and brands (at least 50 for each) -> OK
   3.5. Price from and Price to from input to choice. -> Tomorrow
   3.6. Not return to right type of page and right page number when back from ProductFormPage to ProductsPage, BrandFormPage to BrandsPage. -> Tomorrow

### 2026-05-20

1. Brand, Product:
   1.1. Price from and Price to from input to choice. -> OK
   -> Add dropdown in product form page to products page: showPriceFromDropdown, showPriceToDropdown, priceFromDropdownRef, priceToDropdownRef, add selection default: Select a price to dropdown list
   1.2. Not return to right type of page and right page number when back from ProductFormPage to ProductsPage, BrandFormPage to BrandsPage -> Error
   -> Go back or click Back button on Browser or go to other page then go back this page => Must keep the last pagination page numbers before.
   -> Add, update -> Go back to right page, highlight row have been added/updated. |||
   -> List search -> Add/Edit -> Done -> Then should we come back to search list or original list -> Search list. If add/edit then go back but it's don't have nay data (because add/edit to another search condition) -> Go to search with old condition too, just notify items no longer matches current filters.

2. Fix by note of mentor
   2.1. Small the gap, change size of text, image, table in: MainLayout, BrandsPage, Pagination
   2.2. Add description column in BrandsPage. Remove this column in small tablet, mobile.
   2.3. Change to display: column for actions in BrandsPage
   2.4. Merge show records from Pagination to BrandsPage
   2.5. Change default page size to 5

### 2026-05-21

1. Fix by note of mentor
   1.1. Small the gap, change size of text, image, table in: ProductsPage
   1.2. Add description column in ProductsPage. Remove this column in small tablet, mobile.
   1.3. Change to display: column for actions in ProductsPage
   1.4. Merge show records from Pagination to ProductsPage
   1.5. Change default page size to 5
   1.6. Change price from and price to -> input text in ProductsPage
   1.7. isErrorList error in ProductsPage and BrandsPage -> Table & components section -actions (First section) - except for error & success alert.
   1.8. isError handling - Hide data, show alert, show text and CSS for alert in DashboardPage
   1.9. Edit & Delete button same width & height in BrandsPage & ProductsPage
   1.10. Disable Search button when Price from > Price to in ProductsPage
   -> disabled, setDisabled, useEffect for that, handleKeyDown has !disabled
   1.11. ProductsPage & BrandsPage: Table not scroll with the screen; it only scrolls within itself: https://www.w3schools.com/css/css_overflow.asp
   -> Fixed height for table with overflow: auto at table-responsive
   -> Update 5 records and show all 5 records in one page, height: min(auto, 65dvh)
   -> text-overflow ellipsis + white-space: normal overflow: hidden for all table data, table header
   -> Table fixed for desktop
   1.12. BrandFormPage, ProductFormPage: Form not scroll with screen (just within itself) or fixed/sticky button (Save, Reset, Go Back) and form scroll normal.
   -> Small gap, text, image size.

### 2026-05-22

1. BrandFormPage, ProductFormPage
   1.1. Small gap, text, image size
   1.2. .field display flex row
   1.3. Add div to input and span error
   1.4. Internal scroll for form field, 3 button fixed position: max-height: 65dvh + overflow: auto;
   -> Add .form-fields div for all fields
   -> Add divider between form-fields and actions
   -> Add close X to delete preview image and set image to BE to null
   -> Remove button reset, 2 other buttons: Back - Add/Save (position: sticky, bottom: 0;...). Also position sticky bottom 0 to pagination
   -> Responsive
   1.5. Line clamp 3 to table name & table description (CSS in BrandsPage and ProductsPage), others clamp 1
   -> ProductsPage: Product name, brand name, description
   -> BrandsPage: Brand name, description
   1.6. Modal internal scroll
   1.7. Preserve pagination state and list context when navigating from BrandsPage / ProductsPage to BrandFormPage / ProductFormPage, so using Back actions (page button, browser back, or header link) restores the previous page number and corresponding list state correctly
   1.8. After successful add/edit operations in BrandFormPage or ProductFormPage, return to the correct list state:
   -> Keep the correct pagination state (last page for add, previous page for edit).
   -> Preserve applied search/filter state so the returned list matches the previous search result or full list view.
   -> If the newly added/edited item does not match the current search/filter conditions, the previous list and pagination should still remain unchanged without showing the new item.
   -> appliedSearch: appliedSearch in state for add & edit button in BrandsPage & ProductsPage, add appliedSearch for state in BrandFormPage & ProductFormPage: catch getById, get and catch handleSubmit, Back button
   -> useEffect handle state in BrandsPage & ProductsPage: Check if search state exists, then use it to replace original appliedSearch
2. Backend
   2.1. enums/Role -> model/Role
   2.2. PilotProjectBackendApplication to com.ntq.demo
   2.3. security/SecurityConfig -> config/SecurityConfig
   2.4. security/CustomUserDetailsService -> service/impl/CustomUserDetailsService
   2.5. resources/config/... -> resources/...

### 2026-05-23

1. Padding in main, .action-wrapper; margin-bottom in .alert; font-weight in show-records for BrandsPage, ProductsPage
2. setAppliedSearch(returnedSearch) trigger useEffect([page, appliedSearch]) before logic setPage() triggered -> old fetch => No restore state, fetch just once. Manual fetch with right params, after that setState() to restore UI, then check newTotalPages/goToPages === pages, if has then setPage trigger useEffect, else manual fetch 1 more time.
3. Image/logo add/edit logic in BrandFormPage & ProductFormPage
   3.1. Add isLogoDeleted, logoFileName, add logic set file name (Load from DB) in useEffect getBrandById; save file name and reset delete flag in handleLogoChange
   3.2. Add logic when isLogoDeleted & isEdit & not have logo file -> Logo deleted when edit in handleSubmit
   3.3. Change JSX html display logo
4. Delete logo/image in edit data that has logo/image then update -> Not work
   4.1. Add deleteLogo/deleteImage field in BrandRequest and ProductRequest, check in update in BrandService and ProductService if has it then deleteFile and setLogo null
   4.2. In BrandFormPage & ProductFormPage, append deleteLogo/deleteImage: true

### 2026-05-24

1. Backend
   1.1. Change CustomUserDetailsService -> UserDetailsServiceImpl
   1.2. Merge handling images from service impl -> toEntity and updateEntity in mapper
2. Highlight data when add/edit success
   2.1. Add data in ResponseDataModel in add and update function in [BrandServiceImpl, ProductServiceImpl] for Backend
   2.2. Add brandId/productId in handleSubmit -> apiCall -> state for BrandFormPage, ProductFormPage
   2.3. Add brandId/productId and row-highlighted in location.state, tbody tr for BrandsPage, ProductsPage
   2.4. Add row-highlighted css for BrandsPage, ProductsPage
   2.5. setHighlight after fetch success -> Into goToNewPage & goToPage

### 2026-05-25

1. Add real data with images

### 2026-05-26

1. Squash commits
2. 2 columns in Desktop of ProductFormPage & BrandFormPage
   2.1. form-fields: Display: grid; + grid-template-columns: repeat(2, 1fr). Left-column for first 5 rows, Right-column for the last 2 rows.
   2.2. form: gap -sm, form-fields: gap -md, field: gap -xs
   2.3. field-input-group: remove flex: 1
   2.4. Other devices: form-fields: display: flex + flex-direction: column
3. Sort by name in choose brand for product, isSortByName - a new field in getList of BrandService, BrandServiceImpl, BrandController (default false). Then fetch isSortByName = true in getBrands of ProductFormPage
4. 1 columns in Desktop of modal in BrandsPage & ProductsPage
   4.1. modal-body: display: flex; flex-direction: column.
   4.2. width: fit-content;
   height: fit-content;
   max-width: 60dvw;
   max-height: 60dvh;
5. Name max 50 char; description max 255 chars (input maxLength = ...)
6. application-dev/-prod.properties, delete environment variables, write in -dev/-prod.properties (Not commit these files)

### 2026-05-27

1. Understand the code
2. Fix by mentor's note:
   2.1. Table has extra bottom space -> .table-responsive .table: margin-bottom: 0;
   2.2. .action-wrapper -> remove gap (upgrade distance between search bar and records, records and alert)
   2.3. Update UI for table pages
   -> Search merge with Search bar, add & edit button change color, clear button keep UI and close to search button, sanitize search, disable search when not have data (search empty, appliedSearch not check)
   -> search-group input before search-icon
   -> visibility hidden for hide UI but keep the distance that that component take place
   -> Search, From Price, To Price, Search button, Clear merge to 1, have: From, To (price-title), config width range: search-group need flex: 1 with min-width: 0 to auto shrink; input need width: 100%; search-group need display: grid + grid-template-columns to fix children's width (ProductsPage), or display: flex to auto fix width (BrandsPage)
   -> When on small device, use grid-template-rows to adjust by row (flex-column)
   -> Price from, price to >= 0 (in ProductsPage): Add check negative in useEffect setDisabled search button. And for 2 invalid conditions: invalidRange & hasNegative -> Note for the reason to disabled search: search-validation below action-groups / actions (with validation messages) & add event listeners for window resize to check isDesktop or not
   -> gap: 0 for actions-wrapper; gap change according to media queries for action-groups/actions

### 2026-05-28

1. Clear button default -> disabled
2. Just show note below Price range, not show when search empty (Change validation message position)
3. gap in actions/action-groups
4. Detail modal in desktop for BrandsPage, ProductsPage: change display type, improve width, height.
   4.1. grid-column: 1 / -1; for delete modal to display title & warning into 2 rows
   4.2. modal-body: flex flex-direction: column
   4.3. Change to flex-column when in small device (modal-body)
   4.4. overflow: hidden in .modal; and position: sticky + z-index in .modal-header; overflow-wrap: break-word + overflow: auto; in modal-body
5. BrandFormPage 1 col, no left-column & right-column. ProductFormPage 2 col, has left-column & right-column
   5.1. BrandFormPage & ProductForPage have thumbnail, so when have image not change the form structure
6. form-fields change max-height to min-height 56dvh to fit the desktop height, other device change min-height of form-fields. Use min-height at desktop, max-height at device to fixed size of form always in page, internal scroll
7. Focus to delete required error in BrandFormPage & ProductFormPage
   7.1. onFocus = clearError(...);
8. Input type = file in BrandFormPage & ProductFormPage
   8.1. Change CSS for input type = file
   8.2. Check if not show image or error -> show spacer (Spacing)
   8.3. Add max file size constants in FE to check in FE + check file size in application.properties in BE
   8.4. Check file type image/ in FE, need to check in BE (Not yet)
   8.5. Custom choose file button (htmlFor connect custom button with input file, change flex column in .field for ProductFromPage)
   8.6. onClick in input (display none and linked with custom file btn) and remove -> setError empty
   8.7. Border for choose file group
   8.8. modal-body-delete: flex-column
9. Selection arrow for Brand selection in ProductFormPage
   9.1. How to make selection arrow - button absolute (caret-down-fill) not changed in position when have field error -> field-input-wrapper, error outside
   9.2. arrow-active -> when dropdown, arrow change color
10. Magic bytes (using Apache Tika) to check if file is image in Spring Boot BE.
    10.1. Add Apache Tika (Tika Core) dependency in pom.xml
    10.2. Add function isImageFile using Tika Core in common/util/FileHelper.java
    10.3. Add check isImageFile in editFile function in common/util/FileHelper.java
    10.4. Create InvalidFileException (CustomException) in exception/ folder, for handling isImageFile
    10.5. Throw InvalidFileException in toEntity() & updateEntity(), before editFile() in ProductMapper & BrandMapper upto ServiceImpl
    10.6. Catch exception in ServiceImpl (Brand, Product), catch InvalidFileException before catch Exception in add() & update()
11. Limit to 5 file types: png, jpg, jpeg, webp, gif in FE, update UI according to it
    11.1. Change accept type in input file to: accept=".png,.jpg,.jpeg,.webp,.gif"  
    11.2. Add allowed file types in constants.js, fix handleImageChange() / handleLogoChange() in ProductFormPage, BrandFormPage
12. Brand drop down: open up (not top 100%), so that not be hide when Brand field near at the end of form.

### 2026-05-29

1. ReactToast (Add, update, delete message)
   1.1. ToastContainer at main.jsx, import ToastContainer & ReactToastify.css
   1.2. import toast, Bounce from react-toastify in BrandsPage, ProductsPage, LoginPage (useEffect to render just 1 time)
   1.3. useEffect render 1 time for error and success, setError/setSuccess null after toast, delete alert
2. Change onChange for onFocus for clearError in LoginPage
3. Change LogoutModal (long for desktop)
4. Add ReactToast instead of alert in DashboardPage

### 2026-05-30

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
| 2026-05-07 -> 2026-05-18 | DB, Backend, Frontend | DB, Spring Boot, React |
| 2026-05-19               | Frontend              | React                  |

## Future Work

- [ ] Update app structure, optimize and clean code.
- [ ] UI : Design the UI better, cleaner.
