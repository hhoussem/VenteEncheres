package client.ui;

import java.awt.Container;
import java.awt.GridLayout;
import java.rmi.Remote;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

import client.LanceClient;
import client.actionListeners.InscriptionActionListener;

import javax.swing.UIManager;

public class FenetreInscription extends JFrame {

	private static final long serialVersionUID = 1L;
	public JTextField textID;
	public JTextField textNom;
	public JTextField textPrenom;
	public JButton buttonEnregistrer;
	public JButton buttonAnnuler;
	JLabel messageSurLaVente;
	Container container;
	Container containerForm;

	public FenetreInscription(Remote r) {
		initComponents();
		this.buttonEnregistrer.addActionListener(new InscriptionActionListener(this, r));
		this.buttonAnnuler.addActionListener(new InscriptionActionListener(this, r));

	}

	private void initComponents() {

		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.setTitle("Nouveau acheteur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		container = this.getContentPane();
		containerForm = new Container();
		container.setLayout(new GridLayout(4, 4));
		containerForm.setLayout(new GridLayout(4, 4));
		// zone de texte
		this.textID = new JTextField();
		this.textNom = new JTextField();
		this.textPrenom = new JTextField();
		// boutons
		this.buttonEnregistrer = new JButton("Enregistrer");
		this.buttonAnnuler = new JButton("Annuler");
		// ajout des �l�ments dans le conteneur
		containerForm.add(new JLabel("  ID"));
		containerForm.add(this.textID);
		containerForm.add(new JLabel("  Nom"));
		containerForm.add(this.textNom);
		containerForm.add(new JLabel("  Prenom"));
		containerForm.add(this.textPrenom);
		containerForm.add(this.buttonAnnuler);
		containerForm.add(this.buttonEnregistrer);
		this.messageSurLaVente = new JLabel();
		messageSurLaVente.setVisible(false);
		container.add(containerForm);
		container.add(messageSurLaVente);
		// this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		if (LanceClient.ACHETEUR != null) {
			textID.setText(LanceClient.ACHETEUR.getId());
			textID.setEnabled(false);
		}

	}

	public void afficherMessageSurLaVente(String message) {
		System.out.println("message :" + message);
		containerForm.setVisible(false);
		messageSurLaVente.setText(message);
		messageSurLaVente.setVisible(true);
	}

}
