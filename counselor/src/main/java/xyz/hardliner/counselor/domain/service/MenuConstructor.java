package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import xyz.hardliner.counselor.datacollector.CurrencyDataHandler;
import xyz.hardliner.counselor.db.StorageService;
import xyz.hardliner.counselor.decider.Direction;
import xyz.hardliner.counselor.decider.Navigator;
import xyz.hardliner.counselor.decider.Node;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MenuConstructor {

	private final CurrencyDataHandler dataHandler;
	private final StorageService storageService;

	@PostConstruct
	private void init() {
		dataHandler.getStorageService().setMenuConstructor(this);
	}

	public Navigator construct() {
		Node main = new Node(1);
		main.setKeyboard(() -> singleRowKeyboard("Settings", "Rates"));
		Node rates = new Node(2);
		Node settings = new Node(3);
		settings.setKeyboard(() -> doubleRowKeyboard(2, "Alerts", "Custom converter", "Back"));
		Node alerts = new Node(4);
		alerts.setInvalidError(() -> "Cannot parse alerts, need two numbers\nFor example:\n5000 15000");
		Node converter = new Node(5);
		converter.setInvalidError(() -> "Cannot parse amount, need just a number\nFor example:\n0.5");

		Direction getRatesFromMain = new Direction(main, rates);
		getRatesFromMain.setCommandAndLegendToGo(new ImmutablePair<>("rates", null));
		getRatesFromMain.setAction(pair -> dataHandler.reportData(pair.getLeft(), pair.getRight()));
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

		Direction moveToSettingsFromMain = new Direction(main, settings);
		moveToSettingsFromMain.setCommandAndLegendToGo(new ImmutablePair<>("settings", null));
		moveToSettingsFromMain.setIncomingLegend(() -> "Settings section");
		main.addDirection(moveToSettingsFromMain);

		Direction moveToMainFromSettings = new Direction(settings, main);
		moveToMainFromSettings.setCommandAndLegendToGo(new ImmutablePair<>("back", null));
		moveToMainFromSettings.setIncomingLegend(() -> "Settings didn't change");
		settings.addDirection(moveToMainFromSettings);

		Direction moveToAlertsFromSettings = new Direction(settings, alerts);
		moveToAlertsFromSettings.setCommandAndLegendToGo(new ImmutablePair<>("alerts",
				"With alerts function you can setup threshold BTC rates to notify you"));
		moveToAlertsFromSettings.setIncomingLegend(() ->
				"Enter lower and upper bound alerts for bitcoin in USD\nFor example '5000 15000'");
		settings.addDirection(moveToAlertsFromSettings);

		Direction moveToMainFromAlerts = new Direction(alerts, main);
		moveToMainFromAlerts.setCommandAndLegendToGo(new ImmutablePair<>("\\d*\\s\\d*", null));
		moveToMainFromAlerts.setIncomingLegend(() -> "Alerts saved");
		moveToMainFromAlerts.setAction(pair -> storageService.setBounds(pair.getLeft(), pair.getRight()));
		alerts.addDirection(moveToMainFromAlerts);

		Direction moveToConverterFromSettings = new Direction(settings, converter);
		moveToConverterFromSettings.setCommandAndLegendToGo(new ImmutablePair<>("custom converter",
				"Custom converter function will complement rates report with your own BTC amount calculations"));
		moveToConverterFromSettings.setIncomingLegend(() ->
				"Enter your amount of BTC to enable custom conversion\nEnter 0 to disable custom conversion");
		settings.addDirection(moveToConverterFromSettings);

		Direction moveToMainFromConverter = new Direction(converter, main);
		moveToMainFromConverter.setCommandAndLegendToGo(new ImmutablePair<>("\\d*\\.?,?\\d*", null));
		moveToMainFromConverter.setIncomingLegend(() -> "Custom converter saved");
		moveToMainFromConverter.setAction(pair -> storageService.setAmount(pair.getLeft(), pair.getRight()));
		converter.addDirection(moveToMainFromConverter);

		return new Navigator(main);
	}

	private static ReplyKeyboardMarkup singleRowKeyboard(String... labels) {
		return doubleRowKeyboard(labels.length, labels);
	}

	private static ReplyKeyboardMarkup doubleRowKeyboard(int upperRow, String... labels) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setResizeKeyboard(true);
		int index = 0;
		KeyboardRow upper = new KeyboardRow();
		KeyboardRow lower = new KeyboardRow();
		for (String label : labels) {
			if (index++ < upperRow) {
				upper.add(new KeyboardButton().setText(label));
			} else {
				lower.add(new KeyboardButton().setText(label));
			}
		}
		keyboardMarkup.setKeyboard(Arrays.asList(upper, lower));
		return keyboardMarkup;
	}

}
