# 在线购物系统

**姓名：赵曜**  
**学号：202230445129**

本项目是一个基于 Java Servlet、JSP、JSTL、JDBC 等技术构建的简易在线购物系统示例。通过该系统，用户可以注册、登录、浏览商品、加入购物车并创建订单，管理员则可以对用户、商品和订单进行管理。同时包含了日志记录以及简要的统计分析功能（如商品销量统计）。

## 项目功能概述

1. **用户模块**：
  - 用户注册、登录、注销。
  - 用户角色区分：普通用户（USER）与管理员（ADMIN）。
  - 用户信息编辑与删除（管理员操作）。
  - 用户搜索与列表显示（管理员操作）。

2. **商品模块**：
  - 商品列表展示（用户、管理员均可查看）。
  - 商品搜索功能（根据名称关键字检索）。
  - 管理员可添加、编辑、删除商品。

3. **购物车模块**：
  - 用户可将商品加入购物车、从购物车中移除商品或清空购物车。
  - 用户购物车页面展示已选商品列表。

4. **订单模块**：
  - 用户可将购物车中的商品进行结算，生成订单。
  - 用户可查看自己的订单列表及状态（如未发货、已发货）。
  - 管理员可查看所有订单并修改订单状态（发货）、删除订单等。
  - 发货后自动发送邮件通知用户（需要正确配置邮件服务参数）。

5. **日志模块**（Log）：
  - 系统对用户的重要操作进行日志记录，如搜索商品、购买商品等。
  - 管理员可查看所有的操作日志列表。

6. **统计模块**：
  - 展示商品销量统计的折线图（管理员视图）。

## 代码结构简要说明

- `src/main/java/com/example/shop/dao/`：数据访问层（DAO），负责与数据库交互。
  - `CartDao`: 操作购物车数据的类。
  - `LogDao`: 插入与获取用户操作日志。
  - `OrderDao`: 管理订单和订单详情，包括增删改查订单以及更新订单状态。
  - `ProductDao`: 管理商品信息的增删改查与搜索。
  - `SalesDao`: 用于统计商品销量数据。
  - `UserDao`: 管理用户注册、登录、信息更新和删除等。

- `src/main/java/com/example/shop/model/`：实体类，映射数据库中的表结构。
  - `Cart`: 购物车项实体类。
  - `Order`: 订单实体类。
  - `OrderItem`: 订单详情实体类。
  - `Product`: 商品实体类。
  - `User`: 用户实体类。
  - `UserLog`: 用户操作日志实体类。

- `src/main/java/com/example/shop/servlet/`：控制层，使用Servlet处理请求与响应。
  - `CartServlet`: 处理购物车的相关请求（查看、添加、移除、清空购物车）。
  - `OrderServlet`: 处理订单的相关请求（查看、添加、编辑、支付、发货、删除订单等）。
  - `ProductServlet`: 处理商品的查看、搜索、编辑、添加、删除等请求。
  - `SalesServlet`: 显示销售统计数据的Servlet。
  - `UserLogServlet`: 展示用户日志列表。
  - `UserServlet`: 用户注册、登录、登出、用户列表显示、搜索以及用户信息编辑和删除。

- `src/main/java/com/example/shop/util/`：工具类，主要是数据库连接与邮件发送工具。
  - `DBConnection`: 管理与MySQL数据库的连接。
  - `EmailUtil`: 处理邮件发送功能。

- `webapp/jsp/`：前端页面文件（JSP页面），用于展示数据和实现前后端交互的界面。  
  包含登录注册页、商品列表页、购物车页、订单管理页、用户管理页等。

- `pom.xml`：Maven项目配置文件，包含依赖管理。

## 环境依赖与配置

1. **开发环境**：
  - JDK 17
  - IntelliJ IDEA 2024.2.3
  - Apache Tomcat 9.0.96
  - Maven 3.9.8
  - MySQL 8.0.40
  - Navicat Premium 17

2. **数据库**：
  - MySQL数据库  
    数据库连接配置在 `DBConnection.java` 中，需要根据实际情况修改`JDBC_URL`、`JDBC_USERNAME`、`JDBC_PASSWORD`。

3. **邮件服务**：
  - 在 `EmailUtil.java` 中配置SMTP服务器、发件邮箱与授权码。

## 部署与运行步骤

1. 将代码上传至GitHub。
2. 使用IDE（如 IntelliJ IDEA 或 Eclipse）导入该Maven项目。
3. 创建MySQL数据库并导入相应的表结构与数据（文中给出数据库导入文件shop.sql）。
4. 修改 `DBConnection.java` 中的数据库连接信息为实际数据库地址与账号密码，即DBConnection修改第10行的数据库密码。EmailUtil修改14、15行的账号和密码。
5. 根据需要在 `EmailUtil.java` 中配置邮件发送相关参数（如不需要邮件发送功能，可忽略此步骤），即EmailUtil修改14、15行的账号和密码。
6. 使用 `mvn clean package` 打包并部署到 Tomcat 服务器。
7. 启动Tomcat，在浏览器中访问相应的URL（例如 `http://localhost:8080/shop`），测试系统功能。
8. 也可以访问 http://8.138.133.227:8080/demo/login 来进行测试。

## 系统角色与权限

- **商家（ADMIN）**：  
  可以对用户、商品、订单进行管理，还可查看日志与销售统计。

- **普通用户（USER）**：  
  可以注册登录、浏览商品、添加至购物车、生成订单以及查看自己的订单。

## 注意事项

- 在使用本系统前请确保数据库表结构正确、数据已准备好。
- 部分功能（如邮件发送）可能需要在实际部署环境中正确配置。
- 本项目仅为演示目的，实际生产环境应加入更严格的安全策略和数据校验。

---

**作者信息**：

- **姓名**：赵曜
- **学号**：202230445129
- **项目用途**：作为课程实践

若有任何问题，请及时联系或反馈。
