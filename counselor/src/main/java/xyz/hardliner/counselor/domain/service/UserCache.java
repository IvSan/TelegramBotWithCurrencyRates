package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.db.StorageService;
import xyz.hardliner.counselor.domain.Interrogator;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserCache {

	private final StorageService storageService;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private ConcurrentHashMap<Integer, Interrogator> recentUsers = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		executor.scheduleAtFixedRate(() -> recentUsers.clear(), 1, 1, TimeUnit.DAYS);
	}

	public Interrogator recognizeInterrogator(Message message) {
		User user = message.getFrom();
		if (recentUsers.containsKey(user.getId())) {
			Interrogator interrogator = recentUsers.get(user.getId());
			interrogator.countInvocation();
			storageService.save(interrogator);
			return interrogator;
		}
		Interrogator interrogator = storageService.parseUser(message);
		recentUsers.put(user.getId(), interrogator);
		return interrogator;
	}
}
