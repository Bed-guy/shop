package com.example.shop.servlet;

import com.example.shop.dao.CartDao;
import com.example.shop.model.Cart;
import com.example.shop.model.Product;
import com.example.shop.dao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/cart", "/addToCart", "/removeFromCart", "/clearCart"})
public class CartServlet extends HttpServlet {
    private CartDao cartDao;
    private ProductDao productDao;

    @Override
    public void init() {
        cartDao = new CartDao();
        productDao = new ProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/cart":
                showCart(request, response);
                break;
            case "/removeFromCart":
                removeFromCart(request, response);
                break;
            case "/clearCart":
                clearCart(request, response);
                break;
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
            case "/addToCart":
                addToCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // 显示购物车内容
    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Cart> carts = cartDao.getCartByUserId(userId);
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < carts.size(); i++) {
            productList.add(productDao.getProductById(carts.get(i).getProductId()));
        }
        request.setAttribute("cartItems", productList);
        request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
    }

    // 添加到购物车
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productIdStr = request.getParameter("productId");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (productIdStr == null || productIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        Product product = productDao.getProductById(productId);

        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // 检查该商品是否已经在购物车中
        boolean exists = cartDao.isProductInCart(userId, productId);
        String message;
        if (exists) {
            message = "该商品已存在购物车！";
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);

            boolean success = cartDao.addProductToCart(cart);
            message = success ? "商品已成功加入购物车！" : "添加到购物车失败！";
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }


    // 从购物车中移除产品
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String productIdStr = request.getParameter("productId");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        int productId = Integer.parseInt(productIdStr);

        boolean success = cartDao.removeProductFromCart(userId, productId);
        if (success) {
            System.out.println("产品从购物车中移除成功，用户ID: " + userId + ", 产品ID: " + productId);
        } else {
            System.out.println("产品从购物车中移除失败，用户ID: " + userId + ", 产品ID: " + productId);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    // 清空购物车
    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean success = cartDao.clearCart(userId);
        if (success) {
            System.out.println("清空购物车成功，用户ID: " + userId);
        } else {
            System.out.println("清空购物车失败，用户ID: " + userId);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
