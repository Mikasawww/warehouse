package mmc.warehousemanagementsystem.utils;

import mmc.warehousemanagementsystem.entity.Admins;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Dantence
 * @date 2022/9/7
 */
public class commonUtils {
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static Admins getUserFromSession(HttpServletRequest request) {
        return (Admins) request.getSession().getAttribute("user");
    }

}
