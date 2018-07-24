package xyz.hardliner.counselor.datacollector;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.datacollector.datasources.Bitfinex;
import xyz.hardliner.counselor.datacollector.datasources.DataSource;
import xyz.hardliner.counselor.datacollector.datasources.ExchangeRates;
import xyz.hardliner.counselor.db.CurrencyData;
import xyz.hardliner.counselor.db.StorageService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
public class CurrencyDataHandler {

	private final StorageService storageService;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final List<DataSource> dataSources;
	private CurrencyData actualData;

	@Autowired
	public CurrencyDataHandler(StorageService storageService) {
		this.storageService = storageService;
		this.dataSources = new ArrayList<>();
		dataSources.add(new ExchangeRates());
		dataSources.add(new Bitfinex());
	}

	@PostConstruct
	private void init() {
		actualData = new CurrencyData();
		executor.scheduleAtFixedRate(this::updateAndSaveData, 0, 20, TimeUnit.MINUTES);
	}

	private CurrencyData updateAndSaveData() {
		for (DataSource dataSource : dataSources) {
			dataSource.updateData(actualData);
		}
		log.info("Rates update done: " + actualData.toString());
		storageService.save(actualData);
		return actualData;
	}

	public String getData() {
		return actualData.compileString();
	}

}
