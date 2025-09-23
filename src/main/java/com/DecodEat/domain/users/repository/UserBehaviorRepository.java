package com.DecodEat.domain.users.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.users.entity.Behavior;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {

    void deleteByUserAndProductAndBehavior(User user, Product product, Behavior behavior);


}
