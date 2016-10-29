package client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import client.ui.FenetreEnchere;
import client.ui.FenetreInscription;
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
		try {
			LanceClient client = new LanceClient();
			client.run();
		} catch (RemoteException e) {
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
			if (fenetreInsciption != null) {
				fenetreInsciption.dispose();
			}
			fenetreEnchere = new FenetreEnchere("ENCHERE " + ACHETEUR.getId() + " - " + ACHETEUR.getNom(),
					remoteServer);
		}
		if (fenetreInsciption != null) {
			fenetreInsciption.dispose();
		}
		PRODUITENVENTE = produit;
		fenetreEnchere.setEncherMessage("Produit en cours de Vente :" + produit.getNom() + "/"
				+ produit.getDescription() + " Prix initial:" + produit.getPrix());
		fenetreEnchere.setCountChrono(0);
		fenetreEnchere.getChrono().restart();
		fenetreEnchere.enableEncherir();
	}

	@Override
	public void modifierPrix(double prix, Acheteur dernierEncherisseur) throws RemoteException {
		if (PRODUITENVENTE == null) {
			System.out.println("erreur aucun produit en vente !");
		}
		PRODUITENVENTE.setPrixEnchere(prix);
		PRODUITENVENTE.setDernierEncherisseur(dernierEncherisseur);
		System.out.println("client: Nouveau prix ==> " + prix);
		fenetreEnchere.getPrixEnchere()
				.setText("Gagnant: " + LanceClient.PRODUITENVENTE.getDernierEncherisseur().getId() + "  Prix:"
						+ LanceClient.PRODUITENVENTE.getPrix());
		// On reinitialise le chronometre du client qui vient d'encherir
		if (ACHETEUR.getId().equals(dernierEncherisseur.getId()))
			fenetreEnchere.setCountChrono(0);
	}

	@Override
	public synchronized void venteTerminee(double prix, Acheteur dernierEncherisseur, Produit prochainProduit)
			throws RemoteException {
		boolean finEnchere = prochainProduit == null;
		String msg = "";
		fenetreEnchere.disalbeEncherir();
		fenetreEnchere.getChrono().stopTimer();
		if (dernierEncherisseur != null) {
			msg = "<html><b>VENTE TERMINEE, PRIX=" + prix + " GAGNANT: " + dernierEncherisseur.getNom()
					+ "</b><br/><br/>";
		} else {
			msg = "<html><b>VENTE TERMINEE, AUCUN GAGNANT!</b><br/><br/>";
		}
		if (!finEnchere) {
			msg += "<p>PRODUIT SUIVANT: " + prochainProduit.getNom() + " - " + prochainProduit.getDescription()
					+ "!</p></html>";
		} else {
			msg += "ENCHERE TERMINEE! BYE BYE!! </html>";
		}
		fenetreEnchere.setMessageVente(msg, finEnchere);
	}

	public void initClient() {
		try {
			// Initialisatoin du client , selection d'un port 
			//aléatoire entre 10000 et 60000
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
