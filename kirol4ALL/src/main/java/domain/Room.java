package domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {
	
	private static final long serialVersionUID = 1L;
	@Id
	private String roomNumber;
	private int capacity;
	
	public Room(String i, int capacity) {
		this.roomNumber = i;
		this.capacity = capacity;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}	

}
