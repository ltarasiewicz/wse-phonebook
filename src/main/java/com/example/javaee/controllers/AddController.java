package com.example.javaee.controllers;

import com.example.javaee.PhoneBookController;
import com.example.javaee.entities.ContactRecord;
import com.example.javaee.services.ServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.IOException;
import java.io.Writer;

public class AddController implements PhoneBookController {
    @Override
    public void process(
            final IServletWebExchange webExchange,
            final Writer writer,
            final ServiceFactory serviceFactory,
            final ITemplateEngine templateEngine) throws IOException {

        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());

        if (webExchange.getRequest().getMethod().equals("GET")) {
            templateEngine.process("add", ctx, writer);

            return;
        }

        var entityManager = serviceFactory.getEntityManager();
        var resp = (HttpServletResponse) webExchange.getNativeResponseObject();


        var contact = new ContactRecord(
                webExchange.getRequest().getParameterValue("name"),
                webExchange.getRequest().getParameterValue("surname"),
                webExchange.getRequest().getParameterValue("contactNumber")
        );

        // check if contact already exists
        var existingContact = entityManager.createQuery("SELECT c FROM ContactRecord c WHERE c.name = :name AND c.surname = :surname", ContactRecord.class)
                .setParameter("name", contact.getName())
                .setParameter("surname", contact.getSurname())
                .getResultList();

        // check if number already exists
        var existingNumber = entityManager.createQuery("SELECT c FROM ContactRecord c WHERE c.contactNumber = :contactNumber", ContactRecord.class)
                .setParameter("contactNumber", contact.getContactNumber())
                .getResultList();

        if (!existingContact.isEmpty() || !existingNumber.isEmpty()) {
            var session = ctx.getExchange().getSession();
            session.setAttributeValue("error", "Contact person or the phone number already exists.");
            resp.sendRedirect(webExchange.getRequest().getApplicationPath() + "/validation");
            return;
        }

        entityManager.getTransaction().begin();
        entityManager.persist(contact);
        entityManager.getTransaction().commit();


        resp.sendRedirect(webExchange.getRequest().getApplicationPath() + "/");
    }
}
