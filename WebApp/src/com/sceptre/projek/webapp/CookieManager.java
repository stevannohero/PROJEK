package com.sceptre.projek.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;

public class CookieManager {
    public static String getAccessToken(HttpServletRequest request) throws ServletException, IOException {
        String access_token = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    access_token = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }
        }
        return access_token;
    }

    public static Cookie getAccessTokenCookie(HttpServletRequest request) throws ServletException, IOException {
        Cookie access_token = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    access_token = cookie;
                    break;
                }
            }
        }
        return access_token;
    }

    public static Cookie getExpiryTimeCookie(HttpServletRequest request) throws ServletException, IOException {
        Cookie expiry_time = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("expiry_time")) {
                    expiry_time = cookie;
                    break;
                }
            }
        }
        return expiry_time;
    }

    public static String getUsername(HttpServletRequest request) throws ServletException, IOException {
        String username = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        return username;
    }

    public static String getRefreshToken(HttpServletRequest request) throws ServletException, IOException {
        String refresh_token = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    refresh_token = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }
        }
        return refresh_token;
    }

    public static void deleteCookies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if( cookies != null ) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                else if (cookie.getName().equals("username")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                else if (cookie.getName().equals("refresh_token")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                else if (cookie.getName().equals("expiry_time")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
