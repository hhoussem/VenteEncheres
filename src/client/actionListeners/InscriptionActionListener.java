package client.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import client.Acheteur;
import client.LanceClient;
import client.ui.FenetreInscription;
import commun.Produit;
import serveur.IServeur;

public class InscriptionActionListener implements ActionListener {

	FenetreInscription fenetreInscription;
	IServeur remoteServeur;

	public InscriptionActionListener(FenetreInscription window, Remote r) {
		this.fenetreInscription = window;
		this.remoteServeur = (IServeur) r;
	}

	private void enregistrerAcheteur(Acheteur acheteur) {
		System.out.println("Lancement de l'acheteur");
		Produit produit = null;
		try {
			LanceClient.ACHETEUR = acheteur;
			produit = remoteServeur.demanderInscription(acheteur);
			if (produit != null) {
				LanceClient.PRODUITENVENTE = produit;
				// new FenetreInscriptionVente(this.remoteServeur, produit,//
				// acheteur);
			} else {
				fenetreInscription.afficherMessageSurLaVente("Vente en attente!");
			}

		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Override
	synchronized public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fenetreInscription.buttonEnregistrer)) {
			if (fenetreInscription.textID.getText().equals("") || fenetreInscription.textPrenom.getText().equals("")
					|| fenetreInscription.textNom.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "vous devez remplir tous les champs!");
			} else {
				Acheteur acheteur = LanceClient.ACHETEUR;
				acheteur.setNom(fenetreInscription.textNom.getText());
				acheteur.setPrenom(fenetreInscription.textPrenom.getText());
				enregistrerAcheteur(acheteur);
			}
		}
		if (e.getSource().equals(fenetreInscription.buttonAnnuler)) {
			System.out.println("Fin du acheteur");
			fenetreInscription.dispose();
		}
	}

}
