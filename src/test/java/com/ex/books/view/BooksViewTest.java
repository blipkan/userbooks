package com.ex.books.view;

import com.ex.books.model.User;
import com.ex.books.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j

public class BooksViewTest {
    @Autowired
    UserRepo userRepo;

    @Autowired
    BooksView booksView;


    @Test
    public void completeText() {
        userRepo.deleteAll();
        String[] fios = new String[]{"ф", "ф ф", "фы"};
        userRepo.save(generateUsers(fios));

        checkCompleteResultsLength("sss", 0);
        checkCompleteResultsLength("ф", 3);
        checkCompleteResultsLength("ф ф", 1);

    }

    private List<User> generateUsers(String[] fios) {
        List<User> list = new ArrayList<>(fios.length);
        for (int i = 0; i < fios.length; i++) {
            list.add(User.builder().login(String.valueOf(i)).fio(fios[i]).build());
        }
        return list;
    }

    private void checkCompleteResultsLength(String input, int expectedLength){
        List<String> results = booksView.completeText(input);
        assertEquals(expectedLength, results.size());
    }

}