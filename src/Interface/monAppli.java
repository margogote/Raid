package Interface;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class monAppli {

	JFrame fen = new JFrame();

	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panAccueil = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton entrerCompet = new JButton("Entrer dans la comp�tition");
	private JButton mondifCompet = new JButton("Modifier la comp�tition");
	private JButton suppCompet = new JButton("Supprimer la comp�tition");
	private JButton creerCompet = new JButton("Creer une comp�tition");
	
	
	String[] competitions = { "compet1", "la 2�", "la 3�" };
	private JList compets = new JList(competitions);

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de g�rer le classement d'un Raid");

	public monAppli() {

		Accueil Accueil = new Accueil();

	}

}
