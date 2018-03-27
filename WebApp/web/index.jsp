<%@ page import="com.sceptre.projek.webapp.CookieManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%
    // New location to be redirected
    if (CookieManager.getAccessToken(request) != null) {
        response.sendRedirect("profile");
    } else {
        response.sendRedirect("login");
    }
  %>
