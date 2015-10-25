package com.upwork.job.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upwork.job.entity.Customer;
import com.upwork.job.repository.CustomerRepository;

@RestController
@RequestMapping("/test")
public class TestAPIController {

	@Autowired
	private CustomerRepository repo;

	@RequestMapping(method = RequestMethod.GET)
	private List<Customer> getSensors() {
		return repo.findAll();
	}
}
