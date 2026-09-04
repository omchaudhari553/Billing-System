package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT m FROM Module m WHERE m.isActive = true AND " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Module> searchModules(@Param("keyword") String keyword);
}
