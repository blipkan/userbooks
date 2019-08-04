package com.ex.books.repository;

import com.ex.books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("select b from Book b, User u  where b.date BETWEEN :startDate and :endDate and b.bookUser=u.id and u.fio like %:input%")
    public Page<Book> findBooks(@Param("input") String input, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

}
