package xyz.hardliner.decider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
public class Direction {

	private Node from;
	private Node to;

	private Pair<String, String> commandAndLegendToGo;
	private Supplier<String> leavingLegend;
	private Supplier<String> incomingLegend;
	private Supplier<String> automaticCommand;

	public Direction(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
}
