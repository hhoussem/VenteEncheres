package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import commun.Produit;

public interface IClient extends Remote {
	
	/**
	 * Notifier le client pour une nouvelle vente de produit Methode appelée à
	 * distance au niveau du serveur
	 * 
	 * @param produit
	 */
	void notifierNouvelleVente(Produit produit) throws RemoteException;

	public void modifierPrix(double prix, Acheteur dernierEnchireur) throws RemoteException;

	public void venteTerminee(double prix, Acheteur dernierEnchireur, Produit prochainProduit) throws RemoteException;


}
