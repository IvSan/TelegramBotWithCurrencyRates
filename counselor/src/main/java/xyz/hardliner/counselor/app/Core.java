package xyz.hardliner.counselor.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.hardliner.counselor.telegram.Bot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class Core {

	private final Bot bot;
	private List<Module> modules;
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	@PostConstruct
	public void initCore() {
		modules = new ArrayList<>();
		constructBot();
		executor.scheduleAtFixedRate(tryToFix(), 60, 60, TimeUnit.SECONDS);
	}

	private Runnable tryToFix() {
		return () -> {
			for (Module module : modules) {
				if (!module.isOnline()) {
					module.tryToReinit();
				}
			}
		};
	}

	private void constructBot() {
		try {
			bot.initModule();
			modules.add(bot);
		} catch (Exception ex) {
			log.error("Failed to start telegram bot functionality. Exception: " + ex.getMessage(), ex);
		}
	}
}
