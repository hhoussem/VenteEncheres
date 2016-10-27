package serveur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import commun.Parametres;
import commun.Produit;

public class LanceServeur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LocateRegistry.createRegistry(Parametres.PORT_SERVEUR);
			IServeur iserveur = new Serveur();
			Serveur serveur =(Serveur)iserveur;
			Naming.bind(Parametres.URL_SERVEUR, serveur);

			serveur.lancerLavente();
			List<Produit> produits = new ArrayList<Produit>();
			produits.add(new Produit("Telephone", 500, "ecran 5 pouces, 16go"));
			produits.add(new Produit("Ordinateur Portable", 150, "DELL 15pources, 4go, 1To"));
			produits.add(new Produit("Samsung S6", 500, "ecran 5.5 pouces, 3go"));
			
			int i =0;
			while(i<produits.size()){
				System.out.println("Mise au enchere du produit : "+produits.get(i));
				serveur.mettreAuxEcheres(produits.get(i));
				i++;
			}
			System.out.println("Enregistrement du serveur sur l'url : " + Parametres.URL_SERVEUR);
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
