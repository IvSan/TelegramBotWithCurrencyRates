package xyz.hardliner.counselor.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.telegram.telegrambots.api.objects.User;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Interrogator {
	@Id
	private Integer id; // Unique identifier for this user or bot
	private String firstName; // User‘s or bot’s first name
	private String lastName; // Optional. User‘s or bot’s last name
	private String userName; // Optional. User‘s or bot’s username
	private Long chatId;

	private Long invocations;
	private Settings settings;
	private Integer position;

	public Interrogator(User user, Long chatId) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.userName = user.getUserName();
		this.chatId = chatId;
		this.invocations = 0L;
		this.settings = new Settings();
		this.position = 1;
	}

	public synchronized void countInvocation() {
		this.invocations++;
	}
}
