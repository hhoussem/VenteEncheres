package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import commun.Produit;
import serveur.IServeur;

public class InscriptionActionListener implements ActionListener{
	FenetreInscription fenetreInscription;
	IServeur remoteServeur;
	
	public InscriptionActionListener(FenetreInscription window,Remote r) {
		this.fenetreInscription = window;
		this.remoteServeur = (IServeur) r;
	}
	
	private void enregistrerAcheteur(Acheteur acheteur) {
		System.out.println("Lancement de l'acheteur");
		Produit produit = null;
		boolean venteLancee = false;
		try {
			LanceClient.ACHETEUR = acheteur;
			produit = remoteServeur.demanderInscription(acheteur);
			if(produit != null){
				LanceClient.PRODUITENVENTE = produit;				
			}else{
				fenetreInscription.afficherMessageSurLaVente("Vente en attente!");
			}
			
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		if (produit != null) {
			System.out.println("acheteur : " + acheteur.getNom() + " enregistr� avec succ�s");
		} else {
			System.out.println("enregistrement du acheteur �chou�!");
		}

		
	}
	
	@Override
	synchronized public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fenetreInscription.buttonEnregistrer)) {
			if (fenetreInscription.textID.getText().equals("") || fenetreInscription.textPrenom.getText().equals("")
					|| fenetreInscription.textNom.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "vous devez remplir tous les champs!");
			} else {

				enregistrerAcheteur(new Acheteur(fenetreInscription.textID.getText(), fenetreInscription.textNom.getText(), fenetreInscription.textPrenom.getText()));
			}
		}
		if (e.getSource().equals(fenetreInscription.buttonAnnuler)) {
			System.out.println("Fin du acheteur");
			fenetreInscription.dispose();
		}
	}

}


