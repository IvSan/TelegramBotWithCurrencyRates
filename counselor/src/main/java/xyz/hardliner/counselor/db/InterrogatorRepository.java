package xyz.hardliner.counselor.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.domain.Interrogator;

import java.util.List;

@Service
public interface InterrogatorRepository extends MongoRepository<Interrogator, Integer> {

	public List<Interrogator> findAll();

}
