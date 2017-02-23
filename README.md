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

# database with hibernate

Assume you have install MySQL and create db name sample as the follow
```
DROP DATABASE if EXISTS sample;
CREATE DATABASE sample;
USE sample;
DROP TABLE IF EXISTS `person`;
CREATE TABLE person (
 id BIGINT(20) NOT NULL AUTO_INCREMENT,
 name VARCHAR(200), 
 country VARCHAR(200),
 version BIGINT(20),
 createdDate timestamp default CURRENT_TIMESTAMP,
 updatedDate date  NULL,
 PRIMARY KEY (`id`) USING BTREE
 ) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

 INSERT INTO Person (id, name, country) values(1,'sophea', 'Cambodia');
 INSERT INTO Person (id, name, country) values(2,'somnang', 'Cambodia');
 
DROP TABLE IF EXISTS `stock`;
CREATE TABLE  `stock` (
  `STOCK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `STOCK_CODE` varchar(100) NOT NULL,
  `STOCK_NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`STOCK_ID`) USING BTREE,
  UNIQUE KEY `UNI_STOCK_NAME` (`STOCK_NAME`),
  UNIQUE KEY `UNI_STOCK_CODE` (`STOCK_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `stock_detail`;
CREATE TABLE  `stock_detail` (
 `STOCK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `COMP_NAME` varchar(100) NOT NULL,
 `COMP_DESC` varchar(255) NOT NULL,
 `REMARK` varchar(255) NOT NULL,
 `LISTED_DATE` date NOT NULL,
 PRIMARY KEY (`STOCK_ID`) USING BTREE,
 CONSTRAINT `FK_STOCK_ID` FOREIGN KEY (`STOCK_ID`) REFERENCES `stock` (`STOCK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `type` varchar(256) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `logoUrl` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
```

In this example we use hibernate mapping with table Person Ojbect.

Class Person.java

```
package com.sample.repo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity bean with JPA annotations
 * Hibernate provides JPA implementation
 * 
 * @author Sophea
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** name */
    @Column(name = "name")
    private String name;
    /** country */
    @Column(name = "country")
    private String country;
    
    /**created date*/
    @Column(name = "createdDate")
    private Date createdDate;
    
    @Column(name = "updatedDate")
    private Date updatedDate;
    
    @Column(name = "version")
    private Long version;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String toString() {
        return "id=" + id + ", name=" + name + ", country=" + country;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

}

```

Database connection is setup in xml persistence-db.xml using org.apache.commons.dbcp2.BasicDataSource
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
    xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
  http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
  http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd
  http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd
  http://www.springframework.org/schema/tx
  http://www.springframework.org/schema/tx/spring-tx-3.1.xsd">

 <context:property-placeholder location="classpath:/application.properties" />
    
 <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
     <property name="url" value="jdbc:mysql://${jdbc.hostAndPort}/${jdbc.databasename}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    
    <property name="initialSize" value="5"/>
    <property name="maxIdle" value="50"/>
    <property name="minIdle" value="50"/>
    <property name="validationQuery" value="SELECT 1"/>
    <property name="testOnBorrow" value="true"/>
    <property name="testOnReturn" value="true"/>
    <property name="testWhileIdle" value="true"/>
    <property name="numTestsPerEvictionRun" value="3"/>
    <property name="minEvictableIdleTimeMillis" value="1800000"/>
    <property name="timeBetweenEvictionRunsMillis" value="1800000"/>

    <property name="maxWaitMillis" value="60000"/>
    <property name="poolPreparedStatements" value="true"/>
    <property name="accessToUnderlyingConnectionAllowed" value="true"/>
    <property name="fastFailValidation" value="true"/>
    <property name="maxConnLifetimeMillis" value="0"/>
    <property name="maxTotal" value="500"/>
    <property name="validationQueryTimeout" value="30"/>
    <property name="removeAbandonedOnBorrow" value="true"/>
    <property name="removeAbandonedTimeout" value="30"/>
  </bean>

```

## setup Hibernate configration 

The configuration is used  with  java code annotation in MvcConfig.java
```

package com.sample.server;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.sample.repo.web", "com.sample.repo.service", "com.sample.repo.dao"})
@ImportResource(value = { "classpath:/persistence-db.xml" })
@PropertySource(name="application",value={"classpath:/application.properties"})
@EnableWebMvc
@EnableTransactionManagement
@Lazy
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;
    
    @Autowired
    private DataSource dataSource;
    
    protected Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("hibernate.globally_quoted_identifiers", "true");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.connection.zeroDateTimeBehavior", "convertToNull");

        return properties;
    }
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
      //  sessionFactory.setAnnotatedClasses(Person.class);
        sessionFactory.setPackagesToScan("com.sample.repo.domain");
        
        
        sessionFactory.setHibernateProperties(hibernateProperties());
        
        //<bean id="sessionFactory"
//              class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
//              <property name="dataSource" ref="dataSource" />
//              <property name="annotatedClasses">
//                  <list>
//                      <value>com.sample.repo.domain.Person</value>
//                  </list>
//              </property>
//              <property name="hibernateProperties">
//                  <props>
//                      <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
//                      <prop key="hibernate.show_sql">true</prop>
//                  </props>
//              </property>
//          </bean>
                
        return sessionFactory;
    }
    
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject());
        return txManager;
//    <bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
//    <property name="sessionFactory" ref="sessionFactory" />
//</bean>
    }

    // -------------- Message Converters ----------------------
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        SkipNullObjectMapper skipNullMapper = new SkipNullObjectMapper();
        skipNullMapper.init();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(skipNullMapper);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        skipNullMapper.setDateFormat(formatter);
        
        converters.add(converter);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    /**message properties*/
    @Bean
    public  MessageSource messageSource() {
        ReloadableResourceBundleMessageSource message = new ReloadableResourceBundleMessageSource();
        message.setBasename("classpath:messages");//classpath:messages
        message.setDefaultEncoding("UTF-8");
        return message;
    }
    // -------------- View Stuff -----------------------
    @Bean
    public UrlBasedViewResolver jspViewResolver() {
        UrlBasedViewResolver resolver = new org.springframework.web.servlet.view.UrlBasedViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}

```

## create dao implementation PersonDAO & PersonDAOImpl

```
package com.sample.repo.dao;

import java.util.List;

import com.sample.repo.domain.Person;

public interface PersonDAO {

    public void create(Person p);
    public void update(Person p);
    public List<Person> listPersons();
    public Person getPersonById(long id);
    public void removePerson(long id);
}


package com.sample.repo.dao;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import com.sample.repo.domain.Person;

@Repository
public class PersonDAOImpl implements PersonDAO {

    private static final Logger logger = LoggerFactory.getLogger(PersonDAOImpl.class);
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public void create(Person p) {
        Session session = this.sessionFactory.getObject().getCurrentSession();
        session.persist(p);
        logger.info("Person saved successfully, Person Details=" + p);
    }

    @Override
    public void update(Person p) {
        Session session = this.sessionFactory.getObject().getCurrentSession();
        session.saveOrUpdate(p);
        logger.info("Person updated successfully, Person Details=" + p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Person> listPersons() {
        Session session = this.sessionFactory.getObject().getCurrentSession();
        List<Person> personsList = session.createQuery("from Person").list();
        for (Person p : personsList) {
            logger.info("Person List::" + p);
        }
        return personsList;
    }

    @Override
    public Person getPersonById(long id) {
        Session session = this.sessionFactory.getObject().getCurrentSession();
        Person p = (Person) session.get(Person.class, new Long(id));
        logger.info("Person loaded successfully, Person details=" + p);
        //check null first
        if (p != null) {
            //detach your first entity from session - session.evict(myEntity)
            session.evict(p);
        }
        return p;
    }

    @Override
    public void removePerson(long id) {
        Session session = this.sessionFactory.getObject().getCurrentSession();
        Person p = (Person) session.load(Person.class, new Long(id));
        if (null != p) {
            session.delete(p);
        }
        logger.info("Person deleted successfully, person details=" + p);
    }

}


```

## Service layer PersonService & PersonServiceImpl

```
package com.sample.repo.service;

import java.util.List;

import com.sample.repo.domain.Person;

public interface PersonService {

    void create(Person p);
    void update(Person p);
    List<Person> listPersons();
    Person getPersonById(long id);
    void removePerson(long id);
    
}

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
    public void create(Person p) {
        this.personDAO.create(p);
    }

    @Override
    @Transactional
    public void update(Person p) {
        System.out.println("update method==========");
        Person domain = personDAO.getPersonById(p.getId());
        if (domain != null) {
            p.setVersion(domain.getVersion());
        }
        this.personDAO.update(p);
    }

    @Override
    @Transactional
    public List<Person> listPersons() {
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

```

## controller PersonController

```
package com.sample.repo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sample.repo.domain.Person;
import com.sample.repo.service.PersonService;

@Controller
@RequestMapping(value = "/persons")
public class PersonController {
    
    @Autowired
    private PersonService personService;
    
    
    /**return as json REST-API*/
    @RequestMapping(value = "/v1/all", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> listPersons() {
        return new ResponseEntity<>(personService.listPersons(), HttpStatus.OK);
    }
    
    
    // For add and update person both
    @RequestMapping(value = "/v1", method = RequestMethod.POST)
    public ResponseEntity<Person> addPerson(@ModelAttribute("person") Person p) {

        if (p.getId() == 0) {
            // new person, add it
            this.personService.create(p);
        } else {
            // existing person, call update
            this.personService.update(p);
        }

        return new ResponseEntity<>(p, HttpStatus.OK);

    }
    
    @RequestMapping(value= "/v1/{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPerson(@PathVariable long id){
        return new ResponseEntity<>(personService.getPersonById(id), HttpStatus.OK);
    }
    
    @RequestMapping(value= "/v1/{id}", method = RequestMethod.POST)
    public ResponseEntity<Person> updatePerson(@PathVariable long id, @ModelAttribute Person p){
           Person person = personService.getPersonById(id);
           if (person == null) {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
           p.setId(person.getId());
           //update
           personService.update(p);
           return new ResponseEntity<>(p, HttpStatus.OK);
    }

       
    @RequestMapping(value= "/v1/{id}", method = RequestMethod.DELETE)
    public void removePerson(@PathVariable("id") long id){
        this.personService.removePerson(id);
        
    }
}

```

8 : Integrate Test cases : run command >> mvn clean test


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
