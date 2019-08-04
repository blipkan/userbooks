package com.ex.books.view;


import com.ex.books.model.Book;
import com.ex.books.service.BookService;
import com.ex.books.utils.Msg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.faces.application.FacesMessage;
import java.util.List;
import java.util.Map;

@Slf4j
class LazyBooksDataModel extends LazyDataModel<Book> {

    private final BooksView booksView;

    private List<Book> list;

    boolean newSearch;

    LazyBooksDataModel(BooksView booksView) {

        this.booksView = booksView;

        this.setRowCount((int) booksView.getBookRepo().count());
    }

    @Override
    public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        final Sort sort = new Sort((sortOrder == SortOrder.DESCENDING) ? Sort.Direction.DESC : Sort.Direction.ASC, StringUtils.isBlank(sortField) ? "id" : sortField);
        final PageRequest pageRequest = new PageRequest(first / pageSize, pageSize, sort);

        final Page<Book> page = booksView.getBookRepo().findBooks(StringUtils.trim(booksView.getFio()), booksView.getStartDate(), booksView.getEndDate(), pageRequest);
        final int total = (int) page.getTotalElements();
        log.debug("{} results found for '{}', {} - {}, {}",
                total,
                StringUtils.trim(booksView.getFio()),
                BookService.formatDateForLog(booksView.getStartDate()),
                BookService.formatDateForLog(booksView.getEndDate()),
                pageRequest
        );

        list = page.getContent();
        this.setRowCount(total);

        /// новый поиск
        if (isNewSearch()) {
            log.debug("new search: {}", booksView.getFio());
            newSearch = false;

            Msg.add(FacesMessage.SEVERITY_INFO,
                    "msg.search.report.foundBooks", "msg.search.report.for",
                    new Object[]{total},

                    new Object[]{(StringUtils.isBlank(booksView.getFio())) ? Msg.text("w.all") : booksView.getFio(),
                    booksView.getStartDate(), booksView.getEndDate()

                    });
        }

        return list;
    }

    @Override
    public Book getRowData(String rowKey) {
        return list.stream().
                filter(o -> o.getId().equals(Long.valueOf(rowKey))).
                findFirst().orElse(null);
    }

    @Override
    public Object getRowKey(Book item) {
        return item.getId();
    }

    private boolean isNewSearch() {
        return newSearch;
    }


}
