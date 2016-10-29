package client.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import client.LanceClient;
import client.ui.FenetreEnchere;
import commun.Produit;
import serveur.IServeur;

public class EncherirActionListener implements ActionListener {
	FenetreEnchere fenEnchere;
	IServeur remote;

	public EncherirActionListener(FenetreEnchere window, Remote r) {
		this.fenEnchere = window;
		this.remote = (IServeur) r;
	}

	synchronized public void actionPerformed(ActionEvent e) {
		double prix = 0;
		try {
			prix = Double.parseDouble(fenEnchere.prixInput.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "prix invalide !");

		}
		fenEnchere.prixInput.setText("");
		Produit produit = LanceClient.PRODUITENVENTE;
		String idAcheteur = LanceClient.ACHETEUR.getId();

		try {
			remote.encherir(idAcheteur, produit, prix);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

}