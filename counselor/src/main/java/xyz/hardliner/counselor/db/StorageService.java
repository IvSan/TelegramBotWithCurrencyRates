package xyz.hardliner.counselor.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.domain.CurrencyData;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.MenuConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class StorageService {

	private final InterrogatorRepository interrogatorRepository;
	private final CurrencyDataRepository currencyDataRepository;
	private final MessageRepository messageRepository;

	private MenuConstructor menuConstructor;

	public Interrogator parseUser(Message message) {
		User user = message.getFrom();
		Optional<Interrogator> optional = interrogatorRepository.findById(user.getId());
		Interrogator interrogator = optional.orElseGet(() -> new Interrogator(user, message.getChatId()));
		interrogator.setChatId(message.getChatId());
		interrogator.setFirstName(user.getFirstName());
		interrogator.setLastName(user.getLastName());
		interrogator.setUserName(user.getUserName());
		interrogator.countInvocation();
		if (interrogator.getPosition() == null) {
			interrogator.setPosition(1);
		}
		interrogatorRepository.save(interrogator);
		return interrogator;
	}

	public void setBounds(Interrogator interrogator, String values) {
		String[] strings = values.split(" ");
		Float low = Float.parseFloat(strings[0]);
		Float high = Float.parseFloat(strings[1]);
		interrogator.getSettings().setLowerAlertBound(low);
		interrogator.getSettings().setUpperAlertBound(high);
		interrogatorRepository.save(interrogator);
	}

	public void setAmount(Interrogator interrogator, String value) {
		Float amount = Float.parseFloat(value.replace(',', '.'));
		interrogator.getSettings().setBtcAmount(amount);
		interrogatorRepository.save(interrogator);
	}

	public void saveIncomingMessage(Interrogator interrogator, String text) {
		messageRepository.save(new xyz.hardliner.counselor.domain.Message(true, text, interrogator));
	}

	public void saveOutgoingMessage(Interrogator interrogator, String text) {
		messageRepository.save(new xyz.hardliner.counselor.domain.Message(false, text, interrogator));
	}

	public void save(CurrencyData data) {
		currencyDataRepository.save(data);
	}

	public List<CurrencyData> findAllForLastWeek() {
		return currencyDataRepository.findByUpdatedAfter(LocalDateTime.now().minusDays(7));
	}
}
