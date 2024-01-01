package com.example.user.service;

import com.example.user.domain.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(User user);

    User update(User user, Long id);

    User getById(Long id);

    void deleteById(Long id);

    User getCurrentUser();

    Page<User> getPage(Pageable pageable);

}
