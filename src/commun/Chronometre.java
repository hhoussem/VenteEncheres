package commun;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Chronometre extends Timer{

	private static final long serialVersionUID = 1L;

	public Chronometre(int delay, ActionListener listener) {
		super(delay, listener);
	}

		public void startTimer ()
		{	this.start ();
		}
		
		public void stopTimer ()
		{	this.stop ();
		}
		
		
		public boolean isRunning ()
		{	return ( this.isRunning () );
		}
}
