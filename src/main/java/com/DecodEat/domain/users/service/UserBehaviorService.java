package com.DecodEat.domain.users.service;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.users.entity.Behavior;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.entity.UserBehavior;
import com.DecodEat.domain.users.repository.UserBehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBehaviorService {
    private final UserBehaviorRepository userBehaviorRepository;
    @Transactional
    public void saveUserBehavior(User user, Product product, Behavior behavior) {
        UserBehavior userBehavior = UserBehavior.builder()
                .user(user)
                .product(product)
                .behavior(behavior)
                .build();
        userBehaviorRepository.save(userBehavior);
    }
}
