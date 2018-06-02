package xyz.hardliner.counselor.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.datacollector.CurrencyDataHandler;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.UserCache;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMaster {

	private final UserCache userCache;
	private final CurrencyDataHandler dataHandler;

	public Pair<Interrogator, List<String>> handleUpdate(Update update) {
		if (!isValid(update)) {
			return null;
		}
		try {
			Instant start = Instant.now();
			Pair<Interrogator, List<String>> result = process(update);
			log.debug("Respond on update done in " + Duration.between(start, Instant.now()).toMillis() + "ms");
			return result;
		} catch (Exception ex) {
			log.error("Failed processing message. \nUpdate: {} \nException: {}", update.toString(), ex);
			return null;
		}
	}

	private boolean isValid(Update update) {
		if (!update.hasMessage() || !update.getMessage().hasText()) {
			User user = update.getMessage().getFrom();
			log.error("Invalid update received from user {}{}.",
					user.getFirstName(), user.getLastName() != null ? " " + user.getLastName() : "");
			return false;
		}
		log.debug("New update received: " + update.toString());
		return true;
	}

	private Pair<Interrogator, List<String>> process(Update update) {
		log.info("Update received: " + update.getMessage().getText());
		Interrogator user = userCache.recognizeInterrogator(update.getMessage());
		//TODO temporaly
		String str = update.getMessage().getText();
		List<String> reaction;
		if (str.equalsIgnoreCase("rates")) {
			reaction = Collections.singletonList(dataHandler.getData());
		} else {
			reaction = user.getNavigator().goTo(str);
			reaction.addAll(user.getNavigator().getLegendsToGo());
		}
		return new ImmutablePair<>(user, reaction);
	}
}
