package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Subcategories;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.beans.JavaBean;
import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategories, Long> {
    @Query("""
    SELECT s FROM Subcategories s WHERE s.specialization.id = :id
    """)
    List<Subcategories> findBySpecializationId(int id);

    List<Subcategories> findByNameContainingIgnoreCase(String name);
}
