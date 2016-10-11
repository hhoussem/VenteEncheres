package client;

import java.awt.Container;
import java.awt.GridLayout;
import java.rmi.Remote;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FenetreInscription extends JFrame {

	JTextField textID;
	JTextField textNom;
	JTextField textPrenom;
	JButton buttonEnregistrer;
	JButton buttonAnnuler;
	JLabel messageSurLaVente;
	Container container;
	Container containerForm;

	public FenetreInscription(Remote r) {
		initComponents();
		this.buttonEnregistrer.addActionListener(new InscriptionActionListener(this,r));
		this.buttonAnnuler.addActionListener(new InscriptionActionListener(this,r));
		
	}

	private void initComponents() {

		this.setTitle("Nouveau acheteur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		container = this.getContentPane();
		containerForm = new Container();
		container.setLayout(new GridLayout(4, 2));
		containerForm.setLayout(new GridLayout(4, 2));
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
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	
	public void afficherMessageSurLaVente(String message){
		System.out.println("ICIIIIIII");
		containerForm.setVisible(false);
		messageSurLaVente.setText(message);
		messageSurLaVente.setVisible(true);		
	}

}
