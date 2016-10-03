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

	public FenetreInscription(Remote r) {
		initComponents();
		this.buttonEnregistrer.addActionListener(new InscriptionActionListener(this,r));
		this.buttonAnnuler.addActionListener(new InscriptionActionListener(this,r));
		
	}

	private void initComponents() {

		this.setTitle("Nouveau acheteur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		Container container = this.getContentPane();
		container.setLayout(new GridLayout(4, 2));
		// zone de texte
		this.textID = new JTextField();
		this.textNom = new JTextField();
		this.textPrenom = new JTextField();
		// boutons
		this.buttonEnregistrer = new JButton("Enregistrer");
		this.buttonAnnuler = new JButton("Annuler");
		// ajout des éléments dans le conteneur
		container.add(new JLabel("  ID"));
		container.add(this.textID);
		container.add(new JLabel("  Nom"));
		container.add(this.textNom);
		container.add(new JLabel("  Prenom"));
		container.add(this.textPrenom);
		container.add(this.buttonAnnuler);
		container.add(this.buttonEnregistrer);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

}
