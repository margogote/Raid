package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import BDD.DataSourceProvider;
import Interface.Inte_Acquisition_Crea.EcouteurQ;
import Models.TabModel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class Inte_Acquisition extends JFrame {

	private JFrame theFrame;

	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panTitre = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	private JPanel panBoutAcq = new JPanel();
	private JPanel panBoutQ = new JPanel();

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");
	private JButton acqA = new JButton("Acquisition Auto");
	private JButton quit = new JButton("J'ai fini !");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "", "id", "Nom équipe", "Dossard",
			"Heure pointage", "Balise" };

	int idc;
	int idep;
	String nomEp;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idEpreuve
	 *            , l'id de l' épreuve étudiée
	 * @param nomEpreuve
	 *            , le nom de l' épreuve étudiée
	 */
	Inte_Acquisition(int idC, int idEpreuve, String nomEpreuve) {

		theFrame = this;
		idc = idC;
		idep = idEpreuve;
		nomEp = nomEpreuve;

		updateTable();

		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);

		EcouteurA ecoutA = new EcouteurA();
		acqA.addActionListener(ecoutA);

		EcouteurQ ecoutQ = new EcouteurQ();
		quit.addActionListener(ecoutQ);
	}

	/**
	 * Fonction gérant l'interface du panel
	 */
	public void Interface() {
		theFrame.setTitle("Raidzultats - Acquisition - " + nomEp); // titre
		theFrame.setSize(800, 550); // taille de la fenetre
		theFrame.setLocationRelativeTo(null); // centre la fenetre
		theFrame.setResizable(false);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		// theFrame.removeAll();
		panMega.removeAll();
		panTitre.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));
		acqA.setPreferredSize(new Dimension(140, 50));
		quit.setPreferredSize(new Dimension(140, 50));

		panBoutAcq.add(acqA);
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);
		panBoutQ.add(quit);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutAcq);
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutQ);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(600, 400));

		panTitre.setPreferredSize(new Dimension(700, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les acquisitions pour cette epreuve"));
		panMega.setLayout(new BoxLayout(panMega, BoxLayout.PAGE_AXIS));
		panMega.add(panTitre);
		panMega.add(panBoutQ);

		theFrame.add(panMega);

		theFrame.setVisible(true);
	}

	/**
	 * Permet de gérer les clics du type "Créer" Lancement du formulaire associé
	 * mise à jour du tableau lors de la fermeture du formulaire
	 */
	public class EcouteurCreer implements ActionListener, WindowListener { // Action
																			// du
																			// creer
		public void actionPerformed(ActionEvent arg0) {

			Inte_Acquisition_Crea formulaire = new Inte_Acquisition_Crea(idc,
					idep);
			formulaire.addWindowListener(this);
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			updateTable();
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}

	/**
	 * Permet de gérer les clics du type "Modifier" pour une épreuve
	 * Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurModif implements ActionListener, WindowListener { // Action
																			// du
																			// modif
		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
			int[][] tab = getIndexSelectTab(data);

			if (tab[0].length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'acquisition à modifier!",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				for (int i = 0; i < tab.length; i++) {

					Inte_Acquisition_Modif formulaire = new Inte_Acquisition_Modif(
							idc, idep, tab[i][0], tab[i][1]);

					formulaire.addWindowListener(this);
				}
			}
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			updateTable();
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}

	/**
	 * Permet de gérer les clics du type "Supprimer" pour une épreuve
	 * Suppression de la BDD
	 */
	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int[][] tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab[0].length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'acquisition à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				for (int i = 0; i < tab.length; i++) {
					rep = JOptionPane.showConfirmDialog(null,
							"Voulez vous vraiment supprimer l'acquisition "
									+ tab[i][0] + " ?", "Attention",
							JOptionPane.YES_NO_OPTION);

					if (rep == 0) {
						String requeteSQL = "DELETE FROM `pointer` WHERE CONCAT(`pointer`.`idBalise`) ='"
								+ tab[i][1]
								+ "' AND `pointer`.`idEpreuve` = '"
								+ idep
								+ "' AND CONCAT(`pointer`.`idDoigt`) = (SELECT `idDoigt` FROM `posséder` WHERE `idEquipe`='"
								+ tab[i][0]
								+ "')  AND `pointer`.`idCompetition` = '"
								+ idc
								+ "'";

						BDDupdate(requeteSQL);

						JOptionPane.showMessageDialog(null,
								"L'acquisition est maintenant supprimée",
								"Acquisition " + tab[i][0] + " Supprimée!",
								JOptionPane.INFORMATION_MESSAGE);

						System.out.println("Acquisition " + tab[i][0]
								+ " Supprimée");
					}
				}
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Acquisition" pour une épreuve
	 * Récupération de l'épreuve séléctionnée et lancement du formulaire associé
	 */
	public class EcouteurA implements ActionListener { // Action
		// du
		// creer
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Met à jour du tableau pour le remplir avec les équipes de la compétition
	 * à partir de la BDD.
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT `equipe`.idEquipe, `equipe`.nomEquipe, `equipe`.dossard, `pointer`.`dateHeurePointage`, `pointer`.`idBalise` FROM `pointer` INNER JOIN (`posséder` INNER JOIN `equipe` ON `posséder`.idEquipe = `equipe`.idEquipe) ON `pointer`.`idDoigt` = `posséder`.`idDoigt` WHERE `pointer`.`idCompetition` = '"
				+ idc
				+ "'&& `equipe`.`idCompetition` = '"
				+ idc
				+ "' && `posséder`.`idCompetition` = '"
				+ idc
				+ "' && `pointer`.`idEpreuve`='" + idep + "'";
		int i = 1;

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
						res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5) });
				System.out.println(" nom : " + res.getString(2) + " dossard : "
						+ res.getInt(3) + " tps : " + res.getString(4)
						+ " balise : " + res.getInt(5));
				i++;
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

		Interface();

		System.out.println("MAJ Table equipe");
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
	public int[][] getIndexSelectTab(Object[][] table) {
		ArrayList<Integer[]> ArrayDataSelect = new ArrayList<Integer[]>();
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
				ArrayDataSelect.add(new Integer[] { (int) table[i][1],
						(int) table[i][5] });
			}

		}

		int[][] tab = { {} };
		System.out.println(ArrayDataSelect);
		if (ArrayDataSelect.size() != 0) {
			tab = new int[ArrayDataSelect.size()][ArrayDataSelect.get(0).length];
			for (int i = 0; i < ArrayDataSelect.size(); i++) {
				tab[i][0] = ArrayDataSelect.get(i)[0];
				tab[i][1] = ArrayDataSelect.get(i)[1];
			}
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
			updateTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
