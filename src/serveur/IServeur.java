package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.Acheteur;
import commun.Produit;

public interface IServeur extends Remote {
	
	public Produit demanderInscription(Acheteur acheteur) throws RemoteException;

	public Produit validerInscription(Acheteur acheteur) throws RemoteException;

	public Produit getProduitEnVente() throws RemoteException;

	public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException;

	public void tempsEcoule(String idAcheteur) throws RemoteException;

}
