package Interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Accueil extends JFrame {
	// JFrame fen = new JFrame();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panAccueil = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton entrerCompet = new JButton("Entrer dans la comp�tition");
	private JButton modifCompet = new JButton("Modifier la comp�tition");
	private JButton suppCompet = new JButton("Supprimer la comp�tition");
	private JButton creerCompet = new JButton("Creer une comp�tition");
	private JButton quitter = new JButton("Quitter");

	/* Liste des compet */
	// SELECT `nomCompetition` FROM `competition`
	// mettre ces noms dans la cha�ne
	String[] competitions = { "compet1", "la 2�", "la 3�" };
	private JList<Object> compets = new JList<Object>(competitions);

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de g�rer le classement d'un Raid");

	public Accueil() {

		this.setTitle("Raidzultats"); // titre
		this.setSize(800, 600); // taille de la fenetre
		this.setLocationRelativeTo(null); // centre la fenetre
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout()); // Pour les placements

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
		this.add(panMega);

		this.setVisible(true);

		/* Ecouteurs */
		EcouteurEntrer ecoutEntrer = new EcouteurEntrer();
		entrerCompet.addActionListener(ecoutEntrer);

		EcouteurModif ecoutModif = new EcouteurModif();
		modifCompet.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		suppCompet.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creerCompet.addActionListener(ecoutCreer);

		// EcouteurQ ecoutQ = new EcouteurQ();
		// quitter.addActionListener(ecoutQ);

	}

	public class EcouteurEntrer implements ActionListener { // Action du Entrer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet, on le stock
			// on lance la page suivante
			
			//this.dispose;
			System.exit(0);
			@SuppressWarnings("unused")
			monAppli app = new monAppli();
		}

	}

	public class EcouteurModif implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on lance le formulaire pr�-remplit
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer cette comp�tition?",
					"Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				//Supprimer la comptetition de la BDD
			}

		}
	}

	public class EcouteurCreer implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// On lance le formulaire vide
		}
	}

	public String Quitter() { // Action du quitter
		int rep = 0;
		rep = JOptionPane.showConfirmDialog(null,
				"Voulez vous vraiment quitter?", "Attention",
				JOptionPane.YES_NO_OPTION);

		if (rep == 0) {
			this.dispose(); // Quitter les fenetres
		}
		return ("JFrame.EXIT_ON_CLOSE");
	}

}
