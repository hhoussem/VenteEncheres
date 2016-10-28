package client;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import commun.Chronometre;
import commun.EtatAcheteur;
import commun.Parametres;
import serveur.IServeur;

public class FenetreEnchere extends JFrame implements ActionListener {

	JButton encherirBtn;
	JButton validerBtn;
	JTextField prixInput;
	JLabel prixEnchere;
	JLabel chronoAffichage;

	private Chronometre chrono;
	private int countChrono = 0;

	private IServeur serveur;

	private static final long serialVersionUID = 1L;

	public FenetreEnchere(String title, Remote remote) {
		initComponents();
		this.serveur = (IServeur) remote;
		encherirBtn.addActionListener(new EncherirActionListener(this, remote));
		validerBtn.addActionListener(new ValiderActionListener(this, remote));
		this.setTitle(title);
		chrono = new Chronometre(1000, this);
		chrono.startTimer();
	}

	public JPanel buildContentPane() {
		JPanel panel = new JPanel();
		JPanel msgPanel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(chronoAffichage);
		panel.add(prixInput);
		panel.add(encherirBtn);
		msgPanel.setLayout(new GridLayout());
		msgPanel.add(prixEnchere);
		panel.add(msgPanel);
		panel.add(validerBtn);
		prixInput.setVisible(false);
		encherirBtn.setVisible(false);
		return panel;
	}

	private void initComponents() {

		encherirBtn = new JButton("Encherir");
		validerBtn = new JButton("Valider");
		validerBtn.setVisible(false);
		prixInput = new JTextField("", 10);
		if (LanceClient.PRODUITENVENTE != null) {
			prixEnchere = new JLabel("Prix Enchere actuel est " + LanceClient.PRODUITENVENTE.getPrix());
		} else {
			prixEnchere = new JLabel("aucun produit en vente");
		}
		chronoAffichage = new JLabel("Chronometre: 00");
		this.setSize(425, 150);
		this.add(this.buildContentPane());
		encherirBtn.setVisible(true);
		prixInput.setVisible(true);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		countChrono++;
		chronoAffichage.setText("Chronometre: 0" + (Parametres.TEMPS_ATTENTE_ENCHERE - countChrono));
		if (countChrono >= Parametres.TEMPS_ATTENTE_ENCHERE) {
			try {
				serveur.tempsEcoule(LanceClient.ACHETEUR.getId());
				chronoAffichage.setText("TEMPS ECOULE!!");
				disalbeEncherir();
				LanceClient.ACHETEUR.setEtat(EtatAcheteur.TERMINE);
				chrono.stopTimer();
			} catch (RemoteException e1) {
				System.out.println("Erreur lors de la notification du serveur!");
			}
		}
	}
	
	/**
	 * Afficher le message quand une vente est terminée et que l'acheteur doit passer à la prochaine vent
	 * @param msg: message à afficher
	 * @param finEchere: vrai si c'est la derniere vent (fin de l'enchere) 
	 */
	public void setMessageVente(String msg, boolean finEnchere){
		prixEnchere.setText(msg);
		if(!finEnchere){
			validerBtn.setVisible(true);
		}else{
			validerBtn.setVisible(false);
		}
	}

	public Chronometre getChrono() {
		return chrono;
	}

	public void setCountChrono(int countChrono) {
		this.countChrono = countChrono;
	}

	public int getCountChrono() {
		return countChrono;
	}

	public JLabel getPrixEnchere() {
		return prixEnchere;
	}

	public void setPrixEnchere(JLabel prixEnchere) {
		this.prixEnchere = prixEnchere;
	}

	public JLabel getCurrrentPriceLabel() {
		return prixEnchere;
	}

	public void setEncherMessage(String msg) {
		prixEnchere.setText(msg);
	}

	public void enableEncherir() {
		encherirBtn.setEnabled(true);
		prixInput.setEditable(true);
	}

	public void disalbeEncherir() {
		encherirBtn.setEnabled(false);
		prixInput.setEditable(false);
	}

	public void mettreAjourPrix() {
		prixEnchere.setText("Prix Enchere actuel est " + LanceClient.PRODUITENVENTE.getPrix());
	}

}
