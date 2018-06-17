package xyz.hardliner.counselor.datacollector.datasources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import xyz.hardliner.counselor.datacollector.CurrencyData;

import java.net.URL;
import java.time.LocalDateTime;

@Slf4j
public class ExchangeRates implements DataSource {

	private static final String URL = "https://exchangeratesapi.io/api/latest?base=USD&symbols=RUB,EUR";
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public CurrencyData updateData(CurrencyData data) {
		try {
			ExchangeRatesApiUnit unit = mapper.readValue(new URL(URL), ExchangeRatesApiUnit.class);
			data.setUsdToRub(unit.getRates().get("RUB"));
			data.setUerToRub(data.getUsdToRub() / unit.getRates().get("EUR"));
			data.setUpdated(LocalDateTime.now());
			return data;
		} catch (Exception ex) {
			log.error("Cannot update bitfinex info! " + ex.getMessage(), ex);
			return data;
		}
	}
}