package com.map.call_center_management.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

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

}
