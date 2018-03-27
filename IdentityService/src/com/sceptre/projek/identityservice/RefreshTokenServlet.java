package com.sceptre.projek.identityservice;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RefreshTokenServlet", urlPatterns = {"/refresh_token"})
public class RefreshTokenServlet extends HttpServlet {
    /**
     * Generates a new access token for the given refresh token.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String refreshToken = request.getParameter("refresh_token");
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        try {
            JSONObject json;
            json = AuthManager.regenerateAccessToken(refreshToken, userAgent, ipAddress);
            Utils.sendJsonResponse(response, json);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
