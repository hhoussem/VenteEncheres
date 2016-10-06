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

	public static Acheteur ACHETEUR;
	public static Produit PRODUITENVENTE;

	 FenetreInscription fenetreInsciption;
	 FenetreEnchere fenetreEnchere;
	 
	private static final long serialVersionUID = 1L;

	
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
		if(PRODUITENVENTE==null){
			PRODUITENVENTE  = new Produit("prod", prix, "Desc");
		}
		PRODUITENVENTE.setPrix(prix);
		PRODUITENVENTE.setWinner(winner);
		System.out.println("Côté client: Nouveau prix ==> "+prix);
		fenetreEnchere.getPrixEnchere().setText("Gagnant: "+LanceClient.ACHETEUR.getId()+"  Prix:"+LanceClient.PRODUITENVENTE.getPrix());
		//A determiner quand on re-initialise le chronometre
		fenetreEnchere.setCountChrono(0);
	}

	public static void main(String[] args) {
		try {
			LanceClient client  = new LanceClient();
			client.executer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void executer() {
		
		System.out.println("Lancement du client");
		try {
			
			Random rand = new Random();
			int port =  rand.nextInt(60000-10000) + 10000;
			LocateRegistry.createRegistry(port);

			Acheteur acheteur = new Acheteur("client"+port, "testnom", "test1");
			
			
			//Il faut generer des ports differents pour chaque client et ainsi avoir des urls differents 
			String url ="//localhost:"+port+"/client";
			acheteur.setUrl(url);
			LanceClient.ACHETEUR = acheteur;
			
			System.out.println("Enregistrement de du client avec l'url : " + url);
			Naming.bind(url, this);

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

	@Override
	public void venterTerminee(double prix, Acheteur winner) throws RemoteException {
		String msg = "VENTE TERMINEE, PRIX="+prix+" GAGNANT: "+winner.getNom();
		System.out.println(msg);
		fenetreEnchere.getPrixEnchere().setText(msg);
	}
}

