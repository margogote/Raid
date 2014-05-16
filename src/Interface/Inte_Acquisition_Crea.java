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

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import BDD.DataSourceProvider;
import Models.TabModel;

/**
 * Formulaire permettant de renseigner les informations pour cr�er une �preuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Acquisition_Crea extends JFrame {

	JFrame thePanel = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom de l'�quipe");
	private JComboBox<Object> nomC = new JComboBox<Object>();

	private JLabel dateL = new JLabel("Date et heure de pointage");
	private JTextField dateT = new JTextField("AAAA-MM-JJ hh:mm:ss");

	private JLabel baliseL = new JLabel("Balise");
	private JComboBox<Object> baliseC = new JComboBox<Object>();

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
	private int ide;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la comp�tition �tudi�e
	 * @param idEpreuve
	 *            , l'id de l' �preuve �tudi�e
	 */
	public Inte_Acquisition_Crea(int idC, int idEpreuve) {
		idc = idC;
		ide = idEpreuve;

		InterfaceAcq();

	}

	/**
	 * Fonction g�rant l'interface de la fenetre Partie 1 : cr�ation de
	 * l'epreuve
	 */
	public void InterfaceAcq() {
		System.out.println("InterfaceEp");
		thePanel.setTitle("Raidzultats - Ajout d'acquisition");
		thePanel.setSize(450, 220);
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		String requeteBalise = "SELECT idBalise FROM `valoir` WHERE `idCompetition` = '"
				+ idc + "' && `idEpreuve`='" + ide + "'";
		String requeteNom = "SELECT nomEquipe FROM `equipe` WHERE `idCompetition` = '"
				+ idc + "'";
		updateCombo(baliseC, requeteBalise);
		updateCombo(nomC, requeteNom);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(400, 100));
		megaP.setLayout(new GridLayout(3, 2));
		megaP.add(nomL);
		megaP.add(nomC);
		megaP.add(dateL);
		megaP.add(dateT);
		megaP.add(baliseL);
		megaP.add(baliseC);

		JPanel gigaP = new JPanel();
		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez ajouter une acquisition � votre �preuve"));
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(megaP);
		gigaP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(gigaP);

		thePanel.add(panPan);

		thePanel.setVisible(true);

		EcouteurOKAcq ecoutOK = new EcouteurOKAcq();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
	}

	/**
	 * Fonction g�rant l'interface de la fenetre4 Partie 2 : l'association de
	 * balises dans l'epreuve
	 */
	public void InterfaceMB() {
		System.out.println("InterfaceBa");
		thePanel.setTitle("Raidzultats Attribution de Malus ou Bonus");
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

		// Nous ajoutons notre tableau � notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(400, 350));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez g�rer les malus/bonus de l'�quipe pour l'epreuve"));
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

		EcouteurOKMB ecoutOKMB = new EcouteurOKMB();
		oK2.addActionListener(ecoutOKMB);

		EcouteurPlus plus = new EcouteurPlus();
		creer.addActionListener(plus);

		EcouteurMoins moins = new EcouteurMoins();
		supp.addActionListener(moins);
	}

	/**
	 * Permet de g�rer les clics du type "Ok" pour la fen�tre de cr�ation
	 * d'�preuve. Recup�rations des donn�es entr�es et insertion dans la BDD
	 */
	public class EcouteurOKAcq implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Ok Ep");

			String nom = (String) nomC.getSelectedItem();
			String date = (String) dateT.getText();
			String balise = (String) baliseC.getSelectedItem();
			int idEquipe = -1;

			updateTable();
			/*
			 * String requeteSQL =
			 * "INSERT INTO `epreuve` (`nomEpreuve`, `typeEpreuve`, `difficulte`, `dateHeureEpreuve`, `dureeEpreuve`, `idCompetition`) VALUES ('"
			 * + nom + "', '" + type + "', '" + difficulte + "', '" + date +
			 * "', '" + duree + "', '" + idc + "')";
			 * System.out.println(requeteSQL); BDDupdate(requeteSQL);
			 * 
			 * String requeteSQL2 =
			 * "SELECT `epreuve`.`idEpreuve`  FROM `epreuve` WHERE `nomEpreuve` = '"
			 * + nom + "'&& `typeEpreuve` = '" + type + "'&& `difficulte`='" +
			 * difficulte + "'&& `dateHeureEpreuve`='" + date +
			 * "'&& `dureeEpreuve`=  '" + duree + "'&& `idCompetition`='" + idc
			 * + "'"; System.out.println(requeteSQL2);
			 * 
			 * try { Class.forName("com.mysql.jdbc.Driver");
			 * System.out.println("Driver O.K.");
			 * 
			 * Connection conn = DataSourceProvider.getDataSource()
			 * .getConnection(); System.out.println("Connexion effective !");
			 * Statement stm = conn.createStatement(); ResultSet res =
			 * stm.executeQuery(requeteSQL2);
			 * 
			 * while (res.next()) { modif = res.getInt(1);
			 * System.out.println("Num Epreuve : " + res.getString(1));
			 * 
			 * } System.out.println("Num Epreuve+ : " +modif);
			 * 
			 * conn.close(); res.close();
			 * 
			 * } catch (Exception e) { e.printStackTrace(); }
			 */
		}
	}

	/**
	 * Permet de g�rer les clics du type "+" pour associer une balise � une
	 * �preuve Lancement du formulaire associ� mise � jour du tableau lors de la
	 * fermeture du formulaire
	 */
	public class EcouteurPlus implements ActionListener, WindowListener { // Action
																			// du
																			// +
		public void actionPerformed(ActionEvent arg0) {

			Inte_Acquisition_MB formulaire = new Inte_Acquisition_MB(idc, ide);
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
	 * Permet de g�rer les clics du type "-" pour dissocier une balise d'une
	 * �preuve Suppressions de la BDD
	 */
	public class EcouteurMoins implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			int[] tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de balise � supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.length; i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer la balise " + tab[i]
								+ " de cette �preuve ?", "Attention",
						JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `valoir` WHERE CONCAT(`valoir`.`idBalise`) = '"
							+ tab[i]
							+ "' && `idEpreuve` = '"
							+ ide
							+ "' && `idCompetition` = '" + idc + "'";
					BDDupdate(requeteSQL);

					JOptionPane
							.showMessageDialog(
									null,
									"La balise est maintenant supprim�e de cette �preuve",
									"Balise " + tab[i] + " Supprim�e!",
									JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Balise " + tab[i] + " Supprim�e");

					updateTable();
				}
			}
		}
	}

	/**
	 * Permet de g�rer les clics du type "Ok" pour la fen�tre d'attribution de
	 * balise � une �preuve Fermer la fen�tre
	 */
	public class EcouteurOKMB implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			thePanel.dispose();
		}
	}

	/**
	 * Permet de g�rer les clics du type "Quitter" Ferme la fen�tre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	/**
	 * Met � jour du tableau pour le remplir avec les �preuves de la comp�tition
	 * � partir de la BDD.
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT * FROM malusbonus WHERE idCompetition = '"
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
				ArrayData.add(new Object[] { new Boolean(false), res.getInt(1),
						res.getString(2), res.getString(3), res.getString(4) });
				System.out.println("Id : " + res.getInt(1) + " nom : "
						+ res.getString(2) + " mb : " + res.getString(3)
						+ " tps : " + res.getString(4));
			}

			conn.close();
			res.close();

		} catch (CommunicationsException com) {
			JOptionPane.showMessageDialog(null,
					"Pas de connection avec la Base de Donn�es", "Attention",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		data = ArrayToTab(ArrayData);

		InterfaceMB();

		System.out.println("MAJ Table");
	}

	/**
	 * Met � jour la comboBox pour la remplir selon la requete en parametre �
	 * partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox � mettre � jour
	 * @param requeteSQL
	 *            , la requete SQL � effectuer
	 */
	public void updateCombo(JComboBox<Object> combo, String requeteSQL) {
		combo.removeAllItems();

		combo.addItem("CHOISIR");

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
			oK.setEnabled(false);
		} else {
			oK.setEnabled(true);
		}
		System.out.println("MAJ Combo");
	}

	/**
	 * Fonction transformant une ArrayList en tableau
	 * 
	 * @param array
	 *            , l'arrayList � transformer
	 * 
	 * @return tab, le tableau correspondant � l'arrayList prise en parametre
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
	 * Fonction permettant de renvoyer les diff�rentes lignes coch�es dans un
	 * tableau
	 * 
	 * @param table
	 *            , le tableau � analyser
	 * 
	 * @return ArrayDataSelect, l'arrayList contenant les indices de chaque
	 *         ligne coch�e
	 * 
	 */
	public int[] getIndexSelectTab(Object[][] table) {
		ArrayList<Integer> ArrayDataSelect = new ArrayList<Integer>();
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

	/**
	 * Effectue une requ�te de mise � jour et de gestion dans la BDD.
	 * 
	 * @param requeteSQL
	 *            La requ�te SQL � saisir dans la BDD
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
