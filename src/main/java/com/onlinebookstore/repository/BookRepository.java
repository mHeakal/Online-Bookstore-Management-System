package com.onlinebookstore.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	boolean existsByNameAndIdNotIn(String name, Collection<Long> ids);

	Optional<Book> findByName(String name);

	List<Book> findByNameContainsAndCategory(String name, String category);

	Optional<Book> findByNameLikeIgnoreCase(String name);

	List<Book> findByNameContains(String name);

	List<Book> findByCategory(String category);

	Optional<Book> findByNameAndCategory(String name, String category);

	boolean existsByName(String name);

	@Query(nativeQuery = true, value = "select b.* from books b inner join USER_BROWSING_HISTORY ub on ub.book_id = b.id"
			+ " and ub.user_id = ?1 \n" + " where ub.browsing_history > 0 order by ub.browsing_history desc ")
	List<Book> getSuggestedBooks(Long userId);
}
