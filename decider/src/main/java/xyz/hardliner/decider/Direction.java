package xyz.hardliner.decider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@Setter
@RequiredArgsConstructor
public class Direction {

	private final Node from;
	private final Node to;

	private Pair<String, String> commandAndLegendToGo;
	private String leavingLegend;
	private String incomingLegend;
	private String automaticCommand;

}
