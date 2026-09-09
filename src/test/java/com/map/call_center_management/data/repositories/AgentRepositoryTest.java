package com.map.call_center_management.data.repositories;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.map.call_center_management.data.entities.Agent;

@DataJpaTest
class AgentRepositoryTest {
	@Autowired
	private AgentRepository repo;
	
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

}
