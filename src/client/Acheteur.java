package client;

import java.io.Serializable;
import commun.Produit;

public class Acheteur implements IClient, Serializable {

	private String id;
	private String nom;
	private String prenom;
	private String etat;
	private Produit produitEnVente;

	private String url;
	private int port;

	private static final long serialVersionUID = 1L;

	public Acheteur(String id, String nom, String prenom) {

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

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Override
	public boolean demanderInscription() {
		return false;
	}

	public void updatePrice(double prix, Acheteur winner) {
		produitEnVente.setPrix(prix);
		produitEnVente.setWinner(winner);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String toString() {
		return (id + " " + prenom + " " + nom);
	}
}
