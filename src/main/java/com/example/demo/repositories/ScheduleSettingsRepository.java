package com.example.demo.repositories;

import com.example.demo.models.ScheduleSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScheduleSettingsRepository extends JpaRepository<ScheduleSettings, Long> {
    @Query("SELECT ss FROM ScheduleSettings ss WHERE ss.shift.id = :shiftId")
    Optional<ScheduleSettings> findByShiftId(Long shiftId);
}

