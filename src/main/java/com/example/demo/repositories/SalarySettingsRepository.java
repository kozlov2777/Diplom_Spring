package com.example.demo.repositories;

import com.example.demo.models.SalarySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SalarySettingsRepository extends JpaRepository<SalarySettings, Long> {
    @Query("SELECT ss FROM SalarySettings ss WHERE ss.role.id = :roleId")
    Optional<SalarySettings> findByRoleId(Long roleId);
}

