# Description
This is about java web backend service with REST-APIs, hibernate database connection

# Clone this project
 git clone git@github.com:sophea/rest-api-spring-hibernate.git
  

# To run this backend project in JAVA technology with Maven build tool:

1 : install JAVA JDK version >=1.8.x  (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

2 : install maven 3  (https://maven.apache.org/install.html)
 
3 : install MySQL Server database

4 : import sql file in mysql console : sql_test.sql 

```
 mysql -u root -proot  < sqk.sql
```

5 : go to this project location by console

6 : create class-path for eclipse :mvn eclipse:eclipse

7 : run command >> mvn clean jetty:run (Jetty Server) or mvn clean tomcat7:run (Tomcat Server)

   
===============Test result with API when server started(Jetty/Tomcat)=======


get all persons : GET http://localhost:8080/api/persons/v1/all

get details : GET  http://localhost:8080/api/persons/v1/1

remove item : DELETE http://localhost:8080/api/persons/v1/1

update item : POST http://localhost:8080/api/persons/v1/1

=======================================

6 : Integrate Test cases : run command >> mvn clean test


# How to get Srping bean from Servlet or Filter:
web.xml
```java
 <?xml version="1.0" encoding="UTF-8"?>  
 <web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"  
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
   xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">  
      <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext  
      instead of the default XmlWebApplicationContext -->  
      <context-param>  
           <param-name>contextClass</param-name>  
           <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>  
      </context-param>  
   <context-param>  
       <param-name>contextConfigLocation</param-name>  
       <param-value>com.sample.server.MvcConfig</param-value>  
   </context-param>  
   
   <!-- Bootstrap the root application context as usual using ContextLoaderListener -->  
   <listener>  
     <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>  
   </listener>  
  <!-- custom listener must be under tag ContextLoaderListener otherwise it does not work -->  
   <listener>  
     <listener-class>com.sample.server.AppListener</listener-class>  
   </listener>  
      <filter>  
           <filter-name>encodingFilter</filter-name>  
           <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>  
           <init-param>  
                <param-name>encoding</param-name>  
                <param-value>UTF-8</param-value>  
           </init-param>  
           <init-param>  
                <param-name>forceEncoding</param-name>  
                <param-value>true</param-value>  
           </init-param>  
      </filter>  
      <filter-mapping>  
           <filter-name>encodingFilter</filter-name>  
           <url-pattern>/*</url-pattern>  
      </filter-mapping>  
      <servlet>  
           <servlet-name>spring-dispatcher</servlet-name>  
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>  
           <load-on-startup>1</load-on-startup>  
           <init-param>  
                <param-name>contextClass</param-name>  
                <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>  
           </init-param>  
           <init-param>  
                <param-name>contextConfigLocation</param-name>  
                <param-value>com.sample.server.MvcConfig</param-value>  
           </init-param>  
      </servlet>  
      <servlet-mapping>  
           <servlet-name>spring-dispatcher</servlet-name>  
           <url-pattern>/api/*</url-pattern>  
      </servlet-mapping>  
 </web-app> 
```
AppListener.java
```java
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
  
```
PersonService.java
```java
package com.sample.repo.service;

import java.util.List;

import com.sample.repo.domain.Person;

public interface PersonService {

 void addPerson(Person p);
 void updatePerson(Person p);
 List listPersons();
 Person getPersonById(long id);
 void removePerson(long id);
 
}
```
```java
package com.sample.repo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.repo.dao.PersonDAO;
import com.sample.repo.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
 private PersonDAO personDAO;


 @Override
 @Transactional
 public void addPerson(Person p) {
  this.personDAO.addPerson(p);
 }

 @Override
 @Transactional
 public void updatePerson(Person p) {
  this.personDAO.updatePerson(p);
 }

 @Override
 @Transactional
 public List listPersons() {
  return this.personDAO.listPersons();
 }

 @Override
 @Transactional
 public Person getPersonById(long id) {
  return this.personDAO.getPersonById(id);
 }

 @Override
 @Transactional
 public void removePerson(long id) {
  this.personDAO.removePerson(id);
 }

}

=========result from console=====
INFO 2016-12-05 14:16:40 server.AppListener.requestDestroyed:21 - =============requestDestroyed=========
[org.springframework.context.annotation.internalConfigurationAnnotationProcessor, org.springframework.chttps://github.com/sophea/rest-api-spring-hibernateontext.annotation.internalAutowiredAnnotationProcessor, org.springframework.context.annotation.internalRequiredAnnotationProcessor, org.springframework.context.annotation.internalCommonAnnotationProcessor, org.springframework.context.annotation.internalPersistenceAnnotationProcessor, org.springframework.context.event.internalEventListenerProcessor, org.springframework.context.event.internalEventListenerFactory, mvcConfig, org.springframework.context.annotation.ConfigurationClassPostProcessor.importAwareProcessor, org.springframework.context.annotation.ConfigurationClassPostProcessor.enhancedConfigurationProcessor, personController, personServiceImpl, personDAOImpl, org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration, mvcUrlPathHelper, handlerExceptionResolver, beanNameHandlerMapping, resourceHandlerMapping, mvcResourceUrlProvider, defaultServletHandlerMapping, requestMappingHandlerAdapter, mvcConversionService, mvcValidator, mvcPathMatcher, requestMappingHandlerMapping, mvcContentNegotiationManager, viewControllerHandlerMapping, mvcUriComponentsContributor, httpRequestHandlerAdapter, simpleControllerHandlerAdapter, mvcViewResolver, org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration, org.springframework.transaction.config.internalTransactionAdvisor, transactionAttributeSource, transactionInterceptor, org.springframework.transaction.config.internalTransactionalEventListenerFactory, messageSource, sessionFactory, transactionManager, jspViewResolver, org.springframework.context.support.PropertySourcesPlaceholderConfigurer#0, dataSource, org.springframework.aop.config.internalAutoProxyCreator]

INFO 2016-12-05 14:16:40 server.AppListener.requestDestroyed:29 - personService com.sample.repo.service.PersonServiceImpl@62db495f

INFO 2016-12-05 14:16:40 hibernate5.HibernateTransactionManager.afterPropertiesSet:357 - Using DataSource [org.apache.commons.dbcp2.BasicDataSource@3d24314a] of Hibernate SessionFactory for HibernateTransactionManager
INFO 2016-12-05 14:16:41 internal.QueryTranslatorFactoryInitiator.initiateService:47 - HHH000397: Using ASTQueryTranslatorFactory
Hibernate: select person0_.`id` as id1_0_, person0_.`country` as country2_0_, person0_.`name` as name3_0_ from `Person` person0_
INFO 2016-12-05 14:16:41 dao.PersonDAOImpl.listPersons:41 - Person List::id=1, name=sophea2w, country=Cambodia
INFO 2016-12-05 14:16:41 dao.PersonDAOImpl.listPersons:41 - Person List::id=2, name=sssdddd, country=dfdsfds
INFO 2016-12-05 14:16:41 server.AppListener.requestDestroyed:31 - ===========data======[id=1, name=sophea2w, country=Cambodia, id=2, name=sssdddd, country=dfdsfds]

```
