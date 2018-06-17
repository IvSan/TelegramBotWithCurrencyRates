package xyz.hardliner.counselor.domain.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import xyz.hardliner.counselor.telegram.TelegramDirection;
import xyz.hardliner.counselor.telegram.TelegramNavigator;
import xyz.hardliner.counselor.telegram.TelegramNode;

import java.util.Collections;

public class MenuConstructor {

	private MenuConstructor() {
	}

	public static TelegramNavigator construct(ServiceFacade services) {
		TelegramNode main = new TelegramNode();
		TelegramNode rates = new TelegramNode();

		TelegramDirection getRatesFromMain = new TelegramDirection(main, rates);
		getRatesFromMain.setCommandAndLegendToGo(new ImmutablePair<>("rates", null));
		getRatesFromMain.setIncomingLegend(services::getCurrencyData);
		getRatesFromMain.setAutomaticCommand(() -> "main");
		main.addDirection(getRatesFromMain);

		TelegramDirection getRatesFromStartCommand = new TelegramDirection(main, rates);
		getRatesFromStartCommand.setCommandAndLegendToGo(new ImmutablePair<>("/start", null));
		getRatesFromStartCommand.setIncomingLegend(services::getCurrencyData);
		getRatesFromStartCommand.setAutomaticCommand(() -> "main");
		main.addDirection(getRatesFromStartCommand);

		TelegramDirection autoReturnFromRatesToMain = new TelegramDirection(rates, main);
		autoReturnFromRatesToMain.setCommandAndLegendToGo(new ImmutablePair<>("main", null));
		rates.addDirection(autoReturnFromRatesToMain);

		main.setIncomingKeyboard(() -> oneButtonKeyboard("Rates"));

		return new TelegramNavigator(main);
	}

	private static ReplyKeyboardMarkup oneButtonKeyboard(String label) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setResizeKeyboard(true);
		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton().setText(label));
		keyboardMarkup.setKeyboard(Collections.singletonList(row));
		return keyboardMarkup;
	}


}
