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
	private static boolean ETAT_VENTE_VALIDEE = true; // Vente en cours ou vente validée par les acheteurs
	

	private List<Produit> produits = new ArrayList<Produit>();
	
	private int indexVenteEncours = 0;
	

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
				if(produitEnVente.getDernierEnchireur()!=null && produitEnVente.getDernierEnchireur().getEtat()!=EtatAcheteur.TERMINE) produitEnVente.getDernierEnchireur().setEtat(EtatAcheteur.EN_ATTENTE);
				acheteur.setEtat(EtatAcheteur.ENCHERISSEMENT);
				produitEnVente.setPrix(prix);
				produitEnVente.setDernierEnchireur(acheteur);
				updateBidders(prix, acheteur);
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
				client.modifierPrix(prix, winner);
			} catch (ClientConnexionFailException | RemoteException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

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

	private synchronized void notifierVenteTerminee() {
		IClient client;
		Produit prochainProduit = null;
		if((produits.size()-1)>indexVenteEncours) {
			prochainProduit = produits.get(indexVenteEncours+1);
		}
		for (Acheteur acheteur : listeAcheteurs.values()) {
			try {
				client = connecterAuClient(acheteur);
				client.venteTerminee(produitEnVente.getPrix(), produitEnVente.getDernierEnchireur(), prochainProduit);
			} catch (RemoteException | ClientConnexionFailException e) {
				System.out.println("Impossible de joindre l'acheteur: " + acheteur.getNom() + " :" + e.getMessage());
			}
		}
	}

	@Override
	synchronized public void validerVente(String idAcheteur) throws RemoteException {
		Acheteur acheteur = listeAcheteurs.get(idAcheteur);
		if (acheteur != null) {
			acheteur.setaValiderLaVente(true);
			boolean tousAcheteursOntValide = true;
			for(Acheteur acht:listeAcheteurs.values()){
				if(!acht.isaValiderLaVente()){
					tousAcheteursOntValide = false;
					break;
				}
			}
			if(tousAcheteursOntValide){
				ETAT_VENTE_VALIDEE = true;
				for(Acheteur acht:listeAcheteurs.values()){
					acht.setaValiderLaVente(false);
				}
				notify();
			}
		} else {
			System.out.println("Encherir: L'acheteur n'est pas encore inscrit!");
		}
	}
	
	@Override
	synchronized public void tempsEcoule(String idAcheteur) throws RemoteException {
		//if (ETAT_VENTE_VALIDEE) return;
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

		/*
		 * On attend que tous les acheteurs ont validé la vente encours 
		 * avant de lancer la prochaine
		 */
		while (!ETAT_VENTE_VALIDEE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

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
	
	public Produit getProduitEncours(){
		return produits.get(indexVenteEncours);
	}
	public int getIndexVenteEncours() {
		return indexVenteEncours;
	}

	public void setIndexVenteEncours(int indexVenteEncours) {
		this.indexVenteEncours = indexVenteEncours;
	}

	public void incrementerIndexProduit(){
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
