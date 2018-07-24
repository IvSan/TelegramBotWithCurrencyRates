package xyz.hardliner.counselor.datacollector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.db.StorageService;
import xyz.hardliner.counselor.domain.CurrencyData;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.StrictMath.abs;
import static xyz.hardliner.counselor.util.Utils.twoDigitsFormat;

@Service
@RequiredArgsConstructor
public class CurrencyDataStatistics {

	private final StorageService storageService;

	private Float btcToUsdWeekAverage;
	private LocalDateTime btcToUsdWeekAverageUpdated;

	private Float btcToRubWeekAverage;
	private LocalDateTime btcToRubWeekAverageUpdated;

	private void updateWeekReport() {
		if (btcToUsdWeekAverage == null || btcToRubWeekAverage == null ||
				btcToUsdWeekAverageUpdated.isBefore(LocalDateTime.now().minusHours(6L))) {
			List<CurrencyData> data = storageService.findAllForLastWeek();
			btcToUsdWeekAverage = (float) (data.stream().mapToDouble(CurrencyData::getBtcToUsd).average().orElse(0));
			btcToRubWeekAverage = (float) (data.stream().mapToDouble(d -> d.getBtcToUsd() * d.getUsdToRub())
					.average().orElse(0));
			btcToUsdWeekAverageUpdated = LocalDateTime.now();
		}
	}

	public String compileReport(Float current, Float average) {
		StringBuilder builder = new StringBuilder();
		if (current >= average) {
			builder.append("⬆️ +");
		} else {
			builder.append("⬇️ -");
		}
		builder.append(twoDigitsFormat(abs(100 - (current / average) * 100)));
		builder.append("% to last week average");
		return builder.toString();
	}

	public String getBtcToUsdWeekReport(Float btcToUsd) {
		updateWeekReport();
		return compileReport(btcToUsd, btcToUsdWeekAverage);
	}

	public String getBtcToRubWeekReport(Float btcToRub) {
		updateWeekReport();
		return compileReport(btcToRub, btcToRubWeekAverage);
	}


}
