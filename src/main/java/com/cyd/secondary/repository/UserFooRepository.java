package com.cyd.secondary.repository;

import com.cyd.secondary.domain.UserFoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFooRepository extends JpaRepository<UserFoo, Long> {
}
