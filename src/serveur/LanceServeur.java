package serveur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import commun.Parametres;

public class LanceServeur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LocateRegistry.createRegistry(Parametres.PORT_SERVEUR);
			IServeur iserveur = new Serveur();
			Serveur serveur =(Serveur)iserveur;
//			serveur.setProduitEnVente(new Produit("Telephone", 500, "ecran 5 pouces, 16go"));
			System.out.println("Enregistrement du serveur sur l'url : " + Parametres.URL_SERVEUR);
			Naming.bind(Parametres.URL_SERVEUR, serveur);
			System.out.println("Serveur lanc�");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
