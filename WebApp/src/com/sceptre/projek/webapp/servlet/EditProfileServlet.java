package com.sceptre.projek.webapp.servlet;

import com.sceptre.projek.webapp.CookieManager;
import com.sceptre.projek.webapp.model.User;
import com.sceptre.projek.webapp.webservices.UserWS;
import com.sceptre.projek.webapp.webservices.WSClient;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Scanner;

@WebServlet(name = "EditProfileServlet", urlPatterns = {"/edit_profile"})
@MultipartConfig
public class EditProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (access_token != null) {
            UserWS userWS = WSClient.getUserWS();
            if (userWS != null) {
                String JSONResponse = userWS.getUserDetails(access_token, userAgent, ipAddress);
                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doGet(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    User user = new User(jsonObject);
                    request.setAttribute("user", user);
                    RequestDispatcher rs = request.getRequestDispatcher("/WEB-INF/view/edit_profile.jsp");
                    rs.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String access_token = CookieManager.getAccessToken(request);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (access_token != null) {
            UserWS userWS = WSClient.getUserWS();
            if (userWS != null) {
                InputStream inputStream;
                Scanner s;

                // Convert uploaded image to base64
                String profilePictureBase64 = "";
                Part filePart = request.getPart("profile_picture");
                if (filePart != null && filePart.getSize() > 0) {
                    profilePictureBase64 = "data:" + filePart.getContentType() + ";base64,";
                    inputStream = filePart.getInputStream();
                    byte[] imageBytes = new byte[(int) filePart.getSize()];
                    int readRetVal = inputStream.read(imageBytes, 0, imageBytes.length);
                    inputStream.close();
                    profilePictureBase64 += Base64.getEncoder().encodeToString(imageBytes);
                }

                Part namePart = request.getPart("name");
                s = new Scanner(namePart.getInputStream()).useDelimiter("\\A");
                String name = s.hasNext() ? s.next() : "";

                Part phonePart = request.getPart("phone");
                s = new Scanner(phonePart.getInputStream()).useDelimiter("\\A");
                String phone = s.hasNext() ? s.next() : "";

                Part isDriverPart = request.getPart("is_driver");
                boolean is_driver = isDriverPart != null;

                // Update profile to database
                String JSONResponse = userWS.update(access_token, userAgent, ipAddress, name, phone, is_driver, profilePictureBase64);

                JSONObject jsonObject = new JSONObject(JSONResponse);
                int authResult = WSClient.checkAuth(request, response, jsonObject);
                if (authResult == WSClient.AUTH_RETRY) {
                    doPost(request, response);
                } else if (authResult == WSClient.AUTH_OK) {
                    response.sendRedirect("profile");
                }
            }
        } else {
            response.sendRedirect("login");
        }
    }
}
