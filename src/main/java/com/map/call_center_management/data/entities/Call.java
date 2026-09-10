package com.map.call_center_management.data.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.map.call_center_management.data.enums.CallPriority;
import com.map.call_center_management.data.enums.CallStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Call {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String phoneNumber;
	
	@JsonIgnore
	@Builder.Default
	private Boolean active = Boolean.TRUE;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private CallStatus status = CallStatus.QUEUED;

	@Builder.Default
	@Enumerated(EnumType.ORDINAL)
	private CallPriority priority = CallPriority.LOW;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	@JsonBackReference
	private Agent agent;
	
	private String notes;

	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	
	@PrePersist
	protected void onCreate() {
		this.createdOn = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedOn = LocalDateTime.now();
	}
}
