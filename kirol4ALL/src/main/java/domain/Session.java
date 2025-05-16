package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Session implements Serializable {

	private static final long seralVersionUID = 1L;

	@Id
	private Date date;
	private Date hour;
	@OneToOne
	private Activity activity;
	@OneToMany
	private List<Reservation> reservations;
	@OneToOne
	private Room room;

	public Session(Date date, Date hour, Activity activity, List<Reservation> reservations, Room room) {
		this.date = date;
		this.hour = hour;
		this.activity = activity;
		this.reservations = reservations;
		this.room = room;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getHour() {
		return hour;
	}

	public void setHour(Date hour) {
		this.hour = hour;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
