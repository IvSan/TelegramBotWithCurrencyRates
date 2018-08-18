package xyz.hardliner.counselor.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.db.StorageService;
import xyz.hardliner.counselor.decider.Response;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.NavigatorService;

import java.time.Duration;
import java.time.Instant;

import static xyz.hardliner.counselor.util.Utils.parseIdentities;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMaster {

	private final StorageService storageService;
	private final NavigatorService navigator;

	public Pair<Interrogator, Response> handleUpdate(Update update) {
		if (!isValid(update)) {
			return null;
		}
		try {
			Instant start = Instant.now();
			Pair<Interrogator, Response> result = process(update);
			for (String msg : result.getRight().getMessages()) {
				storageService.saveOutgoingMessage(result.getLeft(), msg);
			}
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
			log.error("Invalid update received from user {}: {}", parseIdentities(user), update.toString());
			return false;
		}
		log.debug("New update received: " + update.toString());
		return true;
	}

	private Pair<Interrogator, Response> process(Update update) {
		log.info("Update received: '" + update.getMessage().getText() + "' from "
				+ parseIdentities(update.getMessage().getFrom()));
		Interrogator user = storageService.parseUser(update.getMessage());
		String str = update.getMessage().getText();
		storageService.saveIncomingMessage(user, str);
		return new ImmutablePair<>(user, navigator.moveTo(user, str));
	}
}
