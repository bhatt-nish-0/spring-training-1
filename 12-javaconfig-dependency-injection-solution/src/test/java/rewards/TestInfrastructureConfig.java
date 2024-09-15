package rewards;

import javax.sql.DataSource;

import SomeBean.SomeBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import config.RewardsConfig;

@Configuration
@Profile("uat")
@Import(RewardsConfig.class)
public class TestInfrastructureConfig {

	/**
	 * Creates an in-memory "rewards" database populated 
	 * with test data for fast testing
	 */
	@Bean
	@Profile("uat")
	public DataSource dataSource(){
		return
			(new EmbeddedDatabaseBuilder())
			.addScript("classpath:rewards/testdb/schema.sql")
			.addScript("classpath:rewards/testdb/data.sql")
			.build();
	}

	@Bean
    @Profile("uatx")
	public Integer return2() {
		return 88390;
	}

	@Bean
	@Profile("prod")
	public Integer return3(){
		return 39;
	}


	@Bean
	@Profile("uat")
	public SomeBean someBean() {
		return new SomeBean();
	}
//	@Bean
//	public String lolo() {
//		return "lolo";
//	}
}
