package client;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import commun.Parametres;
import commun.Produit;
import serveur.IServeur;

public class FenetreClient extends JFrame implements ActionListener {

	private Client client = null;
	private IServeur serveur = null;

	public FenetreClient() {
		initComponents();
		System.out.println("Lancement du client");
		try {
			Remote remote = Naming.lookup(Parametres.URL);
			serveur = (IServeur) remote;
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void enregistrerClient(Client client) {

		Produit produit = null;
		try {
			produit = serveur.demanderInscription(client);
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		if (produit != null) {
			System.out.println("client : " + client.getNom() + " enregistré avec succés");
		} else {
			System.out.println("enregistrement du client échoué!");
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FenetreClient m_fenetre = new FenetreClient();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonEnregistrer)) {
			if (this.textID.getText().equals("") || this.textPrenom.getText().equals("")
					|| this.textNom.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "vous devez remplir tous les champs!");
			} else {

				enregistrerClient(new Client(textID.getText(), textNom.getText(), textPrenom.getText()));
			}
		}
		if (e.getSource().equals(buttonAnnuler)) {
			System.out.println("Fin du client");
			this.dispose();
		}
	}

	private void initComponents() {

		this.setTitle("Nouveau client");
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
		this.buttonEnregistrer.addActionListener(this);
		this.buttonAnnuler = new JButton("Annuler");
		this.buttonAnnuler.addActionListener(this);
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public IServeur getServeur() {
		return serveur;
	}

	public void setServeur(IServeur serveur) {
		this.serveur = serveur;
	}

	// composants graphiques
	private JTextField textID;
	private JTextField textNom;
	private JTextField textPrenom;
	private JButton buttonEnregistrer;
	private JButton buttonAnnuler;

}
