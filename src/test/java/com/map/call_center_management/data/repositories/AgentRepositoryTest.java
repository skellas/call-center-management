package com.map.call_center_management.data.repositories;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.entities.Call;

@DataJpaTest
class AgentRepositoryTest {
	@Autowired
	private AgentRepository repo;
	
	@Autowired
	private CallRepository callRepo;
	
	@AfterEach
	public void cleanUp() throws Exception {
		repo.deleteAll();
	}

	@Test
	void shouldStartWithNoEntities() {
		assertEquals(0, repo.count(), "Should start with zero entities");
	}
	
	@Test
	void shouldPersistEntity() {
		Agent dummyAgent = repo.save(Agent.builder().name("Dummy Agent").phoneNumber("123-456-7890").build());
		
		assertNotNull(dummyAgent.getId(), "ID value is available only after persistence");
	}
	
	@Test
	void shouldReturnEntityById() {
		Agent dummyAgent = repo.save(Agent.builder().name("Dummy Agent").phoneNumber("123-456-7890").build());
		
		assertEquals(dummyAgent, repo.findById(dummyAgent.getId()).get(), "Repo should return proper entity");
	}
	
	@Test
	void shouldOnlyReturnActiveAgents() {
		Agent activeAgentOne = repo.save(Agent.builder().name("Active Agent One").phoneNumber("123-456-7890").active(TRUE).build());
		Agent activeAgentTwo = repo.save(Agent.builder().name("Active Agent Two").phoneNumber("123-456-7890").active(TRUE).build());
		Agent inActiveAgentOne = repo.save(Agent.builder().name("InActive Agent One").phoneNumber("123-456-7890").active(FALSE).build());
		Agent activeAgentThree = repo.save(Agent.builder().name("Active Agent Three").phoneNumber("123-456-7890").active(TRUE).build());
		Agent inActiveAgentTwo = repo.save(Agent.builder().name("InActive Agent Two").phoneNumber("123-456-7890").active(FALSE).build());
		
		List<Agent> results = repo.findByActiveTrue();
		assertTrue(results.containsAll(List.of(activeAgentOne, activeAgentTwo, activeAgentThree)), "Should only contain active agents in results");
		assertFalse(results.containsAll(List.of(inActiveAgentOne, inActiveAgentTwo)), "Should not contain any inactive agents in results");
		
	}
	
	@Test
	void shouldReturnCallsWithAgentLookup() {
		Agent agent = repo.save(Agent.builder().name("Agent One").phoneNumber("123-456-7890").active(TRUE).build());

		List<Call> assignedCalls = List.of(
				callRepo.save(Call.builder().name("Call One").phoneNumber("xxx-xxx-xxxx").agent(agent).build()),
				callRepo.save(Call.builder().name("Call Two").phoneNumber("xxx-xxx-xxxx").agent(agent).build())
				);

		Agent retrievedAgent = repo.save(agent.toBuilder().assignedCalls(assignedCalls).build());
		assertTrue(retrievedAgent.getAssignedCalls().containsAll(assignedCalls), "Should contain both assigned tasks");
	}
	
}
