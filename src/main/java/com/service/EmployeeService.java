package com.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import com.model.Employee;
import com.data.EmployeeRepository;


@Service
public class EmployeeService {

	private Logger logger = Logger.getLogger(EmployeeService.class.getName());

	@Autowired
	EmployeeRepository repository;
	@Autowired
	HazelcastInstance instance;
	
	IMap<Integer, Employee> map;
	
	@PostConstruct
	public void init() {
		map = instance.getMap("employee");
		map.addIndex("company", true);
		logger.info("Employees cache: " + map.size());
	}
	
	@SuppressWarnings("rawtypes")
	public Employee findByPersonId(Integer personId) {
		Predicate predicate = Predicates.equal("personId", personId);
		logger.info("Employee cache find");
		Collection<Employee> ps = map.values(predicate);
		logger.info("Employee cached: " + ps);
		Optional<Employee> e = ps.stream().findFirst();
		if (e.isPresent())
			return e.get();
		logger.info("Employee cache find");
		Employee emp = repository.findByPersonId(personId);
		logger.info("Employee: " + emp);
		map.put(emp.getId(), emp);
		return emp;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Employee> findByCompany(String company) {
		Predicate predicate = Predicates.equal("company", company);
		logger.info("Employees cache find");
		Collection<Employee> ps = map.values(predicate);
		logger.info("Employees cache size: " + ps.size());
		if (ps.size() > 0) {
			return ps.stream().collect(Collectors.toList());
		}
		logger.info("Employees find");
		List<Employee> e = repository.findByCompany(company);
		logger.info("Employees size: " + e.size());
		e.parallelStream().forEach(it -> {
			map.putIfAbsent(it.getId(), it);
		});
		return e;
	}

	public Employee findById(Integer id) {
		Employee e = map.get(id);
		if (e != null)
			return e;
		e = repository.findByEmployeeId(id);
		map.put(id, e);
		return e;
	}
	
	public Employee add(Employee e) {
		e = repository.save(e);
		map.put(e.getId(), e);
		return e;
	}
	
}
