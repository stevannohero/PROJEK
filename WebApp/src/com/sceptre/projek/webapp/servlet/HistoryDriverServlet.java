package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.model.Order;
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
import java.util.List;

@WebServlet(name = "HistoryDriverServlet", urlPatterns = {"/driver_history"})
public class HistoryDriverServlet extends HttpServlet {
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
                String JSONResponse = orderWS.getDriverOrders(access_token, userAgent, ipAddress);
                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doGet(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    List<Order> orders = Order.extractOrders(jsonObject.getJSONArray("driver_orders"));
                    request.setAttribute("driver_orders", orders);
                    RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/driver_history.jsp");
                    rs.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);
        if (access_token != null) {
           if (request.getParameter("method").equals("PUT")) {
               doPut(request, response);
           } else {
               response.sendRedirect("login");
           }
        } else {
            response.sendRedirect("login");
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (access_token != null) {
            OrderWS orderWS = WSClient.getOrderWS();
            if (orderWS != null) {
                int orderId = Integer.parseInt(request.getParameter("order_id"));
                String JSONResponse = orderWS.hideDriver(access_token, userAgent, ipAddress, orderId);
                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doPost(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    response.sendRedirect("driver_history");
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }
}
