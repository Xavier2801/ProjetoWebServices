package com.exampleCurso.course.repositories;

import com.exampleCurso.course.entities.Order;
import com.exampleCurso.course.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
