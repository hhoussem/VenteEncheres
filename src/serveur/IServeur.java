package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.Acheteur;
import commun.Produit;

public interface IServeur extends Remote {

	/**
	 * methode appelée quand un nouveau acheteur compte s'inscrire à une vente
	 * 
	 * @param acheteur
	 * @throws RemoteException
	 * @return Produit : produit en vente
	 */
	public Produit demanderInscription(Acheteur acheteur) throws RemoteException;

	/**
	 * methode appelée quand un acheteur enchérit
	 * 
	 * @param idAcheteur
	 * @param produit
	 * @param prix
	 * @throws RemoteException
	 */
	public boolean encherir(String idAcheteur, Produit produit, double prix) throws RemoteException;

	/**
	 * methode appelée quand le temps du chronometre est écoulé chez un acheteur
	 * 
	 * @param idAcheteur
	 * @throws RemoteException
	 */
	public void tempsEcoule(String idAcheteur) throws RemoteException;

	/**
	 * Quand une vente est terminée les clients doivent valider la vente en
	 * appelant cette methode puis on passe à la vente suivante
	 * 
	 * @param idAcheteur
	 * @throws RemoteException
	 */
	public void validerVente(String idAcheteur) throws RemoteException;

}
