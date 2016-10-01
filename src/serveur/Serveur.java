package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import client.Client;
import commun.Produit;

public class Serveur extends UnicastRemoteObject implements IServeur {

	private List<Client> clients = new ArrayList<Client>();
	private List<Client> listeEnchere = new ArrayList<Client>();
	private List<Client> listeTemporaire = new ArrayList<Client>();
	private Produit produit;
	private final int NOMBRE_MAX_CLIENT = 3;

	protected Serveur() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public Produit validerInscription(Client client) throws RemoteException {
		try {
			Client c = new Client(client.getId(), client.getNom(), client.getPrenom());
			System.out.println("Demande du client => " + c.getId());
			if (listeEnchere.size() < 4) {
				listeEnchere.add(c);
			}
			for (int i = listeTemporaire.size() - 1; i >= 0; i--) {
				if (listeTemporaire.get(i).getId() == client.getId()) {
					listeTemporaire.remove(i);
				}
			}
			return produit;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Produit demanderInscription(Client client) throws RemoteException {
		try {
			Client c = new Client(client.getId(), client.getNom(), client.getPrenom());
			System.out.println("Demande du client => "+c.getId());		
			listeTemporaire.add(c);

			return produit;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
			


	}

	@Override
	public boolean encherir(Client client, Produit produit, double prix) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lancerLavente(Produit produit, int nb_inscrit) throws RemoteException {

		while (nb_inscrit == 4) {

		}
		// produit = new Produit(produit.getId(), produit.getNom(),
		// produit.getPrenom());
		return false;
	}

}
