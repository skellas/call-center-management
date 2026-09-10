package com.map.call_center_management.services;

import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.map.call_center_management.data.entities.Call;
import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.repositories.AgentRepository;
import com.map.call_center_management.data.repositories.CallRepository;

@Component
public class CallService {
	
	@Autowired
	private CallRepository repo;
	@Autowired
	private AgentRepository agentRepo;
	
	public List<Call> getAllCalls() {
		return repo.findAll();
	}

	public List<Call> getAllActiveCalls() {
		return repo.findByActiveTrueOrderByPriorityDescCreatedOnDesc();
	}
	
	public Optional<Call> getCallById(Long identifier) {
		return repo.findById(identifier);
	}
	
	public Optional<Call> assignCallToAgent(Long callId, Long agentId) {

		Optional<Call> callResult = repo.findById(callId);
		if (callResult.isEmpty()) {
			return Optional.empty();
		}
		Optional<Agent> agentResult = agentRepo.findById(agentId);
		if (agentResult.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(repo.save(callResult.get().toBuilder().agent(agentResult.get()).build()));
	}
	
	public Optional<Call> save(Call call) {
		if (Objects.isNull(call.getId())) {
			Call newCall = Call.builder().active(TRUE)
					.name(call.getName())
					.notes(call.getNotes())
					.phoneNumber(call.getPhoneNumber())
					.priority(call.getPriority())
					.status(call.getStatus())
					.build();
			return Optional.of(repo.save(newCall));
		}
		Optional<Call> result = repo.findById(call.getId());
		if (result.isEmpty()) {
			return result;
		}
		
		return Optional.of(
				repo.save(
						result.get().toBuilder()
							.name(call.getName())
							.notes(call.getNotes())
							.phoneNumber(call.getPhoneNumber())
							.priority(call.getPriority())
							.status(call.getStatus())
							.build()
						)
				);
				
		
	}

}
