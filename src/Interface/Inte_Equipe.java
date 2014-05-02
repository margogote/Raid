package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Models.TabModel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class Inte_Equipe extends JPanel {

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
	private JButton creer = new JButton("Cr�er");

	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idEquipe", "Nom d'�quipe",
			"Nom du groupe", "Difficult�", "Type d'�quipe" };

	JLabel bjr = new JLabel("Ici vous pouvez g�rer vous diff�rentes �quipes");

	public Inte_Equipe() {

		thePanel = this;

		data = updateTable();

		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);
	}

	public void Interface() {

		thePanel.removeAll();
		panMega.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));
		// panBoutonsListe.setPreferredSize (new Dimension(200, 150));

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

		tabModel = new TabModel(data, title);

		// Nous ajoutons notre tableau � notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !

		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);

		panMega.setLayout(new BorderLayout());
		panMega.add(bjr, BorderLayout.NORTH);
		panMega.add(panTitre, BorderLayout.WEST);
		panMega.add(new JScrollPane(tableau), BorderLayout.CENTER);

		thePanel.add(panMega);
	}

	public class EcouteurCreer implements ActionListener { // Action du creer

		public void actionPerformed(ActionEvent arg0) {

			Inte_Equipe_Cr�aModif formulaire = new Inte_Equipe_Cr�aModif();

			/*
			 * String nb = JOptionPane.showInputDialog(null,
			 * "Donner le num�ro de votre doigt !", "Nouveau doigt ?",
			 * JOptionPane.QUESTION_MESSAGE);
			 * 
			 * if (nb != null) { while (nb.equals("")) {
			 * JOptionPane.showMessageDialog(null, "Veuillez entrer un num�ro",
			 * "Doigt non cr��!", JOptionPane.INFORMATION_MESSAGE); nb =
			 * JOptionPane.showInputDialog(null,
			 * "Donner le num�ro de votre doigt !", "Nouveau doigt ?",
			 * JOptionPane.QUESTION_MESSAGE); }
			 * 
			 * // ---- Contr�le utilisateur ---- //
			 * "Ce doigt existe d�j�, veuillez entrer un autre num�ro"
			 * 
			 * if (!nb.equals("")) { try { String requeteSQL =
			 * "INSERT INTO `doigt` (`idDoigt`) VALUES ( " + nb + ")";
			 * Class.forName("com.mysql.jdbc.Driver");
			 * System.out.println("Driver O.K.");
			 * 
			 * Connection conn = DriverManager.getConnection(url, user, passwd);
			 * System.out.println("Connexion effective !"); Statement stm =
			 * conn.createStatement(); int res = stm.executeUpdate(requeteSQL);
			 * 
			 * System.out.println("Nb enregistrement : " + res);
			 * 
			 * conn.close();
			 * 
			 * updateTable();
			 * 
			 * } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * JOptionPane.showMessageDialog(null, "Le doigt est " + nb + ".",
			 * "Nouvelle comp�tition !", JOptionPane.INFORMATION_MESSAGE); } }
			 */
		}
	}

	public class EcouteurModif implements ActionListener { // Action du modif

		public void actionPerformed(ActionEvent arg0) {
			ArrayList<Object> tab = getIndexSelectTab(data);

			if (tab.size() == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de doigt � modifier!",
						JOptionPane.INFORMATION_MESSAGE);
			}
			for (int i = 0; i < tab.size(); i++) {
				String nb = JOptionPane.showInputDialog(null,
						"Donner le nouveau num�ro de votre doigt !",
						"Transformation du doigt " + tab.get(i) + "?",
						JOptionPane.QUESTION_MESSAGE);
				System.out.println("Donner num doigt : " + nb);
				if (nb != null) {
					while (nb.equals("")) {
						JOptionPane.showMessageDialog(null,
								"Veuillez entrer un num�ro",
								"Doigt " + tab.get(i) + " non modifi�!",
								JOptionPane.INFORMATION_MESSAGE);
						System.out.println("cha�ne vide");
						nb = JOptionPane
								.showInputDialog(null,
										"Donner le num�ro de votre doigt !",
										"Nouveau doigt ?",
										JOptionPane.QUESTION_MESSAGE);
					}

					if (!nb.equals("")) {
						String requeteSQL = "UPDATE `doigt` SET  `idDoigt` = '"
								+ nb + "' WHERE CONCAT(`doigt`.`idDoigt`) = '"
								+ tab.get(i) + "'";
						BDDupdate(requeteSQL);

						JOptionPane.showMessageDialog(null,
								"Le doigt est maintenant : " + nb, "Doigt "
										+ tab.get(i) + " modifi�!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			ArrayList<Object> tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.size() == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'�quipe � supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.size(); i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer l'�quipe " + tab.get(i)
								+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `raidzultat`.`equipe` WHERE CONCAT(`equipe`.`idEquipe`) = '"
							+ tab.get(i) + "'";
					BDDupdate(requeteSQL);

					JOptionPane.showMessageDialog(null,
							"L'�quipe est maintenant supprim�e", "Equipe "
									+ tab.get(i) + " Supprim�e!",
							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Equipe " + tab.get(i) + " Supprim�e");
				}
			}
		}
	}

	public Object[][] updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT * FROM equipe";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			// int i = 0;
			while (res.next()) {
				ArrayData.add(new Object[] { new Boolean(false),
						res.getString(1), res.getString(2), res.getString(3),
						res.getString(4), res.getString(5) });
				System.out.println("Id : " + res.getInt(1) + " nom : "
						+ res.getString(2) + " Grp : " + res.getString(3)
						+ " Diff : " + res.getString(4) + " Type : "
						+ res.getString(5));
				// i++;
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

		System.out.println(data);
		System.out.println("MAJ Table");
		return data;
	}

	public Object[][] ArrayToTab(ArrayList<Object[]> array) {

		int lengthLig = array.size();
		int lengthCol = array.get(1).length - 1;
		Object[][] tab = new Object[lengthLig][lengthCol];
		for (int i = 0; i < lengthLig; i++) {
			tab[i] = array.get(i);
			// System.out.println(tab[i]);
		}
		return tab;
	}

	public ArrayList<Object> getIndexSelectTab(Object[][] table) {
		ArrayList<Object> ArrayDataSelect = new ArrayList<Object>();
		int lig = table.length;
		int col = table[0].length;

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

	public void BDDupdate(String requeteSQL) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
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

	public void BDDquery(String requeteSQL) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
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
