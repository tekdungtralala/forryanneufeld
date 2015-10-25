package com.upwork.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.job.Application;
import com.upwork.job.entity.Customer;
import com.upwork.job.entity.Sensor;
import com.upwork.job.entity.SensorData;
import com.upwork.job.modelinput.CustomerModel;
import com.upwork.job.modeloutput.TokenModel;
import com.upwork.job.repository.CustomerRepository;
import com.upwork.job.repository.SensorDataRepository;
import com.upwork.job.repository.SensorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class DoTest {
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private RestTemplate restTemplate = new TestRestTemplate();

	private Logger logger = Logger.getLogger(DoTest.class);

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private SensorRepository sensorRepo;
	@Autowired
	private SensorDataRepository sensorDataRepo;

	@Test
	public void testReportEndpoint() throws JsonProcessingException, FileNotFoundException {
		int port = Integer.valueOf(System.getProperty("PORT"));
		logger.info("********************************* ");
		logger.info("*");
		logger.info("* Start Test Report Endpoint");
		logger.info("*");
		logger.info("*********************************");

		logger.info("POST /report");
		HashMap<String, Sensor> sensorMap = new HashMap<>();
		Sensor newSensor = new Sensor();
		newSensor.setFormat("format");
		newSensor.setLength("length");

		List<SensorData> sensorsData = new ArrayList<SensorData>();
		SensorData sd = new SensorData();
		sd.setTime("2001-07-04T12:08:56.235-0700");
		sd.setValue("value");
		sensorsData.add(sd);
		newSensor.setData(sensorsData);
		sensorMap.put("new sensor UUID", newSensor);

		logger.info("  TEST   total sensor is 2");
		Assert.assertEquals(2, sensorRepo.findAll().size());
		logger.info("  TEST   total sensor data is 5");
		Assert.assertEquals(5, sensorDataRepo.findAll().size());

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("token", "111222333");
		headers.add("Content-Type", "application/json");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		HttpEntity<HashMap<String, Sensor>> request = new HttpEntity<HashMap<String, Sensor>>(sensorMap, headers);

		HttpEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/report", HttpMethod.POST,
				request, String.class);

		logger.info("  TEST   total sensor is 3");
		Assert.assertEquals(3, sensorRepo.findAll().size());
		logger.info("  TEST   total sensor data is 6");
		Assert.assertEquals(6, sensorDataRepo.findAll().size());

		response = restTemplate.exchange("http://localhost:" + port + "/report", HttpMethod.POST, request,
				String.class);
		logger.info("  TEST   total sensor is 3");
		Assert.assertEquals(3, sensorRepo.findAll().size());
		logger.info("  TEST   total sensor data is 7");
		Assert.assertEquals(7, sensorDataRepo.findAll().size());

		try {
			sensorMap = new HashMap<>();
			restTemplate.postForObject("http://localhost:" + port + "/report", sensorMap, String.class);
		} catch (HttpClientErrorException e) {
			logger.info("  TEST   response code == 400");
			Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
		}

		logger.info("*********************************");
		logger.info("*");
		logger.info("* End Test Report endpoint");
		logger.info("*");
		logger.info("*********************************");
	}

	@Test
	public void testAuthEndpoint() throws JsonProcessingException, FileNotFoundException {
		int port = Integer.valueOf(System.getProperty("PORT"));
		logger.info("*********************************");
		logger.info("*");
		logger.info("* Start Test Auth endpoint");
		logger.info("*");
		logger.info("*********************************");

		logger.info("POST /auth");
		// default data, only has 2 entry
		logger.info("total customer is 2");
		Assert.assertEquals(2, customerRepo.findAll().size());

		CustomerModel cm = new CustomerModel("new customer");
		TokenModel model = restTemplate.postForObject("http://localhost:" + port + "/auth", cm, TokenModel.class);
		logger.info("  TEST   model != null");
		Assert.assertEquals(true, model != null);
		logger.info("  TEST   model.getToken() != null");
		Assert.assertEquals(true, model.getToken() != null);

		Customer storedCustomer1 = customerRepo.findByToken(model.getToken());
		logger.info("  TEST   storedCustomer1 != null");
		Assert.assertEquals(true, storedCustomer1 != null);
		logger.info("  TEST   storedCustomer1.getToken() != null");
		Assert.assertEquals(true, storedCustomer1.getToken() != null);

		logger.info("  TEST   model.getToken() equals storedCustomer1.getToken()");
		Assert.assertEquals(model.getToken(), storedCustomer1.getToken());
		logger.info("  TEST   cm.getCustomer_id() equals storedCustomer1.getCustomerId()");
		Assert.assertEquals(cm.getCustomer_id(), storedCustomer1.getCustomerId());

		logger.info("  TEST   total customer is 3");
		Assert.assertEquals(3, customerRepo.findAll().size());

		try {
			restTemplate.postForObject("http://localhost:" + port + "/auth", new CustomerModel(), TokenModel.class);
		} catch (HttpClientErrorException e) {
			logger.info("  TEST   response code == 400");
			Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
		}
		try {
			restTemplate.postForObject("http://localhost:" + port + "/auth", new CustomerModel(""), TokenModel.class);
		} catch (HttpClientErrorException e) {
			logger.info("  TEST   response code == 400");
			Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
		}

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		HttpEntity<CustomerModel> request = new HttpEntity<CustomerModel>(cm, headers);
		HttpEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/auth", HttpMethod.POST,
				request, String.class);
		// logger.info("BODY " + response.getBody());
		// logger.info("BODY " + response.getHeaders());

		logger.info("  TEST   total customer is 3");
		Assert.assertEquals(3, customerRepo.findAll().size());

		logger.info("*********************************");
		logger.info("*");
		logger.info("* End test Auth endpoint");
		logger.info("*");
		logger.info("*********************************");
	}
}
