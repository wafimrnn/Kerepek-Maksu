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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user from the session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("Login.html"); // Redirect to login page if user is not found
        }

        // Check if user is logged in (user object should not be null)
        if (user != null) {
            // Retrieve the staff list for the owner
            UserDAO userDAO = new UserDAO();
            List<User> staffList = userDAO.getStaffByOwnerId(user.getId());

            // Set user and staffList as attributes to pass to the JSP
            request.setAttribute("user", user);
            request.setAttribute("staffList", staffList);

            // Forward to the JSP page
            request.getRequestDispatcher("/ViewAccount.jsp").forward(request, response);
        } else {
            // If user is not logged in, redirect to login page or show an error
            response.sendRedirect("Login.html");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
