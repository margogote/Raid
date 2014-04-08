package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Accueil{
	
	JFrame fen = new JFrame();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panAccueil = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton entrerCompet = new JButton("Entrer dans la compétition");
	private JButton modifCompet = new JButton("Modifier la compétition");
	private JButton suppCompet = new JButton("Supprimer la compétition");
	private JButton creerCompet = new JButton("Creer une compétition");
	private JButton quitter = new JButton("Quitter");

	/* Liste des compet */
	// SELECT `nomCompetition` FROM `competition`
	// mettre ces noms dans la chaîne
	String[] competitions = { "compet1", "la 2é", "la 3é" };
	private JList<Object> compets = new JList<Object>(competitions);

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de gérer le classement d'un Raid");

	public Accueil() {

		fen.setTitle("Raidzultats"); // titre
		fen.setSize(800, 600); // taille de la fenetre
		fen.setLocationRelativeTo(null); // centre la fenetre
		fen.setResizable(false);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setLayout(new BorderLayout()); // Pour les placements

		entrerCompet.setPreferredSize(new Dimension(200, 30));
		modifCompet.setPreferredSize(new Dimension(200, 30));
		suppCompet.setPreferredSize(new Dimension(200, 30));
		creerCompet.setPreferredSize(new Dimension(200, 30));
		quitter.setPreferredSize(new Dimension(200, 30));
		
		JPanel panCompet = new JPanel();
		JPanel panBoutEntrer = new JPanel();
		JPanel panBoutModif = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutQ = new JPanel();

		panCompet.add(compets);
		panBoutEntrer.add(entrerCompet);
		panBoutModif.add(modifCompet);
		panBoutSupp.add(suppCompet);
		panBoutCreer.add(creerCompet);
		panBoutQ.add(quitter);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		// panBoutonsListe.setLayout(new BorderLayout());
		panBoutonsListe.add(panCompet);
		panBoutonsListe.add(panBoutEntrer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutQ);

		panAccueil.setLayout(new BoxLayout(panAccueil, BoxLayout.PAGE_AXIS));
		panAccueil.setLayout(new BorderLayout());
		panAccueil.add(bjr, BorderLayout.NORTH);
		panAccueil.add(panBoutonsListe, BorderLayout.CENTER);

		panMega.add(panAccueil);
		fen.add(panMega);

		fen.setVisible(true);

		/* Ecouteurs */
		EcouteurEntrer ecoutEntrer = new EcouteurEntrer();
		entrerCompet.addActionListener(ecoutEntrer);

		EcouteurModif ecoutModif = new EcouteurModif();
		modifCompet.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		suppCompet.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creerCompet.addActionListener(ecoutCreer);

		EcouteurQ ecoutQ = new EcouteurQ();
		quitter.addActionListener(ecoutQ);

	}

	public class EcouteurEntrer implements ActionListener { // Action du Entrer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet, on le stock
			// on lance la page suivante
			monAppli app = new monAppli();
			fen.dispose();
			//System.exit(0);
			//@SuppressWarnings("unused")
			
		}

	}

	public class EcouteurModif implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on lance le formulaire pré-remplit
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer cette compétition?",
					"Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				//Supprimer la comptetition de la BDD
			}

		}
	}
	

	public class EcouteurQ implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment quitter?",
					"Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				fen.dispose();
			}

		}
	}

	public class EcouteurCreer implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// On lance le formulaire vide
		}
	}


}
