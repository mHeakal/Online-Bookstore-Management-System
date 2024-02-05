package com.onlinebookstore.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
	private Long id;
	private String name;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
