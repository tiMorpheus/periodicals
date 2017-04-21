package com.tolochko.periodicals.controller;


import com.tolochko.periodicals.controller.request.RequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Processes a GET request to a backend main page.
 */
public class DisplayBackendHomePage implements RequestProcessor {

    private static final DisplayBackendHomePage instance = new DisplayBackendHomePage();

    private DisplayBackendHomePage(){}

    public static DisplayBackendHomePage getInstance() {
        return instance;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        return FORWARD + "home";
    }
}
