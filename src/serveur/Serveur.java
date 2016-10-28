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
import commun.EtatAcheteur;
import commun.Produit;
import commun.exception.ClientConnexionFailException;

public class Serveur extends UnicastRemoteObject implements IServeur {

	static private Map<String, Acheteur> listeAcheteurs = new HashMap<String, Acheteur>();
	private Produit produitEnVente = null;
	private final int NOMBRE_MAX_CLIENT = 2;
	private static boolean ETAT_VENTE_TERMINEE = true; // Vente en cours ou
														// vente terminée

	private static final long serialVersionUID = 1L;

	private IClient connecterAuClient(Acheteur acheteur) throws ClientConnexionFailException {
		Remote r;
		try {
			r = Naming.lookup(acheteur.getUrl());
			return (IClient) r;
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			throw new ClientConnexionFailException("Echec de la connexion au client " + acheteur.getId());
		}
	}

	@Override
	public synchronized Produit demanderInscription(Acheteur _acheteur) throws RemoteException {

		Acheteur acheteur = new Acheteur(_acheteur.getId(), _acheteur.getNom(), _acheteur.getPrenom());
		acheteur.setUrl(_acheteur.getUrl());
		System.out.println("Inscription de l'acheteur => " + acheteur.toString());
		try {
			if (listeAcheteurs.size() < NOMBRE_MAX_CLIENT) {
				listeAcheteurs.put(acheteur.getId(), acheteur);
				notify();
			}
			return produitEnVente;
		} catch (Exception e) {
			return null;
		}

	}

	protected synchronized boolean lancerLavente() {
		while (listeAcheteurs.size() != NOMBRE_MAX_CLIENT) {
			try {
				System.out.println("server is waiting for "+(NOMBRE_MAX_CLIENT - listeAcheteurs.size())+" clients");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("nombre de clients max atteint, vente commencé !");
		return true;
	}

	@Override
	synchronized public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException {
		Acheteur acheteur = listeAcheteurs.get(idAcheteur);
		if (acheteur != null) {
			if (prix > produitEnVente.getPrix()) {
				acheteur.setEtat(EtatAcheteur.ENCHERISSEMENT);
				produitEnVente.setPrix(prix);
				produitEnVente.setWinner(acheteur);
				updateBidders(prix, acheteur);
				if (verifierSiVenteTerminee()) {
					notifierVenteTerminee();
				}
			}
		} else {
			System.out.println("Encherir: L'acheteur n'est pas encore inscrit!");
			return false;
		}
		return true;
	}

	private void updateBidders(double prix, Acheteur winner) {
		IClient client;
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.updatePrice(prix, winner);
			} catch (ClientConnexionFailException | RemoteException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

	private boolean verifierSiVenteTerminee() {
		boolean terminee = true;
		for (Acheteur acheteur : listeAcheteurs.values()) {
			if (!acheteur.getEtat().equals(EtatAcheteur.TERMINE)) {
				terminee = false;
				break;
			}
		}
		return terminee;
	}

	private synchronized void notifierVenteTerminee() {
		IClient client;
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.venteTerminee(produitEnVente.getPrix(), produitEnVente.getWinner());
			} catch (RemoteException | ClientConnexionFailException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}

		
		try {
			System.out.println("Attentre 10 secondes avant la prochaine vente");
			wait(10000);// attendre un instant avant de lancer la vente suivante
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		ETAT_VENTE_TERMINEE = true;
		notify();
	}

	@Override
	synchronized public void tempsEcoule(String idAcheteur) throws RemoteException {
		if (ETAT_VENTE_TERMINEE)
			return;
		Acheteur acheteur = listeAcheteurs.get(idAcheteur);
		if (acheteur != null) {
			acheteur.setEtat(EtatAcheteur.TERMINE);
			if (verifierSiVenteTerminee()) {
				notifierVenteTerminee();
			}
		} else {
			System.out.println("L'acheteur n'est pas encore inscrit!");
		}
	}

	/*
	 * Mise aux encheres d'un nouveau produit
	 */
	public synchronized void mettreAuxEcheres(Produit produit) {

		while (!ETAT_VENTE_TERMINEE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ETAT_VENTE_TERMINEE = false;
		setProduitEnVente(produit);
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				IClient client = connecterAuClient(acheteur);
				client.notifierNouvelleVente(produit);
			} catch (ClientConnexionFailException | RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	protected Serveur() throws RemoteException {
		super();
	}

	@Override
	public Produit getProduitEnVente() {
		return produitEnVente;
	}

	private synchronized void setProduitEnVente(Produit produit) {
		if (produit != null) {
			this.produitEnVente = produit;
			System.out.println("Produit en vente: " + produit.toString());
		}
	}

}
