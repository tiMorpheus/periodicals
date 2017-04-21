package com.tolochko.periodicals.controller.request.provider.impl;


import com.tolochko.periodicals.controller.DisplayBackendHomePage;
import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.controller.request.admin.panel.DisplayAdminPanel;
import com.tolochko.periodicals.controller.request.invoice.PayOneInvoice;
import com.tolochko.periodicals.controller.request.invoice.PersistOneInvoice;
import com.tolochko.periodicals.controller.request.periodicals.*;
import com.tolochko.periodicals.controller.request.provider.RequestProvider;
import com.tolochko.periodicals.controller.request.sign.DisplaySignUpPage;
import com.tolochko.periodicals.controller.request.sign.SignIn;
import com.tolochko.periodicals.controller.request.sign.SignOut;
import com.tolochko.periodicals.controller.request.user.CreateUser;
import com.tolochko.periodicals.controller.request.user.DisplayAllUsers;
import com.tolochko.periodicals.controller.request.user.DisplayCurrentUser;
import com.tolochko.periodicals.controller.util.HttpUtil;
import com.tolochko.periodicals.controller.validation.AjaxFormValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

// TODO: 21.04.2017 no static
// provides mapping request uri to classes which work with user data
public final class RequestProviderImpl implements RequestProvider {
    private static final Logger logger = Logger.getLogger(RequestProviderImpl.class);
    private static final Map<String, RequestProcessor> requestMapping = new HashMap<>();
    private static final RequestProviderImpl instance = new RequestProviderImpl();

    static {

        requestMapping.put("GET:/app/?", DisplayBackendHomePage.getInstance());
        requestMapping.put("POST:/app/signIn/?", SignIn.getInstance());
        requestMapping.put("GET:/app/signOut/?", SignOut.getInstance());
        requestMapping.put("GET:/app/signUp/?", DisplaySignUpPage.getInstance());

        requestMapping.put("POST:/app/signUp/?", CreateUser.getInstance());
        requestMapping.put("GET:/app/users/currentUser/?", DisplayCurrentUser.getInstance());
        requestMapping.put("GET:/app/users/?", DisplayAllUsers.getInstance());

        requestMapping.put("GET:/app/adminPanel/?", DisplayAdminPanel.getInstance());

        requestMapping.put("GET:/app/periodicals/?", DisplayAllPeriodicals.getInstance());
        requestMapping.put("GET:/app/periodicals/\\d+", DisplayOnePeriodical.getInstance());
        requestMapping.put("GET:/app/periodicals/createNew/?", DisplayNewPeriodicalPage.getInstance());
        requestMapping.put("GET:/app/periodicals/\\d+/update/?", DisplayUpdatePeriodicalPage.getInstance());
        requestMapping.put("POST:/app/periodicals/?", PersistOnePeriodical.getInstance());
        requestMapping.put("POST:/app/periodicals/discarded/?", DeleteDiscardedPeriodicals.getInstance());

        requestMapping.put("POST:/app/users/\\d+/invoices/?", PersistOneInvoice.getInstance());
        requestMapping.put("POST:/app/users/\\d+/invoices/\\d+/pay/?", PayOneInvoice.getInstance());

        requestMapping.put("POST:/app/validation", AjaxFormValidation.getInstance());

    }

    private RequestProviderImpl() {
    }

    public static final RequestProviderImpl getInstance() {
        return instance;
    }

    /**
     * Determines a specific request processor to process the request depending on the current
     * http method and uri
     *
     * @param request http request
     * @return implementation of {@link RequestProcessor} interface
     */
    @Override
    public RequestProcessor getRequestProcessor(HttpServletRequest request) {
        Optional<Map.Entry<String, RequestProcessor>> currentMapping =
                requestMapping.entrySet().stream()
                        .filter(entry ->
                                HttpUtil.filterRequestByHttpMethod(request, entry.getKey()))
                        .filter(entry ->
                                HttpUtil.filterRequestByUri(request, entry.getKey()))
                        .findFirst();

        // if mapping was found
        if (currentMapping.isPresent()) {
            logger.info("found processor ");
            return currentMapping.get().getValue();
        }


        // if wrong request
        logger.error("There no mapping for such a request: " + request.getRequestURI());
        throw new NoSuchElementException("no mapping for " + request.getRequestURI());
    }
}
