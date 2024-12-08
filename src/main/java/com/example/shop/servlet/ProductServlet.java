package com.example.shop.servlet;

import com.example.shop.dao.LogDao;
import com.example.shop.dao.ProductDao;
import com.example.shop.model.Product;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/products", "/addProduct", "/editProduct", "/deleteProduct", "/searchProduct","/product-list","/searchProduct-user"})
public class ProductServlet extends HttpServlet {
    private ProductDao productDao;
    private LogDao logDao;

    @Override
    public void init() {
        productDao = new ProductDao();
        logDao = new LogDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/products":
                listProducts(request, response);
                break;
            case "/product-list":
                listProducts2(request, response);
                break;
            case "/addProduct":
                showAddProductForm(request, response);
                break;
            case "/editProduct":
                showEditProductForm(request, response);
                break;
            case "/deleteProduct":
                deleteProduct(request, response);
                break;
            case "/searchProduct":
                searchProducts(request, response);
                break;
            case "/searchProduct-user":
                searchProducts2(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/addProduct":
                addProduct(request, response);
                break;
            case "/editProduct":
                updateProduct(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // 显示产品列表
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDao.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/jsp/products.jsp").forward(request, response);
    }

    private void listProducts2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDao.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/jsp/product-list.jsp").forward(request, response);
    }

    // 显示添加产品表单
    private void showAddProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/addProduct.jsp").forward(request, response);
    }

    // 添加新产品
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取表单参数
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");

        // 输入验证
        String error = null;
        if (name == null || name.isEmpty() || priceStr == null || priceStr.isEmpty()) {
            error = "产品名称和价格是必填的！";
        }

        BigDecimal price = null;
        try {
            price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                error = "价格必须为非负数！";
            }
        } catch (NumberFormatException e) {
            error = "价格格式不正确！";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/jsp/addProduct.jsp").forward(request, response);
            return;
        }

        // 创建产品对象
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        boolean success = productDao.addProduct(product);
        if (success) {
            System.out.println("新产品添加成功，ID: " + product.getId());
            response.sendRedirect(request.getContextPath() + "/products");
        } else {
            request.setAttribute("error", "添加产品失败！");
            request.getRequestDispatcher("/jsp/addProduct.jsp").forward(request, response);
        }
    }

    // 显示编辑产品表单
    private void showEditProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product product = productDao.getProductById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/jsp/editProduct.jsp").forward(request, response);
    }

    // 更新产品
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取表单参数
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");

        // 输入验证
        String error = null;
        if (idStr == null || idStr.isEmpty() || name == null || name.isEmpty() || priceStr == null || priceStr.isEmpty()) {
            error = "产品ID、名称和价格是必填的！";
        }

        int id = 0;
        BigDecimal price = null;
        try {
            id = Integer.parseInt(idStr);
            price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                error = "价格必须为非负数！";
            }
        } catch (NumberFormatException e) {
            error = "产品ID或价格格式不正确！";
        }

        if (error != null) {
            request.setAttribute("error", error);
            Product product = new Product(id, name, description, price);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/jsp/editProduct.jsp").forward(request, response);
            return;
        }

        // 创建产品对象
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        boolean success = productDao.updateProduct(product);
        if (success) {
            System.out.println("产品更新成功，ID: " + id);
            response.sendRedirect(request.getContextPath() + "/products");
        } else {
            request.setAttribute("error", "更新产品失败！");
            request.setAttribute("product", product);
            request.getRequestDispatcher("/jsp/editProduct.jsp").forward(request, response);
        }
    }

    // 删除产品
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        boolean success = productDao.deleteProduct(id);
        if (success) {
            System.out.println("产品删除成功，ID: " + id);
        } else {
            System.out.println("产品删除失败，ID: " + id);
        }
        response.sendRedirect(request.getContextPath() + "/products");
    }

    // 查询商品
    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        // 如果没有搜索关键词，则显示所有产品
        if (keyword == null || keyword.trim().isEmpty()) {
            listProducts(request, response);
            return;
        }

        List<Product> products = productDao.searchProducts(keyword);

        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/jsp/products.jsp").forward(request, response);
    }

    private void searchProducts2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        // 如果没有搜索关键词，则显示所有产品
        if (keyword == null || keyword.trim().isEmpty()) {
            listProducts2(request, response);
            return;
        }

        List<Product> products = productDao.searchProducts(keyword);

        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);

        // 获取当前用户
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        // 记录日志
        String details = "搜索关键字: " + keyword + ", 结果数量: " + (products != null ? products.size() : 0);
        logDao.insertLog(userId, "浏览", details);

        request.getRequestDispatcher("/jsp/product-list.jsp").forward(request, response);
    }

}
