package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String name;
	private int difficulty;
	private int reserve_n;
	private float price;

	public Activity(String name, int difficulty, int reserve_n, float price) {
		this.name = name;
		this.difficulty = difficulty;
		this.reserve_n = reserve_n;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getReserve_n() {
		return reserve_n;
	}

	public void setReserve_n(int reserve_n) {
		this.reserve_n = reserve_n;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
