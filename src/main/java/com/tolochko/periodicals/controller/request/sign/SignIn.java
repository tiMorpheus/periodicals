package com.tolochko.periodicals.controller.request.sign;

// TODO: 13.04.2017 comments 
// TODO: 13.04.2017 UserService realization 

import com.tolochko.periodicals.controller.message.FrontMessage;
import com.tolochko.periodicals.controller.message.FrontMessageFactory;
import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.controller.util.HttpUtil;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.UserService;
import com.tolochko.periodicals.model.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Performs validation of the username, checks the password for correctness, checks that
 * this user is active (not blocked) and if everything is OK, adds this user into the session.
 */
public final class SignIn implements RequestProcessor {
    private static final Logger logger = Logger.getLogger(SignIn.class);
    private UserService userService = UserServiceImpl.getInstance();
    private FrontMessageFactory messageFactory = FrontMessageFactory.getInstance();
    private static final SignIn instance = new SignIn();

    private SignIn() {
    }

    public static SignIn getInstance() {
        return instance;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        Map<String, FrontMessage> messages = new HashMap<>();
        String redirectUri;

        if (isSignIfUserCorrect(request)) {

            logger.info("after verifying users data");
            redirectUri = signInIfUserIsActive(request, messages);
            logger.info("got redirect");
        } else {
            addErrorMessages(messages);
            redirectUri = "/login.jsp";
        }

        setSessionAttributes(request, messages);
        return REDIRECT + redirectUri;
    }

    private void setSessionAttributes(HttpServletRequest request, Map<String, FrontMessage> messages) {
        HttpSession session = request.getSession();
        String username = request.getParameter("signInUsername");

        session.setAttribute("username", username);
        session.setAttribute("messages", messages);
    }


    private boolean isSignIfUserCorrect(HttpServletRequest request) {
        String username = request.getParameter("signInUsername");
        String password = request.getParameter("password");

        User user = userService.findOneUserByUserName(username);

        logger.debug(user.toString());

        return nonNull(user) && isPasswordCorrect(password, user);
    }

    private String signInIfUserIsActive(HttpServletRequest request, Map<String, FrontMessage> messages) {
        String username = request.getParameter("signInUsername");
        User currentUser = userService.findOneUserByUserName(username);
        String redirectUri;

        if (isUserActive(currentUser)) {
            redirectUri = signInUserAndGetRedirectUri(request, currentUser);
        } else {
            redirectUri = "/login.jsp";
            messages.put("signInUsername", messageFactory.getError("error.userIsBlocked"));
        }

        return redirectUri;
    }

    private boolean isPasswordCorrect(String password, User user) {
        return HttpUtil.getPasswordHash(password).equals(user.getPassword());
    }

    private boolean isUserActive(User currentUser) {
        return currentUser.getStatus() == User.Status.ACTIVE;
    }

    private String signInUserAndGetRedirectUri(HttpServletRequest request, User currentUser) {
        HttpSession session = request.getSession();

        String redirectUri = getRedirectUri(request, currentUser);
        session.setAttribute("currentUser", currentUser);
        session.removeAttribute("originalUri");

        return redirectUri;
    }

    private String getRedirectUri(HttpServletRequest request, User currentUser) {
        String originalUri = (String) request.getSession().getAttribute("originalUri");

        String defaultUri = currentUser.hasRole(User.Role.ADMIN) ? "/app/adminPanel"
                : "/app/users/currentUser";

        return (nonNull(originalUri)) && (!"/app/signOut".equals(originalUri))
                ? originalUri : defaultUri;
    }

    private void addErrorMessages(Map<String, FrontMessage> messages) {
        messages.put("signInUsername",
                messageFactory.getError("validation.credentialsAreNotCorrect"));
        messages.put("password",
                messageFactory.getError("validation.credentialsAreNotCorrect"));
    }


}