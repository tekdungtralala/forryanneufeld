package com.upwork.job.controller;

import java.util.HashMap;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upwork.job.entity.Customer;
import com.upwork.job.modelinput.CustomerModel;
import com.upwork.job.modeloutput.TokenModel;
import com.upwork.job.repository.CustomerRepository;
import com.upwork.job.util.RestResult;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private Logger logger = Logger.getLogger(AuthController.class);

	@Autowired
	private CustomerRepository customerRepo;

	@RequestMapping(method = RequestMethod.GET)
	public String getAuth() {
		return "post /auth is available";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TokenModel> postAuth(@RequestBody @Valid CustomerModel model) {
		try {
			String customerId = model.getCustomer_id();

			logger.info("POST /auth");
			logger.info("   customer_id " + customerId);

			if (StringUtils.isBlank(customerId)) {
				logger.info("   return 400, customer_id is empty");
				return RestResult.generateResp400("customer_id is empty");
			}

			Customer storedCustomer = customerRepo.findByCustomerId(customerId);
			if (storedCustomer != null) {
				logger.info("   return 200, customer is exist");
				return new ResponseEntity<TokenModel>(new TokenModel(storedCustomer.getToken()), HttpStatus.OK);

			}

			UUID uuid = UUID.randomUUID();

			Customer newCustomer = new Customer();
			newCustomer.setCustomerId(customerId);
			newCustomer.setToken(uuid.toString());
			customerRepo.save(newCustomer);

			logger.info("   return 201, save new customer");

			return new ResponseEntity<TokenModel>(new TokenModel(newCustomer.getToken()), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("   return 500, error : " + e.getMessage());
			return RestResult.generateResp500(e.getMessage());
		}
	}
}
