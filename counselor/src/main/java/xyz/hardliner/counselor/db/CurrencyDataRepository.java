package xyz.hardliner.counselor.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurrencyDataRepository extends MongoRepository<CurrencyData, Long> {

	public List<CurrencyData> findAll();

}
