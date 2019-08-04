package com.ex.books.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class BookService {
    private static final SimpleDateFormat logDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Value("${app.dummy.booksStartYear: #{T(com.ex.books.model.Book).DEFAULT_booksStartYear}}")
    int booksStartYear;

    public static String formatDateForLog(Date date) {
        return logDateFormat.format(date);
    }

    public int getBooksStartYear() {
        return booksStartYear;
    }
}
