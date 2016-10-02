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
import commun.Produit;

public class Serveur extends UnicastRemoteObject implements IServeur {

	private Map<String, Acheteur> listeEnchere = new HashMap<String, Acheteur>();

	private List<Acheteur> listeTemporaire = new ArrayList<Acheteur>();
	private Produit produitEnVente;
	private final int NOMBRE_MAX_CLIENT = 3;

	private static final long serialVersionUID = 1L;

	protected Serveur() throws RemoteException {
		super();
		produitEnVente = new Produit("Telephone", 500, "ecran 5 pouces, 16go");
	}

	public Produit getProduitEnVente() {
		return produitEnVente;
	}

	public void setProduitEnVente(Produit produit) {
		this.produitEnVente = produit;
		System.out.println("Produit en vente: " + produit.toString());
	}

	@Override
	public Produit validerInscription(Acheteur acheteur) throws RemoteException {
		try {
			Acheteur c = new Acheteur(acheteur.getId(), acheteur.getNom(), acheteur.getPrenom());
			System.out.println("Valider l'inscri du acheteur => " + c.getPrenom()+" "+c.getNom());
			if (listeEnchere.size() < NOMBRE_MAX_CLIENT - 1) {
				listeEnchere.put(c.getId(), c);
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
			Acheteur c = new  Acheteur(acheteur.getId(), acheteur.getNom(), acheteur.getPrenom());
			c.setUrl(acheteur.getUrl());
			System.out.println("Demande d'inscri du acheteur => " + c.getPrenom()+" "+c.getNom());
			listeEnchere.put(c.getId(), c);
			System.out.println("acheteur enregistré à la vente de " + produitEnVente.toString());
			return produitEnVente;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	public void updateBidders(double prix, Acheteur winner){
		IClient client;
		for (Acheteur acheteur : listeEnchere.values()) {
			try{
				client = connecterAuClient(acheteur);
				client.updatePrice(prix, winner);
			}catch (MalformedURLException | NotBoundException | RemoteException e) {
				System.out.println("Impossible de joindre l'acheteur: "+acheteur.getNom()+" :"+e.getMessage());
			}
		}		
	}
	
	private IClient connecterAuClient(Acheteur acheteur) throws MalformedURLException, RemoteException, NotBoundException{
			//Remote r = Naming.lookup("//localhost:9000/client");
			Remote r = Naming.lookup(acheteur.getUrl());
			return (IClient) r;
	}

	@Override	
	synchronized public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException {
			Acheteur acheteur = listeEnchere.get(idAcheteur);
			if(acheteur!=null){
		        if (prix > produitEnVente.getPrix()) {
		        	produitEnVente.setPrix(prix);
		        	produitEnVente.setWinner(acheteur);
		        }
			}else{
				System.out.println("L'acheteur n'est pas encore inscrit!");
			}
			updateBidders(prix, acheteur);
		return false;
	}
	
	@Override
	public boolean lancerLavente(Produit produit, int nb_inscrit) throws RemoteException {

		if (nb_inscrit == NOMBRE_MAX_CLIENT) {

		}
		return false;
	}

}
