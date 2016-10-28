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

	IServeur remoteServer;

	private static final long serialVersionUID = 1L;

	protected LanceClient() throws RemoteException {
		super();
	}

	public static void main(String[] args) {

		// new FenetreEnchere("TEST", null);
		// if(true) return;

		try {
			LanceClient client = new LanceClient();
			client.run();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		initClient();
		fenetreInsciption = new FenetreInscription(remoteServer);
	}

	@Override
	public void notifierNouvelleVente(Produit produit) throws RemoteException {
		if (fenetreEnchere == null) {
			if(fenetreInsciption != null){
			fenetreInsciption.dispose();
			}
			fenetreEnchere = new FenetreEnchere("ENCHERE " + ACHETEUR.getId() + " - " + ACHETEUR.getNom(),
					remoteServer);
		}
		if(fenetreInsciption!=null) {
			fenetreInsciption.dispose();
		}
		PRODUITENVENTE = produit;
		fenetreEnchere.setEncherMessage("Produit en cours de Vente :" + produit.getNom() + "/"
				+ produit.getDescription() + " Prix initial:" + produit.getPrix());
	}

	@Override
	public void updatePrice(double prix, Acheteur winner) throws RemoteException {
		if (PRODUITENVENTE == null) {
			PRODUITENVENTE = new Produit("prod", prix, "Desc");
		}
		PRODUITENVENTE.setPrix(prix);
		PRODUITENVENTE.setWinner(winner);
		System.out.println("client: Nouveau prix ==> " + prix);
		fenetreEnchere.getPrixEnchere().setText("Gagnant: " + LanceClient.PRODUITENVENTE.getWinner().getId() + "  Prix:"
				+ LanceClient.PRODUITENVENTE.getPrix());
		// A determiner quand on re-initialise le chronometre
		if(ACHETEUR.getId().equals(winner.getId())) fenetreEnchere.setCountChrono(0);
	}

	@Override
	public synchronized void venteTerminee(double prix, Acheteur winner) throws RemoteException {
		if (winner != null) {
			String msg = "VENTE TERMINEE, PRIX=" + prix + " GAGNANT: " + winner.getNom();
			System.out.println(msg);
			fenetreEnchere.getPrixEnchere().setText(msg);
		}
	}

	public void initClient() {
		try {
			// Initialisatoin du client
			Random rand = new Random();
			int port = rand.nextInt(60000 - 10000) + 10000;
			LocateRegistry.createRegistry(port);
			Acheteur acheteur = new Acheteur("client" + port, "", "");
			String url = "//localhost:" + port + "/client";
			acheteur.setUrl(url);
			LanceClient.ACHETEUR = acheteur;
			Naming.bind(url, this);

			// connexion au serveur
			remoteServer = (IServeur) Naming.lookup(Parametres.URL_SERVEUR);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (java.rmi.AlreadyBoundException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Serveur Introuvable");
			e.printStackTrace();
		}

	}

}
