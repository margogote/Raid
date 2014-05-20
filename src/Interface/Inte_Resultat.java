package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
import javax.swing.JTextField;

import BDD.DataSourceProvider;
import Classements.Classement;
import Classements.Filtre;
import Models.Equipe;
import Models.TabModel;

/**
 * Onglet Resultat : Permet de generer un classement en fonction de differents
 * filtres, permet(ra) de generer un PDF resumant ce classement et de l'imprimer
 * 
 * @author Margaux
 * 
 */
public class Inte_Resultat extends JPanel {

	/* Boutons Radio */
	private ButtonGroup bG = new ButtonGroup();
	private JRadioButton epreuveR = new JRadioButton("Epreuve");
	private JRadioButton jourR = new JRadioButton("Journ�e");
	private JRadioButton generalR = new JRadioButton("G�n�ral");
	private JRadioButton deAR = new JRadioButton("De ");

	/* Combo */
	private String[] init = { "CHOISIR" };
	// private String[] diffS = { "CHOISIR", "Aventure", "Expert" };
	// private String[] catS = { "CHOISIR", "Masculin", "Feminin", "Mixte" };

	private JComboBox<String> epreuveC = new JComboBox<String>(init);
	private JComboBox<String> diffC = new JComboBox<String>(init);
	private JComboBox<String> grpC = new JComboBox<String>(init);
	private JComboBox<String> catC = new JComboBox<String>(init);

	/* Labels */
	private JLabel diffL = new JLabel("Difficult�");
	private JLabel grpL = new JLabel("Groupe");
	private JLabel catL = new JLabel("Cat�gorie");
	private JTextField jourL = new JTextField("AAAA-MM-JJ");
	private JLabel aL = new JLabel(" � ");
	private JTextField deT = new JTextField("AAAA-MM-JJ");
	private JTextField aT = new JTextField("AAAA-MM-JJ");

