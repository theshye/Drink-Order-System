package com.example.drink_order_system;

import java.util.ArrayList;

public class Order {
	private String order_number;
	private String time;
	private String takeAway;
	private String cost;
	private int rating; // 新增评星属性

	Order(String info) {
		String[] info_list = info.split(",");
		order_number = info_list[0];
		time = info_list[1];
		takeAway = info_list[2];
		cost = info_list[3];
		if (info_list.length > 4) {
			rating = Integer.parseInt(info_list[4]);
		} else {
			rating = 0; // 默认未评分
		}
	}

	public String getOrder_number() {
		return order_number;
	}

	public String getTime() {
		return time;
	}

	public String getTakeAway() {
		return takeAway;
	}

	public String getCost() {
		return cost;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return order_number + "," + time + "," + takeAway + "," + cost + "," + rating;
	}
}