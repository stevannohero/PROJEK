package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.HttpUtils;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("access_token", (access_token));

        try {
            HttpUtils.requestPost(HttpUtils.getIdentityServiceUrl("/logout"), body, request);

            CookieManager.deleteCookies(request, response);
            response.sendRedirect("login");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            response.sendRedirect("profile");
        }
    }
}
