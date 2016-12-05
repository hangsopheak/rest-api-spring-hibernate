package com.sample.server;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sample.repo.service.PersonService;

public class AppListener implements ServletRequestListener {
    
    private Logger LOG = LoggerFactory.getLogger(AppListener.class);
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        LOG.info("=============requestDestroyed=========");
        ServletContext ctx = sre.getServletContext();
        //ApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        System.out.println(Arrays.asList(springContext.getBeanDefinitionNames()));

        
        PersonService personService = (PersonService) springContext.getBean("personServiceImpl");
        LOG.info("personService " + personService);
        if (personService != null) {
            LOG.info("===========data======{}", personService.listPersons());
        }        
        
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("========ApprequestListener=========");
        ServletContext ctx = sre.getServletContext();
        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        
        LOG.info("{}", Arrays.asList(springContext.getBeanDefinitionNames()));

        
    }

}
