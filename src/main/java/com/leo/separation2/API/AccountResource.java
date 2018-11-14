package com.leo.separation2.API;

import com.leo.separation2.entity.Account;
import com.leo.separation2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Leo_lei on 2018/11/13
 */
@RestController
@RequestMapping(value = "/api/account")
public class AccountResource {

    @Autowired
    private AccountService service;

    @PostMapping("/insert")
    public Account insert(@RequestBody Account account){
        return  service.insert(account);
    }


    @PostMapping("/findAll")
    public List<Account> findAll(){
        return  service.findAll();
    }
}
