package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.db.StorageService;
import xyz.hardliner.counselor.domain.Interrogator;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCache {

	private final StorageService storageService;

	private List<Interrogator> recentUsers = new LinkedList<>();

	public Interrogator recognizeInterrogator(Message message) {
		User user = message.getFrom();
		for (Interrogator interrogator : recentUsers) {
			if (interrogator.getId().equals(user.getId())) {
				return interrogator;
			}
		}
		Interrogator interrogator = storageService.parseUser(message);
		recentUsers.add(interrogator);
		return interrogator;
	}
}
