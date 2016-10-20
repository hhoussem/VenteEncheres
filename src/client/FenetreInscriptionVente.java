
	package client;

	import java.awt.Container;
import java.awt.GridLayout;
import java.rmi.Remote;

	import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import commun.Produit;
	import com.seaglasslookandfeel.*;

	public class FenetreInscriptionVente extends JFrame {

		JLabel textClient;
		JLabel textProduit;
		JButton buttonEnregistrer;

		public FenetreInscriptionVente(Remote r,Produit produit,Acheteur acheteur) {
			initComponents(produit,acheteur);
			//this.buttonEnregistrer.addActionListener(/*new InscriptionActionListener(this,r)*/);

		}

		private void initComponents(Produit produit,Acheteur acheteur) {

			this.setTitle("Inscription à la vente");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(400, 400);
			Container container = this.getContentPane();
			container.setLayout(new GridLayout(3, 1));
			// zone de texte
			this.textClient = new JLabel((acheteur.getId()+" "+acheteur.getPrenom()));
			this.textProduit = new JLabel(produit.getNom()+" "+produit.getDescription());
			// boutons
			this.buttonEnregistrer = new JButton("S'inscrire");
			// ajout des elements dans le conteneur
			container.add(textClient);
			container.add(textProduit);
			container.add(buttonEnregistrer);

			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);

		}

	}


