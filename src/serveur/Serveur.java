package serveur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Acheteur;
import client.IClient;
import client.LanceClient;
import commun.EtatAcheteur;
import commun.Produit;

public class Serveur extends UnicastRemoteObject implements IServeur {

	static private Map<String, Acheteur> listeEnchere = new HashMap<String, Acheteur>();
	private List<Acheteur> listeTemporaire = new ArrayList<Acheteur>();
	private Produit produitEnVente=null;
	private final int NOMBRE_MAX_CLIENT = 2;

	private static final long serialVersionUID = 1L;

	protected Serveur() throws RemoteException {
		super();
	}

	public Map<String, Acheteur> getListeEnchere() {
		return listeEnchere;
	}

	public List<Acheteur> getListeTemporaire() {
		return listeTemporaire;
	}

	@Override
	public Produit getProduitEnVente() {
		return produitEnVente;
	}

	public synchronized void setProduitEnVente(Produit produit) {
		if(produit != null) {
			this.produitEnVente = produit;
			System.out.println("Produit en vente: " + produit.toString());
		}
	}

	@Override
	public Produit validerInscription(Acheteur acheteur) throws RemoteException {
		try {
			Acheteur c = new Acheteur(acheteur.getId(), acheteur.getNom(), acheteur.getPrenom());
			System.out.println("Valider l'inscri du acheteur => " + c.getPrenom() + " " + c.getNom());
			if (listeEnchere.size() < NOMBRE_MAX_CLIENT - 1) {
				listeEnchere.put(c.getId(), c);
				notify();
			}
			for (int i = listeTemporaire.size() - 1; i >= 0; i--) {
				if (listeTemporaire.get(i).getId() == acheteur.getId()) {
					listeTemporaire.remove(i);
				}
			}
			return produitEnVente;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	synchronized public Produit demanderInscription(Acheteur acheteur) throws RemoteException {
		try {
			Acheteur c = new Acheteur(acheteur.getId(), acheteur.getNom(), acheteur.getPrenom());
			c.setUrl(acheteur.getUrl());
			System.out.println("Demande d'inscri du acheteur => " + c.getPrenom() + " " + c.getNom());
			listeEnchere.put(c.getId(), c);
			System.out.println(listeEnchere.size());
			notify();
			System.out.println("acheteur enregistr� � la vente de " + produitEnVente.toString());
			produitEnVente.setWinner(c);
			LanceClient.PRODUITENVENTE = produitEnVente;
			
			return produitEnVente;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void updateBidders(double prix, Acheteur winner) {
		IClient client;
		for (Acheteur acheteur : listeEnchere.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.updatePrice(prix, winner);
			} catch (MalformedURLException | NotBoundException | RemoteException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

	private IClient connecterAuClient(Acheteur acheteur)
			throws MalformedURLException, RemoteException, NotBoundException {
		Remote r = Naming.lookup(acheteur.getUrl());
		return (IClient) r;
	}
	
	private boolean verifierSiVenteTerminee(){
		boolean terminee = true;
		for (Acheteur acheteur : listeEnchere.values()) {
			if(!acheteur.getEtat().equals(EtatAcheteur.TERMINE)){
				terminee = false;
				break;
			}
		}
		return terminee;
	}

	private void notifierVenteTerminee(){
		IClient client;
		for (Acheteur acheteur : listeEnchere.values()) {
				try {
					client = connecterAuClient(acheteur);
					client.venterTerminee(produitEnVente.getPrix(), produitEnVente.getWinner());
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
				}				
		}
	}
	
	@Override
	synchronized public void tempsEcoule(String idAcheteur) throws RemoteException {
		Acheteur acheteur = listeEnchere.get(idAcheteur);
		if (acheteur != null) {
			acheteur.setEtat(EtatAcheteur.TERMINE);
			if(verifierSiVenteTerminee()){
				notifierVenteTerminee();
			}
		} else {
			System.out.println("L'acheteur n'est pas encore inscrit!");
		}
	}
	@Override
	synchronized public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException {
		Acheteur acheteur = listeEnchere.get(idAcheteur);
		if (acheteur != null) {
			if (prix > produitEnVente.getPrix()) {
				acheteur.setEtat(EtatAcheteur.ENCHERISSEMENT);
				produitEnVente.setPrix(prix);
				produitEnVente.setWinner(acheteur);
				updateBidders(prix, acheteur);
				if(verifierSiVenteTerminee()){
					notifierVenteTerminee();
				}
			}
		} else {
			System.out.println("Encherir: L'acheteur n'est pas encore inscrit!");
			return false;
		}
		return true;
	}

	protected synchronized boolean lancerLavente(Produit produit) {

		while (listeEnchere.size() != NOMBRE_MAX_CLIENT) {
			try {
				System.out.println("waiting");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("error!");
				return false;
			}
		}
		System.out.println("notified!");
		updateBidders(produit.getPrix(), null);
		return true;
	}

}
