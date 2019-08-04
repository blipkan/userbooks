package com.ex.books.model;

import com.ex.books.service.BookService;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
public class Book {
    public static final int DEFAULT_booksStartYear = 1900;

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank

    /// use JSF validation instead
    //@Size(min = 2, max = 255)
    @Column(nullable = false)
    private String title;

    @ToString.Exclude
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    @NotNull
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private User bookUser;


    @ToString.Include(name = "date")
    private String formatDate() {
        return BookService.formatDateForLog(date);
    }

}
