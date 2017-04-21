package com.tolochko.periodicals.controller.request.sign;

import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.model.domain.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplaySignUpPage implements RequestProcessor {
    private static final DisplaySignUpPage instance = new DisplaySignUpPage();

    private DisplaySignUpPage(){}

    public static DisplaySignUpPage getInstance() {
        return instance;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("roles", User.Role.values());
        return FORWARD + "signUp";
    }
}
