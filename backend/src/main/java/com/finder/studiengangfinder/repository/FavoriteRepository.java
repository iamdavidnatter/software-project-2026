package com.finder.studiengangfinder.repository;

import com.finder.studiengangfinder.entity.Favorite;
import com.finder.studiengangfinder.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findBySession(UserSession session);
    void deleteBySession(UserSession session);
}
