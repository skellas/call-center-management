package com.map.call_center_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping
	public ResponseEntity<Agent> addAgent(@RequestBody Agent agent){ 
		Agent persistedAgent = agentRepository.save(agent);
		
		return ResponseEntity.ok(persistedAgent);
	}

}
