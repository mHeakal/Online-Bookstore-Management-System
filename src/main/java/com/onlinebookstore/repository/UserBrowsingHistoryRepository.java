package com.onlinebookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.UserBrowsingHistory;

@Repository
public interface UserBrowsingHistoryRepository extends JpaRepository<UserBrowsingHistory, Long> {
	Optional<UserBrowsingHistory> findByReferredUser_IdAndBook_Id(Long userId, Long bookId);
}
