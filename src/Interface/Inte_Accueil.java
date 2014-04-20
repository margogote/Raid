package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Inte_Accueil{
	
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
	private JButton entrerCompet = new JButton("Entrer dans la comp�tition");
	private JButton modifCompet = new JButton("Modifier la comp�tition");
	private JButton suppCompet = new JButton("Supprimer la comp�tition");
	private JButton creerCompet = new JButton("Cr�er une comp�tition");
	private JButton quitter = new JButton("Quitter");

	/* Liste des compet */
	// SELECT `nomCompetition` FROM `inte_Competition`
	// mettre ces noms dans la cha�ne
	String[] competitions = { "compet1", "la 2�", "la 3�" };
	private JComboBox<Object> compets = new JComboBox<Object>(competitions);

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de g�rer le classement d'un Raid");

	public Inte_Accueil() {

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
	

	public class EcouteurCreer implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// On lance le formulaire vide
			
			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nom = jop.showInputDialog(null, "Donner le nom de votre comp�tition !", "Nouvelle comp�tition ?", JOptionPane.QUESTION_MESSAGE);
			if(nom == null)
			{
				jop2.showMessageDialog(null, "Comp�tition non cr��e", "Comp�tition non cr��e!", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				jop2.showMessageDialog(null, "La comp�tition est " + nom, "Nouvelle comp�tition !", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public class EcouteurModif implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on lance le formulaire pr�-remplit
			
			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nom = jop.showInputDialog(null, "Donner le nouveau nom de votre competition !", "Modifier comp�tition ?", JOptionPane.QUESTION_MESSAGE);
			if(nom == null){
			jop2.showMessageDialog(null, "Pas de modification", "Comp�tition non modifi�e!", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
			jop2.showMessageDialog(null, "La competition est maintenant" + nom, "Comp�tition modifi�e!", JOptionPane.INFORMATION_MESSAGE);	
			}
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
}
