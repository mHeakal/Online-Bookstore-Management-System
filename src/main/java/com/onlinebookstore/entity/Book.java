package com.onlinebookstore.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String authorName;
	@NotNull
	private BigDecimal price;
	@NotNull
	private Integer inStock;
	@Column(nullable = false, columnDefinition = "int default 0")
	@Builder.Default()
	private Integer borrowedCopiesCount = 0;
	@Column(nullable = false, columnDefinition = "int default 1")
	@Builder.Default()
	private Integer stockLevel = 1;
	@CreatedDate
	private Timestamp createdAt;
	@LastModifiedDate
	private Timestamp updatedAt;
	@NotNull
	private String category;
	private Integer numberOfDaysForBorrow;

}
