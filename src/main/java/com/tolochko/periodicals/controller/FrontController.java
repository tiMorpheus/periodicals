package com.tolochko.periodicals.controller;

import com.tolochko.periodicals.controller.request.provider.RequestProvider;
import com.tolochko.periodicals.controller.request.provider.impl.RequestProviderImpl;
import com.tolochko.periodicals.controller.util.HttpUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontController.class);
    private RequestProvider requestProvider = RequestProviderImpl.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("user GET");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("user POST");
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String abstractViewName = requestProvider.getRequestProcessor(req).process(req, resp);
            dispatch(abstractViewName, req, resp);
        } catch (RuntimeException e) {
            redirectUserToErrorPageAndLogException(req, resp, e);
        }
    }


    private void dispatch(String viewName, HttpServletRequest request, HttpServletResponse response) {
        // TODO: 11.04.2017 realization 

    }

    private void redirectUserToErrorPageAndLogException(HttpServletRequest req, HttpServletResponse resp, RuntimeException e) {

        logger.error("User " + req.getSession().getAttribute("currentUser") + ". Request uri "+ req.getRequestURI(), e);

        HttpUtil.sendRedirect(req,resp,);
    }
}
