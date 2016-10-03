package client;

import java.awt.FlowLayout;

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

	public FenetreEnchere() {

		encherirBtn = new JButton("Enchérir");
		prixInput = new JTextField("", 10);
		prixEnchere = new JLabel("Prix Enchere actuel est: 0!");
		this.setSize(400, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

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

}
