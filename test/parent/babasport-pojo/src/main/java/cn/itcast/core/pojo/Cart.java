package cn.itcast.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车实体类
 * 
 * @author Administrator
 *
 */
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 购物项的集合
	private List<Item> items = new ArrayList<Item>();

	/**
	 * 自定义添加item到cart中方法，如果item的skuId（id）一样，则item的购买数量加上amount
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		// 遍历老的购物项，看里面没有id与新进了库存商品一样的
		for (Item item2 : items) {
			// 如果有一样
			if (item2.getSkuId().longValue() == item.getSkuId().longValue()) {
				item2.setAmount(item2.getAmount() + item.getAmount());
				return;
			}
		}
		this.items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	// 总价等信息

	// 计算多少件商品
	// 不转成json
	@JsonIgnore
	public Integer getProductAmount() {
		Integer totalAmount = 0;// 总数量

		for (Item item : items) {

			totalAmount = totalAmount + item.getAmount();

		}
		return totalAmount;
	}

	// 计算商品的总金额
	@JsonIgnore
	public Float getProductPrice() {
		Float productPrice = 0f;

		for (Item item : items) {
			productPrice = productPrice + item.getAmount()
					* Float.parseFloat(item.getSku().get("price").toString());
		}
		return productPrice;
	}

	// 计算运费
	@JsonIgnore
	public Float getFee() {
		if (this.getProductPrice() > 79) {
			return 0f;
		} else {
			return 20f;
		}
	}

	// 计算总计
	@JsonIgnore
	public Float getTotalPrice() {
		return this.getProductPrice() + this.getFee();
	}
}
