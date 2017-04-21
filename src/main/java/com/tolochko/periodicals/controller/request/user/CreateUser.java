package com.tolochko.periodicals.controller.request.user;

import com.tolochko.periodicals.controller.message.FrontMessage;
import com.tolochko.periodicals.controller.message.FrontMessageFactory;
import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.controller.util.HttpUtil;
import com.tolochko.periodicals.controller.validation.ValidationFactory;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.UserService;
import com.tolochko.periodicals.model.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;


// TODO: 13.04.2017 comments 
// TODO: 13.04.2017 service impl 
// TODO: 13.04.2017 logger
// TODO: 13.04.2017 fix process method on If/else constr



public class CreateUser implements RequestProcessor {
    private static final Logger logger = Logger.getLogger(CreateUser.class);
    private static final CreateUser instance = new CreateUser();
    private UserService userService = UserServiceImpl.getInstance();
    private FrontMessageFactory messageFactory = FrontMessageFactory.getInstance();

    private CreateUser(){

    }

    public static CreateUser getInstance() {
        return instance;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        Map<String, FrontMessage> formMessages = new HashMap<>();
        List<FrontMessage> generalMessages = new ArrayList<>();
        HttpSession session = request.getSession();
        String redirectUri = "/app/signUp";

        // form data
        String username = request.getParameter("signUpUsername");
        String userEmail = request.getParameter("userEmail");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");


        if (!arePasswordsValidAndEqual(password,repeatPassword)){
            formMessages.put("password",
                    messageFactory.getError("validation.passwordsAreNotEqual"));
        } else if (usernameExistsInDb(username)){
            formMessages.put("signUpUsername",
                    messageFactory.getError("validation.usernameIsNotUnique"));
        } else {
            boolean isNewUserCreated = createUser(username, userEmail, password);
            if (isNewUserCreated){
                redirectUri = "/login.jsp";
            } else {
                generalMessages.add(messageFactory.getError("userWasNotCreated.error"));
            }
        }

        if ("/app/signUp".equals(redirectUri)) {
            HttpUtil.addGeneralMessagesToSession(request, generalMessages);

            session.setAttribute("username", username);

            session.setAttribute("userEmail", userEmail);
            session.setAttribute("messages", formMessages);
        }

        return REDIRECT + redirectUri;
    }

    private boolean createUser(String username, String userEmail, String password) {
        User.Builder builder =  new User.Builder();
        builder.setUsername(username)
                .setEmail(userEmail)
                .setPassword(HttpUtil.getPasswordHash(password))
                .setStatus(User.Status.ACTIVE);
        // TODO: 13.04.2017 BUILD user

        logger.debug("user is builded user: ");
        logger.debug(builder.build());

        return userService.createNewUser(builder.build());
    }

    private boolean arePasswordsValidAndEqual(String password, String repeatPassword) {
        int validationResult = ValidationFactory.getUserPasswordValidator().validate(password, null).getStatusCode();
        return (validationResult == 200) && password.equals(repeatPassword);
    }

    private boolean usernameExistsInDb(String username) {
        return nonNull(userService.findOneUserByUserName(username));
    }
}
