package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.HttpUtils;
import com.sceptre.projek.webapp.model.User;
import com.sceptre.projek.webapp.webservices.OrderWS;
import com.sceptre.projek.webapp.webservices.WSClient;
import org.json.JSONArray;
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

@WebServlet(name = "OrderSelectDriverServlet", urlPatterns = {"/order_select_driver"})
public class OrderSelectDriverServlet extends HttpServlet {
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
                String pickingPoint = request.getParameter("picking_point");
                String destination = request.getParameter("destination");
                String preferredDriver = request.getParameter("preferred_driver");
                String JSONResponse = orderWS.getDrivers(access_token, userAgent, ipAddress, pickingPoint, destination, preferredDriver);
                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doGet(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    String responseString = "[]";
                    try {
                        responseString = HttpUtils.requestGet("http://localhost:8003/driver", request);
                    } catch (IOException | HttpUtils.HttpUtilsException e) {
                        e.printStackTrace();
                    }
                    JSONArray onlineDrivers = new JSONArray(responseString);
                    List<User> preferredDrivers = User.extractDrivers(jsonObject.getJSONArray("preferred_drivers"), onlineDrivers);
                    List<User> otherDrivers = User.extractDrivers(jsonObject.getJSONArray("other_drivers"), onlineDrivers);
                    request.setAttribute("preferred_drivers", preferredDrivers);
                    request.setAttribute("other_drivers", otherDrivers);
                    RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/order_select_driver.jsp");
                    rs.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }
}
