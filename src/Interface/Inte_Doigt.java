package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Interface.Inte_Accueil.EcouteurSupp;

public class Inte_Doigt extends JPanel{

	/* Panes */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panDoigts = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");

	/* Menu */
	/*
	 * private JMenuBar barreMenus = new JMenuBar();
	private JMenu accueil = new JMenu("Inte_Accueil");
	private JMenu doigts = new JMenu("Doigts");
	private JMenu balises = new JMenu("Balises");
	private JMenu equipes = new JMenu("Equipes");
	private JMenu epreuves = new JMenu("Epreuves");
	private JMenu classement = new JMenu("Classement");
	*/

	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents doigts");

	public Inte_Doigt() {

		/* Detail fenêtre */
		/*fen.setTitle("Raidzultats"); 	// + nom de la compétition
		fen.setSize(800, 600); 
		fen.setLocationRelativeTo(null);
		fen.setResizable(false);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// fen.setDefaultCloseOperation(Quitter());
		fen.setLayout(new BorderLayout());
		
		/* Menu 
		fen.setJMenuBar(barreMenus);
		barreMenus.add(accueil);
		barreMenus.add(doigts);
		barreMenus.add(balises);
		barreMenus.add(equipes);
		barreMenus.add(epreuves);
		barreMenus.add(classement);
		*/
		
		modif.setPreferredSize(new Dimension(200, 30));
		creer.setPreferredSize(new Dimension(200, 30));
		supp.setPreferredSize(new Dimension(200, 30));
		
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutModif = new JPanel();
		
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);
		
		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutCreer);

		panDoigts.setLayout(new BoxLayout(panDoigts, BoxLayout.PAGE_AXIS));
		panDoigts.setLayout(new BorderLayout());
		panDoigts.add(bjr, BorderLayout.NORTH);
		panDoigts.add(panBoutonsListe, BorderLayout.CENTER);

		panMega.add(panDoigts);
		this.add(panMega);
		
		//fen.setVisible(true);
		
		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);
		
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer ce doigt?",
					"Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				//Supprimer la comptetition de la BDD
			}

		}
	}
	
}
