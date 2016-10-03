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
		try {
			produit = remoteServeur.demanderInscription(acheteur);
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		if (produit != null) {
			System.out.println("acheteur : " + acheteur.getNom() + " enregistré avec succés");
		} else {
			System.out.println("enregistrement du acheteur échoué!");
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


