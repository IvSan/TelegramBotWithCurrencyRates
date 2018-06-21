package xyz.hardliner.counselor.telegram;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.decider.Response;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@SuppressWarnings("WeakerAccess")
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

	public void sendText(Interrogator interrogator, List<String> strings, ReplyKeyboardMarkup keyboard) {
		List<BotApiMethod> messages = new ArrayList<>();
		for (String string : strings) {
			SendMessage message = (new SendMessage()).setText(string).setChatId(interrogator.getChatId());
			message.setReplyMarkup(keyboard != null ? keyboard : new ReplyKeyboardRemove());
			messages.add(message);
		}
		this.executeCommands(messages);
	}

	public void sendText(Pair<Interrogator, Response> pair) {
		if (pair == null) {
			return;
		}
		sendText(pair.getLeft(), pair.getRight().getMessages(), pair.getRight().getKeyboard());
	}
}
