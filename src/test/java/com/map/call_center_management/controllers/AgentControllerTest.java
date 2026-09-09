package com.map.call_center_management.controllers;

import static com.map.call_center_management.data.enums.AgentAvailability.BUSY;
import static java.lang.Boolean.FALSE;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
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
import com.map.call_center_management.data.repositories.AgentRepository;

import tools.jackson.databind.ObjectMapper;


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

	@Test
	void shouldReturnAgentById() throws Exception {
		// Given
		Agent agent = Agent.builder().name("Jane Doe").phoneNumber("123-456-7890").id(1L).build();
		
		when(repo.findById(agent.getId())).thenReturn(Optional.of(agent));

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
		
		when(repo.save(postedAgent)).thenReturn(persistedAgent);

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
		
		when(repo.findById(persistedAgent.getId())).thenReturn(Optional.of(persistedAgent));
		when(repo.save(any(Agent.class))).thenAnswer(returnsFirstArg());

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

		when(repo.findById(agent.getId())).thenReturn(Optional.of(agent));
		when(repo.save(any(Agent.class))).thenAnswer(returnsFirstArg());

		// When / Then
		mockMvc.perform(
					delete(String.format("/agents/%d", agent.getId()))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(agent.getId()))
				.andExpect(jsonPath("active").value(FALSE));

	}

	@Test
	void shouldSafelyHandleBadIdOnLookup() throws Exception {
		// Given
		
		when(repo.findById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					get(String.format("/agents/%d", 1l))
				)
				.andExpect(status().isBadRequest());

	}

	@Test
	void shouldSafelyHandleBadIdOnUpdate() throws Exception {
		// Given
		
		when(repo.findById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					put(String.format("/agents/%d", 1l))
				)
				.andExpect(status().isBadRequest());

	}

	@Test
	void shouldSafelyHandleBadIdOnDecom() throws Exception {
		// Given
		
		when(repo.findById(any())).thenReturn(Optional.empty());

		// When / Then
		mockMvc.perform(
					delete(String.format("/agents/%d", 1l))
				)
				.andExpect(status().isBadRequest());

	}
	
	private static String asJsonString(final Object obj) {

		  try {
		      return new ObjectMapper().writeValueAsString(obj);
		  } catch (Exception e) {
		      throw new RuntimeException(e);
		  }
		}

}
