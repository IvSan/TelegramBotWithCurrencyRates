package xyz.hardliner.counselor.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.UserCache;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMaster {

	private final UserCache userCache;
	@Getter
	private final ResponseSender responseSender;

	public void handleUpdate(Update update) {
		if (!isValid(update)) {
			return;
		}
		try {
			Instant start = Instant.now();
			process(update);
			log.debug("Respond on update done in " + Duration.between(start, Instant.now()).toMillis() + "ms");
		} catch (Exception ex) {
			log.error("Failed processing message. \nUpdate: {} \nException: {}", update.toString(), ex);
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

	private void process(Update update) {
		log.info("Update received: " + update.getMessage().getText());
		Interrogator user = userCache.recognizeInterrogator(update.getMessage());
		List<String> reaction = user.getNavigator().goTo(update.getMessage().getText());
		reaction.addAll(user.getNavigator().getLegendsToGo());
		responseSender.sendText(reaction, user);
	}
}
