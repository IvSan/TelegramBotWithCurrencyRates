package xyz.hardliner.counselor.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Settings {
	Float lowerAlertBound = Float.MIN_VALUE;
	Float upperAlertBound = Float.MAX_VALUE;
	Float btcAmount = 0f;
}
