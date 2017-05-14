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
public class InsertTest {
	
	
	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;

	@Test
	public void insert(){
		Employee joe = new Employee("10", "Messi", 29);
		Skill javaSkill = new Skill("Dribbling", 98);
		Skill db = new Skill("Chipping", 99);
		joe.setSkills(Arrays.asList(javaSkill, db));
		addToRepository(joe);
		List<Employee> empList = repository.findEmployeesByName(joe.getName());
		Assert.assertNotNull(empList);
	}
	
	public void addToRepository(Employee joe){
		template.putMapping(Employee.class);
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(joe.getId());
		indexQuery.setObject(joe);
		template.index(indexQuery);
		template.refresh(Employee.class, true);
		repository.save(joe);
	}
}
