package com.leo.separation2.service;

import com.leo.separation2.entity.Account;

import java.util.List;

/**
 * Created by Leo_lei on 2018/11/13
 */
public interface AccountService {

    Account insert(Account account);

    List<Account> findAll();
}
