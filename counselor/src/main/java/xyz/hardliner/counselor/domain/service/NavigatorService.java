package xyz.hardliner.counselor.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import xyz.hardliner.counselor.db.InterrogatorRepository;
import xyz.hardliner.counselor.decider.Navigator;
import xyz.hardliner.counselor.decider.Node;
import xyz.hardliner.counselor.decider.Response;
import xyz.hardliner.counselor.domain.Interrogator;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NavigatorService {

	private final MenuConstructor menuConstructor;
	private final InterrogatorRepository interrogatorRepository;
	private Navigator navigator;

	@PostConstruct
	private void init() {
		navigator = menuConstructor.construct();
	}

	public Response moveTo(Interrogator interrogator, String command) {
		navigator.setCurrent(navigator.getNodes().get(interrogator.getPosition()));
		Pair<List<String>, Node> result = navigator.moveTo(interrogator, command);
		interrogator.setPosition(result.getRight().getId());
		interrogatorRepository.save(interrogator);
		return new Response(result.getLeft(), result.getRight().getKeyboard().get());
	}
}
