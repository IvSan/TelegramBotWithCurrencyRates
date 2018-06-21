package xyz.hardliner.decider;

import lombok.Data;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Data
public class Node {

	private final List<Direction> directions = new ArrayList<>();
	private Supplier<ReplyKeyboardMarkup> incomingKeyboard;

	public void addDirection(Direction direction) {
		directions.add(direction);
	}

	public String invalidCommand() {
		return "invalidCommand";
	}

}
