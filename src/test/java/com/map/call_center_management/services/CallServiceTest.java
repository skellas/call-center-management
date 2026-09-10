package com.map.call_center_management.services;

import static com.map.call_center_management.data.enums.CallStatus.IN_PROGRESS;
import static com.map.call_center_management.data.enums.CallStatus.QUEUED;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.map.call_center_management.data.entities.Call;
import com.map.call_center_management.data.repositories.AgentRepository;
import com.map.call_center_management.data.repositories.CallRepository;

@ExtendWith(MockitoExtension.class)
class CallServiceTest {
	
	@Mock
	private CallRepository repo;
	@Mock
	private AgentRepository agentRepo;
	
	@InjectMocks
	private CallService service;

	@Test
	void shouldInvokeFindAll() {
		service.getAllCalls();
		
		verify(repo).findAll();
	}

	@Test
	void shouldInvokeFindByActive() {
		service.getAllActiveCalls();
		
		verify(repo).findByActiveTrueOrderByPriorityDescCreatedOnDesc();
	}


	@Test
	void shouldInvokeFindById() {
		Long identifier = 1l;
		service.getCallById(identifier);
		
		verify(repo).findById(identifier);
	}
	
	@Test
	void shouldPersistNewCall() {
		Call call = Call.builder().name("John Doe").phoneNumber("123-456-7890").status(QUEUED).build();
		when(repo.save(call)).thenAnswer(returnsFirstArg());
		
		service.save(call);
		
		verify(repo).save(call);
		verify(repo, never()).findById(any());
	}

	@Test
	void shouldUpdateExistingCall() {
		Call call = Call.builder().name("John Doe").phoneNumber("123-456-7890").status(QUEUED).id(1L).build();
		Call updatedCall = call.toBuilder().status(IN_PROGRESS).build();
		
		when(repo.findById(call.getId())).thenReturn(Optional.of(call));
		when(repo.save(any())).thenAnswer(returnsFirstArg());
		
		service.save(updatedCall);

		verify(repo).findById(call.getId());
		verify(repo).save(updatedCall);
	}

	@Test
	void shouldAssignAgent() {
		Call call = Call.builder().name("John Doe").phoneNumber("123-456-7890").status(QUEUED).id(1L).build();
		Agent agent = Agent.builder().name("John Doe").phoneNumber("123-456-7890").id(1L).build();

		when(repo.findById(call.getId())).thenReturn(Optional.of(call));
		when(agentRepo.findById(agent.getId())).thenReturn(Optional.of(agent));
		when(repo.save(any())).thenAnswer(returnsFirstArg());
		
		Optional<Call> result = service.assignCallToAgent(call.getId(), agent.getId());
		
		assertEquals(result.get().getAgent(), agent, "Should have assigned the agent before persisting");
	}

}
