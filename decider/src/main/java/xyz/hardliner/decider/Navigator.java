package xyz.hardliner.decider;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class Navigator {

	private Node current;

	public Navigator(Node current) {
		this.current = current;
	}

	public List<String> getLegendsToGo() {
		return current.getDirections().stream().map(direction -> direction.getCommandAndLegendToGo().getRight())
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	public List<String> goTo(@NonNull String command) {
		for (Direction direction : current.getDirections()) {
			if (command.equalsIgnoreCase(direction.getCommandAndLegendToGo().getLeft())) {
				List<String> messages = new ArrayList<>();
				nullsafeAdd(messages, direction.getLeavingLegend());
				current = direction.getTo();
				nullsafeAdd(messages, direction.getIncomingLegend());

				if (direction.getAutomaticCommand() != null) {
					messages.addAll(goTo(direction.getAutomaticCommand()));
				}
				return messages;
			}
		}
		ArrayList<String> result = new ArrayList<>();
		result.add(current.invalidCommand());
		return result;
	}

	private void nullsafeAdd(List<String> list, String string) {
		if (string != null) {
			list.add(string);
		}
	}
}
