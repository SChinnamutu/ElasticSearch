package com.perf.blog.junit;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.perf.blog.model.Employee;
import com.perf.blog.model.Skill;
import com.perf.blog.repository.EmployeeRepository;


@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ViewTest {


	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;

	@Test
	public void findByName(){
		List<Employee> empList = repository.findEmployeesByName("Messi");
		Assert.assertNotNull(empList);
	}
	
	@Test
	public void findByAge(){
		List<Employee> empList = repository.findEmployeesByAge(32);
		Assert.assertNotNull(empList);
	}
	
	@Test
	public void findByIndex(){
		Employee joe = new Employee("01", "Joe", 32);
		Skill javaSkill = new Skill("Java", 10);
		Skill db = new Skill("Oracle", 5);
		joe.setSkills(Arrays.asList(javaSkill, db));
		IndexQuery indexQuery = new IndexQuery();
		System.out.println("Index is" +joe.getId());
		indexQuery.setId(joe.getId());
		indexQuery.setObject(joe);
		template.index(indexQuery);
		List<Employee> empList = repository.findEmployeesByAge(32);
		Assert.assertNotNull(empList);
	}
}
