package client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import commun.Parametres;
import commun.Produit;
import serveur.IServeur;


public class LanceClient extends UnicastRemoteObject implements IClient, Serializable {

	private Acheteur acheteur;
	private Produit produitEnVente;
	private static final long serialVersionUID = 1L;

	static FenetreInscription fenetreInsciption;
	static FenetreEnchere fenetreEnchere;
	
	protected LanceClient() throws RemoteException {
		super();
	}

	@Override
	public boolean demanderInscription() {
		return false;
	}

	@Override
	public void updatePrice(double prix, Acheteur winner) throws RemoteException
	{
		if(produitEnVente==null){
			produitEnVente  = new Produit("prod", prix, "Desc");
		}
		produitEnVente.setPrix(prix);
		produitEnVente.setWinner(winner);
		System.out.println("Côté client: Nouveau prix ==> "+prix);
		fenetreEnchere.getCurrrentPriceLabel().setText("Nouveau prix ==> "+prix);
	}
	
	public static void main(String[] args) {
		
		System.out.println("Lancement du client");
		try {
			Random rand = new Random();
			int port =  rand.nextInt(60000-10000) + 10000;
			LocateRegistry.createRegistry(port);

			Acheteur acheteur = new Acheteur("client"+port, "testnom", "test1");
			IClient client  = new LanceClient();
			
			//Il faut generer des ports differents pour chaque client et ainsi avoir des urls differents 
			String url ="//localhost:"+port+"/client";
			acheteur.setUrl(url);
			
			System.out.println("Enregistrement de du client avec l'url : " + url);
			Naming.bind(url, client);

			Remote remoteServer = Naming.lookup(Parametres.URL_SERVEUR);
			//fenetreInsciption = new FenetreInscription(remoteServer);
			fenetreEnchere = new  FenetreEnchere("Client"+port,remoteServer);
			
			Produit produit = ((IServeur) remoteServer).demanderInscription(acheteur);
			if (produit != null) {
				System.out.println("client : " + acheteur.getNom() + " enregistré");

			} else {
				System.out.println("fail!");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (java.rmi.AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin du client");
	}

	public Acheteur getAcheteur() {
		return acheteur;
	}

	public void setAcheteur(Acheteur acheteur) {
		this.acheteur = acheteur;
	}
}

