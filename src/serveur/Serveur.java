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

	private Map<String, Acheteur> listeAcheteurs = new HashMap<String, Acheteur>();
	private List<Acheteur> listeTemporaire = new ArrayList<Acheteur>();
	private Produit produitEnVente = null;
	private final int NOMBRE_MAX_CLIENT = 2;
	private static boolean ETAT_VENTE_VALIDEE = true; // Vente en cours ou vente
														// validée par les
														// acheteurs

	private List<Produit> produits = new ArrayList<Produit>();

	private int indexVenteEncours = 0;

	private static final long serialVersionUID = 1L;

	/**
	 * methode privée du serveur appelée pour se connecter à un client
	 * 
	 * @param acheteur
	 *            : acheteur
	 * @return IClient : reference sur le remote client
	 * @throws ClientConnexionFailException
	 */
	private IClient connecterAuClient(Acheteur acheteur) throws ClientConnexionFailException {
		Remote r;
		try {
			r = Naming.lookup(acheteur.getUrl());
			return (IClient) r;
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			throw new ClientConnexionFailException("Echec de la connexion au client " + acheteur.getId());
		}
	}

	/**
	 * mehtode privée qui copie les retartadaires de la liste temporaire à la
	 * listeAcheteurs
	 */
	private void validerLesRetardataires() {
		for (Acheteur ach : listeTemporaire) {
			if (!listeAcheteurs.containsKey(ach.getId())) {
				listeAcheteurs.put(ach.getId(), ach);
			}
		}
	}

	/**
	 * methode appelée quand un nouveau acheteur compte s'inscrire à une vente
	 * 
	 * @param acheteur
	 * @throws RemoteException
	 * @return Produit en vente
	 */
	@Override
	public synchronized Produit demanderInscription(Acheteur _acheteur) throws RemoteException {

		Acheteur acheteur = new Acheteur(_acheteur.getId(), _acheteur.getNom(), _acheteur.getPrenom());
		acheteur.setUrl(_acheteur.getUrl());
		System.out.println("Inscription de l'acheteur => " + acheteur.toString());
		listeTemporaire.add(acheteur);
		try {
			if ((listeTemporaire.size() >= NOMBRE_MAX_CLIENT) && ETAT_VENTE_VALIDEE) {
				validerLesRetardataires();
				notify(); // on notifie lanceVente
				return produitEnVente;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * methode qui attend à ce que le nombre des acheteurs soit égal à Nb max
	 * pour lancer la vente
	 * 
	 * @return si la vente peut commencer ou non
	 */
	protected synchronized boolean lancerLavente() {
		while (listeAcheteurs.size() != NOMBRE_MAX_CLIENT) {
			try {
				System.out.println("server is waiting for " + (NOMBRE_MAX_CLIENT - listeAcheteurs.size()) + " clients");
				wait(); // on attend la notification de demanderInscription()
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("nombre de clients max atteint, vente commencé !");
		return true;
	}

	/**
	 * methode appelée quand un acheteur enchérit
	 */
	@Override
	synchronized public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException {
		Acheteur acheteur = listeAcheteurs.get(idAcheteur);
		if (acheteur != null) {
			if (produitEnVente.getDernierEncherisseur() != null
					&& produitEnVente.getDernierEncherisseur().getEtat() != EtatAcheteur.TERMINE)
				produitEnVente.getDernierEncherisseur().setEtat(EtatAcheteur.EN_ATTENTE);
			acheteur.setEtat(EtatAcheteur.ENCHERISSEMENT);
			produitEnVente.setPrixEnchere(prix);
			produitEnVente.setDernierEncherisseur(acheteur);
			mettreAjourAcheteurs(prix, acheteur);

		} else {
			System.out.println("Encherir: L'acheteur n'est pas encore inscrit!");
			return false;
		}
		return true;
	}

	/**
	 * methode appelée quand un acheteur enchérit, elle diffuse le nouveau prix,
	 * et le dernier Encherisseur pour tous les acheteurs
	 * 
	 * @param prix
	 * @param dernierencherisseur
	 */
	private void mettreAjourAcheteurs(double prix, Acheteur dernierEncherisseur) {
		IClient client;
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.modifierPrix(prix, dernierEncherisseur);
			} catch (ClientConnexionFailException | RemoteException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

	/**
	 * methode qui verifie si la vente est terminée ou non
	 */
	private boolean verifierSiVenteTerminee() {
		boolean terminee = true;
		for (Acheteur acheteur : listeAcheteurs.values()) {
			if (acheteur.getEtat().equals(EtatAcheteur.EN_ATTENTE)) {
				terminee = false;
				break;
			}
		}
		return terminee;
	}

	/**
	 * appelée pour notifier tous les acheteurs quand la vente est terminée et
	 * mettre en vente le produit suivant
	 */
	private synchronized void notifierVenteTerminee() {
		IClient client;
		Produit prochainProduit = null;
		if ((produits.size() - 1) > indexVenteEncours) {
			prochainProduit = produits.get(indexVenteEncours + 1);
		}
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.venteTerminee(produitEnVente.getPrix(), produitEnVente.getDernierEncherisseur(), prochainProduit);
			} catch (RemoteException | ClientConnexionFailException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

	/**
	 * Quand une vente est terminée les clients doivent valider la vente en
	 * appelant cette methode puis on passe à la vente suivante
	 */
	@Override
	synchronized public void validerVente(String idAcheteur) throws RemoteException {
		Acheteur acheteur = listeAcheteurs.get(idAcheteur);
		if (acheteur != null) {
			acheteur.setaValiderLaVente(true);
			boolean tousAcheteursOntValide = true;
			for (Acheteur acht : listeAcheteurs.values()) {
				if (!acht.isaValiderLaVente()) {
					tousAcheteursOntValide = false;
					break;
				}
			}
			if (tousAcheteursOntValide) {
				ETAT_VENTE_VALIDEE = true;
				for (Acheteur acht : listeAcheteurs.values()) {
					acht.setaValiderLaVente(false);
				}
				notify();
			}
		} else {
			System.out.println("Encherir: L'acheteur n'est pas encore inscrit!");
		}
	}

	/**
	 * methode appelée quand le temps du chronometre est écoulé chez un acheteur
	 */
	@Override
	synchronized public void tempsEcoule(String idAcheteur) throws RemoteException {
		// if (ETAT_VENTE_VALIDEE) return;
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

	/**
	 * Mise aux encheres d'un nouveau produit
	 */
	public synchronized void mettreAuxEcheres(Produit produit) {

		/*
		 * On attend que tous les acheteurs ont validé la vente encours avant de
		 * lancer la prochaine
		 */
		while (!ETAT_VENTE_VALIDEE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		validerLesRetardataires();
		System.out.println("Mise au enchere du produit : " + produits.get(indexVenteEncours));
		ETAT_VENTE_VALIDEE = false;
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

	public Produit getProduitEncours() {
		return produits.get(indexVenteEncours);
	}

	public int getIndexVenteEncours() {
		return indexVenteEncours;
	}

	public void setIndexVenteEncours(int indexVenteEncours) {
		this.indexVenteEncours = indexVenteEncours;
	}

	public void incrementerIndexProduit() {
		indexVenteEncours++;
	}

	public List<Produit> getProduits() {
		return produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	protected Serveur() throws RemoteException {
		super();
		produits.add(new Produit("Telephone", 500, "ecran 5 pouces, 16go"));
		produits.add(new Produit("Ordinateur Portable", 150, "DELL 15pources, 4go, 1To"));
		produits.add(new Produit("Samsung S6", 500, "ecran 5.5 pouces, 3go"));
	}

	private synchronized void setProduitEnVente(Produit produit) {
		if (produit != null) {
			this.produitEnVente = produit;
			System.out.println("Produit en vente: " + produit.toString());
		}
	}

}
