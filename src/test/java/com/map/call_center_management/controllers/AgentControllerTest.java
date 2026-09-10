package com.map.call_center_management.controllers;

import static com.map.call_center_management.data.enums.AgentAvailability.BUSY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.services.AgentService;

import tools.jackson.databind.ObjectMapper;


@WebMvcTest(AgentController.class)
class AgentControllerTest {

	@MockitoBean
	private AgentService service;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnAllAgents() throws Exception {
		// Given
		List<Agent> agents = List.of(
				Agent.builder().id(1L).name("John Doe").phoneNumber("123-456-7890").build(),
				Agent.builder().id(2L).name("Jane Doe").phoneNumber("123-456-7890").build()
				);
		when(service.getAllAgents()).thenReturn(agents);

		// When / Then
		mockMvc.perform(get("/agents"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L));

	}

	@Test
	void shouldReturnAllActiveAgents() throws Exception {
		// Given
		List<Agent> agents = List.of(
				Agent.builder().id(1L).name("John Doe").phoneNumber("123-456-7890").build(),
				Agent.builder().id(2L).name("Jane Doe").phoneNumber("123-456-7890").build()
				);
		when(service.getAllAgents()).thenReturn(null);
		when(service.getAllActiveAgents()).thenReturn(agents);

		// When / Then
		mockMvc.perform(get("/agents/active"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L));

	}

	@Test
	void shouldReturnAgentById() throws Exception {
		// Given
		Agent agent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").id(1L).build();
		
		when(service.getAgentById(agent.getId())).thenReturn(Optional.of(agent));

		// When / Then
		mockMvc.perform(
					get(String.format("/agents/%d", agent.getId()))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(agent.getId()));

	}

	@Test
	void shouldCreateAgent() throws Exception {
		// Given
		Agent postedAgent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").build();
		Agent persistedAgent = postedAgent.toBuilder().id(1L).build();
		
		when(service.save(postedAgent)).thenReturn(Optional.of(persistedAgent));

		// When / Then
		mockMvc.perform(
					post("/agents")
					.content(asJsonString(postedAgent))
					.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(persistedAgent.getId()));

	}
	

	@Test
	void shouldSaveAgent() throws Exception {
		// Given
		Agent persistedAgent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").id(1L).build();
		Agent updatedAgent = persistedAgent.toBuilder().availability(BUSY).build();
		
		when(service.getAgentById(persistedAgent.getId())).thenReturn(Optional.of(persistedAgent));
		when(service.save(any(Agent.class)))
			.thenAnswer(invocation -> {
				return Optional.of(invocation.getArgument(0));
			});

		// When / Then
		mockMvc.perform(
					put(String.format("/agents/%d", persistedAgent.getId()))
					.content(asJsonString(updatedAgent))
					.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(persistedAgent.getId()))
				.andExpect(jsonPath("availability").value("BUSY"));

	}
	
 
	@Test
	void shouldDecommissionAgent() throws Exception {
		// Given
		Agent agent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").id(1L).build();

		when(service.getAgentById(agent.getId())).thenReturn(Optional.of(agent));
		when(service.decommission(agent.getId())).thenReturn(Optional.of(agent));

		// When / Then
		mockMvc.perform(
					delete(String.format("/agents/%d", agent.getId()))
				)
				.andExpect(status().isOk());
		
		verify(service).decommission(agent.getId());

	}

	@Test
	void shouldSafelyHandleBadIdOnLookup() throws Exception {
		// Given
		
		when(service.getAgentById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					get(String.format("/agents/%d", 1l))
				)
				.andExpect(status().isNotFound());

	}

	@Test
	void shouldSafelyHandleBadIdOnUpdate() throws Exception {
		// Given
		Agent persistedAgent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").id(1L).build();
		Agent updatedAgent = persistedAgent.toBuilder().availability(BUSY).build();
		
		when(service.getAgentById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					put(String.format("/agents/%d", persistedAgent.getId()))
						.content(asJsonString(updatedAgent))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isNotFound());

	}

	@Test
	void shouldSafelyHandleBadIdOnDecom() throws Exception {
		// Given
		
		when(service.getAgentById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					delete(String.format("/agents/%d", 1l))
				)
				.andExpect(status().isNotFound());

	}
	
	private static String asJsonString(final Object obj) {

		  try {
		      return new ObjectMapper().writeValueAsString(obj);
		  } catch (Exception e) {
		      throw new RuntimeException(e);
		  }
		}

}
