package com.tolochko.periodicals.controller.util;

import com.tolochko.periodicals.model.domain.user.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

// helper class
public final class HttpUtil {
    private static final Logger LOGGER = Logger.getLogger(HttpUtil.class);

    private HttpUtil() {
    }

    /**
     * Sends a redirect on this response.
     */
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response,
                                    String redirectUri) {
        try {
            response.sendRedirect(redirectUri);

        } catch (IOException e) {
            String message = String.format("User id = %d. Exception during redirection to '%s'.",
                    HttpUtil.getUserIdFromSession(request), redirectUri);

            throw new RuntimeException(message, e);
        }
    }

    /**
     * Retrieves a current user's id from the session.
     * @return id of the current signed in user or 0 if a user has not been authenticated yet
     */
    public static long getUserIdFromSession(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("currentUser");
        return nonNull(user) ? user.getId() : 0;
    }

    public static boolean filterRequestByHttpMethod(HttpServletRequest request, String mapping) {
        String methodPattern = mapping.split(":")[0]; // methods uri separator
        String[] methods = methodPattern.split("\\|"); // method separator
        String requestMethod = request.getMethod().toUpperCase();

        return Arrays.asList(methods).contains(requestMethod);
    }

    public static boolean filterRequestByUri(HttpServletRequest request, String mapping) {
        String urlPattern = mapping.split(":")[1]; // methods uri separator
        String requestUri = request.getRequestURI();

        return Pattern.matches(urlPattern, requestUri);
    }

}
