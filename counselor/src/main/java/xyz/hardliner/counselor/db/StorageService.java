package xyz.hardliner.counselor.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import xyz.hardliner.counselor.domain.Interrogator;
import xyz.hardliner.counselor.domain.service.MenuConstructor;
import xyz.hardliner.counselor.domain.service.ServiceFacade;

@Service
@AllArgsConstructor
public class StorageService {

	//	private final Datastore datastore;
	private final ServiceFacade services;
	private final InterrogatorRepository repository;

//	public StorageService(Environment environment, ServiceFacade services) {
//		datastore = new Morphia().createDatastore(new MongoClient(), environment.getRequiredProperty("db.name"));
//		datastore.ensureIndexes();
//		this.services = services;
//	}

	public Interrogator parseUser(Message message) {
		User user = message.getFrom();
//		Interrogator interrogator = datastore.createQuery(Interrogator.class).field("id").equal(user.getId()).get();
		Interrogator interrogator = repository.findById(user.getId()).get();
		if (interrogator == null) {
			interrogator = new Interrogator(user, message.getChatId(), MenuConstructor.construct(services));
		}
		interrogator.setChatId(message.getChatId());
		interrogator.setFirstName(user.getFirstName());
		interrogator.setLastName(user.getLastName());
		interrogator.setUserName(user.getUserName());
		interrogator.countInvocation();
		repository.save(interrogator);
		return interrogator;
	}

	public void save(Interrogator interrogator) {
		repository.save(interrogator);
	}

//	public List<Interrogator> findAllInterrogators() {
//		return datastore.createQuery(Interrogator.class).asList();
//	}
}
