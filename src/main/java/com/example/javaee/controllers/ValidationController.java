package com.example.javaee.controllers;

import com.example.javaee.PhoneBookController;
import com.example.javaee.services.ServiceFactory;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.Writer;

public class ValidationController implements PhoneBookController {
    @Override
    public void process(
            final IServletWebExchange webExchange,
            final Writer writer,
            final ServiceFactory serviceFactory,
            final ITemplateEngine templateEngine) {

        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        templateEngine.process("validation", ctx, writer);
    }
}
