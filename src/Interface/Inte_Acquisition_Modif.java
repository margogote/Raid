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
import Interface.Inte_Acquisition_Crea.EcouteurMoins;
import Interface.Inte_Acquisition_Crea.EcouteurOKAcq;
import Interface.Inte_Acquisition_Crea.EcouteurOKMB;
import Interface.Inte_Acquisition_Crea.EcouteurPlus;
import Interface.Inte_Acquisition_Crea.EcouteurQ;
import Models.TabModel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

/**
 * Formulaire permettant de renseigner les informations pour modifier une
 * �preuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Acquisition_Modif extends JFrame {

	private JFrame theFrame = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom de l'�quipe");
	private JComboBox<Object> nomC = new JComboBox<Object>();
	private ArrayList<Integer> nomLis = new ArrayList<>();

	private JLabel dateL = new JLabel("Date et heure de pointage");
	private JTextField dateT = new JTextField("AAAA-MM-JJ hh:mm:ss");

	private JLabel baliseL = new JLabel("Balise");
	private JComboBox<Object> baliseC = new JComboBox<Object>();

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
	private JPanel ultraP = new JPanel();
	private JPanel gigaP = new JPanel();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "id", "Nom Malus/Bonus", "Type", "Temps" };

	private int idc;
	private int idep;
	private int ideq;
	private int idb;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la comp�tition �tudi�e
	 * @param idEpreuve
	 *            , l'id de l' �preuve �tudi�e
	 * @param idEquipe
	 *            , l'id de l'�quipe � modifier
	 */
	Inte_Acquisition_Modif(int idC, int idEpreuve, int idEquipe, int idBalise) {
		idc = idC;
		idep = idEpreuve;
		ideq = idEquipe;
		idb = idBalise;
		theFrame = this;

		updateTable();

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

		EcouteurPlus plus = new EcouteurPlus();
		creer.addActionListener(plus);

		EcouteurMoins moins = new EcouteurMoins();
		supp.addActionListener(moins);

	}

	public void Interface() {
		System.out.println("InterfaceAcq");

		panTitre.removeAll();
		gigaP.removeAll();
		panPan.removeAll();
		ultraP.removeAll();

		theFrame.setTitle("Raidzultats - Modification d'acquisition");
		theFrame.setSize(800, 400);
		theFrame.setLocationRelativeTo(null);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		String requeteBalise = "SELECT idBalise, idBalise FROM `valoir` WHERE `idCompetition` = '"
				+ idc + "' && `idEpreuve`='" + idep + "'";
		String requeteNom = "SELECT nomEquipe, idEquipe FROM `equipe` WHERE `idCompetition` = '"
				+ idc + "'";

		updateCombo(baliseC, requeteBalise);
		nomLis = updateCombo(nomC, requeteNom);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(300, 100));
		megaP.setLayout(new GridLayout(3, 2));
		megaP.add(nomL);
		megaP.add(nomC);
		megaP.add(dateL);
		megaP.add(dateT);
		megaP.add(baliseL);
		megaP.add(baliseC);

		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez modifier une acquisition de votre �preuve"));
		// gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(megaP);
		// gigaP.add(btnP);

		creer.setPreferredSize(new Dimension(50, 30));
		supp.setPreferredSize(new Dimension(50, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		// panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		// Nous ajoutons notre tableau � notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(350, 250));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez g�rer les malus/bonus de l'�quipe pour l'epreuve"));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panPan.add(gigaP);
		panPan.add(panTitre);

		// JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panPan);
		ultraP.add(btnP);

		theFrame.add(ultraP);

		theFrame.setVisible(true);

		String requeteSQL = "SELECT `equipe`.nomEquipe, pointer.`dateHeurePointage`, pointer.`idBalise` FROM `pointer` INNER JOIN (`poss�der` INNER JOIN `equipe` ON `poss�der`.idEquipe = `equipe`.idEquipe) ON `pointer`.`idDoigt` = `poss�der`.`idDoigt` WHERE `pointer`.`idCompetition` = '"
				+ idc
				+ "'&& `equipe`.`idCompetition` = '"
				+ idc
				+ "' && `equipe`.`idEquipe`='"
				+ ideq
				+ "' && `poss�der`.`idCompetition` = '"
				+ idc
				+ "' && `pointer`.`idBalise`='"
				+ idb
				+ "' && `pointer`.`idEpreuve`='" + idep + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				nomC.setSelectedItem(res.getString(1));
				dateT.setText(res.getString(2));
				baliseC.setSelectedItem(res.getString(3));
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
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

			Inte_Acquisition_MB formulaireMB = new Inte_Acquisition_MB(idc,
					idep, ideq);
			System.out.println("Fenetre ajout MB ouverte");
			formulaireMB.addWindowListener(this);
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			System.out.println("Fenetre ajout MB ferm�e");
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
						"Pas de Malus/Bonus � supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.length; i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer le Malus/Bonus "
								+ tab[i] + " de cette �preuve ?", "Attention",
						JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `avoir` WHERE `avoir`.`idMB` = '"
							+ tab[i]
							+ "' && `avoir`.`idEquipe` ='"
							+ ideq
							+ "' && `idCompetition` = '" + idc + "'";
					BDDupdate(requeteSQL);

					JOptionPane
							.showMessageDialog(
									null,
									"Le Malus/Bonus est maintenant supprim� de cette �preuve",
									"Malus/Bonus " + tab[i] + " Supprim�!",
									JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Malus/Bonus " + tab[i] + " Supprim�");

					updateTable();
				}
			}
		}
	}

	/**
	 * Permet de g�rer les clics du type "Ok" pour la fen�tre d'attribution de
	 * balise � une �preuve Fermer la fen�tre
	 */
	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			theFrame.dispose();

			String nom = (String) nomC.getSelectedItem();
			String date = (String) dateT.getText();
			String balise = (String) baliseC.getSelectedItem();
			int ideqN = nomLis.get(nomC.getSelectedIndex() - 1);
			System.out.println("Equipe choisie : " + ideq);

			if (nom.equals("CHOISIR") || date.equals("")
					|| balise.equals("CHOISIR")
					|| date.equals("AAAA-MM-JJ hh:mm:ss")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Acquisition non modifi��e!",
						JOptionPane.WARNING_MESSAGE);

			} else {

				String requeteSQL2 = "UPDATE `pointer` SET `idBalise` = '"
						+ balise
						+ "', `idDoigt` = (SELECT `idDoigt` FROM `poss�der` WHERE `idEquipe`='"
						+ ideqN
						+ "'), `dateHeurePointage` = '"
						+ date
						+ "' WHERE CONCAT(`pointer`.`idBalise`) = '"
						+ idb
						+ "' AND `pointer`.`idEpreuve` = '"
						+ idep
						+ "' AND CONCAT(`pointer`.`idDoigt`) = (SELECT `idDoigt` FROM `poss�der` WHERE `idEquipe`='"
						+ ideq + "') AND `pointer`.`idCompetition` = '" + idc
						+ "'";

				System.out.println(requeteSQL2);
				BDDupdate(requeteSQL2);
			}
		}
	}

	/**
	 * Permet de g�rer les clics du type "Quitter" Ferme la fen�tre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			theFrame.dispose();
		}
	}

	/**
	 * Met � jour du tableau pour le remplir avec les malus/bonus des equipes de
	 * la comp�tition � partir de la BDD.
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT `malusbonus`.`idMB`,`malusbonus`.`nomMalusBonus`, `malusbonus`.`malus`, `malusbonus`.`tempsMalusBonus` FROM `malusbonus` INNER JOIN `avoir` ON `malusbonus`.`idMB`=`avoir`.`idMB` WHERE `malusbonus`.`idCompetition` = '"
				+ idc
				+ "' && `avoir`.`idCompetition`='"
				+ idc
				+ "' && `avoir`.`idEquipe`='" + ideq + "'";

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

		Interface();

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
	 * @return comboList , la liste des id associ�s aux noms dans la combo dans
	 *         la BDD
	 */
	public ArrayList<Integer> updateCombo(JComboBox<Object> combo,
			String requeteSQL) {
		combo.removeAllItems();
		ArrayList<Integer> comboList = new ArrayList<Integer>();

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
				comboList.add(res.getInt(2));
				System.out.println("Nom : " + res.getString(1) + " Id : "
						+ res.getInt(2));
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
		return (comboList);
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
