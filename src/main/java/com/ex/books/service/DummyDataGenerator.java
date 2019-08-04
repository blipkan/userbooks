package com.ex.books.service;


import com.ex.books.model.Book;
import com.ex.books.model.User;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@ConfigurationProperties(prefix = "app.dummy")
@Setter
public class DummyDataGenerator {

    @Getter
    private boolean enabled;
    private int totalUsers = 10;
    private int minBooksPerUser = 1;
    private int maxBooksPerUser = 10;
    private int booksStartYear = Book.DEFAULT_booksStartYear;

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public List<User> all() {

        Faker faker = new Faker(new Locale("ru"));
        List<User> list = new ArrayList<>();

        log.info("generating {} users: minBooksPerUser={}, maxBooksPerUser={}, booksStartYear={}", totalUsers, minBooksPerUser, maxBooksPerUser, booksStartYear);
        int totalBooks = 0;
        for (int i = 0; i < totalUsers; i++) {

            final String fio = faker.name().fullName();
            final String login = faker.internet().emailAddress("nik_" + (i + 1));
            final String pass = "222";
            User user = User.builder()
                    .login(login)
                    .fio(fio)
                    .pass(pass)
                    .build();
            List<Book> books = booksForUser(faker, user, i);
            totalBooks += books.size();
            user.setBooks(books);

            list.add(user);
        }
        log.info("{} books has been created for {} users.", totalBooks, list.size());
        return list;
    }

    private List<Book> booksForUser(Faker faker, User user, int userInd) {

        final int booksCount = random.nextInt(minBooksPerUser, maxBooksPerUser);
        final List<Book> list = new ArrayList<>(booksCount);

        Calendar cal = Calendar.getInstance();
        final long maxMilis = cal.getTimeInMillis();
        cal.set(booksStartYear, Calendar.JANUARY, 0);
        final long minMilis = cal.getTimeInMillis();

        for (int i = 0; i < booksCount; i++) {
            String title;
            int num = random.nextInt(5);

            if (num == 0) {
                title = faker.artist().name();
            } else if (num == 1) {
                title = faker.book().title();
            } else if (num == 2) {
                title = faker.commerce().productName();
            } else if (num == 3) {
                title = faker.commerce().material();
            } else if (num == 4) {
                title = faker.commerce().productName();
            } else if (num == 5) {
                title = faker.artist().name();
            } else {
                title = faker.commerce().productName();
            }

            Date date = new Date(random.nextLong(minMilis, maxMilis));
            list.add(Book.builder()
                    .title(title + "      (" + userInd + ")")
                    .date(date)
                    .bookUser(user)
                    .build());
        }

        return list;
    }


}
