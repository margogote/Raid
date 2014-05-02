package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Interface.Inte_Menu.EcouteurQ;

public class Inte_Equipe_CréaModif extends JFrame {
	JFrame thePanel = new JFrame();

	JLabel bjr = new JLabel(
			"Ici vous pouvez créer/modifier vous différentes équipes");

	JLabel nomL = new JLabel("Nom");
	JTextField nomT = new JTextField();

	JLabel doigtL = new JLabel("Doigt");
	private JComboBox<Object> doigtC = new JComboBox<Object>();

	JLabel dossartL = new JLabel("Dossart");
	JTextField dossartT = new JTextField();

	JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	JLabel grpL = new JLabel("Groupe");
	private String[] grpS = { "", "HEI", "Entreprise1", "Entreprise2" };
	private JComboBox<Object> grpC = new JComboBox<Object>(grpS);

	JLabel catL = new JLabel("Catégorie");
	private String[] categorie = { "", "Masculin", "Feminin", "Mixte" };
	private JComboBox<Object> catC = new JComboBox<Object>(categorie);

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	Inte_Equipe_CréaModif() {
		thePanel = this;
		
		thePanel.setTitle("Raidzultats"); // titre 
		thePanel.setSize(800,600); 
		// taille de la fenetre 
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel nomP = new JPanel();
		nomP.add(nomL);
		nomP.add(nomT);

		JPanel doigtP = new JPanel();
		doigtP.add(doigtL);

		JPanel dossartP = new JPanel();
		dossartP.add(dossartL);

		JPanel diffP = new JPanel();
		diffP.add(difficL);
		diffP.add(difficC);

		JPanel grpP = new JPanel();
		grpP.add(grpL);

		JPanel catP = new JPanel();
		catP.add(catL);
		catP.add(catC);

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(100, 100));
		megaP.setLayout(new GridLayout(6,2));
		//megaP.setLayout(new BoxLayout(megaP, BoxLayout.PAGE_AXIS));
		megaP.add(nomL);
		megaP.add(nomT);
		megaP.add(doigtL);
		megaP.add(doigtC);
		megaP.add(dossartL);
		megaP.add(dossartT);
		megaP.add(difficL);
		megaP.add(difficC);
		megaP.add(grpL);
		megaP.add(grpC);
		megaP.add(catL);
		megaP.add(catC);
		
		/*megaP.add(nomP);
		megaP.add(doigtP);
		megaP.add(dossartP);
		megaP.add(diffP);
		megaP.add(grpP);
		megaP.add(catP);*/
		
		JPanel gigaP = new JPanel();
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(bjr);
		gigaP.add(megaP);
		gigaP.add(btnP);
		
		thePanel.add(gigaP);
		
		thePanel.setVisible(true);
		
		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);
		
		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
		
	}

	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			
		}
	}
	
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

				thePanel.dispose();
		}
	}

}
