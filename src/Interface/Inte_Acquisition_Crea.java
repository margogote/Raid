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
 * Formulaire permettant de renseigner les informations pour créer une épreuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Acquisition_Crea extends JFrame {

	private JFrame theFrame = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom de l'équipe");
	private JComboBox<Object> nomC = new JComboBox<Object>();
	private ArrayList<Integer> nomLis = new ArrayList<>();

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
	private JPanel panPan = new JPanel();
	private JPanel ultraP = new JPanel();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "id", "Nom Malus/Bonus", "Type", "Temps" };

	private int idc;
	private int idep;
	private int ideq;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idEpreuve
	 *            , l'id de l' épreuve étudiée
	 */
	public Inte_Acquisition_Crea(int idC, int idEpreuve) {
		theFrame = this;
		idc = idC;
		idep = idEpreuve;

		InterfaceAcq();

		EcouteurOKAcq ecoutOK = new EcouteurOKAcq();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

		EcouteurOKMB ecoutOKMB = new EcouteurOKMB();
		oK2.addActionListener(ecoutOKMB);

		EcouteurPlus plus = new EcouteurPlus();
		creer.addActionListener(plus);

		EcouteurMoins moins = new EcouteurMoins();
		supp.addActionListener(moins);
	}

	/**
	 * Fonction gérant l'interface de la fenetre Partie 1 : création de
	 * l'epreuve
	 */
	public void InterfaceAcq() {
		System.out.println("InterfaceAcq");
		theFrame.setTitle("Raidzultats - Ajout d'acquisition");
		theFrame.setSize(450, 220);
		theFrame.setLocationRelativeTo(null);
		// centre la fenetre theFrame.setResizable(false);
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
				.createTitledBorder("Ici vous pouvez ajouter une acquisition à votre épreuve"));
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(megaP);
		gigaP.add(btnP);

		panPan.add(gigaP);

		theFrame.add(panPan);

		theFrame.setVisible(true);
	}

	/**
	 * Fonction gérant l'interface de la fenetre4 Partie 2 : l'association de
	 * balises dans l'epreuve
	 */
	public void InterfaceMB() {
		System.out.println("InterfaceMB");
		theFrame.setTitle("Raidzultats Attribution de Malus ou Bonus");
		theFrame.setLocationRelativeTo(null);
		theFrame.setSize(500, 500);

		ultraP.removeAll();
		panTitre.removeAll();
		panPan.removeAll();

		oK2.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(50, 30));
		supp.setPreferredSize(new Dimension(50, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		// panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(400, 350));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les malus/bonus de l'équipe pour l'epreuve"));
		// panTitre.setPreferredSize(new Dimension(350, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		JPanel btnP = new JPanel();
		btnP.add(oK2);
		btnP.add(annuler);

		// JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panTitre);
		ultraP.add(btnP);

		// JPanel panPan = new JPanel();
		panPan.add(ultraP);

		System.out.println("panpan");
		theFrame.add(panPan);

		theFrame.revalidate();
		theFrame.repaint();
		theFrame.setVisible(true);
	}

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre de création
	 * d'épreuve. Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurOKAcq implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Ok Acq");

			String nom = (String) nomC.getSelectedItem();
			String date = (String) dateT.getText();
			String balise = (String) baliseC.getSelectedItem();


			if (nom.equals("CHOISIR") || date.equals("")
					|| balise.equals("CHOISIR")
					|| date.equals("AAAA-MM-JJ hh:mm:ss")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Acquisition non créée!", JOptionPane.WARNING_MESSAGE);

			} else {
				
				ideq = nomLis.get(nomC.getSelectedIndex() - 1);
				System.out.println("Equipe choisie : " + ideq);
				int doigt = 0;
				
				updateTable();
				String requeteSQL = "SELECT `idDoigt` FROM `posséder` WHERE `idEquipe`='"
						+ ideq + "'";

				System.out.println(requeteSQL);
				try {
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DataSourceProvider.getDataSource()
							.getConnection();
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					ResultSet res = stm.executeQuery(requeteSQL);

					while (res.next()) {
						doigt = res.getInt(1);
						System.out.println("doigt : " + res.getString(1));

					}
					conn.close();
					res.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				String requeteSQL2 = "INSERT INTO `pointer`(`idBalise`, `idEpreuve`, `idDoigt`, `dateHeurePointage`, `idCompetition`) VALUES ('"
						+ balise
						+ "', '"
						+ idep
						+ "', '"
						+ doigt
						+ "', '"
						+ date + "', '" + idc + "')";
				System.out.println(requeteSQL2);
				BDDupdate(requeteSQL2);
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
			System.out.println("Fenetre ajout MB fermée");
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

			int[] tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de Malus/Bonus à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.length; i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer le Malus/Bonus "
								+ tab[i] + " de cette épreuve ?", "Attention",
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
									"Le Malus/Bonus est maintenant supprimé de cette épreuve",
									"Malus/Bonus " + tab[i] + " Supprimé!",
									JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Malus/Bonus " + tab[i] + " Supprimé");

					updateTable();
				}
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre d'attribution de
	 * balise à une épreuve Fermer la fenêtre
	 */
	public class EcouteurOKMB implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			theFrame.dispose();
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter" Ferme la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			theFrame.dispose();
		}
	}

	/**
	 * Met à jour du tableau pour le remplir avec les malus/bonus des equipes de
	 * la compétition à partir de la BDD.
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
					"Pas de connection avec la Base de Données", "Attention",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		data = ArrayToTab(ArrayData);

		InterfaceMB();

		System.out.println("MAJ Table");
	}

	/**
	 * Met à jour la comboBox pour la remplir selon la requete en parametre à
	 * partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox à mettre à jour
	 * @param requeteSQL
	 *            , la requete SQL à effectuer
	 * @return comboList , la liste des id associés aux noms dans la combo dans
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
