package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.HttpUtils;
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
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "CompleteOrderServlet", urlPatterns = {"/complete_order"})
public class CompleteOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                String pickingPoint = request.getParameter("picking_point");
                String destination = request.getParameter("destination");
                int rating = Integer.parseInt(request.getParameter("star"));
                String comment = request.getParameter("comment");
                String JSONResponse = orderWS.completeOrder(access_token, userAgent, ipAddress, driverId, pickingPoint, destination, rating, comment);

                int authResult = WSClient.checkAuth(request, response, new JSONObject(JSONResponse));
                if (authResult == WSClient.AUTH_RETRY) {
                    doPost(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    Map<String, String> body = new LinkedHashMap<>();
                    body.put("driverUsername", request.getParameter("driverUsername"));
                    body.put("customer", CookieManager.getUsername(request));

                    String responseString;
                    try {
                        responseString = HttpUtils.requestPost("http://localhost:8003/completeOrder", body, request);
                        responseString = HttpUtils.requestDelete("http://localhost:8003/user/" + CookieManager.getUsername(request), request);
                        response.sendRedirect("my_previous_order");
                    } catch (IOException e) {
                        request.setAttribute("error", e.getMessage());
                        RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/complete_order.jsp");
                        rs.forward(request, response);
                    } catch (HttpUtils.HttpUtilsException e) {
                        RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/complete_order.jsp");
                        rs.forward(request, response);
                    }
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }

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
                    RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/complete_order.jsp");
                    rs.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }
}
