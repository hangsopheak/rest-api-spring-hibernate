package com.sample.repo.server;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.sample.server.MvcConfig;
import com.sample.server.SkipNullObjectMapper;

@Configuration
@PropertySource(name = "application", value = { "classpath:/application.properties" })
public class TestMvcConfig extends MvcConfig {
    protected static final Logger LOG = LoggerFactory.getLogger(TestMvcConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        final SkipNullObjectMapper skipNullMapper = new SkipNullObjectMapper();

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        skipNullMapper.setDateFormat(formatter);
        skipNullMapper.init();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(skipNullMapper);
        converters.add(converter);
    }
    @Override
    protected Properties hibernateProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }
    
}
