package Interface;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Inte_monAppli {

	JFrame fen2 = new JFrame();
	JTabbedPane tabbedPane = new JTabbedPane();

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

	public Inte_monAppli(String nomC, int idC) {
		
		
		Inte_Doigt inte_Doigt = new Inte_Doigt(idC);
		Inte_Balise inte_Balise = new Inte_Balise(idC);
		Inte_Equipe inte_Equipe = new Inte_Equipe(idC);
		Inte_Resultat inte_Resultat = new Inte_Resultat(idC);
		Inte_Epreuve inte_Epreuve = new Inte_Epreuve(idC);
		Inte_MalusBonus inte_MalusBonus =new Inte_MalusBonus(idC);
		Inte_Menu inte_Menu = new Inte_Menu(fen2);


		fen2.setTitle("Raidzultats - " +nomC); // titre
		//fen2.setTitle("Raidzultats - "+nomCompet);
		fen2.setSize(800, 600); // taille de la fenetre
		fen2.setLocationRelativeTo(null); // centre la fenetre
		fen2.setResizable(false);
		fen2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen2.setLayout(new BorderLayout()); // Pour les placements

		tabbedPane.add("Doigts",inte_Doigt);
		tabbedPane.add("Balises",inte_Balise);
		tabbedPane.add("Equipes",inte_Equipe);
		tabbedPane.add("Malus & Bonus",inte_MalusBonus);
		tabbedPane.add("Epreuves",inte_Epreuve);
		tabbedPane.add("Résultats",inte_Resultat);
		tabbedPane.add("Menu", inte_Menu);
		
		fen2.add(tabbedPane);

		fen2.setVisible(true);
		
	}

}
