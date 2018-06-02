package xyz.hardliner.counselor.datacollector;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CurrencyDataHandler {

	private static final String FIXER = "http://data.fixer.io/api/";
	private static final String LATEST = "latest";
	private static final String PROPS = "&symbols=USD,RUB,BTC&format=1";
	private final Environment environment;
	private final ObjectMapper mapper = new ObjectMapper();
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	private CurrencyData actualData;

	@PostConstruct
	private void init() {
		executor.scheduleAtFixedRate(this::updateData, 0, 20, TimeUnit.MINUTES);
	}

	@SneakyThrows({IOException.class})
	private CurrencyData updateData() {
		FixerIoJsonUnit unit = mapper.readValue(compileUrl(), FixerIoJsonUnit.class);
		this.actualData = new CurrencyData(unit);
		return actualData;
	}

	@SneakyThrows({MalformedURLException.class})
	private URL compileUrl() {
		String str = FIXER + LATEST + "?access_key=" + environment.getRequiredProperty("fixer.io.key") + PROPS;
		return new URL(str);
	}

	public String getData(){
		return actualData.compileString();
	}

}
