package com.upwork.job;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FlywayConfig {

	@Autowired
	private Environment env;

	@Bean
	public Flyway flyway(DataSource theDataSource) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(theDataSource);
		flyway.setLocations("classpath:db/migration");
		if (!isProdProfile(env))
			flyway.clean();
		flyway.migrate();

		return flyway;
	}

	public static boolean isProdProfile(Environment env) {
		for (String profile : env.getActiveProfiles()) {
			if (profile.contains("prod"))
				return true;
		}
		return false;
	}
}