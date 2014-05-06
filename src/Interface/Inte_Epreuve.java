package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Interface.Inte_Equipe.EcouteurCreer;
import Interface.Inte_Equipe.EcouteurModif;
import Interface.Inte_Equipe.EcouteurSupp;
import Models.TabModel;

public class Inte_Epreuve extends JPanel {
	/**
	 * Attention là c'est un test pour la page création/modif épreuve
	 * 
	 */
	/* Panels */
	private JPanel thePanel;
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panTitre = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	JPanel panBoutCreer = new JPanel();
	JPanel panBoutSupp = new JPanel();
	JPanel panBoutModif = new JPanel();

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");

	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idEquipe", "Nom d'équipe",
			"Nom du groupe", "Difficulté", "Type d'équipe" };

	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différentes équipes");

	public Inte_Epreuve() {

		thePanel = this;

		/*
		 * data = updateTable();
		 * 
		 * EcouteurModif ecoutModif = new EcouteurModif();
		 * modif.addActionListener(ecoutModif);
		 * 
		 * EcouteurSupp ecoutSupp = new EcouteurSupp();
		 * supp.addActionListener(ecoutSupp);
		 * 
		 * EcouteurCreer ecoutCreer = new EcouteurCreer();
		 * creer.addActionListener(ecoutCreer);
		 */
		thePanel.removeAll();
		panMega.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		panTitre.setLayout(new BoxLayout(panTitre, BoxLayout.PAGE_AXIS));
		panTitre.setLayout(new BorderLayout());
		// panTitre.add(bjr, BorderLayout.NORTH);
		panTitre.add(panBoutonsListe, BorderLayout.WEST);

		/*
		 * tabModel = new TabModel(data, title);
		 * 
		 * // Nous ajoutons notre tableau à notre contentPane dans un scroll //
		 * Sinon les titres des colonnes ne s'afficheront pas !
		 * 
		 * tableau = new JTable(tabModel); tableau.setRowHeight(30);
		 */

		panMega.setLayout(new BorderLayout());
		panMega.add(bjr, BorderLayout.NORTH);
		panMega.add(panTitre, BorderLayout.WEST);
		// panMega.add(new JScrollPane(tableau), BorderLayout.CENTER);

		thePanel.add(panMega);

	}
}
