package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.dao.BuyerDAO;
import cn.itcast.core.pojo.Buyer;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDAO buyerDAO;

	@Override
	public Buyer findByUsername(String username) {

		Buyer buyer = new Buyer();
		buyer.setUsername(username);
		return buyerDAO.selectOne(buyer);
	}

}
