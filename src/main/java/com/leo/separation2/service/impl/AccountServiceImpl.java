package com.leo.separation2.service.impl;

import com.leo.separation2.config.ReadDataSource;
import com.leo.separation2.config.WriteDataSource;
import com.leo.separation2.dao.AccountRepository;
import com.leo.separation2.entity.Account;
import com.leo.separation2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Leo_lei on 2018/11/13
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountDao;

    @Override
    @WriteDataSource
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.DEFAULT,readOnly=false)
    public Account insert(Account account) {
        Account res = accountDao.save(account);
        return res;
    }

    @Override
    @ReadDataSource
    public List<Account> findAll() {
        List<Account> res = accountDao.findAll();
        return res;
    }
}
