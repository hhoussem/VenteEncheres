package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import commun.Produit;

public interface IClient extends Remote {

	public void updatePrice(double prix, Acheteur winner) throws RemoteException;

	public void venteTerminee(double prix, Acheteur winner) throws RemoteException;

	/**
	 * Notifier le client pour une nouvelle vente de produit Methode appelée à
	 * distance au niveau du serveur
	 * 
	 * @param produit
	 */
	void notifierNoulleVente(Produit produit) throws RemoteException;
}
