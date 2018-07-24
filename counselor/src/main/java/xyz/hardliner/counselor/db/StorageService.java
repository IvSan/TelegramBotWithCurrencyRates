package xyz.hardliner.counselor.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.MenuConstructor;

import java.util.Optional;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class StorageService {

	private final InterrogatorRepository interrogatorRepository;
	private final CurrencyDataRepository currencyDataRepository;

	private MenuConstructor menuConstructor;

	public Interrogator parseUser(Message message) {
		User user = message.getFrom();
		Optional<Interrogator> optional = interrogatorRepository.findById(user.getId());
		Interrogator interrogator = optional.orElseGet(() ->
				new Interrogator(user, message.getChatId(), menuConstructor.construct()));
		interrogator.setChatId(message.getChatId());
		interrogator.setFirstName(user.getFirstName());
		interrogator.setLastName(user.getLastName());
		interrogator.setUserName(user.getUserName());
		interrogator.countInvocation();
		if (interrogator.getNavigator() == null) {
			interrogator.setNavigator(menuConstructor.construct());
		}
		interrogatorRepository.save(interrogator);
		return interrogator;
	}

	public void save(Interrogator interrogator) {
		interrogatorRepository.save(interrogator);
	}

	public void save(CurrencyData data) {
		currencyDataRepository.save(data);
	}

}
