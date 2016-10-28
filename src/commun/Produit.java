package commun;

import java.io.Serializable;

import client.Acheteur;

public class Produit implements Serializable {

	private int id;
	private String nom;
	private double prix;
	private String description;
	private Acheteur winner;

	private static final long serialVersionUID = 1485L;

	public Produit(String nom, double prix, String description) {
		super();
		this.nom = nom;
		this.prix = prix;
		this.description = description;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Acheteur getWinner() {
		return winner;
	}


	public void setWinner(Acheteur winner) {
		this.winner = winner;
	}
	public String toString() {
		return nom + " " + prix + " " + description;
	}

}
