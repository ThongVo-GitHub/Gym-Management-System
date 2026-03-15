package com.fitness.gymManagementSystem.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GymItem {
	@Id
	private Long itemId;
	private String itemName;
	private Integer totalSeat;
	public GymItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GymItem(Long itemId, String itemName, Integer totalSeat) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.totalSeat = totalSeat;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
		
	}
	public Integer getTotalSeat() {
		return totalSeat;
	}
	public void setTotalSeat(Integer totalSeat) {
		this.totalSeat = totalSeat;
	}
	
}
