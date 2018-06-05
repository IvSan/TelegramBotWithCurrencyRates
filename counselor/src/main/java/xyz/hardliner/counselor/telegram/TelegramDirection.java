package xyz.hardliner.counselor.telegram;

import lombok.Data;
import xyz.hardliner.decider.Direction;

@Data
public class TelegramDirection extends Direction {

	private TelegramNode from;
	private TelegramNode to;

	public TelegramDirection(TelegramNode from, TelegramNode to) {
		this.from = from;
		this.to = to;
	}

}
