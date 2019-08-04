package com.ex.books.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@Entity (name="User")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"books", "pass"})
@Builder(toBuilder = true)

@Table(name = "usr", uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})})
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    private String pass;

    @Column(nullable = false)
    private String fio;

    @OneToMany(mappedBy = "bookUser", cascade = CascadeType.ALL)
    private List<Book> books;
}
