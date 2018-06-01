package xyz.hardliner.decider;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Node {

	private final List<Direction> directions = new ArrayList<>();

	public void addDirection(Direction direction) {
		directions.add(direction);
	}

	public String invalidCommand() {
		return "invalidCommand";
	}

}
