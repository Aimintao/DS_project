package cn.itcast.core.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.dao.TestTbDAO;
import cn.itcast.core.pojo.TestTb;

@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService{

	@Autowired
	private TestTbDAO testTbDAO;
	
	
	@Override
	public void add(TestTb testTb1,TestTb testTb2) {
		
		testTbDAO.add(testTb1);

		//int i = 5 / 0;

		testTbDAO.add(testTb2);
		
	}
}
