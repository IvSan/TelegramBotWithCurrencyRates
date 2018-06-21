package xyz.hardliner.counselor.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import xyz.hardliner.counselor.db.InterrogatorRepository;
import xyz.hardliner.counselor.domain.Interrogator;

@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories(basePackageClasses = InterrogatorRepository.class)
@ComponentScan(basePackages = {"xyz.hardliner.counselor"})
@PropertySource(value = "application.properties", encoding = "UTF-8")
public class Context {

}
