package com.example.demo.services;

import com.example.demo.dto.EmployeeCreateDTO;
import com.example.demo.dto.EmployeeListDto;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.models.Employees;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleRepository = roleRepository;
    }

    public List<EmployeeListDto> getEmployeeList(){
        return employeeRepository.getEmployeeList();
    }
    public List<EmployeeSalaryDto> getEmployeeSalaries() {
        return employeeRepository.getEmployeeSalaries();
    }

    public void registerEmployee(EmployeeCreateDTO employeeCreateDTO) {
        Employees employee = new Employees();
        employee.setFirstName(employeeCreateDTO.getFirstName());
        employee.setLastName(employeeCreateDTO.getLastName());
        employee.setUsername(employeeCreateDTO.getUsername());
        employee.setPassword(passwordEncoder.encode(employeeCreateDTO.getPassword()));

        roleRepository.findById(employeeCreateDTO.getRoleId()).ifPresent(role -> employee.setRole(role));

        employeeRepository.save(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employees employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(employee.getRole().getName());

        return new User(employee.getUsername(), employee.getPassword(), Collections.singletonList(simpleGrantedAuthority));
    }
}
