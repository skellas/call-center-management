package com.map.call_center_management.controllers;

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
import com.map.call_center_management.services.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@GetMapping
	public List<Agent> getAgentList() {
		return agentService.getAllAgents();
	}

	@GetMapping(value = "/active")
	public List<Agent> getActiveAgentList() {
		return agentService.getAllActiveAgents();
	}

	@GetMapping(value = "/{agentId}")
	public ResponseEntity<Agent> getAgentById(@PathVariable(name = "agentId") Long agentId) {
		Optional<Agent> serviceResponse = agentService.getAgentById(agentId);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Agent> addAgent(@RequestBody Agent agent){ 
		Optional<Agent> serviceResponse = agentService.save(agent);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}

	@PutMapping(value = "/{agentId}")
	public ResponseEntity<Agent> updateAgent(@PathVariable(name = "agentId") Long agentId, @RequestBody Agent agentInfo) {
		Optional<Agent> serviceRequest = agentService.getAgentById(agentId);
		if (serviceRequest.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Agent> serviceResponse = agentService.save(agentInfo);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}

	@DeleteMapping(value = "/{agentId}")
	public ResponseEntity<Agent> decommissionAgent(@PathVariable(name = "agentId") Long agentId) {
		Optional<Agent> serviceRequest = agentService.getAgentById(agentId);
		if (serviceRequest.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Agent> serviceResponse = agentService.decommission(agentId);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}


}
