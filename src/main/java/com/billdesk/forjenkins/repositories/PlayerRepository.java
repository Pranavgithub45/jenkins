package com.billdesk.forjenkins.repositories;

import com.billdesk.forjenkins.models.player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<player, Integer> {
}