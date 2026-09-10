package com.map.call_center_management.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.map.call_center_management.data.entities.Call;
import com.map.call_center_management.services.CallService;

@RestController
@RequestMapping("/calls")
public class CallController {
	
	@Autowired
	private CallService callService;
	
	@GetMapping
	public List<Call> getCallList() {
		return callService.getAllCalls();
	}

	@GetMapping(value = "/active")
	public List<Call> getActiveCallList() {
		return callService.getAllActiveCalls();
	}
	
	@GetMapping(value = "/{callId}")
	public ResponseEntity<Call> getCallById(@PathVariable(name = "callId") Long callId) {
		Optional<Call> serviceResponse = callService.getCallById(callId);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Call> addCall(@RequestBody Call call){ 
		Optional<Call> serviceResponse = callService.save(call);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}

	@PutMapping(value = "/{callId}")
	public ResponseEntity<Call> updateCall(@PathVariable(name = "callId") Long callId, @RequestBody Call callInfo) {
		Optional<Call> serviceRequest = callService.getCallById(callId);
		if (serviceRequest.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Call> serviceResponse = callService.save(callInfo);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}
	
	@PostMapping(value = "/{callId}/assign/{agentId}")
	public ResponseEntity<Call> assignCallToAgent(@PathVariable(name = "callId") Long callId, @PathVariable(name = "agentId") Long agentId) {
		Optional<Call> serviceRequest = callService.getCallById(callId);
		if (serviceRequest.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Call> serviceResponse = callService.assignCallToAgent(callId, agentId);
		return serviceResponse.isPresent() ? ResponseEntity.ok(serviceResponse.get()) : ResponseEntity.badRequest().build();
	}

}
