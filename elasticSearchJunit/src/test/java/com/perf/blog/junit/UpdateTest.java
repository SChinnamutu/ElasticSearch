package com.perf.blog.junit;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.perf.blog.model.Employee;
import com.perf.blog.model.Skill;
import com.perf.blog.repository.EmployeeRepository;


@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UpdateTest {

	

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;

	public Employee getDetails(){
		List<Employee> empList = repository.findEmployeesByAge(29);
		if(empList != null && empList.size() > 0){
			return empList.get(0);
		}
		return null;
	}
	
	@Test
	public void updateEmployee(){
		Employee employee = this.getDetails();
		if(employee != null ){
			employee.setAge(31);
			repository.save(employee);
		}else{
			Employee employeeJnr =  new Employee();
			employeeJnr.setAge(21);
			employeeJnr.setId("11");
			employeeJnr.setName("Paulo Dybala");
			Skill javaSkill = new Skill("Dribbling", 98);
			Skill db = new Skill("Chipping", 99);
			employeeJnr.setSkills(Arrays.asList(javaSkill,db));
			repository.save(employeeJnr);
		}
		List<Employee> empList = repository.findEmployeesByAge(31);
		Assert.assertNotNull(empList);
	}
	
	
	@Test
	public void viewAll(){
		Iterable<Employee> empListAsItr = repository.findAll();
		for (Employee employee : empListAsItr) {
			System.out.println(employee.getName() + " " + employee.getId() + " " +employee.getAge() +" "+ employee.getSkills().get(0).getName() +" "+ employee.getSkills().get(1).getName());
		}
	}
}
