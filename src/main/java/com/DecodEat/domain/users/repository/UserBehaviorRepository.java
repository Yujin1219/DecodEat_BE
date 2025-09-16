package com.DecodEat.domain.users.repository;

import com.DecodEat.domain.users.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {
}
