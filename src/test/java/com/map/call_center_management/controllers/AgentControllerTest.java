package com.map.call_center_management.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.repositories.AgentRepository;


@WebMvcTest(AgentController.class)
class AgentControllerTest {
	
	@MockitoBean
	private AgentRepository repo;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnAllAgents() throws Exception {
		// Given
		List<Agent> agents = List.of(
				Agent.builder().id(1L).name("John Doe").phoneNumber("123-456-7890").build(),
				Agent.builder().id(2L).name("Jane Doe").phoneNumber("123-456-7890").build()
				);
		when(repo.findAll()).thenReturn(agents);

		// When / Then
		mockMvc.perform(get("/agents"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L));

	}

}
