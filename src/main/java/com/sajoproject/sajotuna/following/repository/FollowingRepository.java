package com.sajoproject.sajotuna.following.repository;

import com.sajoproject.sajotuna.following.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Follow, Long> {
}
