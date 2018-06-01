package xyz.hardliner.counselor.domain;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.domain.service.MenuConstructor;
import xyz.hardliner.decider.Navigator;

@Data
@Entity
public class Interrogator {
	@Id
	private Integer id; // Unique identifier for this user or bot
	private String firstName; // User‘s or bot’s first name
	private String lastName; // Optional. User‘s or bot’s last name
	private String userName; // Optional. User‘s or bot’s username
	private Long chatId;

	private Navigator navigator;

	public Interrogator(User user, Long chatId) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.userName = user.getUserName();
		this.chatId = chatId;

		this.navigator = MenuConstructor.construct();
	}
}