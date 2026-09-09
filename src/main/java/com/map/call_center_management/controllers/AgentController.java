package com.map.call_center_management.controllers;

import static com.map.call_center_management.data.enums.AgentAvailability.OFFLINE;
import static java.lang.Boolean.FALSE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.map.call_center_management.data.entities.Agent;
import com.map.call_center_management.data.repositories.AgentRepository;

@RestController
@RequestMapping("/agents")
public class AgentController {

	@Autowired
	private AgentRepository agentRepository;

	@GetMapping
	public List<Agent> getAgentList() {
		return agentRepository.findAll();
	}

	@GetMapping(value = "/active")
	public List<Agent> getActiveAgentList() {
		return agentRepository.findByActiveTrue();
	}

	@GetMapping(value = "{agentId}")
	public ResponseEntity<Agent> getAgentById(@PathVariable(name = "agentId") Long agentId) {
		Optional<Agent> queryResult = agentRepository.findById(agentId);
		return queryResult.isPresent() ? ResponseEntity.ok(queryResult.get()) : ResponseEntity.badRequest().build();
	}

	@PostMapping
	public ResponseEntity<Agent> addAgent(@RequestBody Agent agent){ 
		return ResponseEntity.ok(agentRepository.save(agent));
	}

	@PutMapping(value = "/{agentId}")
	public ResponseEntity<Agent> updateAgent(@PathVariable(name = "agentId") Long agentId, @RequestBody Agent agentInfo) {
		Optional<Agent> queryResult = agentRepository.findById(agentId);
		if(queryResult.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(
				agentRepository.save(
						queryResult.get().toBuilder()
						.name(agentInfo.getName())
						.phoneNumber(agentInfo.getPhoneNumber())
						.availability(agentInfo.getAvailability())
						.build()
						)
				);
	}

	@DeleteMapping(value = "/{agentId}")
	public ResponseEntity<Agent> decommissionAgent(@PathVariable(name = "agentId") Long agentId) {
		Optional<Agent> queryResult = agentRepository.findById(agentId);
		if(queryResult.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(
				agentRepository.save(
						queryResult.get().toBuilder()
						.active(FALSE)
						.availability(OFFLINE)
						.build()
						)
				);
	}


}
