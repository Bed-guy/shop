package com.example.shop.servlet;

import com.example.shop.dao.SalesDao;
import com.example.shop.dao.SalesDao.ProductSales;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/salesChart"})
public class SalesServlet extends HttpServlet {

    private SalesDao salesDao;

    @Override
    public void init() {
        salesDao = new SalesDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取商品销量数据
        List<ProductSales> salesData = salesDao.getProductSales();
        System.out.println(salesData.get(1).getTotalSales() + salesData.get(1).getProductName());

        // 将数据传递给JSP
        request.setAttribute("salesData", salesData);
        request.getRequestDispatcher("/jsp/salesChart.jsp").forward(request, response);
    }
}

