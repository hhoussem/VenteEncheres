package commun;

import java.io.Serializable;

public class Produit implements Serializable {

	private String nom;
	private double prix;
	private String description;

	private static final long serialVersionUID = 1485L;

	public Produit(String nom, double prix, String description) {
		super();
		this.nom = nom;
		this.prix = prix;
		this.description = description;
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

	public String toString() {
		return nom + " " + prix + " " + description;
	}

}
