package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	public boolean demanderInscription() throws RemoteException;
	public void updatePrice(double prix, Acheteur winner) throws RemoteException;
	public void venterTerminee(double prix, Acheteur winner) throws RemoteException;
}
