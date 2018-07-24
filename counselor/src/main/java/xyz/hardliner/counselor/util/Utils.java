package xyz.hardliner.counselor.util;

import org.telegram.telegrambots.api.objects.User;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

	public static String parseIdentities(User user) {
		StringBuilder builder = new StringBuilder();

		if (user.getFirstName() != null) {
			builder.append(user.getFirstName()).append(" ");
		}
		if (user.getLastName() != null) {
			builder.append(user.getLastName()).append(" ");
		}
		if (user.getUserName() != null) {
			builder.append("(").append(user.getUserName()).append(")").append(" ");
		}

		String result = builder.toString();
		if (result.length() == 0) {
			return user.getId().toString();
		} else {
			return result.trim();
		}
	}

	public static String twoDigitsFormat(Float floatValue) {
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		return formatter.format(floatValue);
	}
}
