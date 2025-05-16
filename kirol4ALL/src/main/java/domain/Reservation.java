package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;
	//private int id;
	private Member client;
	private Session session;
	private String status;
	private Date reservationDate;

	public Reservation(Member client, Session session, String status, Date reservationDate) {
		this.client = client;
		this.session = session;
		this.status = status;
		this.reservationDate = reservationDate;
	}

	public Member getClient() {
		return client;
	}

	public void setClient(Member client) {
		this.client = client;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

}
