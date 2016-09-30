package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import client.Client;
import commun.Produit;

public class Serveur extends UnicastRemoteObject implements IServeur{
	
	private List<Client> clients = new ArrayList<Client>();
	private List<Client> listeTemporaire = new ArrayList<Client>();
	private final int NOMBRE_MAX_CLIENT = 3;
	
	protected Serveur() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean validerInscription() throws RemoteException {
		return true;
	}

	@Override
	public boolean demanderInscription(Client client) throws RemoteException {
		Client c = new Client(client.getId(), client.getNom(), client.getPrenom());
		System.out.println("Demande du client => "+c.getId());		
		listeTemporaire.add(c);
		System.out.println("Nombre d'inscrits => "+listeTemporaire.size());
		Scanner s = new Scanner(System.in);
		String a = s.nextLine();

		if(listeTemporaire.size() == NOMBRE_MAX_CLIENT){
			
		}
		return true;
	}

	@Override
	public boolean encherir(Client client, Produit produit, double prix) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lancerLavente(Produit produit) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
