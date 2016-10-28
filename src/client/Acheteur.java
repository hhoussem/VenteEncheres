package client;

import java.io.Serializable;

import commun.EtatAcheteur;
import commun.Produit;

public class Acheteur implements Serializable {

	private String id;
	private String nom;
	private String prenom;
	private Produit produitEnVente;

	private String url;
	private int port;
	private EtatAcheteur etat = EtatAcheteur.EN_ATTENTE;
	
	private boolean aValiderLaVente = false;

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

	public EtatAcheteur getEtat() {
		return etat;
	}

	public void setEtat(EtatAcheteur etat) {
		this.etat = etat;
	}

	/**
	 * @return the aValiderLaVente
	 */
	public boolean isaValiderLaVente() {
		return aValiderLaVente;
	}

	/**
	 * @param aValiderLaVente the aValiderLaVente to set
	 */
	public void setaValiderLaVente(boolean aValiderLaVente) {
		this.aValiderLaVente = aValiderLaVente;
	}

}
