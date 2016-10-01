package client;

import java.io.Serializable;

public class Client implements IClient, Serializable {

	private String id;
	private String nom;
	private String prenom;

	private static final long serialVersionUID = 1L;

	public Client(String id, String nom, String prenom) {

		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Override
	public boolean demanderInscription() {
		// TODO Auto-generated method stub
		return false;
	}
}
