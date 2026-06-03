package com.exampleCurso.course.repositories;

import com.exampleCurso.course.entities.Category;
import com.exampleCurso.course.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
