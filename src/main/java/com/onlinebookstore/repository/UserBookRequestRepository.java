package com.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.UserBookRequest;
import com.onlinebookstore.entity.UserBookRequestStatus;

@Repository
public interface UserBookRequestRepository extends JpaRepository<UserBookRequest, Long> {
	Optional<UserBookRequest> findByBook_IdAndReferredUser_Id(Long bookId, Long userId);

	List<UserBookRequest> findByBook_Id(Long id);

	Optional<UserBookRequest> findByBook_IdAndReferredUser_IdAndStatus(Long bookId, Long userId,
			UserBookRequestStatus status);
}
