package Interface;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class monAppli {

	JFrame fen2 = new JFrame();
	JTabbedPane tabbedPane = new JTabbedPane();
	
	Doigt doigt = new Doigt();
	Balise balise = new Balise();
	Competition competition = new Competition();
	Epreuve epreuve = new Epreuve();
	MalusBonus malusBonus =new MalusBonus();
	Menu menu = new Menu();

	/*private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panAccueil = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	/*private JButton entrerCompet = new JButton("Entrer dans la compétition");
	private JButton mondifCompet = new JButton("Modifier la compétition");
	private JButton suppCompet = new JButton("Supprimer la compétition");
	private JButton creerCompet = new JButton("Creer une compétition");
	*/
	
	//String[] competitions = { "compet1", "la 2é", "la 3é" };
	//private JList compets = new JList(competitions);

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de gérer le classement d'un Raid");

	public monAppli() {

		fen2.setTitle("Raidzultats"); // titre
		fen2.setSize(800, 600); // taille de la fenetre
		fen2.setLocationRelativeTo(null); // centre la fenetre
		fen2.setResizable(false);
		fen2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen2.setLayout(new BorderLayout()); // Pour les placements

		tabbedPane.add("Doigts",doigt);
		tabbedPane.add("Balises",balise);
		tabbedPane.add("Epreuves",epreuve);
		tabbedPane.add("Competitions",competition);
		tabbedPane.add("Malus & Bonus",malusBonus);
		tabbedPane.add("Menu", menu);
		
		fen2.add(tabbedPane);

		fen2.setVisible(true);
		
	}

}
