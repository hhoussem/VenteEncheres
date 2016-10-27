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

	static private Map<String, Acheteur> listeEnchere = new HashMap<String, Acheteur>();
	private List<Acheteur> listeTemporaire = new ArrayList<Acheteur>();
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
	public synchronized Produit demanderInscription(Acheteur acheteur) throws RemoteException {
		try {
			Acheteur c = new Acheteur(acheteur.getId(), acheteur.getNom(), acheteur.getPrenom());
			c.setUrl(acheteur.getUrl());
			System.out.println("Demande d'inscri du acheteur => " + c.getPrenom() + " " + c.getNom());
			listeTemporaire.add(c);
			// System.out.println("acheteur enregistr� � la vente de " +
			// produitEnVente.toString());
			// LanceClient.PRODUITENVENTE = produitEnVente;

			return validerInscription(c);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public synchronized Produit validerInscription(Acheteur acheteur) throws RemoteException {
		try {
			Acheteur c = acheteur;
			System.out.println("Valider l'inscri du acheteur => " + c.getPrenom() + " " + c.getNom());
			if (listeEnchere.size() <= NOMBRE_MAX_CLIENT) {
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

	protected synchronized boolean lancerLavente() {
		while (listeEnchere.size() != NOMBRE_MAX_CLIENT) {
			try {
				System.out.println("server is waiting for ...");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("error!");
				return false;
			}
		}
		System.out.println("notified, vente va commencer!");
		return true;
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
		for (Acheteur acheteur : listeEnchere.values()) {
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
		for (Acheteur acheteur : listeEnchere.values()) {
			if (!acheteur.getEtat().equals(EtatAcheteur.TERMINE)) {
				terminee = false;
				break;
			}
		}
		return terminee;
	}

	private synchronized void notifierVenteTerminee() {
		IClient client;
		for (Acheteur acheteur : listeEnchere.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.venteTerminee(produitEnVente.getPrix(), produitEnVente.getWinner());
			} catch (RemoteException | ClientConnexionFailException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}

		/*
		 * try { System.out.println("Attentre 20 secondes");
		 * //wait(20000);//attendre un instant avant de lancer la vente suivante
		 * } catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		ETAT_VENTE_TERMINEE = true;
		notify();
	}

	@Override
	synchronized public void tempsEcoule(String idAcheteur) throws RemoteException {
		if (ETAT_VENTE_TERMINEE)
			return;
		Acheteur acheteur = listeEnchere.get(idAcheteur);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ETAT_VENTE_TERMINEE = false;
		setProduitEnVente(produit);
		for (Acheteur acheteur : listeEnchere.values()) {
			try {
				IClient client = connecterAuClient(acheteur);
				client.notifierNoulleVente(produit);
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
