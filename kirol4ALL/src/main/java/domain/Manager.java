package domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Manager extends User {
	private static final long serialVersionUID = 1L;
	
	private String rol = "Manager";
	
	public Manager() {
		super();
	}
	
	public Manager(String name, String password, String completeName, String documentID) {
		super();
		this.setName(name);
		this.setPassword(password);
		this.setCompleteName(completeName);
		this.setDocumentID(documentID);
	}
	
	public String getRol() {
		return rol;
	}

}
