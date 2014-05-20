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
import Interface.Inte_Epreuve_Modif.EcouteurMoins;
import Interface.Inte_Epreuve_Modif.EcouteurOK;
import Interface.Inte_Epreuve_Modif.EcouteurPlus;
import Interface.Inte_Epreuve_Modif.EcouteurQ;
import Models.TabModel;

/**
 * Formulaire permettant de renseigner les informations pour créer une épreuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Epreuve_Crea extends JFrame {

	private JFrame thePanel = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom");
	private JTextField nomT = new JTextField("");

	private JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Course d`orientation", "Course",
			"MassStart" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	private JLabel dateL = new JLabel("Date de début");
	private JTextField dateT = new JTextField("AAAA-MM-JJ hh:mm:ss");

	private JLabel dureeL = new JLabel("Durée");
	private JTextField dureeT = new JTextField("hh:mm:ss");

	private JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	private JLabel absL = new JLabel("Temps si absent");
	private JTextField absT = new JTextField("hh:mm:ss");

	/* Boutons */
	private JButton supp = new JButton(" - ");
	private JButton creer = new JButton(" + ");

	private JButton oK = new JButton("Suivant");
	private JButton oK2 = new JButton("Valider");
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
	private int idEp;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 */
	public Inte_Epreuve_Crea(int idC) {
		thePanel = this;
		idc = idC;

		InterfaceEp();

		EcouteurOKEp ecoutOKEp = new EcouteurOKEp();
		oK.addActionListener(ecoutOKEp);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
	}

	/**
	 * Fonction gérant l'interface de la fenetre Partie 1 : création de
	 * l'epreuve
	 */
	public void InterfaceEp() {
		System.out.println("InterfaceEp");
		thePanel.setTitle("Raidzultats Création Epreuve");
		thePanel.setSize(400, 300);
		thePanel.setLocationRelativeTo(null);
		thePanel.setLayout(new BorderLayout());

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel labelP = new JPanel();
		labelP.setLayout(new GridLayout(7, 2));
		labelP.setPreferredSize(new Dimension(300, 170));
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

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(megaP);
		ultraP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		thePanel.add(panPan);

		thePanel.setVisible(true);
	}

	/**
	 * Fonction gérant l'interface de la fenetre4 Partie 2 : l'association de
	 * balises dans l'epreuve
	 */
	public void InterfaceBa() {
		System.out.println("InterfaceBa");
		thePanel.setTitle("Raidzultats Attribution de balises");
		thePanel.setSize(500, 500);

		panTitre.removeAll();

		oK2.setPreferredSize(new Dimension(100, 30));
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
		jScroll.setPreferredSize(new Dimension(400, 350));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les balises de l'epreuve"));
		// panTitre.setPreferredSize(new Dimension(350, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		JPanel btnP = new JPanel();
		btnP.add(oK2);
		btnP.add(annuler);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panTitre);
		ultraP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		System.out.println("panpan");
		thePanel.add(panPan);

		thePanel.setContentPane(ultraP);
		thePanel.revalidate();
		thePanel.repaint();
		thePanel.setVisible(true);

		EcouteurOKBa ecoutOKBa = new EcouteurOKBa();
		oK2.addActionListener(ecoutOKBa);

		EcouteurPlus plus = new EcouteurPlus();
		creer.addActionListener(plus);

		EcouteurMoins moins = new EcouteurMoins();
		supp.addActionListener(moins);
	}

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre de création
	 * d'épreuve. Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurOKEp implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Ok Ep");

			String nom = nomT.getText();
			String type = (String) typeC.getSelectedItem();
			String difficulte = (String) difficC.getSelectedItem();
			String date = (String) dateT.getText();
			String duree = (String) dureeT.getText();
			String abs = (String) absT.getText();
			try {
				if (nom.equals("") || type.equals("CHOISIR")
						|| difficulte.equals("CHOISIR")
						|| date.equals("AAAA-MM-JJ hh:mm:ss")
						|| date.equals("") || duree.equals("hh:mm:ss")
						|| duree.equals("") || abs.equals("hh:mm:ss")
						|| abs.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Veuillez remplir tous les champs",
							"Paramètres non créés!",
							JOptionPane.WARNING_MESSAGE);
				} else {

					String testTpsdate[] = date.split("-| |:");
					String testTpsduree[] = duree.split(":");
					String testTpsabs[] = abs.split(":");
					for (int i = 0; i < testTpsdate.length; i++) {
						System.out.println(testTpsdate[i]);
						Integer.parseInt(testTpsdate[i]);
					}
					for (int i = 0; i < testTpsduree.length; i++) {
						System.out.println(testTpsduree[i]);
						Integer.parseInt(testTpsduree[i]);
					}
					for (int i = 0; i < testTpsabs.length; i++) {
						System.out.println(testTpsabs[i]);
						Integer.parseInt(testTpsabs[i]);
					}
					
					String requeteSQL = "INSERT INTO `epreuve` (`nomEpreuve`, `typeEpreuve`, `difficulte`, `dateHeureEpreuve`, `dureeEpreuve`, `idCompetition`) VALUES ('"
							+ nom
							+ "', '"
							+ type
							+ "', '"
							+ difficulte
							+ "', '"
							+ date
							+ "', '"
							+ duree
							+ "', '"
							+ idc
							+ "')";
					System.out.println(requeteSQL);
					BDDupdate(requeteSQL);

					String requeteSQL2 = "SELECT `epreuve`.`idEpreuve`  FROM `epreuve` WHERE `nomEpreuve` = '"
							+ nom
							+ "'&& `typeEpreuve` = '"
							+ type
							+ "'&& `difficulte`='"
							+ difficulte
							+ "'&& `dateHeureEpreuve`='"
							+ date
							+ "'&& `dureeEpreuve`=  '"
							+ duree
							+ "'&& `idCompetition`='" + idc + "'";
					System.out.println(requeteSQL2);

					try {
						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DataSourceProvider.getDataSource()
								.getConnection();
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();
						ResultSet res = stm.executeQuery(requeteSQL2);

						while (res.next()) {
							idEp = res.getInt(1);
							System.out.println("Num Epreuve : "
									+ res.getString(1));

						}
						System.out.println("Num Epreuve+ : " + idEp);

						conn.close();
						res.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

					String requeteSQL3 = "INSERT INTO `malusbonus` (`idMB`, `nomMalusBonus`, `malus`, `tempsMalusBonus`, `idCompetition`) VALUES (NULL, 'abs"
							+ nom + "', '1', '" + abs + "', '" + idc + "')";

					System.out.println("Création MB : " + requeteSQL);
					BDDupdate(requeteSQL3);

					InterfaceBa();

					if (type.equals("Course d`orientation")) {
						Inte_Epreuve_CO co = new Inte_Epreuve_CO(idc, idEp, nom);
					}
				}
			} catch (Exception e) {
				System.out.println("Je ne suis pas un entier");
				JOptionPane
						.showMessageDialog(null,
								"Veuillez entrer une heure de type hh:mm:ss et une date de type AAAA-MM-JJ hh:mm:ss",
								"Epreuve non créée!",
								JOptionPane.WARNING_MESSAGE);

			}
		}
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
					idc, idEp);
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
							+ idEp
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
	 * Permet de gérer les clics du type "Ok" pour la fenêtre d'attribution de
	 * balise à une épreuve Fermer la fenêtre
	 */
	public class EcouteurOKBa implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			thePanel.dispose();
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter". Fermer la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	/**
	 * Met à jour la comboBox pour la remplir avec les balises de la compétition
	 * qui ont été affécté à l'eppreuve à partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox à mettre à jour
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT `idBalise`,`type`,`valeurBalise` FROM `valoir` WHERE `idCompetition` = '"
				+ idc + "' && `idEpreuve` = '" + idEp + "'";

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

		InterfaceBa();

		System.out.println(requeteSQL);
		System.out.println("MAJ Table balise");
	}

	/**
	 * Fonction transformant une ArrayList en tableau
	 * 
	 * @param array
	 *            , l'arrayList à transformer
	 * 
	 * @return tab, le tableau correspondant à l'arrayList prise en parametre
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
