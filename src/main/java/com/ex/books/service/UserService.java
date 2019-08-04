package com.ex.books.service;

import com.ex.books.model.User;
import com.ex.books.repository.UserRepo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service

public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Setter(onMethod = @__({@Autowired}))
    DummyDataGenerator dataGenerator;

    @PostConstruct
    public void init() {
        initDummyDbData();
    }

    private void initDummyDbData() {
        if (!dataGenerator.isEnabled()) {
            log.info("skip dummy data generating");
            return;
        }
        log.info("generate random data");
        List<User> list = dataGenerator.all();

        log.info("filling database by {} users...", list.size());
        userRepo.save(list);
    }

}
