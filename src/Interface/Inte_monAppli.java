package Interface;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
/**
 * monAppli est la classe qui correspond à la seconde fenêtre
 * elle est contituée de différents onglets
 * 
 * @author Margaux
 *
 */
public class Inte_monAppli {
	
	JFrame fen2 = new JFrame();
	JTabbedPane tabbedPane = new JTabbedPane();

	public Inte_monAppli(String nomC, int idC) {
		
		
		Inte_Doigt inte_Doigt = new Inte_Doigt(idC);
		Inte_Balise inte_Balise = new Inte_Balise(idC);
		Inte_Equipe inte_Equipe = new Inte_Equipe(idC);
		Inte_Resultat inte_Resultat = new Inte_Resultat(idC);
		Inte_Epreuves inte_Epreuves = new Inte_Epreuves(idC);
		Inte_MalusBonus inte_MalusBonus =new Inte_MalusBonus(idC);
		Inte_Menu inte_Menu = new Inte_Menu(fen2);


		fen2.setTitle("Raidzultats - " +nomC); // titre
		fen2.setSize(800, 600); // taille de la fenetre
		fen2.setLocationRelativeTo(null); // centre la fenetre
		fen2.setResizable(false);
		fen2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen2.setLayout(new BorderLayout()); // Pour les placements

		tabbedPane.add("Doigts",inte_Doigt);
		tabbedPane.add("Balises",inte_Balise);
		tabbedPane.add("Equipes",inte_Equipe);
		tabbedPane.add("Malus & Bonus",inte_MalusBonus);
		tabbedPane.add("Epreuves",inte_Epreuves);
		tabbedPane.add("Résultats",inte_Resultat);
		tabbedPane.add("Menu", inte_Menu);
		
		fen2.add(tabbedPane);

		fen2.setVisible(true);
		
	}

}
