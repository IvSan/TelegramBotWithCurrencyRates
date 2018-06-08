package xyz.hardliner.counselor.telegram;

import lombok.Data;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Data
public class Response {
	private List<String> messages;
	private ReplyKeyboardMarkup keyboard;

	public Response(List<String> messages, ReplyKeyboardMarkup keyboard) {
		this.messages = messages;
		this.keyboard = keyboard;
	}
}
