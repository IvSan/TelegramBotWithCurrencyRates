package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import xyz.hardliner.counselor.datacollector.CurrencyDataHandler;
import xyz.hardliner.decider.Direction;
import xyz.hardliner.decider.Navigator;
import xyz.hardliner.decider.Node;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class MenuConstructor {

	private final CurrencyDataHandler dataHandler;

	@PostConstruct
	private void init() {
		dataHandler.getStorageService().setMenuConstructor(this);
	}

	public Navigator construct() {
		Node main = new Node();
		Node rates = new Node();

		Direction getRatesFromMain = new Direction(main, rates);
		getRatesFromMain.setCommandAndLegendToGo(new ImmutablePair<>("rates", null));
		getRatesFromMain.setIncomingLegend(dataHandler::getData);
		getRatesFromMain.setAutomaticCommand(() -> "main");
		main.addDirection(getRatesFromMain);

		Direction getRatesFromStartCommand = new Direction(main, rates);
		getRatesFromStartCommand.setCommandAndLegendToGo(new ImmutablePair<>("/start", null));
		getRatesFromStartCommand.setIncomingLegend(dataHandler::getData);
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
