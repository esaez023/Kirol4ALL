package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private String completeName;
	@Id
	private String documentID;

	public User() {
		super();
	}
	
	public String getName() {
		return email;
	}

	public void setName(String name) {
		this.email = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}

	public abstract String getRol();

}
