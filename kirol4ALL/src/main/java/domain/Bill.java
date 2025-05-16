package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bill {
	
	@ManyToOne
	private Member client;
	@Id
	private int code;
	private Date date;
	private float amount;
	private String[] status = {"Unpaid", "Paid"};
	
	public Bill(Member client, int code, Date date, float amount) {
		this.client = client;
		this.code = code;
		this.date = date;
		this.amount = amount;
	}

	public Member getClient() {
		return client;
	}

	public void setClient(Member client) {
		this.client = client;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

}
