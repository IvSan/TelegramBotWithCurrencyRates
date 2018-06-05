package xyz.hardliner.counselor.telegram;

import lombok.NonNull;
import xyz.hardliner.decider.Navigator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TelegramNavigator extends Navigator {

	private TelegramNode current;

	public TelegramNavigator(TelegramNode current) {
		this.current = current;
	}

	public List<String> getLegendsToGo() {
		return current.getDirections().stream().map(direction -> direction.getCommandAndLegendToGo().getRight())
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	public Response moveTo(@NonNull String command) {
		for (TelegramDirection direction : current.getTelegramDirections()) {
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
		return new Response(result);
	}

}
