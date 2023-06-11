package com.example.javaee.controllers;

import com.example.javaee.PhoneBookController;
import com.example.javaee.entities.ContactRecord;
import com.example.javaee.services.ServiceFactory;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.Writer;
import java.util.List;

public class HomeController implements PhoneBookController {
    @Override
    public void process(
            final IServletWebExchange webExchange,
            final Writer writer,
            final ServiceFactory serviceFactory,
            final ITemplateEngine templateEngine) {

        var entityManager = serviceFactory.getEntityManager();

        List<ContactRecord> contacts;

        var searchHint = webExchange.getRequest().getParameterValue("search");

        if (searchHint != null) {
            contacts = entityManager.createQuery("SELECT c FROM ContactRecord c WHERE c.name LIKE :searchHint OR c.surname LIKE :searchHint OR c.contactNumber LIKE :searchHint", ContactRecord.class)
                    .setParameter("searchHint", "%" + searchHint + "%")
                    .getResultList();
        } else {
            contacts = entityManager.createQuery("SELECT c FROM ContactRecord c", ContactRecord.class).getResultList();
        }

        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("contacts", contacts);

        templateEngine.process("home", ctx, writer);
    }
}
