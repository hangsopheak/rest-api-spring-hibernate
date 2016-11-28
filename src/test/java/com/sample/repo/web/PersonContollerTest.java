package com.sample.repo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.sample.repo.server.AbstractWebAppTest;

public class PersonContollerTest extends AbstractWebAppTest {
   
    
    public PersonContollerTest() {
      
    }
    @Test
    public void testGetPersons() throws Exception {
        ResultActions result = server
                .perform(get("/api/persons/v1/all"));
        LOG.info("===== Test Result : {}", json(result));
        status200(result);
        
    }
    
    @Test
    public void testPerson() throws Exception {
        ResultActions result = server
                .perform(post("/api/persons/v1").param("name", "myname").param("country", "myCountry"));
        status200(result);
        
        result = server
                .perform(get("/api/persons/v1/1"));
        
        
        result = server
                .perform(post("/api/persons/1").param("name", "mynameUpdate").param("country", "myCountryUpdate"));
        
        
        result = server
                .perform(delete("/api/person/remove/1"));
    }
    
}
