package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Client;
import commun.Produit;

public interface IServeur extends Remote {
	/**
	 * validerInscription et demanderInscription sont synchronised
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public Produit validerInscription(Client client) throws RemoteException;

	public Produit demanderInscription(Client client) throws RemoteException;

	/**
	 * Mettre a jour le prix dans encherir
	 * 
	 * @param client
	 * @param produit
	 * @param prix
	 * @return
	 * @throws RemoteException
	 */
	public boolean encherir(String idClient, Produit produit, double prix) throws RemoteException;

	public boolean lancerLavente(Produit produit, int nb_inscrit) throws RemoteException;

}
