package cn.itcast.core.pojo;

import java.io.Serializable;

/**
 * 购物项实体类
 * @author Administrator
 *
 */
public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 原始库存信息（复合型对象）
	private SuperPojo sku;
	
	//购买数量
	private Integer amount;
	
	// 库存id（单独提出来方便操作）
	private Long skuId;
	
	//有货无货标识
	private Boolean isHave = true;
	
	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public SuperPojo getSku() {
		return sku;
	}

	public void setSku(SuperPojo sku) {
		this.sku = sku;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
}
