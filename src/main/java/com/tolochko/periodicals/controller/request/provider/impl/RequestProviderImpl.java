package com.tolochko.periodicals.controller.request.provider.impl;


import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.controller.request.provider.RequestProvider;
import com.tolochko.periodicals.controller.util.HttpUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

// provides mapping request uri to classes which work with user data
public final class RequestProviderImpl implements RequestProvider {
    private static final Logger logger = Logger.getLogger(RequestProviderImpl.class);
    private static final Map<String, RequestProcessor> requestMapping = new HashMap<>();
    private static final RequestProviderImpl instance = new RequestProviderImpl();

    static {
        // TODO: 11.04.2017 put request mapping instances into the map!!!
        requestMapping.put(null,null);
    }

    private RequestProviderImpl() {
    }

    public static final RequestProviderImpl getInstance() {
        return instance;
    }

    /**
     * Determines a specific reques processor to process the request depending on the current
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
        if (currentMapping.isPresent()){
            logger.info("found processor ");
            return currentMapping.get().getValue();
        }


        // if wrong request
        logger.error("There no mapping for such a request: " + request.getRequestURI());
        throw new NoSuchElementException("no mapping for " + request.getRequestURI());
    }
}
