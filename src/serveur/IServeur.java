package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.Acheteur;
import commun.Produit;

public interface IServeur extends Remote {
	/**
	 * validerInscription et demanderInscription sont synchronised
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public Produit validerInscription(Acheteur acheteur) throws RemoteException;

	public Produit demanderInscription(Acheteur acheteur) throws RemoteException;

	public Produit getProduitEnVente() throws RemoteException;
	/**
	 * Mettre a jour le prix dans encherir
	 * 
	 * @param client
	 * @param produit
	 * @param prix
	 * @return
	 * @throws RemoteException
	 */
	public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException;

	public boolean lancerLavente(Produit produit, int nb_inscrit) throws RemoteException;

}
