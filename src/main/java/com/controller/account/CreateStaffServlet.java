package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;
import com.model.User;

public class CreateStaffServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser != null && "OWNER".equals(currentUser.getRole())) {
            String username = request.getParameter("userName");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            UserDAO userDAO = new UserDAO();
            boolean isCreated = userDAO.createStaff(username, password, phone, address, currentUser.getId());

            if (isCreated) {
                response.sendRedirect("DashboardHome.jsp?success=Staff created successfully");
            } else {
                request.setAttribute("error", "Failed to create staff account.");
                request.getRequestDispatcher("CreateStaff.html").forward(request, response);
            }
        } else {
            response.sendRedirect("Login.html"); // Redirect to login if not logged in as owner.
        }
    }
}
