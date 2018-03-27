package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.model.User;
import com.sceptre.projek.webapp.webservices.OrderWS;
import com.sceptre.projek.webapp.webservices.WSClient;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChatDriverServlet", urlPatterns = {"/chat_driver"})
public class ChatDriverServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (access_token != null) {
            OrderWS orderWS = WSClient.getOrderWS();
            if (orderWS != null) {
                int driverId = Integer.parseInt(request.getParameter("driver_id"));
                String JSONResponse = orderWS.getDriver(access_token, userAgent, ipAddress, driverId);
                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doGet(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    User driver = new User(jsonObject.getJSONObject("driver"));
                    request.setAttribute("driver", driver);
                    RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/chat_driver.jsp");
                    rs.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }
}
