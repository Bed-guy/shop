package com.example.shop.servlet;

import com.example.shop.dao.UserDao;
import com.example.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/register", "/login", "/logout", "/users", "/editUser", "/deleteUser", "/searchUser"})
public class UserServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/register":
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                break;
            case "/login":
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/users": // 新增
                handleListUsers(request, response);
                break;
            case "/editUser":
                showEditUserForm(request, response);
                break;
            case "/deleteUser":
                handleDeleteUser(request, response);
                break;
            case "/searchUser":
                handleSearchUser(request, response);
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
            case "/register":
                handleRegister(request, response);
                break;
            case "/login":
                handleLogin(request, response);
                break;
            case "/editUser":
                handleEditUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (username == null || password == null || email == null ||
                username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            request.setAttribute("error", "所有字段都是必填的！");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        if (userDao.getUserByUsername(username) != null) {
            request.setAttribute("error", "用户名已存在！");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("USER");

        boolean success = userDao.registerUser(user);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "注册失败，可能用户名或邮箱已存在！");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "所有字段都是必填的！");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        User user = userDao.loginUser(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "用户名或密码错误！");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("id");

        if (username == null || username.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        User user = userDao.getUserByUsername(username);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/jsp/editUser.jsp").forward(request, response);
        }
    }

    private void handleEditUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取表单参数
        String idStr = request.getParameter("id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        // 输入验证
        String error = null;
        if (username == null || username.isEmpty() || email == null || email.isEmpty() || role == null || role.isEmpty()) {
            error = "用户名、邮箱和角色是必填的！";
        }

        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            error = "ID格式不正确！";
        }

        // 如果有错误，返回并展示错误信息
        if (error != null) {
            request.setAttribute("error", error);
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(role);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/jsp/editUser.jsp").forward(request, response);
            return;
        }


        // 创建用户对象
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);

        boolean success = userDao.updateUserById(user);
        if (success) {
            System.out.println("用户更新成功，用户名: " + username);
            response.sendRedirect(request.getContextPath() + "/users");
        } else {
            request.setAttribute("error", "更新用户失败！");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/jsp/editUser.jsp").forward(request, response);
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        int id = 0;
        try{
            id = Integer.parseInt(idStr);
        }catch(NumberFormatException e){
            response.sendRedirect(request.getContextPath() + "/users");
        }
        boolean success = userDao.deleteUser(id);
        if (success) {
            System.out.println("用户删除成功，ID: " + id);
        } else {
            System.out.println("用户删除失败，ID: " + id);
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }

    private void handleSearchUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");

        // 如果没有搜索关键词，则显示所有用户
        if (username == null || username.trim().isEmpty()) {
            handleListUsers(request, response);
            return;
        }

        List<User> users = userDao.searchUsers(username);

        request.setAttribute("users", users);
        request.setAttribute("username", username);
        request.getRequestDispatcher("/jsp/users.jsp").forward(request, response);
    }


    private void handleListUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> userList = userDao.getAllUsers();
        request.setAttribute("users", userList);
        request.getRequestDispatcher("/jsp/users.jsp").forward(request, response);
    }
}

