package com.example.shop.servlet;

import com.example.shop.dao.*;
import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.util.EmailUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(urlPatterns = {"/orders", "/orders-list", "/addOrder", "/editOrder", "/deleteOrder", "/viewOrder", "/changeStatus", "/pay", "/getOrderDetails"})
public class OrderServlet extends HttpServlet {

    private OrderDao orderDao;
    private CartDao cartDao;
    private ProductDao productDao;
    private Gson gson;
    private UserDao userDao;
    private LogDao logDao;

    @Override
    public void init() {
        orderDao = new OrderDao();
        cartDao = new CartDao();
        productDao = new ProductDao();
        gson = new Gson();
        userDao = new UserDao();
        logDao = new LogDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/orders":
                handleListOrders(request, response); // 查看所有订单
                break;
            case "/orders-list":
                handleListOrders2(request, response); // 用户获取自己的订单
                break;
            case "/viewOrder":
                handleViewOrder(request, response); // 查看单个订单详情
                break;
            case "/editOrder":
                showEditOrderForm(request, response); // 显示编辑订单表单
                break;
            case "/deleteOrder":
                handleDeleteOrder(request, response); // 删除订单
                break;
            case "/changeStatus":
                handleChangeStatus(request, response); // 修改订单状态
                break;
            case"/getOrderDetails":
                getOrderDetails(request, response);
                break; //获取订单详情
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/addOrder":
                handleAddOrder(request, response);  // 添加订单
                break;
            case "/editOrder":
                handleEditOrder(request, response); // 提交编辑订单
                break;
            case "/pay":
                handlePay(request, response);        // 处理支付
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void getOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing orderId parameter");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid orderId parameter");
            return;
        }

        // 获取订单详情
        List<OrderItem> orderItems = orderDao.getOrderDetailsByOrderId(orderId);
        if (orderItems == null || orderItems.isEmpty()) {
            // 返回空数组
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            gson.toJson(Collections.emptyList(), response.getWriter());
            return;
        }

        // 获取每个OrderItem对应的Product信息
        List<Map<String, Object>> detailedItems = new ArrayList<>();
        for (OrderItem item : orderItems) {
            Product product = productDao.getProductById(item.getProductId());
            if (product != null) {
                Map<String, Object> detailedItem = new HashMap<>();
                detailedItem.put("productId", product.getId());
                detailedItem.put("productName", product.getName());
                detailedItem.put("description", product.getDescription());
                detailedItem.put("price", product.getPrice());
                detailedItems.add(detailedItem);
            }
        }

        // 将结果转换为JSON并返回
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        gson.toJson(detailedItems, response.getWriter());
    }

    // 处理支付请求
    private void handlePay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取当前用户
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"用户未登录，请先登录。\"}");
            return;
        }

        // 获取选中的商品ID
        String[] selectedProductIds = request.getParameterValues("selectedProductIds");
        if (selectedProductIds == null || selectedProductIds.length == 0) {
            response.getWriter().write("{\"success\": false, \"message\": \"请选择至少一件商品进行付款！\"}");
            return;
        }

        // 获取商品详情并计算总价
        List<Product> products = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (String productIdStr : selectedProductIds) {
            try {
                int productId = Integer.parseInt(productIdStr);
                Product product = productDao.getProductById(productId);
                if (product != null) {
                    products.add(product);
                    totalPrice = totalPrice.add(product.getPrice());
                }
            } catch (NumberFormatException e) {
                // 处理无效的产品ID
                e.printStackTrace();
            }
        }

        if (products.isEmpty()) {
            response.getWriter().write("{\"success\": false, \"message\": \"购物车中没有可付款的商品！\"}");
            return;
        }

        // 创建订单对象
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus("未发货");

        // 创建订单详情
        List<OrderItem> orderItems = new ArrayList<>();
        List<Integer> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            orderItems.add(item);
        }

        // 插入订单和订单详情
        boolean orderCreated = orderDao.addOrder(order, orderItems);

        if (orderCreated) {
            // 从购物车中移除已支付的商品
            for (Product product : products) {
                cartDao.removeProductFromCart(userId, product.getId());
            }

            // 假设创建订单成功（实际逻辑略）
            String orderDetails = "购买商品ID: " + String.join(", ", productIds.toString());

            // 记录日志
            logDao.insertLog(userId, "购买", orderDetails);

            // 发送成功响应
            response.getWriter().write("{\"success\": true, \"message\": \"支付成功！\"}");
        } else {
            // 发送失败响应
            response.getWriter().write("{\"success\": false, \"message\": \"支付失败，请稍后再试。\"}");
        }
    }


    // 显示所有订单
    private void handleListOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = orderDao.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/jsp/orders.jsp").forward(request, response);
    }

    private void handleListOrders2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        List<Order> orders = orderDao.getOrdersByUserId(userId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/jsp/orders-list.jsp").forward(request, response);
    }

    // 查看单个订单详情
    private void handleViewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int id = Integer.parseInt(idStr);
        Order order = orderDao.getOrderById(id);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // 获取订单详情
        List<OrderItem> orderItems = orderDao.getOrderDetailsByOrderId(id);
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.getRequestDispatcher("/jsp/viewOrder.jsp").forward(request, response);
    }

    // 显示编辑订单表单
    private void showEditOrderForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int id = Integer.parseInt(idStr);
        Order order = orderDao.getOrderById(id);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/jsp/editOrder.jsp").forward(request, response);
    }

    // 添加订单
    private void handleAddOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        String totalPriceStr = request.getParameter("totalPrice");
        String status = request.getParameter("status");

        // 验证订单基本信息
        if (userIdStr == null || totalPriceStr == null || status == null ||
                userIdStr.isEmpty() || totalPriceStr.isEmpty() || status.isEmpty()) {
            request.setAttribute("error", "所有字段都是必填的！");
            request.getRequestDispatcher("/jsp/addOrder.jsp").forward(request, response);
            return;
        }

        // 解析订单的基本信息
        int userId = Integer.parseInt(userIdStr);
        BigDecimal totalPrice = new BigDecimal(totalPriceStr);

        // 创建订单对象
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus(status);

        // 获取订单详情（假设前端是通过多个 input 字段传递产品 ID 和价格）
        List<OrderItem> orderItems = new ArrayList<>();
        String[] productIds = request.getParameterValues("productId");  // 获取产品ID数组
        String[] prices = request.getParameterValues("price");  // 获取价格数组

        // 验证订单详情（至少需要一个订单项）
        if (productIds == null || prices == null || productIds.length == 0 || prices.length == 0) {
            request.setAttribute("error", "至少需要添加一个订单项！");
            request.getRequestDispatcher("/jsp/addOrder.jsp").forward(request, response);
            return;
        }

        // 收集订单详情信息
        for (int i = 0; i < productIds.length; i++) {
            int productId = Integer.parseInt(productIds[i]);
            BigDecimal price = new BigDecimal(prices[i]);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setPrice(price);
            orderItems.add(orderItem);
        }

        // 调用 OrderDao 添加订单和订单详情
        boolean success = orderDao.addOrder(order, orderItems);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } else {
            request.setAttribute("error", "添加订单失败！");
            request.getRequestDispatcher("/jsp/addOrder.jsp").forward(request, response);
        }
    }

    // 编辑订单
    private void handleEditOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String totalPriceStr = request.getParameter("totalPrice");
        String status = request.getParameter("status");

        if (idStr == null || totalPriceStr == null || status == null ||
                idStr.isEmpty() || totalPriceStr.isEmpty() || status.isEmpty()) {
            request.setAttribute("error", "所有字段都是必填的！");
            request.getRequestDispatcher("/jsp/editOrder.jsp").forward(request, response);
            return;
        }

        int id = Integer.parseInt(idStr);
        BigDecimal totalPrice = new BigDecimal(totalPriceStr);

        Order order = new Order();
        order.setId(id);
        order.setTotalPrice(totalPrice);
        order.setStatus(status);

        boolean success = orderDao.updateOrder(order);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } else {
            request.setAttribute("error", "更新订单失败！");
            request.setAttribute("order", order);
            request.getRequestDispatcher("/jsp/editOrder.jsp").forward(request, response);
        }
    }

    // 删除订单
    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int id = Integer.parseInt(idStr);
        boolean success = orderDao.deleteOrder(id);
        if (success) {
            System.out.println("订单删除成功，ID: " + id);
        } else {
            System.out.println("订单删除失败，ID: " + id);
        }
        response.sendRedirect(request.getContextPath() + "/orders");
    }

    // 修改订单状态并发送邮件
    private void handleChangeStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            // 无效的订单ID，重定向或显示错误
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // 修改订单状态
        boolean success = orderDao.changeOrderStatus(id, "已发货");
        if (success) {
            // 获取订单信息
            Order order = orderDao.getOrderById(id);
            if (order != null) {
                // 获取用户信息
                User user = userDao.getUserById(order.getUserId());
                if (user != null && user.getEmail() != null) {
                    String userEmail = user.getEmail();
                    String subject = "您的订单已发货";
                    String body = "尊敬的用户，\n\n您的订单编号 #" + order.getId() + " 已发货。\n\n感谢您的购买！\n\n此致，\n在线购物网站";

                    // 发送邮件
                    boolean emailSent = EmailUtil.sendEmail(userEmail, subject, body);
                    if (emailSent) {
                        System.out.println("已向用户 " + userEmail + " 发送发货通知邮件。");
                    } else {
                        System.err.println("向用户 " + userEmail + " 发送发货通知邮件失败。");
                    }
                } else {
                    System.err.println("未找到用户或用户邮箱为空，订单ID: " + id);
                }
            } else {
                System.err.println("未找到订单，订单ID: " + id);
            }

            // 重定向到订单列表
            response.sendRedirect(request.getContextPath() + "/orders");
        } else {
            // 修改订单状态失败，重定向或显示错误
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }
}
