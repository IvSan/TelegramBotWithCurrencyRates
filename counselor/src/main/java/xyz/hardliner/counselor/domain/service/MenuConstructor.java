package xyz.hardliner.counselor.domain.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import xyz.hardliner.decider.Direction;
import xyz.hardliner.decider.Navigator;
import xyz.hardliner.decider.Node;

import java.util.Collections;

public class MenuConstructor {

	private MenuConstructor() {
	}

	public static Navigator construct(ServiceFacade services) {
		Node main = new Node();
		Node rates = new Node();

		Direction getRatesFromMain = new Direction(main, rates);
		getRatesFromMain.setCommandAndLegendToGo(new ImmutablePair<>("rates", null));
		getRatesFromMain.setIncomingLegend(services::getCurrencyData);
		getRatesFromMain.setAutomaticCommand(() -> "main");
		main.addDirection(getRatesFromMain);

		Direction getRatesFromStartCommand = new Direction(main, rates);
		getRatesFromStartCommand.setCommandAndLegendToGo(new ImmutablePair<>("/start", null));
		getRatesFromStartCommand.setIncomingLegend(services::getCurrencyData);
		getRatesFromStartCommand.setAutomaticCommand(() -> "main");
		main.addDirection(getRatesFromStartCommand);

		Direction autoReturnFromRatesToMain = new Direction(rates, main);
		autoReturnFromRatesToMain.setCommandAndLegendToGo(new ImmutablePair<>("main", null));
		rates.addDirection(autoReturnFromRatesToMain);

		main.setIncomingKeyboard(() -> oneButtonKeyboard("Rates"));

		return new Navigator(main);
	}

	private static ReplyKeyboardMarkup oneButtonKeyboard(String label) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setResizeKeyboard(true);
		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton().setText(label));
		keyboardMarkup.setKeyboard(Collections.singletonList(row));
		return keyboardMarkup;
	}


}
