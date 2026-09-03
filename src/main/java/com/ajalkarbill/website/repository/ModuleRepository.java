package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByIsActiveTrueOrderByDisplayOrderAsc();
}
