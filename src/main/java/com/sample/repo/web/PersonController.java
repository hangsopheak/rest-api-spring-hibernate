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
            this.personService.addPerson(p);
        } else {
            // existing person, call update
            this.personService.updatePerson(p);
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
	       personService.updatePerson(p);
	       return new ResponseEntity<>(p, HttpStatus.OK);
	}

	   
	@RequestMapping(value= "/v1/{id}", method = RequestMethod.DELETE)
    public void removePerson(@PathVariable("id") long id){
        this.personService.removePerson(id);
        
    }
}
