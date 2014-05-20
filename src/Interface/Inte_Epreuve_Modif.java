package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

/**
 * Formulaire permettant de renseigner les informations pour modifier une
 * épreuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Epreuve_Modif extends JFrame {

	private JFrame theFrame = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom");
	private JTextField nomT = new JTextField("");
	private String nomAv ="";

	private JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Course d`orientation", "Course",
			"MassStart" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	private JLabel dateL = new JLabel("Date de début");
	private JTextField dateT = new JTextField("..... date ....");

	private JLabel dureeL = new JLabel("Durée");
	private JTextField dureeT = new JTextField("..... durée ....");

	private JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	private JLabel absL = new JLabel("Temps si absent");
	private JTextField absT = new JTextField("hh:mm:ss");

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
	private JPanel panPan = new JPanel();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "idBalise", "Type", "Valeur" };

	private int idc;
	private int modif;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idModif
	 *            , l'id de l'épreuve à modifier
	 */
	public Inte_Epreuve_Modif(int idC, int idModif) {
		theFrame = this;
		idc = idC;
		modif = idModif;

		updateTable();

		if (idModif != -1) {
			theFrame.setTitle("Raidzultats - Modification épreuve " + idModif);

			try {

				String requeteSQL = "SELECT epreuve.`nomEpreuve`, epreuve.`typeEpreuve`, epreuve.`difficulte`, epreuve.`dateHeureEpreuve`, epreuve.`dureeEpreuve` FROM epreuve WHERE epreuve.`idCompetition` = '"
						+ idc + "' && epreuve.`idEpreuve` = '" + idModif + "'";

				String requeteSQLabs = "";

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
					
					nomAv=res.getString(1);
				}

				requeteSQLabs = "SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus` = 'abs"
						+ nomAv + "'";
				System.out.println(requeteSQLabs);
				ResultSet res2 = stm.executeQuery(requeteSQLabs);
				while (res2.next()) {
					absT.setText(res2.getString(1));
					System.out.println(res2.getString(1));

				}

				conn.close();
				res.close();
				res2.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		EcouteurPlus plus = new EcouteurPlus();
		creer.addActionListener(plus);

		EcouteurMoins moins = new EcouteurMoins();
		supp.addActionListener(moins);

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
	}

	/**
	 * Fonction gérant l'interface de la fenetre
	 */
	public void Interface() {
		theFrame.setTitle("Raidzultats Modification d'épreuve");
		theFrame.setSize(850, 600);
		theFrame.setLocationRelativeTo(null);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		panTitre.removeAll();
		panPan.removeAll();

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
		labelP.add(absL);
		labelP.add(absT);

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

		panPan.add(ultraP);

		theFrame.add(panPan);

		theFrame.setVisible(true);
	}

	/**
	 * Permet de gérer les clics du type "+" pour associer une balise à une
	 * épreuve Lancement du formulaire associé mise à jour du tableau lors de la
	 * fermeture du formulaire
	 */
	public class EcouteurPlus implements ActionListener, WindowListener { // Action
		// du
		// +

		public void actionPerformed(ActionEvent arg0) {

			Inte_Epreuve_Balise_Crea formulaire = new Inte_Epreuve_Balise_Crea(
					idc, modif);
			formulaire.addWindowListener(this);
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			updateTable();
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	}

	/**
	 * Permet de gérer les clics du type "-" pour dissocier une balise d'une
	 * épreuve Suppressions de la BDD
	 */
	public class EcouteurMoins implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			ArrayList<Object> tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.size() == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de balise à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.size(); i++) {
				rep = JOptionPane.showConfirmDialog(
						null,
						"Voulez vous vraiment supprimer la balise "
								+ tab.get(i) + " de cette épreuve ?",
						"Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `valoir` WHERE CONCAT(`valoir`.`idBalise`) = '"
							+ tab.get(i)
							+ "' && `idEpreuve` = '"
							+ modif
							+ "' && `idCompetition` = '" + idc + "'";
					BDDupdate(requeteSQL);

					JOptionPane
							.showMessageDialog(
									null,
									"La balise est maintenant supprimée de cette épreuve",
									"Balise " + tab.get(i) + " Supprimée!",
									JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Balise " + tab.get(i) + " Supprimée");

					updateTable();
				}
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre de modification
	 * d'épreuve. Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String nom = nomT.getText();
			String type = (String) typeC.getSelectedItem();
			String difficulte = (String) difficC.getSelectedItem();
			String date = (String) dateT.getText();
			String duree = (String) dureeT.getText();
			String abs = (String) absT.getText();

			try {
				// Integer.parseInt(dossard);

				String requeteSQL = "UPDATE `epreuve` SET `nomEpreuve` = '"
						+ nom + "', `typeEpreuve` = '" + type
						+ "', `difficulte`='" + difficulte
						+ "', `dateHeureEpreuve`='" + date
						+ "', `dureeEpreuve`=  '" + duree
						+ "' WHERE idEpreuve = '" + modif
						+ "' && `idCompetition`='" + idc + "'";

				BDDupdate(requeteSQL);
				String requeteSQLabs = "UPDATE `malusbonus` SET `nomMalusBonus`='abs" + nom	+ "',`tempsMalusBonus`='"
						+ abs
						+ "' WHERE `idCompetition`= '"
						+ idc
						+ "'&& `nomMalusBonus`='abs" + nomAv + "'";

				BDDupdate(requeteSQLabs);
				

				theFrame.dispose();
			}

			catch (Exception e) {
				System.out.println("Je ne suis pas un entier");
				JOptionPane.showMessageDialog(null,
						"Attention entrer un entier comme numéro de dossard",
						"Equipe non modifiée!", JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter". Fermer la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			theFrame.dispose();
		}
	}

	/**
	 * Met à jour du tableau pour le remplir avec les balises de l'épreuve de la
	 * compétition à partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox à mettre à jour
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

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

	/**
	 * Fonction transformant une ArrayList en tableau
	 * 
	 * @param array
	 *            , l'arrayList à transformer
	 * 
	 * @return tab , le tableau correspondant à l'arrayList prise en parametre
	 * 
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

	/**
	 * Fonction permettant de renvoyer les différentes lignes cochées dans un
	 * tableau
	 * 
	 * @param table
	 *            , le tableau à analyser
	 * 
	 * @return ArrayDataSelect, l'arrayList contenant les indices de chaque
	 *         ligne cochée
	 * 
	 */
	public ArrayList<Object> getIndexSelectTab(Object[][] table) {
		ArrayList<Object> ArrayDataSelect = new ArrayList<Object>();
		int lig = table.length;
		int col;

		if (lig > 0) {
			col = table[0].length;
		} else {
			col = 0;
		}

		System.out.println(lig);
		System.out.println(col);
		for (int i = 0; i < lig; i++) {
			if ((boolean) table[i][0] == (true)) {
				System.out.println(ArrayDataSelect);
				ArrayDataSelect.add(table[i][1]);
			}

		}
		Object[] tab = new Object[ArrayDataSelect.size()];
		System.out.println(ArrayDataSelect);

		return ArrayDataSelect;
	}

	/**
	 * Effectue une requête de mise à jour et de gestion dans la BDD.
	 * 
	 * @param requeteSQL
	 *            La requête SQL à saisir dans la BDD
	 */
	public void BDDupdate(String requeteSQL) {
		try {
			System.out.println(requeteSQL);
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
