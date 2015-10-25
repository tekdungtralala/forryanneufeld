package com.upwork.job;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upwork.job.entity.Customer;
import com.upwork.job.entity.Sensor;
import com.upwork.job.entity.SensorData;
import com.upwork.job.repository.CustomerRepository;
import com.upwork.job.repository.SensorDataRepository;
import com.upwork.job.repository.SensorRepository;
import com.upwork.job.util.RestResult;

@RestController
@RequestMapping("/report")
public class ReportController {

	private Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private SensorRepository sensorRepo;
	@Autowired
	private SensorDataRepository sensorDataRepo;

	@RequestMapping(method = RequestMethod.GET)
	public String getReport() {
		return "post /reprot is available";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<RestResult> postReport(@RequestHeader(value = "token", required = false) String token,
			@RequestBody HashMap<String, Sensor> sensorMap) {
		try {

			logger.info("POST /report");
			logger.info("   token (header) " + token);
			if (StringUtils.isBlank(token)) {
				logger.info("   return 400, missing token (header)");
				return RestResult.generateResp400("token (header) is empty");
			}

			Customer storedCustomer = customerRepo.findByToken(token);
			if (storedCustomer == null) {
				logger.info("   return 403, Unknown token, do not add any records");
				return RestResult.generateResp403("Unknown token, do not add any records");
			}

			if (sensorMap == null || sensorMap.size() == 0) {
				logger.info("   return 204, body == null");
				return RestResult.generateResp204();
			}

			// validation
			for (String sensorUUID : sensorMap.keySet()) {
				Sensor s = sensorMap.get(sensorUUID);
				if (StringUtils.isEmpty(s.getFormat())) {
					logger.info("   return 400, Format field from " + sensorUUID + " is empty");
					return RestResult.generateResp400("Format field from " + sensorUUID + " is empty");
				}

				if (StringUtils.isEmpty(s.getLength())) {
					logger.info("   return 400, Length field from " + sensorUUID + " is empty");
					return RestResult.generateResp400("Length field from " + sensorUUID + " is empty");
				}

				if (s.getData() == null || s.getData().size() == 0) {
					logger.info("   return 400, Data from " + sensorUUID + " is empty");
					return RestResult.generateResp400("Data from " + sensorUUID + " is empty");
				}

				for (SensorData sd : s.getData()) {

					if (StringUtils.isBlank(sd.getTime())) {
						logger.info("   return 400, Time from " + sensorUUID + " is empty");
						return RestResult.generateResp400("Time from " + sensorUUID + " is empty");
					}

					if (StringUtils.isBlank(sd.getValue())) {
						logger.info("   return 400, Value from " + sensorUUID + " is empty");
						return RestResult.generateResp400("Value from " + sensorUUID + " is empty");
					}
				}
			}

			for (String sensorUUID : sensorMap.keySet()) {
				Sensor storedSensor = sensorRepo.findBySensorUUID(sensorUUID);

				Sensor s = sensorMap.get(sensorUUID);
				Sensor parent = null;
				if (storedSensor == null) {
					s.setCustomer(storedCustomer);
					s.setSensorUUID(sensorUUID);
					sensorRepo.save(s);

					parent = s;
				} else {
					parent = storedSensor;
				}

				for (SensorData sd : s.getData()) {
					sd.setSensor(parent);
					sd.setDateTime(Calendar.getInstance());
					sensorDataRepo.save(sd);
				}
			}

			return RestResult.generateResp201();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("   return 500, error : " + e.getMessage());
			return RestResult.generateResp500(e.getMessage());
		}
	}
}
