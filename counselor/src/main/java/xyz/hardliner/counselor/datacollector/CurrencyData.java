package xyz.hardliner.counselor.datacollector;

import lombok.Getter;
import lombok.Setter;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
public class CurrencyData {

	Float usdToRub;
	Float uerToRub;
	Float btcToUsd;

	LocalDateTime updated;

	public CurrencyData() {
		usdToRub = Float.MIN_NORMAL;
		uerToRub = Float.MIN_NORMAL;
		btcToUsd = Float.MIN_NORMAL;
		updated = LocalDateTime.now();
	}

	public String compileString() {
		StringBuilder builder = new StringBuilder();
		builder.append("1 USD = ").append(twoDigitsFormat(usdToRub)).append(" RUB\n");
		builder.append("1 EUR = ").append(twoDigitsFormat(uerToRub)).append(" RUB\n\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToUsd)).append(" USD\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToUsd * usdToRub)).append(" RUB\n");
		return builder.toString();
	}

	private static String twoDigitsFormat(Float floatValue) {
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		return formatter.format(floatValue);
	}
}
