package com.map.call_center_management.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.map.call_center_management.data.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {

	public List<Agent> findByActiveTrue(); 
}
