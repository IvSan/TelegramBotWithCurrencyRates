package xyz.hardliner.counselor.util;

import org.telegram.telegrambots.api.objects.User;

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
}
