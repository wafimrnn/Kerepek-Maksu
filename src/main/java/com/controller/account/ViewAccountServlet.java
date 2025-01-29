package com.controller.account;

import com.model.User;
import com.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ViewAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ViewAccountServlet() {
        super();
    }

    /**
     * Handles GET requests to display accounts.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect("Login.html"); // Redirect to login page if user is not logged in
            return;
        }

        // Fetch the updated user information from the database
        UserDAO userDAO = new UserDAO();
        User updatedUser = userDAO.getUserById(sessionUser.getId());

        // Update session data with the latest user details
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
        }

        // Fetch staff list (if applicable) and set as request attribute
        List<User> staffList = userDAO.getStaffByOwnerId(sessionUser.getId());
        request.setAttribute("staffList", staffList);

        // Forward to the JSP page
        request.getRequestDispatcher("/ViewAccount.jsp").forward(request, response);
    }
}
