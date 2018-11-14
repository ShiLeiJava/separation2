package com.leo.separation2.dao;

import com.leo.separation2.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Leo_lei on 2018/11/13
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
}
