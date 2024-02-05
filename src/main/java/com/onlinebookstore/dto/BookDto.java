package com.onlinebookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class BookDto {
	private Long id;
	private String name;
	private String authorName;
	@NotNull
	private BigDecimal price;
	@NotNull
	private Integer inStock;
	@JsonIgnore
	private LocalDateTime createdAt;
	@JsonIgnore
	private LocalDateTime updatedAt;
	@NotNull
	private String category;
	@NotNull
	private Integer numberOfDaysForBorrow;
	@Builder.Default()
	private Integer stockLevel = 1;
	@Builder.Default()
	private Integer borrowedCopiesCount = 0;
}
