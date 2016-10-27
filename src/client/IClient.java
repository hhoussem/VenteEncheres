package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import commun.Produit;

public interface IClient extends Remote {
	public boolean demanderInscription() throws RemoteException;
	public void updatePrice(double prix, Acheteur winner) throws RemoteException;
	public void venterTerminee(double prix, Acheteur winner) throws RemoteException;
	/**
	 * Notifier le client pour une nouvelle vente de produit
	 * Methode appelée à distance au niveau du serveur
	 * @param produit
	 */
	void notifierNoulleVente(Produit produit) throws RemoteException;
}
