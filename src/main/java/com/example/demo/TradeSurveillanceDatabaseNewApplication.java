package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com")
@EntityScan(basePackages = "com")
@EnableJpaRepositories(basePackages = "com")
public class TradeSurveillanceDatabaseNewApplication {
	// private static final Logger logger =
	// LoggerFactory.getLogger(TradeController.class);

	public static void main(String[] args) {
		/*
		 * List<Trade> li = new ArrayList<>(); GenerateTradeList g = new
		 * GenerateTradeList(); li = g.Generate(); TradeService service1 = new
		 * TradeService(); for (Trade trade : li) { System.out.println(trade);
		 * 
		 * service1.add(trade);
		 * 
		 * }
		 */
		// System.out.println(li);
		// logger.info(li);

		// @Query (value= "INSERT INTO trade (tradeId,tradeExecutionTime,brokerName,)")

		SpringApplication.run(TradeSurveillanceDatabaseNewApplication.class, args);

	}
}
