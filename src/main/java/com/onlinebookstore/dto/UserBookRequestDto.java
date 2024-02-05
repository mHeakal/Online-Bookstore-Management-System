package com.onlinebookstore.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlinebookstore.entity.UserBookRequestStatus;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBookRequestDto {
	private Long id;
	private UserDto userDto;
	private BookDto bookDto;
	private LocalDateTime requestedAt;
	private LocalDateTime updatedAt;
	private LocalDateTime returnedAt;
	private UserBookRequestStatus status;
}
