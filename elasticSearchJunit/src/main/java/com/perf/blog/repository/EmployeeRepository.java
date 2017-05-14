package com.perf.blog.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.perf.blog.model.Employee;


public interface EmployeeRepository extends ElasticsearchRepository<Employee,String> {
    List<Employee> findEmployeesByAge(int age);  
    List<Employee> findEmployeesByName(String name);
    List<Employee> findEmployeesBySkillsIn(List<String> skills);
}
