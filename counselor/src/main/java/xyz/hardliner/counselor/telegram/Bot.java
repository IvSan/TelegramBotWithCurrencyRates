package xyz.hardliner.counselor.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import xyz.hardliner.counselor.app.Module;
import xyz.hardliner.counselor.app.TelegramInitializer;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot implements Module {

	private final Environment environment;
	private final ChatMaster chatMaster;
	private final ResponseSender responseSender;
	private final TelegramInitializer telegramInitializer; // You need it here

	@PostConstruct
	private void init() {
		responseSender.setBot(this);
	}

	@Override
	@SneakyThrows(TelegramApiRequestException.class)
	public Module initModule() {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		telegramBotsApi.registerBot(this);
		return this;
	}

	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public Module tryToReinit() {
		return initModule();
	}

	@Override
	public String getBotUsername() {
		return environment.getRequiredProperty("telegram.username");
	}

	@Override
	public String getBotToken() {
		return environment.getRequiredProperty("telegram.token");
	}

	@Override
	public void onUpdateReceived(Update update) {
		responseSender.sendText(chatMaster.handleUpdate(update));
	}
}
