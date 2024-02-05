package com.onlinebookstore.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.onlinebookstore.dto.BookBriefDto;
import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

	BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

	@Mapping(target = "createdAt", source = "createdAt")
	BookDto mapToDto(Book book);

	List<BookDto> mapToDto(List<Book> book);

	@Mapping(target = "createdAt", source = "createdAt")
	Book map(BookDto bookDto);

	default LocalDateTime mapCreatedAtToCreatedAt(final Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return timestamp.toLocalDateTime();
	}

	default Timestamp mapCreatedAtToCreatedAt(final LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return Timestamp.valueOf(localDateTime);
	}

	List<BookBriefDto> mapToBriefDto(List<Book> books);
}
