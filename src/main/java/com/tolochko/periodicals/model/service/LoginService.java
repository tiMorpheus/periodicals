package com.tolochko.periodicals.model.service;

import com.tolochko.periodicals.model.domain.user.User;

public interface LoginService {
    User getUserByEmailAndPassword(String email, String password);
}
