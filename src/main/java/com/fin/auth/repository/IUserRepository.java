package com.fin.auth.repository;

import com.fin.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long> {

    public User findByUsername(String userName);

}
