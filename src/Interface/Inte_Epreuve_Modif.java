package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import BDD.DataSourceProvider;
import Models.TabModel;

public class Inte_Epreuve_Modif extends JFrame {

	JFrame thePanel = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom");
	private JTextField nomT = new JTextField("");

	private JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Course d`orientation", "Course",
			"Orientshow" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	private JLabel dateL = new JLabel("Date de début");
	private JTextField dateT = new JTextField("..... date ....");

	private JLabel dureeL = new JLabel("Durée");
	private JTextField dureeT = new JTextField("..... durée ....");

	
	private JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	/* Boutons */
	private JButton supp = new JButton(" - ");
	private JButton creer = new JButton(" + ");

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons
	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	private JPanel panTitre = new JPanel();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "idBalise", "Type", "Valeur" };

	private int idc;
	private int modif;

	public Inte_Epreuve_Modif(int idC, int idModif) {
		thePanel = this;
		idc = idC;
		modif = idModif;

		updateTable();

		if (idModif != -1) {
			thePanel.setTitle("Raidzultats - Modification épreuve " + idModif);

			String requeteSQL = "SELECT epreuve.`nomEpreuve`, epreuve.`typeEpreuve`, epreuve.`difficulte`, epreuve.`dateHeureEpreuve`, epreuve.`dureeEpreuve` FROM epreuve WHERE epreuve.`idCompetition` = '"
					+ idc + "' && epreuve.`idEpreuve` = '" + idModif + "'";

			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource()
						.getConnection();
				System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);

				while (res.next()) {
					nomT.setText(res.getString(1));
					typeC.setSelectedItem(res.getString(2));
					difficC.setSelectedItem(res.getString(3));
					dateT.setText(res.getString(4));
					dureeT.setText(res.getString(5));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
	}

	public void Interface() {
		thePanel.setTitle("Raidzultats");
		thePanel.setSize(850, 600);
		thePanel.setLocationRelativeTo(null);
		thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel labelP = new JPanel();
		labelP.setLayout(new GridLayout(6, 2));
		labelP.setPreferredSize(new Dimension(280, 180));
		labelP.add(nomL);
		labelP.add(nomT);
		labelP.add(typeL);
		labelP.add(typeC);
		labelP.add(dateL);
		labelP.add(dateT);
		labelP.add(dureeL);
		labelP.add(dureeT);
		labelP.add(difficL);
		labelP.add(difficC);

		JPanel megaP = new JPanel();
		megaP.setBorder(BorderFactory
				.createTitledBorder("Créer/modifier votre épreuve"));
		megaP.add(labelP);

		creer.setPreferredSize(new Dimension(50, 30));
		supp.setPreferredSize(new Dimension(50, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(400, 400));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les balises de l'epreuve"));
		// panTitre.setPreferredSize(new Dimension(350, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		JPanel gigaP = new JPanel();
		gigaP.add(megaP);
		gigaP.add(panTitre);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(gigaP);
		ultraP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		thePanel.add(panPan);

		thePanel.setVisible(true);

	}

	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String nom = nomT.getText();
			String type = (String) typeC.getSelectedItem();
			String difficulte = (String) difficC.getSelectedItem();
			String date = (String) dateT.getText();
			String duree = (String) dureeT.getText();

			try {
				// Integer.parseInt(dossard);
				
				String requeteSQL = "UPDATE`raidzultat`.`epreuve` SET `nomEpreuve` = '"
						+ nom
						+ "', `typeEpreuve` = '"
						+ type
						+ "', `difficulte`='"
						+ difficulte
						+ "', `dateHeureEpreuve`='"
						+ date
						+ "', `dureeEpreuve`=  '"
						+ duree
						+ "' WHERE idEpreuve = '"
						+ modif
						+ "' && `idCompetition`='" + idc + "'";
				
				System.out.println(requeteSQL);

				BDDupdate(requeteSQL);

				thePanel.dispose();
			}

			catch (Exception e) {
				System.out.println("Je ne suis pas un entier");
				JOptionPane.showMessageDialog(null,
						"Attention entrer un entier comme numéro de dossard",
						"Equipe non modifiée!", JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		// String requeteSQL =
		// "SELECT * FROM balise WHERE `idCompetition` = '"+idc+"'";
		String requeteSQL = "SELECT `idBalise`,`type`,`valeurBalise` FROM `valoir` WHERE `idCompetition` = '"
				+ idc + "' && `idEpreuve` = '" + modif + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				ArrayData.add(new Object[] { new Boolean(false), res.getInt(1),
						res.getString(2), res.getString(3) });
				System.out.println("idbalise : " + res.getString(1)
						+ " type : " + res.getString(2) + " val : "
						+ res.getString(3));
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		data = ArrayToTab(ArrayData);

		Interface();

		System.out.println(requeteSQL);
		System.out.println("MAJ Table balise");
	}

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

	public int[] getIndexSelectTab(Object[][] table) {
		ArrayList<Integer> ArrayDataSelect = new ArrayList<Integer>();
		int lig = table.length;	
		int col;
		
		if(lig>0){
		col = table[0].length;
		}else{col=0;}

		System.out.println(lig);
		System.out.println(col);

		for (int i = 0; i < lig; i++) {
			if ((boolean) table[i][0] == (true)) {
				System.out.println(ArrayDataSelect);
				ArrayDataSelect.add((Integer) table[i][1]);
			}

		}

		System.out.println(ArrayDataSelect);
		int[] tab = new int[ArrayDataSelect.size()];
		for (int i = 0; i < ArrayDataSelect.size(); i++) {
			tab[i] = ArrayDataSelect.get(i);
		}

		return tab;
	}

	public void BDDupdate(String requeteSQL) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			int res = stm.executeUpdate(requeteSQL);

			System.out.println("Nb enregistrement : " + res);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void BDDquery(String requeteSQL) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			int res = stm.executeUpdate(requeteSQL);

			System.out.println("Nb enregistrement : " + res);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
