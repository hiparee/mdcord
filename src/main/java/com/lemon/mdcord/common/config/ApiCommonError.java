package com.lemon.mdcord.common.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiCommonError implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
        ErrorPage errorPage401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/index.html");
        registry.addErrorPages(errorPage404, errorPage401);
    }

}
