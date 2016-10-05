package commun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
public abstract class Listenable extends UnicastRemoteObject {
	
	private static final long serialVersionUID = 1123L;

	protected Listenable() throws RemoteException {
		super();
	}

	List<ActionListener> listeners = new ArrayList<ActionListener>();
	List<ActionListener> venteTermineeListeners = new ArrayList<ActionListener>();
	List<ActionListener> updatePriceListeners = new ArrayList<ActionListener>();
	
	public void addActionListener(ActionListener listener){
		listeners.add(listener);
	}
	
	public void addVenteTermineeListener(ActionListener listener){
		venteTermineeListeners.add(listener);
	}
	
	public void addUpdatePriceListeners(ActionListener listener){
		updatePriceListeners.add(listener);
	}
	
	public void notifyUpdatePriceListeners(){
		for(ActionListener listener: updatePriceListeners){
			listener.actionPerformed(new ActionEvent(this, Command.UPDATE_PRICE, null));
		}
	}
	
	public void notifyVenteTermineeListener(String msg){
		for(ActionListener listener: venteTermineeListeners){
			listener.actionPerformed(new ActionEvent(this, Command.VENTE_TERMINEE, msg));
		}
	}
	
	public void notifyListeners(){
		for(ActionListener listener: listeners){
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}
	}

}
