package com.ex.books.repository;

import com.ex.books.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User findFirstBy();

    @Query("SELECT fio FROM User where fio like %:input%")
    public List<String> searchFios(@Param("input") String input);
}