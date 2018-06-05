package xyz.hardliner.counselor.telegram;

import lombok.Data;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import xyz.hardliner.decider.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Data
public class TelegramNode extends Node {

	private final List<TelegramDirection> telegramDirections = new ArrayList<>();
	private Supplier<ReplyKeyboardMarkup> incomingKeyboard;

	public void addDirection(TelegramDirection direction) {
		telegramDirections.add(direction);
	}

}
