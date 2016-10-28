package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import serveur.IServeur;

class ValiderActionListener implements ActionListener {
	FenetreEnchere fenEnchere;
	IServeur remote;

	public ValiderActionListener(FenetreEnchere window, Remote r) {
		this.fenEnchere = window;
		this.remote = (IServeur) r;
	}

	synchronized public void actionPerformed(ActionEvent e) {
		try {
			fenEnchere.setMessageVente("En attente des validations des autres acheteurs!", true);
			remote.validerVente(LanceClient.ACHETEUR.getId());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

}