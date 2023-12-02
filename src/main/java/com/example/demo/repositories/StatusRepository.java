package com.example.demo.repositories;

import com.example.demo.models.Statuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Statuses, Long> {
}
