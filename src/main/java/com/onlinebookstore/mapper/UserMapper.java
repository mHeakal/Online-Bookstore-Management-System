package com.onlinebookstore.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.onlinebookstore.dto.UserDto;
import com.onlinebookstore.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "createdAt", source = "createdAt")
	UserDto mapToDto(User user);

	List<UserDto> mapToDto(List<User> user);

	@Mapping(target = "createdAt", source = "createdAt")
	User map(UserDto userDto);

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
}
