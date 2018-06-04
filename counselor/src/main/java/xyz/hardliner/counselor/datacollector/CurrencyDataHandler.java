package xyz.hardliner.counselor.datacollector;

import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.datacollector.datasources.Bitfinex;
import xyz.hardliner.counselor.datacollector.datasources.DataSource;
import xyz.hardliner.counselor.datacollector.datasources.FixerIo;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyDataHandler {

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final List<DataSource> dataSources;
	private CurrencyData actualData;

	public CurrencyDataHandler(FixerIo fixerIo, Bitfinex bitfinex) {
		this.dataSources = new ArrayList<>();
		dataSources.add(fixerIo);
		dataSources.add(bitfinex);
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
		return actualData;
	}

	public String getData() {
		return actualData.compileString();
	}

}
