package xyz.hardliner.counselor.decider;

import lombok.Data;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Data
public class Response {
	private List<String> messages;
	private ReplyKeyboard keyboard;

	public Response(List<String> messages, ReplyKeyboard keyboard) {
		this.messages = messages;
		this.keyboard = keyboard;
	}
}