	/* Boutons */
	private JButton print = new JButton("Imprimer");
	private JButton telec = new JButton("T�l�charger PDF");
	private JButton classement = new JButton("Classement");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "Rang", "Dossard", "Cat�gorie", "Nom �quipe",
			"Temps" };

	/* JPanel */
	private JPanel deP = new JPanel();
	private JPanel aP = new JPanel(); 

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
	 *            , l'id de la competition etudiee
	 */
	public Inte_Resultat(int idC) {
		idc = idC;

		generalR.setSelected(true);
		bG.add(epreuveR);
		bG.add(jourR);
		bG.add(generalR);

		updateTable();

		EcouteurCl clas = new EcouteurCl();
		classement.addActionListener(clas);

		EcouteurPrint imp = new EcouteurPrint();
		print.addActionListener(imp);

		EcouteurPDF pdf = new EcouteurPDF();
		telec.addActionListener(pdf);
	}

	/**
	 * Fonction gerant l'interface du panel
	 */
	public void Interface() {
		panTitre.removeAll();
		checkPanel.removeAll();
		generalP.add(generalR);

		
		deP.setLayout(new BorderLayout());
		deP.add(deAR, BorderLayout.WEST);
		deP.add(deT, BorderLayout.CENTER);

		
		aP.setLayout(new BorderLayout()); 
		aP.add(aL, BorderLayout.WEST); 
		aP.add(aT, BorderLayout.CENTER);

		checkPanel.setBorder(BorderFactory
				.createTitledBorder("Type de classement"));
		checkPanel.setPreferredSize(new Dimension(350, 100));
		checkPanel.setLayout(new GridLayout(4, 2));
		checkPanel.add(epreuveR);
		checkPanel.add(epreuveC);
		checkPanel.add(jourR);
		checkPanel.add(jourL);
		checkPanel.add(generalR);
		checkPanel.add(new JPanel());
		checkPanel.add(deP); 
		checkPanel.add(aP);

		updateComboEpreuve(epreuveC);
		updateComboEquipe(catC, "typeEquipe");
		updateComboEquipe(grpC, "nomGroupe");
		updateComboEquipe(diffC, "typeDifficulte");

		filtreP.setBorder(BorderFactory.createTitledBorder("Filtres"));
		// filtreP.setLayout(new BoxLayout(filtreP, BoxLayout.PAGE_AXIS));
		filtreP.setPreferredSize(new Dimension(250, 100));
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
				.createTitledBorder("Ici vous pouvez g�n�rer les classements"));
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

		// Nous ajoutons notre tableau a notre contentPane dans un scroll
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
	 * Permet de gerer les clics du type "Classement".
	 */
	public class EcouteurCl implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			updateTable();
		}
	}

	/**
	 * Permet de gerer les clics du type "Telecharger PDF".
	 */
	public class EcouteurPDF implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Permet de gerer les clics du type "Imprimer".
	 */
	public class EcouteurPrint implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Met a jour la comboBox pour la remplir avec les epeuves presentes dans la
	 * BDD.
	 * 
	 * @param combo
	 *            La comboBox a mettre a jour
	 */
	public void updateComboEpreuve(JComboBox<String> combo) {
		Object itemSelectione = combo.getSelectedItem();
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
		combo.setSelectedItem(itemSelectione);
	}

	/**
	 * Met a jour la comboBox pour la remplir avec les elements presents dans la
	 * BDD en fonction du champ demande
	 * 
	 * @param combo
	 *            , la combo a mettre a jour
	 * @param nomChampBDD
	 *            , le champ qui dont les items doivent remplir la combo
	 */
	public void updateComboEquipe(JComboBox<String> combo, String nomChampBDD) {
		int indexSelectione = combo.getSelectedIndex();
		combo.removeAllItems();
		combo.addItem("CHOISIR");
		String requeteSQL = "SELECT DISTINCT " + nomChampBDD
				+ " FROM equipe WHERE idCompetition = '" + idc + "' ORDER BY "
				+ nomChampBDD + " ASC";

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
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("MAJ Combo");
		combo.setSelectedIndex(indexSelectione);
	}

	/**
	 * Mise a jour du tableau avec le bon classement
	 * 
	 * @return, le tableau pas mis en forme
	 */
	public Object[][] updateTable() {

		// Declarations de variables
		Filtre filtre;
		ArrayList<Object[]> ArrayData = new ArrayList<>();
		String difficulteEq, groupeEq, typeEq;

		// ----Creation de la liste de dates----
		ArrayList<Date> dates = new ArrayList<>();
		Date date = new Date();
		// -------------------------------

		// ----Creation de la liste de noms d'epreuve----
		ArrayList<String> nomsEpreuves = new ArrayList<>();
		// -------------------------------

		// -------------- DEBUT - Selection des conditions----------------
		// Si classement par date
		if (jourR.isSelected()) {
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
			dfm.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
			try {
				date = dfm.parse(jourL.getText());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			dates.add(date);
		}// Si classement par �preuve
		else if (epreuveR.isSelected()) {
			nomsEpreuves.add(epreuveC.getSelectedItem().toString());
		}// Sinon classement g�n�ral

		// selon la difficultee
		if (diffC.getSelectedIndex() > 0) {
			difficulteEq = diffC.getSelectedItem().toString();
		} else
			difficulteEq = "";

		// selon le groupe
		if (grpC.getSelectedIndex() > 0) {
			groupeEq = grpC.getSelectedItem().toString();
		} else
			groupeEq = "";

		// selon le type d'equipe
		if (catC.getSelectedIndex() > 0) {
			typeEq = catC.getSelectedItem().toString();
		} else
			typeEq = "";

		// Cr�ation du filtre selon les conditions determinees ci-avant
		filtre = new Filtre(difficulteEq, groupeEq, typeEq, nomsEpreuves, idc);
		// ---------------------- FIN -------------------------

		// On genere le classement
		Classement testClassement = new Classement(dates, filtre, idc);

		// On extrait le classement
		ArrayList<Equipe> equipesClassees = testClassement.getClassement();

		// On affiche le classement dans le tableau
		int i;
		for (i = 0; i < equipesClassees.size(); i++) {
			Equipe eq = equipesClassees.get(i);
			ArrayData.add(new Object[] { i + 1, eq.getDossard(),
					eq.getTypeEquipe(), eq.getNomEquipe(),
					dateToString(eq.getScore()) });
			System.out.println("Rang : " + i + 1 + " Dossard : "
					+ eq.getDossard() + " Type d'equipe : "
					+ eq.getTypeEquipe() + " Nom equipe : " + eq.getNomEquipe()
					+ " Score : " + dateToString(eq.getScore()));
		}

		data = ArrayToTab(ArrayData);

		Interface();

		System.out.println(data);
		System.out.println("MAJ Table");
		return data;
	}

	/**
	 * Met en forme un tableau
	 * 
	 * @param array
	 *            , tableau a mettre en forme
	 * @return, tableau mise en forme
	 */
	public Object[][] ArrayToTab(ArrayList<Object[]> array) {

		int lengthLig = array.size();
		int lengthCol;
		if (lengthLig > 0) {
			lengthCol = array.get(0).length;
		} else {
			lengthCol = 0;
		}
		Object[][] tab = new Object[lengthLig][lengthCol];
		for (int i = 0; i < lengthLig; i++) {
			tab[i] = array.get(i);
		}
		return tab;
	}

	public static String dateToString(Date temps) {
		String HHmmss = "";

		Calendar calTemps = Calendar.getInstance();
		calTemps.setTime(temps);

		int h, m, s;
		h = m = s = 0;
		if (calTemps.get(Calendar.DAY_OF_MONTH) != 1) {
			h += 24 * (calTemps.get(Calendar.DAY_OF_MONTH) - 1);
		}
		h += calTemps.get(Calendar.HOUR);
		// System.out.println("h = "+h);
		m += calTemps.get(Calendar.MINUTE);
		// System.out.println("m = "+m);
		s += calTemps.get(Calendar.SECOND);
		// System.out.println("s = "+s);

		HHmmss += h + ":" + m + ":" + s;

		// System.out.println("HHmmss = "+HHmmss);
		return HHmmss;
	}

}
