package com.exampleCurso.course.repositories;

import com.exampleCurso.course.entities.Category;
import com.exampleCurso.course.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
