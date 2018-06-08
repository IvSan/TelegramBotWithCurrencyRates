package xyz.hardliner.counselor.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.datacollector.CurrencyDataHandler;

@Getter
@Service
@AllArgsConstructor
public class ServiceFacade {

	private final CurrencyDataHandler dataHandler;

	public String getCurrencyData() {
		return dataHandler.getData();
	}

}
