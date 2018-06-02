package xyz.hardliner.counselor.app;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;

@Component
public class TelegramInitializer {

	public TelegramInitializer() {
		ApiContextInitializer.init();
	}

}
