package serveur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import commun.Parametres;

public class LanceServeur {

	public static void main(String[] args) {

		try {
			LocateRegistry.createRegistry(Parametres.PORT_SERVEUR);
			IServeur iserveur = new Serveur();
			Serveur serveur = (Serveur) iserveur;
			Naming.bind(Parametres.URL_SERVEUR, serveur);
			System.out.println("Enregistrement du serveur sur l'url : " + Parametres.URL_SERVEUR);
			System.out.println("Serveur lance");

			serveur.lancerLavente();
			int i = 0;
			while (i < serveur.getProduits().size()) {
				serveur.mettreAuxEcheres(serveur.getProduits().get(i));
				serveur.setIndexVenteEncours(i);
				i++;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
