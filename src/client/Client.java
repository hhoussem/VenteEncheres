package client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import serveur.IServeur;
import serveur.Serveur;

public class Client  implements IClient,Serializable {
	
	String id;
	String nom;
	String prenom;
	
	
	public Client(String id, String nom, String prenom)  {

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
	    	Remote r = Naming.lookup("//127.0.0.1:8000/vente");
	    	Client client = new Client("55","TOTO","titi");
	      
	      System.out.println(r.getClass());
	        boolean s = ((IServeur) r).demanderInscription(client);
	        System.out.println("chaine renvoyee = " + s);
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
