package xyz.hardliner.counselor.domain.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.hardliner.counselor.datacollector.CurrencyDataHandler;
import xyz.hardliner.decider.Direction;
import xyz.hardliner.decider.Navigator;
import xyz.hardliner.decider.Node;

public class MenuConstructor {

	public static Navigator construct(CurrencyDataHandler dataHandler) {
		Node vil = new Node();
		Node fors = new Node();
		Node town = new Node();

		Direction villageToForest = new Direction(vil, fors);
		villageToForest.setCommandAndLegendToGo(new ImmutablePair<>("forest", "able to go forest"));
		villageToForest.setLeavingLegend(() -> "leaving village");
		villageToForest.setIncomingLegend(() -> "welcome to forest");
		vil.addDirection(villageToForest);

		Direction forestToVillage = new Direction(fors, vil);
		forestToVillage.setCommandAndLegendToGo(new ImmutablePair<>("village", "you can go village"));
		forestToVillage.setLeavingLegend(() -> "leaving forest");
		forestToVillage.setIncomingLegend(() -> "welcome to village");
		fors.addDirection(forestToVillage);

		Direction villageToTown = new Direction(vil, town);
		villageToTown.setCommandAndLegendToGo(new ImmutablePair<>("town", "you can go to town"));
		villageToTown.setLeavingLegend(() -> "escaping village");
//		villageToTown.setIncomingLegend(() -> "guard force you to go back");
		villageToTown.setIncomingLegend(() -> dataHandler.getData());
		villageToTown.setAutomaticCommand(() -> "village");
		vil.addDirection(villageToTown);

		Direction townToVillage = new Direction(town, vil);
		townToVillage.setCommandAndLegendToGo(new ImmutablePair<>("village", null));
		townToVillage.setIncomingLegend(() -> "You sadly returned to village");
		town.addDirection(townToVillage);

		Navigator navigator = new Navigator(vil);
		return navigator;
	}

}
