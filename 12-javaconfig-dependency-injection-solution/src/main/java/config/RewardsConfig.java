package config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rewards.RewardNetwork;
import rewards.internal.RewardNetworkImpl;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JdbcAccountRepository;
import rewards.internal.restaurant.JdbcRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

@Configuration
//@Profile("popo")
@Profile("uat")
		//default profile... also will be loaded
@PropertySource("classpath:/nish.properties")
public class RewardsConfig {
	private DataSource dataSource;

	@Value("${using.value.anno}")
	//properties are Strings!
	private String testBro;

	@Value("${aradhana.gulati}")
	private Integer newInt;

	@Value("${aradhana.gulati}")
	private String newStr;

	@Value("#{ systemProperties['parto.ghost']}")
	private String partoGhosh;

	@Value("#{someBean.headerFooter}")
	private String test;

	@Value("${harbo: spiel}")
	private String harbo;


	private Integer profileVal;

	// As this is the only constructor, @Autowired is not needed.
	public RewardsConfig(DataSource dataSource, Integer profileVal) {
		this.dataSource = dataSource;
		this.profileVal = profileVal;
	}

	@Bean
	public String getTheString(Environment environment) {
		System.out.println("printing testbro");
		System.out.println(testBro);
		System.out.println(profileVal);
		System.out.println(partoGhosh);
		System.out.println(test);
		System.out.println(newInt * 8); //implicit conversion to int from string
		System.out.println(newStr); //implicit conversion to int from string
		System.out.println(harbo);
		String property = environment.getProperty("harman.baweja");
		return property;
	}
		
	@Bean
	public RewardNetwork rewardNetwork(){
		return new RewardNetworkImpl(
			accountRepository(), 
			restaurantRepository(), 
			rewardRepository());
	}
	
	@Bean
	public AccountRepository accountRepository(){
		JdbcAccountRepository repository = new JdbcAccountRepository();
		repository.setDataSource(dataSource);
		return repository;
	}
	
	@Bean
	public RestaurantRepository restaurantRepository(){
		JdbcRestaurantRepository repository = new JdbcRestaurantRepository();
		repository.setDataSource(dataSource);
		return repository;
	}
	
	@Bean
	public RewardRepository rewardRepository(){
		JdbcRewardRepository repository = new JdbcRewardRepository();
		repository.setDataSource(dataSource);
		return repository;
	}
	
}
