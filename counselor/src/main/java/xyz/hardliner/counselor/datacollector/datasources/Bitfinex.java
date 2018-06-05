package xyz.hardliner.counselor.datacollector.datasources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import xyz.hardliner.counselor.datacollector.CurrencyData;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class Bitfinex implements DataSource {

	private static final String BITFINEX = "https://api.bitfinex.com/v2/tickers?symbols=tBTCUSD";
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public CurrencyData updateData(CurrencyData data) {
		try {
			// I know next dozen lines look weird, but they work.
			List<List<Object>> unit = mapper.readValue(new URL(BITFINEX), List.class);
			Float btcRate;
			Object value = unit.get(0).get(1);
			if (value instanceof Double) {
				btcRate = ((Double) value).floatValue();
			} else if (value instanceof Integer) {
				btcRate = ((Integer) value).floatValue();
			} else {
				btcRate = Float.MIN_NORMAL;
			}
			data.setBtcToUsd(btcRate);
			data.setUpdated(LocalDateTime.now());
			log.info("Bitfinex data successfully updated: " + data.toString());
			return data;
		} catch (Exception ex) {
			log.error("Cannot update bitfinex info! " + ex.getMessage(), ex);
			return data;
		}
	}
}
