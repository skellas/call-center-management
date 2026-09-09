package com.map.call_center_management.services;

import static com.map.call_center_management.data.enums.AgentAvailability.OFFLINE;
import static java.lang.Boolean.FALSE;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.repositories.AgentRepository;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {
	
	@Mock
	private AgentRepository repo;
	
	@InjectMocks
	private AgentService service;

	@Test
	void shouldInvokeFindAll() {
		service.getAllAgents();
		
		verify(repo).findAll();
	}

	@Test
	void shouldInvokeFindByActive() {
		service.getAllActiveAgents();
		
		verify(repo).findByActiveTrue();
	}

	@Test
	void shouldInvokeFindById() {
		Long identifier = 1l;
		service.getAgentById(identifier);
		
		verify(repo).findById(identifier);
	}
	
	@Test
	void shouldPersistNewAgent() {
		Agent agent = Agent.builder().name("John Doe").phoneNumber("123-456-7890").build();
		when(repo.save(agent)).thenAnswer(returnsFirstArg());
		
		service.save(agent);
		
		verify(repo).save(agent);
		verify(repo, never()).findById(any());
	}

	@Test
	void shouldUpdateExistingAgent() {
		Agent agent = Agent.builder().name("John Doe").phoneNumber("123-456-7890").id(1L).build();
		Agent updatedAgent = agent.toBuilder().name("Jane Doe").build();
		
		when(repo.findById(agent.getId())).thenReturn(Optional.of(agent));
		when(repo.save(any())).thenAnswer(returnsFirstArg());
		
		service.save(updatedAgent);

		verify(repo).findById(agent.getId());
		verify(repo).save(updatedAgent);
	}

	@Test
	void shouldDecommissionExistingAgent() {
		Agent agent = Agent.builder().name("John Doe").phoneNumber("123-456-7890").id(1L).build();
		Agent updatedAgent = agent.toBuilder().active(FALSE).availability(OFFLINE).build();
		
		when(repo.findById(agent.getId())).thenReturn(Optional.of(agent));
		when(repo.save(any())).thenAnswer(returnsFirstArg());
		
		service.decommission(agent.getId());

		verify(repo).findById(agent.getId());
		verify(repo).save(updatedAgent);
	}

}
