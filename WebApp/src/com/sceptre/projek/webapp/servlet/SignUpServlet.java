package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.HttpUtils;
import com.sceptre.projek.webapp.webservices.UserWS;
import com.sceptre.projek.webapp.webservices.WSClient;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "SignUpServlet", urlPatterns = {"/signup"})
public class SignUpServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (CookieManager.getAccessToken(request) != null) {
            response.sendRedirect("profile");
        } else {
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/signup.jsp");
            rs.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phone_number");
        boolean isDriver = request.getParameter("is_driver") != null;

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        Map<String, String> body = new LinkedHashMap<>();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);

        String responseString;
        try {
            responseString = HttpUtils.requestPost(HttpUtils.getIdentityServiceUrl("/signup"), body, request);

            JSONObject jsonObject = new JSONObject(responseString);
            String refresh_token = jsonObject.getString("refresh_token");
            String access_token = jsonObject.getString("access_token");
            Timestamp expiry_time = Timestamp.valueOf(jsonObject.getString("expiry_time"));

            signUp(access_token, userAgent, ipAddress, name, username, email, phoneNumber, isDriver);

            Cookie access_token_cookie = new Cookie("access_token", URLEncoder.encode(access_token, "UTF-8"));
            Cookie refresh_token_cookie = new Cookie("refresh_token", URLEncoder.encode(refresh_token, "UTF-8"));
            Cookie expiry_time_cookie = new Cookie("expiry_time", expiry_time.toString());
            Cookie username_cookie = new Cookie("username", request.getParameter("username"));

            response.addCookie(access_token_cookie);
            response.addCookie(refresh_token_cookie);
            response.addCookie(expiry_time_cookie);
            response.addCookie(username_cookie);

            if (isDriver) {
                response.sendRedirect("profile");
            } else {
                response.sendRedirect("order");
            }

        } catch (IOException e) {
            request.setAttribute("error", e.getMessage());
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/signup.jsp");
            rs.forward(request, response);
        } catch (HttpUtils.HttpUtilsException e) {
            if (e.status == 401) {
                request.setAttribute("error", "Username or email is already taken.");
            } else {
                request.setAttribute("error", e.getMessage());
            }
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/signup.jsp");
            rs.forward(request, response);
        }
    }

    /**
     * Registers a user.
     *
     * @param access_token Access token used for authentication.
     * @param name         Name of the user.
     * @param username     Username of the user.
     * @param email        Email of the user.
     * @param phoneNumber  Phone number of the user.
     * @param isDriver     Driver status.
     */
    private void signUp(String access_token, String userAgent, String ipAddress, String name, String username, String email, String phoneNumber, boolean isDriver) {
        UserWS userWS = WSClient.getUserWS();
        if (userWS != null) {
            userWS.store(access_token, userAgent, ipAddress, name, username, email, phoneNumber, isDriver);
        }
    }
}
