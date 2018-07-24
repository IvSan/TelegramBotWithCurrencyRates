package xyz.hardliner.counselor.domain;

import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Message {
	private boolean incoming;
	private String text;
	private Interrogator interrogator;
	private LocalDateTime time;

	public Message(boolean incoming, String text, Interrogator interrogator) {
		this.incoming = incoming;
		this.text = text;
		this.interrogator = interrogator;
		this.time = LocalDateTime.now();
	}
}
