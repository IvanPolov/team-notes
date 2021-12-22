package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByEmail(String email);

    User findByMyBoards_Id(UUID boardId);

    List<User> findAllByBoards_Id(UUID boardId);
}
