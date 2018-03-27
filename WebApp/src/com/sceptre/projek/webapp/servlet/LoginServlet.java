package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.HttpUtils;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends javax.servlet.http.HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (CookieManager.getAccessToken(request) != null) {
            response.sendRedirect("profile");
        } else {
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
            rs.forward(request, response);
        }
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("username", request.getParameter("username"));
        body.put("password", request.getParameter("password"));

        String responseString;
        try {
            responseString = HttpUtils.requestPost(HttpUtils.getIdentityServiceUrl("/login"), body, request);

            JSONObject responseJson = new JSONObject(responseString);
            String refresh_token = responseJson.getString("refresh_token");
            String access_token = responseJson.getString("access_token");
            Timestamp expiry_time = Timestamp.valueOf(responseJson.getString("expiry_time"));

            Cookie access_token_cookie = new Cookie("access_token", URLEncoder.encode(access_token, "UTF-8"));
            Cookie refresh_token_cookie = new Cookie("refresh_token", URLEncoder.encode(refresh_token, "UTF-8"));
            Cookie expiry_time_cookie = new Cookie("expiry_time", expiry_time.toString());
            Cookie username_cookie = new Cookie("username", request.getParameter("username"));

            response.addCookie(access_token_cookie);
            response.addCookie(refresh_token_cookie);
            response.addCookie(expiry_time_cookie);
            response.addCookie(username_cookie);

            response.sendRedirect("profile");

        } catch (IOException e) {
            request.setAttribute("error", e.getMessage());
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
            rs.forward(request, response);
        } catch (HttpUtils.HttpUtilsException e) {
            if (e.status == 401) {
                request.setAttribute("error", "Wrong username or password.");
            } else {
                request.setAttribute("error", e.getMessage());
            }
            RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
            rs.forward(request, response);
        }
    }

}
