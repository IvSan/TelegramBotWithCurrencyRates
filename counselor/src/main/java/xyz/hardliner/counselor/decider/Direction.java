package xyz.hardliner.counselor.decider;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import xyz.hardliner.counselor.domain.Interrogator;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class Direction {

	private Node from;
	private Node to;

	private Pair<String, String> commandAndLegendToGo;
	private Supplier<String> leavingLegend;
	private Supplier<String> incomingLegend;
	private Supplier<String> automaticCommand;

	private Function<Pair<Interrogator, String>, List<String>> action = a -> null;

	public Direction(Node from, Node to) {
		this.from = from;
		this.to = to;
	}

}
