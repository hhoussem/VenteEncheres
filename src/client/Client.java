package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import commun.Produit;
import serveur.IServeur;


public class Client implements IClient, Serializable {

	private String id;
	private String nom;
	private String prenom;
	private String etat; 
	private Produit prod;
	private static final long serialVersionUID = 1L;

	public Client(String id, String nom, String prenom) {

		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	@Override
	public boolean demanderInscription() {
		return false;
	}

	public void updatePrice(double prix, Client winner)
	{
		prod.setPrix(prix);
		prod.setWinner(winner);
	}
	public static void main(String[] args) {
		InterfaceClient window = new  InterfaceClient();
		System.out.println("Lancement du client");
		try {
			LocateRegistry.createRegistry(9000);

			Client client = new Client("test", "test", "hfxndjh");


			String url ="//localhost:9000/client";
			System.out.println("Enregistrement de l'objet avec l'url : " + url);
			Naming.bind(url, client);

			Remote r = Naming.lookup("//127.0.0.1:8000/vente");

			
			window.add(window.buildContentPane());
			window.setVisible(true);


			class CustomActionListener implements ActionListener
			{
				InterfaceClient window;
				IServeur r;
				public CustomActionListener(InterfaceClient window, Remote r)
				{
					this.window = window;
					this.r = (IServeur)r;

				}

				public void actionPerformed(ActionEvent e) {
					double prix = Double.parseDouble(window.price.getText());
					Produit pro = new Produit ("produit", 123654, "hfxndjh");

					try {
						r.encherir("test", pro, prix);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			
			window.encherir.addActionListener(new CustomActionListener(window, r));

			Produit produit = ((IServeur) r).demanderInscription(client);
			if (produit != null) {
				System.out.println("client : " + client.getNom() + " enregistré!");

				window.signIn.setVisible(false);
				window.encherir.setVisible(true);
				window.price.setVisible(true);
				

			} else {
				System.out.println("fail!");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (java.rmi.AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin du client");
	}
}
