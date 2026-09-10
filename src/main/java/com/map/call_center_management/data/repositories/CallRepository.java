package com.map.call_center_management.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.map.call_center_management.data.entities.Call;

public interface CallRepository extends JpaRepository<Call, Long> {
	
	public List<Call> findByActiveTrueOrderByPriorityDescCreatedOnDesc();

}
