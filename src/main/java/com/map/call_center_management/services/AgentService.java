package com.map.call_center_management.services;


import static com.map.call_center_management.data.enums.AgentAvailability.OFFLINE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.repositories.AgentRepository;

@Service
public class AgentService {
	
	@Autowired
	private AgentRepository repo;
	
	public List<Agent> getAllAgents() {
		return repo.findAll();
	}

	public List<Agent> getAllActiveAgents() {
		return repo.findByActiveTrue();
	}

	public Optional<Agent> getAgentById(Long identifier) {
		return repo.findById(identifier);
	}
	
	public Optional<Agent> save(Agent agent) {
		if (Objects.isNull(agent.getId())) {
			Agent newAgent = Agent.builder().active(TRUE)
					.availability(OFFLINE)
					.name(agent.getName())
					.phoneNumber(agent.getPhoneNumber())
					.build();
			return Optional.of(repo.save(newAgent));
		}
		Optional<Agent> result = repo.findById(agent.getId());
		if (result.isEmpty()) {
			return result;
		}
		
		return Optional.of(
				repo.save(
						result.get().toBuilder()
							.name(agent.getName())
							.phoneNumber(agent.getPhoneNumber())
							.availability(agent.getAvailability())
							.build()
						)
				);
				
		
	}

	public Optional<Agent> decommission(Long identifier) {
		Optional<Agent> result = repo.findById(identifier);
		if (result.isEmpty()) {
			return result;
		}

		return Optional.of(
				repo.save(
					result.get().toBuilder()
					.active(FALSE)
					.availability(OFFLINE)
					.build()
					)
				);
		
	}

}
