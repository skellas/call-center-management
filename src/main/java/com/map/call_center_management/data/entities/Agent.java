package com.map.call_center_management.data.entities;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.map.call_center_management.data.enums.AgentAvailability;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Agent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String phoneNumber;
	@Builder.Default
	private Boolean active = Boolean.TRUE;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private AgentAvailability availability = AgentAvailability.OFFLINE;
	
	@Builder.Default
	@OneToMany(mappedBy = "agent", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private List<Call> assignedCalls = new ArrayList<Call>();

}
