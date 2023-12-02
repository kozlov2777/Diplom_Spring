package com.example.demo.repositories;

import com.example.demo.dto.TableStatusDto;
import com.example.demo.models.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {
    @Query("SELECT new com.example.demo.dto.TableStatusDto(t.id, ts.name) FROM Tables t JOIN Table_Statuses ts ON t.tableStatus.id = ts.id ORDER BY t.id ASC")
    List<TableStatusDto> getTables();

    @Query("SELECT t.id FROM Tables t WHERE t.tableStatus.id = 1")
    List<Long> free_tables();
}
