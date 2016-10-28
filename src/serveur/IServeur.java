package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.Acheteur;
import commun.Produit;

public interface IServeur extends Remote {
	
	public Produit demanderInscription(Acheteur acheteur) throws RemoteException;

	public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException;

	public void tempsEcoule(String idAcheteur) throws RemoteException;

	/**
	 * Quand une vente est terminée les clients doivent valider la vente en appelant cette methode
	 * puis on passe à la vente suivante
	 * @param idAcheteur
	 * @throws RemoteException
	 */
	void validerVente(String idAcheteur) throws RemoteException;

}
