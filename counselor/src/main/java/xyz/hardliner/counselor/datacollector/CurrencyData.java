package xyz.hardliner.counselor.datacollector;

import lombok.Data;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
public class CurrencyData {

	Float usdToRub;
	Float uerToRub;
	Float btcToRub;
	Float btcToUsd;

	LocalDateTime created;

	public CurrencyData(FixerIoJsonUnit data) {
		Float floatRub = 1f;
		this.uerToRub = (data.getRates().get("RUB")) / floatRub;
		this.usdToRub = uerToRub / (data.getRates().get("USD"));
		this.btcToRub = uerToRub / (data.getRates().get("BTC"));
		this.btcToUsd = data.getRates().get("USD") / data.getRates().get("BTC");

		created = LocalDateTime.now();
	}

	public String compileString() {
		StringBuilder builder = new StringBuilder();
		builder.append("1 USD = ").append(twoDigitsFormat(usdToRub)).append(" RUB\n");
		builder.append("1 EUR = ").append(twoDigitsFormat(uerToRub)).append(" RUB\n\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToUsd)).append(" USD\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToRub)).append(" RUB\n");
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
