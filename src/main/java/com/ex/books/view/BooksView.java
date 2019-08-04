package com.ex.books.view;

import com.ex.books.model.Book;
import com.ex.books.repository.BookRepo;
import com.ex.books.repository.UserRepo;
import com.ex.books.service.BookService;
import com.ex.books.utils.Msg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.CellEditEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@Component
@Scope("session")
@RequiredArgsConstructor
public class BooksView {

    private final UserRepo userRepo;
    @Getter
    private final BookRepo bookRepo;
    private final BookService bookService;
    private final Environment environment;

    @Getter
    @Setter
    private String fio;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    private LazyBooksDataModel model;

    @Getter
    @Setter
    private Book selectedItem;


    @PostConstruct
    public void init() {
        Calendar c1 = Calendar.getInstance();
        endDate = c1.getTime();
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, bookService.getBooksStartYear());
        c2.set(Calendar.DAY_OF_YEAR, 1);
        startDate = c2.getTime();

        model = new LazyBooksDataModel(this);

        /// для профиля dev сразу устанавливаем выборку Book для первого User
        if (ArrayUtils.contains(environment.getActiveProfiles(), "dev")) {
            fio = userRepo.findFirstBy().getFio();
            doSearch();
            log.debug("init 'dev' profile by fio of first user in DB: '{}'", fio);
        }

    }

    public List<String> completeText(String query) {
        return userRepo.searchFios(query);
    }

    public void doSearch() {
        log.debug("searching books for fio: '{}'", fio);
        model.newSearch = true;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            final String columnKey = event.getColumn().getColumnKey();
            Book book = model.getRowData(event.getRowKey());
            final String fieldName = columnKey.substring(1 + columnKey.lastIndexOf(":"));
            log.debug("onCellEdit. fieldName:{}, oldValue:{}, newValue:{} ", fieldName, oldValue, newValue);
            try {
                if ("title".equals(fieldName)) {
                    book.setTitle(String.valueOf(newValue));
                    saveBook(book, fieldName, newValue);
                } else if ("date".equals(fieldName)) {
                    book.setDate((Date) newValue);
                    saveBook(book, fieldName, newValue);
                }
            } catch (Exception e) {
                log.error("error updating book {} {}", book, e.getMessage());
                Msg.add(FacesMessage.SEVERITY_ERROR,
                        "msg.edit.err", "msg.edit.err.detail",
                        null, new Object[]{newValue});
            }
        }
    }

    private void saveBook(Book book, String fieldName, Object newValue) {
        log.debug("updating {}:'{}' for book {}", fieldName, newValue, book);
        bookRepo.save(book);
        Msg.add(FacesMessage.SEVERITY_INFO,
                "msg.edit.ok", "msg.edit.ok.detail",
                null, new Object[]{newValue});

    }

    public boolean isRendered() {
        return fio != null;
    }

    public String getTableHeaderText() {
        if (StringUtils.isBlank(fio)) {
            return Msg.text("msg.search.report.booksForAll");
        } else {
            return Msg.format("msg.search.report.booksFor", new Object[]{fio});
        }
    }

}
