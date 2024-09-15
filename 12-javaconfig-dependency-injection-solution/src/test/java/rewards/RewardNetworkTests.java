package rewards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import common.money.MonetaryAmount;
import rewards.internal.account.AccountRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.RewardRepository;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * A system test that verifies the components of the RewardNetwork application work together to reward for dining
 * successfully. Uses Spring to bootstrap the application for use in a test environment.
 */
public class RewardNetworkTests {

	/**
	 * The object being tested.
	 */
	private RewardNetwork rewardNetwork;

	@BeforeEach
	public void setUp() throws SQLException {
		// Create the test configuration for the application:
		
		ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
		
		// Get the bean to use to invoke the application
		rewardNetwork = context.getBean(RewardNetwork.class);
		DataSource d = context.getBean(DataSource.class);
		AccountRepository accountRepository = context.getBean(AccountRepository.class);
		RestaurantRepository restaurantRepo = context.getBean(RestaurantRepository.class);
		RewardRepository bean = context.getBean(RewardRepository.class);
		String bean1 = context.getBean(String.class);
		System.out.println("printing........" + rewardNetwork+" " + d + " " + accountRepository + " " + restaurantRepo + " " + bean + " " + bean1) ;
		//DataSource bean = context.getBean(DataSource.class);
		//System.out.println(bean.getConnection());
	}

	@Test
	public void testRewardForDining() {
		// create a new dining of 100.00 charged to credit card '1234123412341234' by merchant '123457890' as test input
		Dining dining = Dining.createDining("100.00", "1234123412340012", "1234567890");

		// call the 'rewardNetwork' to test its rewardAccountFor(Dining) method
		// this fails if you have selected an account without beneficiaries!
		RewardConfirmation confirmation = rewardNetwork.rewardAccountFor(dining);

		// assert the expected reward confirmation results
		assertNotNull(confirmation);
		assertNotNull(confirmation.getConfirmationNumber());

		// assert an account contribution was made
		AccountContribution contribution = confirmation.getAccountContribution();
		assertNotNull(contribution);

		// the contribution account number should be '123456789'
		assertEquals("123456012", contribution.getAccountNumber());

		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals(MonetaryAmount.valueOf("8.00"), contribution.getAmount());

		// the total contribution amount should have been split into 2 distributions
		assertEquals(2, contribution.getDistributions().size());

		// each distribution should be 4.00 (as both have a 50% allocation)
		assertEquals(MonetaryAmount.valueOf("6.00"), contribution.getDistribution("Brian").getAmount());
		assertEquals(MonetaryAmount.valueOf("2.00"), contribution.getDistribution("Shelby").getAmount());
		//assertEquals(MonetaryAmount.valueOf("2.00"), contribution.getDistribution("Gian").getAmount());
		//assertEquals(MonetaryAmount.valueOf("2.00"), contribution.getDistribution("Argeo").getAmount());
	}
}
