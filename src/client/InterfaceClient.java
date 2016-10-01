package client;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
 

public class InterfaceClient extends JFrame{
	
	JButton signIn;
	JButton encherir;
	JTextField  price;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public InterfaceClient() {
		
		signIn = new JButton("S'inscrire ");
		
		encherir = new JButton("Enchérir");
		
		price = new JTextField ("", 10);
		
		 this.setSize(400, 100);
		 
	}



	public JPanel buildContentPane(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
 
		
		panel.add(signIn);
		
		panel.add(encherir);
		panel.add(price);
		
		price.setVisible(false);
		encherir.setVisible(false);
 
		return panel;
	}

	
	
	
	

}
