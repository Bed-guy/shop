package com.example.shop.servlet;

import com.example.shop.dao.LogDao;
import com.example.shop.model.UserLog;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/userLogs"})
public class UserLogServlet extends HttpServlet {

    private LogDao logDao;

    @Override
    public void init() {
        logDao = new LogDao(); // 初始化LogDao
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取所有日志记录
        List<UserLog> logs = logDao.getAllLogs();

        // 将日志记录添加到请求属性中
        request.setAttribute("logs", logs);

        // 转发到日志展示页面
        request.getRequestDispatcher("/jsp/userLogs.jsp").forward(request, response);
    }
}
