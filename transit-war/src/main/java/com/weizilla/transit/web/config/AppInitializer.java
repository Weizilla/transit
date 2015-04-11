package com.weizilla.transit.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer
{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
        AnnotationConfigWebApplicationContext appContext =
            new AnnotationConfigWebApplicationContext();
        appContext.register(AppConfig.class);
        servletContext.addListener(new ContextLoaderListener(appContext));

        AnnotationConfigWebApplicationContext mvcContext =
            new AnnotationConfigWebApplicationContext();
        mvcContext.register(WebMvcConfig.class);

        ServletRegistration.Dynamic dispatcher =
            servletContext.addServlet("dispatcher", new DispatcherServlet(mvcContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
    }
}
