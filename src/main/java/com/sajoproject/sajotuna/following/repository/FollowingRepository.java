package com.sajoproject.sajotuna.following.repository;

import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowingAndFollowed(User following, User followed);
    List<Follow> findByFollowing_UserId(Long id);

    Optional<Follow> findByFollowingAndFollowed(User following, User followed);
}
