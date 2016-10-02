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
import java.rmi.server.UnicastRemoteObject;

import commun.Produit;
import serveur.IServeur;


public class Client extends UnicastRemoteObject implements IClient, Serializable {

	private Acheteur acheteur;
	private Produit prod;
	private static final long serialVersionUID = 1L;

	static InterfaceClient window;
	
	protected Client() throws RemoteException {
		super();
	}

	@Override
	public boolean demanderInscription() {
		return false;
	}

	@Override
	public void updatePrice(double prix, Acheteur winner) throws RemoteException
	{
		if(prod==null) prod  = new Produit("prod", prix, "Desc");
		prod.setPrix(prix);
		prod.setWinner(winner);
		System.out.println("Côté client: Nouveau prix ==> "+prix);
		window.getCurrrentPriceLabel().setText("Nouveau prix ==> "+prix);
	}
	
	public static void main(String[] args) {
		window = new  InterfaceClient();
		System.out.println("Lancement du client");
		try {
			int port = 9081;
			LocateRegistry.createRegistry(port);

			Acheteur acheteur = new Acheteur("client"+port, "test", "hfxndjh");
			IClient client  = new Client();
			
			//Il faut generer des ports differents pour chaque client et ainsi avoir des urls differents 
			String url ="//localhost:"+port+"/client";
			acheteur.setUrl(url);
			System.out.println("Enregistrement de l'objet avec l'url : " + url);
			Naming.bind(url, client);

			Remote r = Naming.lookup("//127.0.0.1:8000/vente");

			window.setTitle("Client"+port);
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

			Produit produit = ((IServeur) r).demanderInscription(acheteur);
			if (produit != null) {
				System.out.println("client : " + acheteur.getNom() + " enregistrï¿½!");

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

	public Acheteur getAcheteur() {
		return acheteur;
	}

	public void setAcheteur(Acheteur acheteur) {
		this.acheteur = acheteur;
	}
}
