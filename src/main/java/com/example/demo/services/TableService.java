package com.example.demo.services;

import com.example.demo.dto.TableStatusDto;
import com.example.demo.repositories.TableRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<TableStatusDto> getTableStatusDTO() {
        return tableRepository.getTables();
    }

    public List<Long> getFreeTable(){
        return tableRepository.free_tables();
    }
}
