package Interface;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * monAppli est la classe qui correspond à la seconde fenêtre elle est remplie
 * des différents onglets de l'application
 * 
 * @author Margaux
 * 
 */
public class Inte_monAppli {

	JFrame fen2 = new JFrame();
	JTabbedPane tabbedPane = new JTabbedPane();
	int index = 0;

	/**
	 * Classe principale
	 * 
	 * @param nomC
	 *            , le nom de la compétition étudiée
	 * @param idC
	 *            , l'id de la compétition étudiée
	 */
	public Inte_monAppli(String nomC, int idC) {

		final Inte_Doigt inte_Doigt = new Inte_Doigt(idC);
		final Inte_Balise inte_Balise = new Inte_Balise(idC);
		final Inte_Equipe inte_Equipe = new Inte_Equipe(idC);
		final Inte_Resultat inte_Resultat = new Inte_Resultat(idC);
		final Inte_Epreuves inte_Epreuves = new Inte_Epreuves(idC);
		final Inte_MalusBonus inte_MalusBonus = new Inte_MalusBonus(idC);
		final Inte_Menu inte_Menu = new Inte_Menu(fen2);

		fen2.setTitle("Raidzultats - " + nomC); // titre
		fen2.setSize(800, 600); // taille de la fenetre
		fen2.setLocationRelativeTo(null); // centre la fenetre
		fen2.setResizable(false);
		fen2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen2.setLayout(new BorderLayout()); // Pour les placements

		tabbedPane.add("Doigts", inte_Doigt);
		tabbedPane.add("Balises", inte_Balise);
		tabbedPane.add("Equipes", inte_Equipe);
		tabbedPane.add("Malus & Bonus", inte_MalusBonus);
		tabbedPane.add("Epreuves", inte_Epreuves);
		tabbedPane.add("Résultats", inte_Resultat);
		tabbedPane.add("Menu", inte_Menu);

		fen2.add(tabbedPane);

		fen2.setVisible(true);

		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
						.getSource();
				index = sourceTabbedPane.getSelectedIndex();
				System.out.println("Tab changed to: "
						+ sourceTabbedPane.getTitleAt(index) + " Index : "
						+ index);
				switch (index) {
				case 0:
					inte_Doigt.updateTable();
					System.out.println("Update Doigt");
					break;
				case 1:
					inte_Balise.updateTable();
					System.out.println("Update Balise");
					break;
				case 2:
					inte_Equipe.updateTable();
					System.out.println("Update Equipe");
					break;
				case 3:
					inte_MalusBonus.updateTable();
					System.out.println("Update MB");
					break;
				case 4:
					inte_Epreuves.updateTable();
					System.out.println("Update Epreuve");
					break;
				case 5:
					inte_Resultat.updateTable();
					System.out.println("Update Resultat");
					break;
				default:
					System.out.println("Pas d'Update");
					break;
				}
			}
		};
		tabbedPane.addChangeListener(changeListener);

	}

}
