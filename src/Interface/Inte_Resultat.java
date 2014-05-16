package Interface;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import BDD.DataSourceProvider;
import Interface.Inte_Epreuve_Modif.EcouteurPlus;
import Models.TabModel;

/**
 * Onglet Resultat : Permet de générer un classement en fonction de différents
 * filtres, permet(ra) de générer un PDF résumant ce classement et de l'imprimer
 * 
 * @author Margaux
 * 
 */
public class Inte_Resultat extends JPanel {

	/* Boutons Radio */
	private ButtonGroup bG = new ButtonGroup();
	private JRadioButton epreuveR = new JRadioButton("Epreuve");
	private JRadioButton jourR = new JRadioButton("Journée");
	private JRadioButton generalR = new JRadioButton("Général");

	/* Combo */
	private String[] grpS = { "CHOISIR", "Etudiant", "Salarié" };
	private String[] diffS = { "CHOISIR", "Aventure", "Expert" };
	private String[] catS = { "CHOISIR", "Masculin", "Feminin", "Mixte" };

	private JComboBox<String> epreuveC = new JComboBox<String>();
	private JComboBox<String> diffC = new JComboBox<String>(diffS);
	private JComboBox<String> grpC = new JComboBox<String>(grpS);
	private JComboBox<String> catC = new JComboBox<String>(catS);

	/* Labels */
	private JLabel diffL = new JLabel("Difficulté");
	private JLabel grpL = new JLabel("Groupe");
	private JLabel catL = new JLabel("Catégorie");
	private JLabel jourL = new JLabel("Date...");

	/* Boutons */
	private JButton print = new JButton("Imprimer");
	private JButton telec = new JButton("Télécharger PDF");
	private JButton classement = new JButton("Classement");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "Rang", "Dossard", "Catégorie",
			"Nom équipe", "Temps", "Point", "Tps Total" };

	/* JPanel */

	private JPanel generalP = new JPanel();
	private JPanel checkPanel = new JPanel();

	private JPanel filtreP = new JPanel();

	private JPanel classementPB = new JPanel();
	private JPanel choixP = new JPanel();

	private JPanel imprePB = new JPanel();
	private JPanel telechPB = new JPanel();
	private JPanel panBoutonsListe = new JPanel();

	private JPanel panTitre = new JPanel();
	private JPanel panMega = new JPanel();

	int idc;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 */
	public Inte_Resultat(int idC) {
		idc = idC;

		bG.add(epreuveR);
		bG.add(jourR);
		bG.add(generalR);

		Interface();

		EcouteurCl clas = new EcouteurCl();
		classement.addActionListener(clas);

		EcouteurPrint imp = new EcouteurPrint();
		print.addActionListener(imp);

		EcouteurPDF pdf = new EcouteurPDF();
		telec.addActionListener(pdf);
	}

	/**
	 * Fonction gérant l'interface du panel
	 */
	public void Interface() {
		generalP.add(generalR);

		checkPanel.setBorder(BorderFactory
				.createTitledBorder("Type de classement"));
		// checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.PAGE_AXIS));
		checkPanel.setPreferredSize(new Dimension(300, 100));
		checkPanel.setLayout(new GridLayout(3, 2));
		checkPanel.add(epreuveR);
		checkPanel.add(epreuveC);
		checkPanel.add(jourR);
		checkPanel.add(jourL);
		checkPanel.add(generalR);

		updateCombo(epreuveC);

		filtreP.setBorder(BorderFactory.createTitledBorder("Filtres"));
		// filtreP.setLayout(new BoxLayout(filtreP, BoxLayout.PAGE_AXIS));
		filtreP.setPreferredSize(new Dimension(300, 100));
		filtreP.setLayout(new GridLayout(3, 2));
		filtreP.add(diffL);
		filtreP.add(diffC);
		filtreP.add(grpL);
		filtreP.add(grpC);
		filtreP.add(catL);
		filtreP.add(catC);

		classement.setPreferredSize(new Dimension(120, 50));
		classementPB.add(classement);

		choixP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez générer les classements"));
		choixP.setPreferredSize(new Dimension(750, 135));
		choixP.add(checkPanel);
		choixP.add(filtreP);
		choixP.add(classementPB);

		print.setPreferredSize(new Dimension(130, 30));
		telec.setPreferredSize(new Dimension(130, 30));
		imprePB.add(print);
		telechPB.add(telec);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(imprePB);
		panBoutonsListe.add(telechPB);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(600, 360));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez voir les classement"));
		// panTitre.setPreferredSize(new Dimension(750, 350));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.setLayout(new BoxLayout(panMega, BoxLayout.PAGE_AXIS));
		panMega.add(choixP);
		panMega.add(panTitre);

		this.add(panMega);
	}

	/**
     * Permet de gérer les clics du type "Classement".
     */
	public class EcouteurCl implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

		}
	}

	/**
     * Permet de gérer les clics du type "Telecharger PDF".
     */
	public class EcouteurPDF implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
     * Permet de gérer les clics du type "Imprimer".
     */
	public class EcouteurPrint implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
     * Met à jour la comboBox pour la remplir avec les épeuves présentes dans la BDD.
     * 
     * @param combo
     * 			La comboBox à mettre à jour
     */
	public void updateCombo(JComboBox<String> combo) {
		combo.removeAllItems();
		String requeteSQL = "SELECT nomEpreuve FROM epreuve WHERE idCompetition = '"
				+ idc + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				combo.addItem(res.getString(1));
				System.out.println("Nom : " + res.getString(1));
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (combo.getItemCount() == 0) {
			epreuveR.setEnabled(false);
		} else {
			epreuveR.setEnabled(true);
		}
		System.out.println("MAJ Combo");
	}

}
