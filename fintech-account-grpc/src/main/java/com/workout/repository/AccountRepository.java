package com.workout.repository;

import com.workout.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>,
    CrudRepository<Account, Long>, QuerydslPredicateExecutor<Account> {

    List<Account> findAll();

    Optional<Account> findByCode(String code);
}
