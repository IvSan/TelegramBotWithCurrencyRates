package xyz.hardliner.counselor.decider;

import lombok.Data;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Data
public class Node {

	private final Integer id;

	private final List<Direction> directions = new ArrayList<>();
	private Supplier<ReplyKeyboard> keyboard = ReplyKeyboardRemove::new;
	private Supplier<String> invalidError = () -> "Invalid command";

	public Node(int id) {
		this.id = id;
	}

	public void addDirection(Direction direction) {
		directions.add(direction);
	}

	public String invalidCommand() {
		return invalidError.get();
	}

}
