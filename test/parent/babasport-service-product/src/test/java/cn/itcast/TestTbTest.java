package cn.itcast;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.dao.TestTbDAO;
import cn.itcast.core.pojo.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * Junit + Spring
 * 
 * @author Administrator 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })

public class TestTbTest {

	@Autowired
	private TestTbDAO testTbDAO;

	@Autowired
	private TestTbService testTbService;

	@Test
	public void testAdd() {
		TestTb testTb = new TestTb();
		testTb.setName("范冰冰");
		testTb.setBirthday(new Date());
		testTbDAO.add(testTb);
	}

	@Test
	public void testAdd2() {
		TestTb testTb = new TestTb();
		testTb.setName("范冰冰2");
		testTb.setBirthday(new Date());
		//testTbService.add(testTb);
	}

	
	@Test
	public void testAdd3() {
		TestTb testTb1 = new TestTb();
		testTb1.setName("范冰冰22");
		testTb1.setBirthday(new Date());
		
		TestTb testTb2 = new TestTb();
		testTb2.setName("范冰冰23");
		testTb2.setBirthday(new Date());
		
		testTbService.add(testTb1, testTb2);
	}

}
