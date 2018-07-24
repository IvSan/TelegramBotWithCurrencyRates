package xyz.hardliner.counselor.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

import java.time.LocalDateTime;

import static xyz.hardliner.counselor.util.Utils.twoDigitsFormat;

@Getter
@Setter
@Entity
@ToString
public class CurrencyData {
	@Id
	LocalDateTime updated;

	Float usdToRub;
	Float eurToRub;
	Float btcToUsd;

	@Transient
	String btcToUsdStatistics;
	@Transient
	String btcToRubStatistics;

	public CurrencyData() {
		usdToRub = Float.MIN_NORMAL;
		eurToRub = Float.MIN_NORMAL;
		btcToUsd = Float.MIN_NORMAL;
		updated = LocalDateTime.now();
	}

	public String compileString() {
		StringBuilder builder = new StringBuilder();
		builder.append("1 USD = ").append(twoDigitsFormat(usdToRub)).append(" RUB\n");
		builder.append("1 EUR = ").append(twoDigitsFormat(eurToRub)).append(" RUB\n\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToUsd)).append(" USD\n");
		builder.append(btcToUsdStatistics).append("\n");
		builder.append("1 BTC = ").append(twoDigitsFormat(btcToUsd * usdToRub)).append(" RUB\n");
		builder.append(btcToRubStatistics).append("\n");
		return builder.toString();
	}


}

