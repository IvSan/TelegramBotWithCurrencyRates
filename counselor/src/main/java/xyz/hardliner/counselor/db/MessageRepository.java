package xyz.hardliner.counselor.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.domain.Message;

import java.util.List;

@Service
public interface MessageRepository extends MongoRepository<Message, Integer> {

	public List<Message> findAll();

}
