package com.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

	@Query("SELECT em FROM Employee em WHERE em.id=:employeeId")
	public Employee findByEmployeeId(Integer employeeId);

	public Employee findByPersonId(Integer personId);
	public List<Employee> findByCompany(String company);
	
}
