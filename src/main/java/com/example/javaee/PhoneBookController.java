package com.example.javaee;

import com.example.javaee.services.ServiceFactory;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.Writer;

public interface PhoneBookController {
    void process(
            final IServletWebExchange webExchange,
            final Writer writer,
            final ServiceFactory serviceFactory,
            final ITemplateEngine templateEngine)
            throws Exception;
}
