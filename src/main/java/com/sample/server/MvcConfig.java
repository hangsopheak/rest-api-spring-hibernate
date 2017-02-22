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
