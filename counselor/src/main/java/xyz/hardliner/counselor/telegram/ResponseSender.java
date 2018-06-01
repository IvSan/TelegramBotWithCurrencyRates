package xyz.hardliner.counselor.telegram;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import xyz.hardliner.counselor.app.Bot;
import xyz.hardliner.counselor.domain.Interrogator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ResponseSender {

	@Setter
	private Bot bot;

	public void executeCommand(BotApiMethod method) {
		try {
			bot.execute(method);
		} catch (TelegramApiException ex) {
			log.error("Cannot execute bot's method. Exception: " + ex.getMessage(), ex);
		}
	}

	public void executeCommands(List<BotApiMethod> methods) {
		for (BotApiMethod method : methods) {
			executeCommand(method);
		}
	}

	public void sendText(List<String> strings, Interrogator interrogator) {
		List<BotApiMethod> messages = new ArrayList<>();
		for (String string : strings) {
			messages.add((new SendMessage()).setText(string).setChatId(interrogator.getChatId()));
		}
		this.executeCommands(messages);
	}
}