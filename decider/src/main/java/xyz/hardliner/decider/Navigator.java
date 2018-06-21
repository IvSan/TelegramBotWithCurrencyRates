package xyz.hardliner.decider;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Navigator {

	private Node current;

	public Navigator(Node current) {
		this.current = current;
	}

	public List<String> getLegendsToGo() {
		return current.getDirections().stream().map(direction -> direction.getCommandAndLegendToGo().getRight())
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	public Response moveTo(@NonNull String command) {
		for (Direction direction : current.getDirections()) {
			if (command.equalsIgnoreCase(direction.getCommandAndLegendToGo().getLeft())) {
				List<String> messages = new ArrayList<>();
				nullsafeAdd(messages, direction.getLeavingLegend());
				current = direction.getTo();
				nullsafeAdd(messages, direction.getIncomingLegend());

				if (direction.getAutomaticCommand() != null) {
					messages.addAll(moveTo(direction.getAutomaticCommand().get()).getMessages());
				}

				for (String legend : getLegendsToGo()) {
					nullsafeAdd(messages, () -> legend);
				}

				return new Response(messages, current.getIncomingKeyboard().get());
			}
		}
		ArrayList<String> result = new ArrayList<>();
		result.add(current.invalidCommand());
		return new Response(result, current.getIncomingKeyboard().get());
	}

	public void nullsafeAdd(List<String> list, Supplier<String> supplier) {
		if (supplier != null) {
			String string = supplier.get();
			if (string != null) {
				list.add(string);
			}
		}
	}
}
