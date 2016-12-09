package com.sample.repo.dao;

import java.util.List;

import org.hibernate.LockMode;
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
