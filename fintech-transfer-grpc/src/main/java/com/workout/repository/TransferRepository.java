package com.workout.repository;

import com.workout.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>,
    CrudRepository<Transfer, Long>, QuerydslPredicateExecutor<Transfer> {

    List<Transfer> findAll();


}
