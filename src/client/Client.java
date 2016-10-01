package client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import serveur.IServeur;
import commun.*;

public class Client implements IClient, Serializable {

	private String id;
	private String nom;
	private String prenom;

	private static final long serialVersionUID = 1L;

	public Client(String id, String nom, String prenom) {

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

	public static void main(String[] args) {
		System.out.println("Lancement du client");
		try {
			Remote remote = Naming.lookup("//127.0.0.1:8000/vente");
			Client client = new Client("55", "TOTO", "titi");

			IServeur serveur = (IServeur) remote;
			Produit produit = serveur.demanderInscription(client);

			if (produit != null) {
				System.out.println("client : " + client.getNom() + " enregistré avec succés");
			} else {
				System.out.println("enregistrement du client échoué!");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		System.out.println("Fin du client");

	}

	@Override
	public boolean demanderInscription() {
		// TODO Auto-generated method stub
		return false;
	}
}
