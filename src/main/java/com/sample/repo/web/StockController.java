package com.sample.repo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sample.repo.dao.StockDaoImpl;
import com.sample.repo.domain.Stock;

@Controller
@RequestMapping(value = "/stocks")
public class StockController {
	
    @Autowired
	private StockDaoImpl dao;
	
	
	/**return as json REST-API*/
	@RequestMapping(value = "/v1/all", method = RequestMethod.GET)
    public ResponseEntity<List<Stock>> listPersons() {
        return new ResponseEntity<>(dao.list(), HttpStatus.OK);
    }
	
	
    
	
	@RequestMapping(value= "/v1/{id}", method = RequestMethod.GET)
    public ResponseEntity<Stock> getPerson(@PathVariable int id){
        return new ResponseEntity<>(dao.findStockById(id), HttpStatus.OK);
    }
	
}
