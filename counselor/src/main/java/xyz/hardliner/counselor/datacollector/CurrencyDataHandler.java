package xyz.hardliner.counselor.datacollector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.datacollector.datasources.Bitfinex;
import xyz.hardliner.counselor.datacollector.datasources.DataSource;
import xyz.hardliner.counselor.datacollector.datasources.ExchangeRates;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CurrencyDataHandler {

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final List<DataSource> dataSources;
	private CurrencyData actualData;

	public CurrencyDataHandler() {
		this.dataSources = new ArrayList<>();
		dataSources.add(new ExchangeRates());
		dataSources.add(new Bitfinex());
	}

	@PostConstruct
	private void init() {
		actualData = new CurrencyData();
		executor.scheduleAtFixedRate(this::updateData, 0, 20, TimeUnit.MINUTES);
	}

	private CurrencyData updateData() {
		for (DataSource dataSource : dataSources) {
			dataSource.updateData(actualData);
		}
		log.info("Rates update done: " + actualData.toString());
		return actualData;
	}

	public String getData() {
		return actualData.compileString();
	}

}
