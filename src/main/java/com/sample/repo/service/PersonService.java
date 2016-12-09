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
