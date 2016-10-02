package serveur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.Client;
import commun.Produit;

public class Serveur extends UnicastRemoteObject implements IServeur {

	private List<Client> listeEnchere = new ArrayList<Client>();

	private List<Client> listeTemporaire = new ArrayList<Client>();
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
	public Produit validerInscription(Client client) throws RemoteException {
		try {
			Client c = new Client(client.getId(), client.getNom(), client.getPrenom());
			System.out.println("Valider l'inscri du client => " + c.getPrenom()+" "+c.getNom());
			if (listeEnchere.size() < NOMBRE_MAX_CLIENT - 1) {
				listeEnchere.add(c);
			}
			for (int i = listeTemporaire.size() - 1; i >= 0; i--) {
				if (listeTemporaire.get(i).getId() == client.getId()) {
					listeTemporaire.remove(i);
				}
			}
			return produitEnVente;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	synchronized public Produit demanderInscription(Client client) throws RemoteException {
		try {
			Client c = new  Client(client.getId(), client.getNom(), client.getPrenom());
			System.out.println("Demande d'inscri du client => " + c.getPrenom()+" "+c.getNom());
			listeEnchere.add(c);
			System.out.println("client enregistré à la vente de " + produitEnVente.toString());
			return produitEnVente;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	public void updateBidders(double prix, Client winner){
		for (Client client : listeEnchere) {
			client.updatePrice(prix, winner);
		}
	}

	@Override	
	synchronized public boolean encherir(String idClient, Produit produit, double prix) throws RemoteException {

		try {
			Remote r = Naming.lookup("//127.0.0.1:9000/client");
	        Client client = (Client) r;
	        System.out.println(prix);
	        
	        if (prix > produitEnVente.getPrix()) {
	        	produitEnVente.setPrix(prix);
	        	produitEnVente.setWinner(client);
	        }
			
			updateBidders(prix, client);
		} catch (MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	@Override
	public boolean lancerLavente(Produit produit, int nb_inscrit) throws RemoteException {

		if (nb_inscrit == NOMBRE_MAX_CLIENT) {

		}
		return false;
	}

}
