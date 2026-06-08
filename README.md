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
  - [Fix Bugs](#fix-bugs)
    - [2026-05-02](#2026-05-02-1)
    - [2026-05-04](#2026-05-04-1)
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

### Backend

Java Spring Boot (IDE: Intellij): pilot-project-backend (Spring Web, Spring Data JPA, MySQL Driver, Spring Security, OAuth2 Resource Server, Spring Boot DevTools, Lombok, Validation, Maven)

- entity/: Map DB tables to java object
  - AccountEntity
  - BrandEntity
  - OrderDetailEntity
  - OrderDetailId
  - OrderEntity
  - ProductEntity
- dao(repository)/: communicate/react with DB (CRUD, query)
  - AccountDao
  - BrandDao
  - OrderDao
  - OrderDetailDao
  - ProductDao
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
  - SecurityConfig
- config/: CORS, MainApplication, ServletInitializer, ExceptionHandler
  - ServletInitializer
  - WebConfig
  - GlobalExceptionHandler: exception handler for error when mapping request <-> object in Controller, after Spring Security.
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

### Frontend

React (IDE: VS Code): pilot-project-frontend

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

## Fix Bugs

### 2026-05-02

1. Using generated security password: 6dbe7ff8-c396-44e2-a32b-6f9ab6051c3e This generated password is for development use only. Your security configuration must be updated before running your application in production -> Cannot access API because Spring generate default user information in UserDetails object -> block all request, must login through default Spring form.
   --> Change PilotProjectBackendApplication (main) to com.ntq.demo (outside packages)
   --> Disable Spring Security default user, Authentication Manager suppress auto-generated password warning
2. @Valid in Controller make 403 Forbidden Error, because Spring Security not know MethodArgumentNotValidException
   --> Add config/GlobalExceptionHandler, use @RestControllerAdvice and @ExceptionHandler

### 2026-05-04

1. Fix default Spring Security to add unauthorized check

## Time Tracking

| Date                     | Task              | Notes              |
| ------------------------ | ----------------- | ------------------ |
| 2026-04-24               | Database          | MySQL              |
| 2026-04-25               | Database, Backend | MySQL, Spring Boot |
| 2026-04-27 -> 2026-05-04 | Backend           | Spring Boot        |

## Future Work

- [ ] Update app structure, optimize and clean code.
- [ ] UI : Design the UI better, cleaner.
