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
