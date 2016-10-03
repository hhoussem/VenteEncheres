package client;

import java.awt.FlowLayout;
import java.rmi.Remote;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FenetreEnchere extends JFrame {

	JButton encherirBtn;
	JTextField prixInput;
	JLabel prixEnchere;

	private static final long serialVersionUID = 1L;

	public FenetreEnchere(String title,Remote remote) {

		initComponents();
		encherirBtn.addActionListener(new EncherirActionListener(this, remote));
		this.setTitle(title);
	

	}

	public JPanel buildContentPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(prixInput);
		panel.add(encherirBtn);
		panel.add(prixEnchere);
		prixInput.setVisible(false);
		encherirBtn.setVisible(false);

		return panel;
	}

	public JLabel getCurrrentPriceLabel() {
		return prixEnchere;
	}
	private void initComponents(){

		encherirBtn = new JButton("Enchérir");
		prixInput = new JTextField("", 10);
		prixEnchere = new JLabel("Prix Enchere actuel est: 0!");
		this.setSize(400, 100);
		this.add(this.buildContentPane());
		encherirBtn.setVisible(true);
		prixInput.setVisible(true);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
}
