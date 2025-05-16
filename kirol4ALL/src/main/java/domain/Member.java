package domain;

import javax.persistence.Entity;

@Entity
public class Member extends User {
	private static final long serialVersionUID = 1L;

	private final int n_maxReservations;
	private int n_leftReservations;

	private String rol = "Member";

	public Member() {
		super();
		this.n_maxReservations = 5;
		this.n_leftReservations = n_maxReservations;
	}

	public Member(String name, String password, String completeName, String documentID) {
		super();
		this.setName(name);
		this.setPassword(password);
		this.setCompleteName(completeName);
		this.setDocumentID(documentID);
		this.n_maxReservations = 5;
		this.n_leftReservations = n_maxReservations;
	}

	public String getRol() {
		return rol;
	}

	public int getN_maxReservations() {
		return n_maxReservations;
	}

	public int getN_leftReservations() {
		return n_leftReservations;
	}

// Check if the member can make a reservation
	public boolean canMakeReservation() {
		return n_leftReservations > 0;
	}

// Decrement the number of remaining reservations
	public void decrementReservations() {
		if (n_leftReservations > 0) {
			n_leftReservations--;
		}
	}
}
