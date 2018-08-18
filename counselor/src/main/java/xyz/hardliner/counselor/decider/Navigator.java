package xyz.hardliner.counselor.decider;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.hardliner.counselor.domain.Interrogator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
public class Navigator {

	private Node current;
	private Map<Integer, Node> nodes = new HashMap<>();

	public Navigator(Node current) {
		this.current = current;
		nodes.put(current.getId(), current);
		addChildren(nodes, current);
	}

	private void addChildren(Map<Integer, Node> nodes, Node node) {
		for (Direction direction : node.getDirections()) {
			Node to = direction.getTo();
			if (!nodes.containsKey(to.getId())) {
				nodes.put(to.getId(), to);
				addChildren(nodes, to);
			}
		}
	}

	public Pair<List<String>, Node> moveTo(Interrogator user, @NonNull String command) {
		return moveTo(user, command, true);
	}

	public Pair<List<String>, Node> moveTo(Interrogator user, @NonNull String command, boolean addLegendsToGo) {
		for (Direction direction : current.getDirections()) {
			if (command.toLowerCase().matches(direction.getCommandAndLegendToGo().getLeft())) {
				List<String> messages = new ArrayList<>();
				nullsafeAdd(messages, direction.getLeavingLegend());
				current = direction.getTo();
				nullsafeAdd(messages, direction.getIncomingLegend());

				direction.getAction().accept(new ImmutablePair<>(user, command));

				if (direction.getAutomaticCommand() != null) {
					messages.addAll(moveTo(user, direction.getAutomaticCommand().get(), false).getLeft());
				}

				if (addLegendsToGo) {
					for (String legend : getLegendsToGo()) {
						nullsafeAdd(messages, () -> legend);
					}
				}

				return new ImmutablePair<>(messages, current);
			}
		}
		ArrayList<String> result = new ArrayList<>();
		result.add(current.invalidCommand());

		return new ImmutablePair<>(result, current);
	}

	private List<String> getLegendsToGo() {
		return current.getDirections().stream().map(direction -> direction.getCommandAndLegendToGo().getRight())
				.filter(Objects::nonNull).collect(Collectors.toList());
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
