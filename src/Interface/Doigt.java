package Interface;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Doigt extends JPanel{

	/* Panes */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panDoigts = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modifDoigt = new JButton("Modifier");
	private JButton suppDoigt = new JButton("Supprimer");
	private JButton creerDoigt = new JButton("Creer");

	/* Menu */
	private JMenuBar barreMenus = new JMenuBar();
	private JMenu accueil = new JMenu("Accueil");
	private JMenu doigts = new JMenu("Doigts");
	private JMenu balises = new JMenu("Balises");
	private JMenu equipes = new JMenu("Equipes");
	private JMenu epreuves = new JMenu("Epreuves");
	private JMenu classement = new JMenu("Classement");

	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents doigts");

	public Doigt() {

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
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutModif = new JPanel();
		
		panBoutCreer.add(creerDoigt);
		panBoutSupp.add(suppDoigt);
		panBoutModif.add(modifDoigt);
		
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
	}

}
