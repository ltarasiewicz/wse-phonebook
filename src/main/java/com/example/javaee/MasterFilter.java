package com.example.javaee;

import com.example.javaee.entities.ContactRecord;
import com.example.javaee.services.ServiceFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.Writer;

public class MasterFilter implements Filter {
  private ITemplateEngine templateEngine;
  private JakartaServletWebApplication application;

  private EntityManagerFactory entityManagerFactory;

  private ServiceFactory serviceFactory;

  public MasterFilter() {
    super();
  }

  @Override
  public void init(final FilterConfig filterConfig) {
    this.application = JakartaServletWebApplication.buildApplication(filterConfig.getServletContext());
    this.templateEngine = createTemplateEngine(this.application);
    this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    ServiceFactory.initialize(entityManagerFactory);
    this.serviceFactory = ServiceFactory.getInstance();

    var entityManager = this.serviceFactory.getEntityManager();

    // Populate the database with some sample data
    var firstContact = new ContactRecord("John", "Bean", "123456789");
    var secondContact = new ContactRecord("Jane", "Doe", "987654321");
    var thirdContact = new ContactRecord("John", "Dove", "123123123");
    var fourthContact = new ContactRecord("Jane", "Bean", "321321321");
    var fifthContact = new ContactRecord("John", "Doe", "456456456");

    try {
      entityManager.getTransaction().begin();
      entityManager.persist(firstContact);
      entityManager.persist(secondContact);
      entityManager.persist(thirdContact);
      entityManager.persist(fourthContact);
      entityManager.persist(fifthContact);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      // Do nothing, the database is already populated
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    try {
      if (!process((HttpServletRequest)request, (HttpServletResponse)response)) {
        chain.doFilter(request, response);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void destroy() {
    this.entityManagerFactory.close();
  }


  private boolean process(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {

      final IServletWebExchange webExchange =
              this.application.buildExchange(request, response);
      final IWebRequest webRequest = webExchange.getRequest();

      // This prevents triggering engine executions for resource URLs
      if (request.getRequestURI().startsWith("/css") ||
              request.getRequestURI().startsWith("/images") ||
              request.getRequestURI().startsWith("/favicon")) {
        return false;
      }

      /*
       * Query controller/URL mapping and obtain the controller
       * that will process the request. If no controller is available,
       * return false and let other filters/servlets process the request.
       */
      final PhoneBookController controller = ControllerMappings.resolveControllerForRequest(webRequest);
      if (controller == null) {
        return false;
      }

      /*
       * Write the response headers
       */
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);

      /*
       * Obtain the response writer
       */
      final Writer writer = response.getWriter();

      /*
       * Execute the controller and process view template,
       * writing the results to the response writer.
       */
      controller.process(webExchange, writer, this.serviceFactory, this.templateEngine);

      return true;

    } catch (Exception e) {
      try {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      } catch (final IOException ignored) {
        // Just ignore this
      }
      throw new ServletException(e);
    }
  }

  private static ITemplateEngine createTemplateEngine(final IWebApplication webApplication) {
    var templateResolver = new WebApplicationTemplateResolver(webApplication);
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);

    var templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);

    return templateEngine;
  }
}
