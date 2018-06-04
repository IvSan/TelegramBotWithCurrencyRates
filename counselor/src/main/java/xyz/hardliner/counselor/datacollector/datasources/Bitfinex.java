package xyz.hardliner.counselor.datacollector.datasources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.hardliner.counselor.datacollector.CurrencyData;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class Bitfinex implements DataSource {

	private static final String BITFINEX = "https://api.bitfinex.com/v2/tickers?symbols=tBTCUSD";
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public CurrencyData updateData(CurrencyData data) {
		try {
			// I know next two lines looks weird, but that's optimum way.
			List<List<Object>> unit = mapper.readValue(new URL(BITFINEX), List.class);
			Float btcRate = ((Double) unit.get(0).get(1)).floatValue();
			data.setBtcToUsd(btcRate);
			data.setUpdated(LocalDateTime.now());
			log.debug("Bitfinex data successfully updated: " + data.toString());
			return data;
		} catch (Exception ex) {
			log.error("Cannot update bitfinex info! " + ex.getMessage(), ex);
			return data;
		}
	}
}
