package com.sample.repo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.repo.domain.Person;
import com.sample.repo.server.AbstractWebAppTest;

/**
 * @author Sophea <a href='mailto:sopheamak@gmail.com'> sophea </a>
 * @version $id$ - $Revision$
 * @date 2016
 */
public class PersonServiceTest extends AbstractWebAppTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceTest.class);
    
    @Autowired
    private PersonService service;    
   
    @org.junit.Test
    public void testSearch() throws Exception {
        //initial data
        for (int i=0; i<=10; i++) {
            Person p = new Person();
            //p.setId(100+i);
            p.setName("Name_" +i);
            p.setCountry("Country_"+i);
            service.addPerson(p);
            LOG.info("create persion " + i);
        }
        
        //find by id
        service.getPersonById(9);
        //remove by id
        service.removePerson(9);
        //list
        LOG.info("size : " + service.listPersons().size());
        
        Person p = new Person();
        p.setId(10);
        p.setName("update_Name_" );
        p.setCountry("updaet_Country_");
        //update
        service.updatePerson(p);
    }

}
