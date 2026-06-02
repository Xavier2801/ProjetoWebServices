package com.exampleCurso.course.repositories;

import com.exampleCurso.course.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {




}
