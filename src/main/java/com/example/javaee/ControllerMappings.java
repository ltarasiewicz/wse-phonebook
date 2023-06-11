package com.example.javaee;

import com.example.javaee.controllers.AddController;
import com.example.javaee.controllers.DeleteController;
import com.example.javaee.controllers.HomeController;
import com.example.javaee.controllers.ValidationController;
import org.thymeleaf.web.IWebRequest;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappings {
    private static final Map<String, PhoneBookController> controllersByURL;

    static {
        controllersByURL = new HashMap<>();
        controllersByURL.put("/", new HomeController());
        controllersByURL.put("/add", new AddController());
        controllersByURL.put("/delete", new DeleteController());
        controllersByURL.put("/validation", new ValidationController());
    }

    public static PhoneBookController resolveControllerForRequest(final IWebRequest request) {
        final String path = getRequestPath(request);
        return controllersByURL.get(path);
    }

    // Path within application might contain the ";jsessionid" fragment due to URL rewriting
    private static String getRequestPath(final IWebRequest request) {

        String requestPath = request.getPathWithinApplication();

        final int fragmentIndex = requestPath.indexOf(';');
        if (fragmentIndex != -1) {
            requestPath = requestPath.substring(0, fragmentIndex);
        }

        return requestPath;
    }

    private ControllerMappings() {
        super();
    }
}
