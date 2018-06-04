package xyz.hardliner.counselor.datacollector.datasources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import xyz.hardliner.counselor.datacollector.CurrencyData;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class FixerIo implements DataSource {

	private static final String FIXER = "http://data.fixer.io/api/";
	private static final String LATEST = "latest";
	private static final String PROPS = "&symbols=USD,RUB&format=1";
	private final ObjectMapper mapper = new ObjectMapper();
	private final Environment environment;

	@Override
	public CurrencyData updateData(CurrencyData data) {
		try {
			FixerIoJsonUnit unit = mapper.readValue(compileUrl(), FixerIoJsonUnit.class);
			data.setUerToRub((unit.getRates().get("RUB")) / 1f);
			data.setUsdToRub(data.getUerToRub() / unit.getRates().get("USD"));
			data.setUpdated(LocalDateTime.now());
			log.debug("FixerIo data successfully updated: " + data.toString());
			return data;
		} catch (Exception ex) {
			log.error("Cannot update bitfinex info! " + ex.getMessage(), ex);
			return data;
		}
	}

	@SneakyThrows({MalformedURLException.class})
	private URL compileUrl() {
		String str = FIXER + LATEST + "?access_key=" + environment.getRequiredProperty("fixer.io.key") + PROPS;
		return new URL(str);
	}
}
