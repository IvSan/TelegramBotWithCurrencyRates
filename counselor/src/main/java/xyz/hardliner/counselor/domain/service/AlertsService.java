package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.db.InterrogatorRepository;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.Settings;
import xyz.hardliner.counselor.telegram.ResponseSender;

import java.util.Collections;
import java.util.List;

import static xyz.hardliner.counselor.util.Utils.twoDigitsFormat;

@Service
@RequiredArgsConstructor
public class AlertsService {
	private final InterrogatorRepository interrogatorRepository;
	private final ResponseSender responseSender;

	public void checkAlerts(Float btcToUsdRate) {
		List<Interrogator> interrogators = interrogatorRepository.findAll();
		for (Interrogator interrogator : interrogators) {
			Settings settings = interrogator.getSettings();

			if (settings == null) {
				continue;
			}

			if (settings.getLowerAlertBound() > Float.MIN_VALUE && settings.getUpperAlertBound() < Float.MAX_VALUE) {
				if (btcToUsdRate < settings.getLowerAlertBound()) {
					fireLowAlert(interrogator, btcToUsdRate);
					settings.setLowerAlertBound(Float.MIN_VALUE);
					interrogatorRepository.save(interrogator);
				}
				if (btcToUsdRate > settings.getUpperAlertBound()) {
					fireHighAlert(interrogator, btcToUsdRate);
					settings.setUpperAlertBound(Float.MAX_VALUE);
					interrogatorRepository.save(interrogator);
				}
			}
		}
	}

	private void fireLowAlert(Interrogator interrogator, Float btcToUsdRate) {
		fireAlert(interrogator, "LOW ALERT!\n1 BTC = " + twoDigitsFormat(btcToUsdRate) + " USD");
	}

	private void fireHighAlert(Interrogator interrogator, Float btcToUsdRate) {
		fireAlert(interrogator, "HIGH ALERT!\n1 BTC = " + twoDigitsFormat(btcToUsdRate) + " USD");
	}

	private void fireAlert(Interrogator interrogator, String text) {
		responseSender.sendText(interrogator, Collections.singletonList(text));
	}
}