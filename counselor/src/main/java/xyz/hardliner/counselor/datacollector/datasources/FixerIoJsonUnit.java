package xyz.hardliner.counselor.datacollector.datasources;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FixerIoJsonUnit {
	private String base;
	private String date;
	private Map<String, Float> rates = new HashMap<>();

	@JsonAnySetter
	public void setDynamicProperty(String name, String rate) {
		try {
			rates.put(name, Float.parseFloat(rate));
		} catch (NumberFormatException ex) {
			//not a number, ignore
		}
	}
}


