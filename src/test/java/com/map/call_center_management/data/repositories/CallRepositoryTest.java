package com.map.call_center_management.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.map.call_center_management.data.entities.Call;

@DataJpaTest
class CallRepositoryTest {

	@Autowired
	private CallRepository repo;
	
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
		Call dummyCall = repo.save(Call.builder().name("Dummy Call").phoneNumber("123-456-7890").build());
		
		assertNotNull(dummyCall.getId(), "ID value is available only after persistence");
	}
	
	@Test
	void shouldReturnEntityById() {
		Call dummyCall = repo.save(Call.builder().name("Dummy Call").phoneNumber("123-456-7890").build());
		
		assertEquals(dummyCall, repo.findById(dummyCall.getId()).get(), "Repo should return proper entity");
	}

	@Test
	void shouldSetCreatedDateOnPersist() {
		Call dummyCall = repo.save(Call.builder().name("Dummy Call").phoneNumber("123-456-7890").build());
		
		assertNotNull(dummyCall.getCreatedOn(), "Should set the created on date upon persistence");
	}

	@Test
	void shouldNotSetUpdatedDateOnPersist() {
		Call dummyCall = repo.save(Call.builder().name("Dummy Call").phoneNumber("123-456-7890").build());
		
		assertNull(dummyCall.getUpdatedOn(), "Should not set the updated on date upon persistence, only update");
	}

	@Test
	void shouldSetUpdatedDateOnUpdate() {
		Call dummyCall = repo.save(Call.builder().name("Dummy Call").phoneNumber("123-456-7890").build());
		repo.saveAndFlush(dummyCall.toBuilder().notes("Dummy Notes").build());
		
		assertNotNull(repo.findById(dummyCall.getId()).get().getUpdatedOn(), "Should set the updated on date upon update");
	}
}
