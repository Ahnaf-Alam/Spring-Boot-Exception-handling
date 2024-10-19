package com.example.exceptionHandlingExercise.controller;

import com.example.exceptionHandlingExercise.Exception.ResourceNotFoundException;
import com.example.exceptionHandlingExercise.dao.EmployeeDao;
import com.example.exceptionHandlingExercise.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;


    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> employees = employeeDao.findAll();
        if(employees.isEmpty()) {
            throw new ResourceNotFoundException("No employee is available");
        }

        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeDao.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found::" + employeeId));

        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) throws Exception {
        Employee savedEmployee = employeeDao.saveAndFlush(employee);

        return ResponseEntity.ok().body(savedEmployee);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId, @RequestBody Employee updatedEmployee) throws ResourceNotFoundException {
        Employee employee = employeeDao.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found::" + employeeId));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());

        final Employee savedEmployee = employeeDao.saveAndFlush(employee);

        return ResponseEntity.ok(savedEmployee);
    }

}
