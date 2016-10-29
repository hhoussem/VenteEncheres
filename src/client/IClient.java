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
	 * @throws RemoteException
	 */
	void notifierNouvelleVente(Produit produit) throws RemoteException;

	/**
	 * Notifier le client du dernier prix et du dernier encherisseur
	 * 
	 * @param prix
	 * @param dernierEncherisseur
	 * @throws RemoteException
	 */
	public void modifierPrix(double prix, Acheteur dernierEncherisseur) throws RemoteException;

	/**
	 * Notifier le client quand la vente est termniée
	 * 
	 * @param prix
	 * @param dernierEncherisseur
	 * @param prochainProduit
	 * @throws RemoteException
	 */
	public void venteTerminee(double prix, Acheteur dernierEncherisseur, Produit prochainProduit)
			throws RemoteException;

}
