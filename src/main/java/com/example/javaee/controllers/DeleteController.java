package com.example.javaee.controllers;

import com.example.javaee.PhoneBookController;
import com.example.javaee.services.ServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.IOException;
import java.io.Writer;

public class DeleteController implements PhoneBookController {
    @Override
    public void process(
            final IServletWebExchange webExchange,
            final Writer writer,
            final ServiceFactory serviceFactory,
            final ITemplateEngine templateEngine) throws IOException {


        var deleteID = webExchange.getRequest().getParameterValue("deleteID");

        var entityManager = serviceFactory.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM ContactRecord c WHERE c.id = :deleteID")
                .setParameter("deleteID", Long.parseLong(deleteID))
                .executeUpdate();
        entityManager.getTransaction().commit();

        var resp = (HttpServletResponse) webExchange.getNativeResponseObject();

        resp.sendRedirect(webExchange.getRequest().getApplicationPath() + "/");
    }
}
