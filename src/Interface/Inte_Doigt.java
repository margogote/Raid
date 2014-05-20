package Interface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import BDD.DataSourceProvider;
import Models.TabModel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

/**
 * Onglet de gestion des doigts : permet le permet de cr�er, modifier et
 * supprimer des doigts
 * 
 * @author Margaux
 * 
 */
public class Inte_Doigt extends JPanel {

	/* Panels */
	private JPanel thePanel;
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panTitre = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Cr�er");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idDoigt" };

	int idc;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la comp�tition �tudi�e
	 */
	public Inte_Doigt(int idC) {

		thePanel = this;
		idc = idC;

		updateTable();

		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);
	}

	/**
	 * Fonction g�rant l'interface du panel
	 */
	public void Interface() {

		thePanel.removeAll();
		panTitre.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);

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
		jScroll.setPreferredSize(new Dimension(600, 400));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez g�rer vos diff�rents doigts"));
		panTitre.setPreferredSize(new Dimension(750, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.add(panTitre);

		thePanel.add(panMega);
	}

	/**
	 * Permet de g�rer les clics du type "Cr�er" Recup�ration de la saisie
	 * Insertion dans la BDD
	 */
	public class EcouteurCreer implements ActionListener { // Action du creer

		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
			String nb = JOptionPane.showInputDialog(null,
					"Donner le num�ro de votre doigt !", "Nouveau doigt ?",
					JOptionPane.QUESTION_MESSAGE);

			if (nb != null) {
				while (nb.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Veuillez entrer un num�ro positif",
							"Doigt non cr��!", JOptionPane.WARNING_MESSAGE);
					nb = JOptionPane.showInputDialog(null,
							"Donner le num�ro de votre doigt !",
							"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);
				}

				// ---- Contr�le utilisateur ----
				// "Ce doigt existe d�j�, veuillez entrer un autre num�ro"

				if (!nb.equals("")) {
					for (int i = 0; i < data.length; i++) {
						if (nb.equals(data[i][1])) {
							flagExiste = 1;
						}
					}
					if (flagExiste == 1) {
						JOptionPane.showMessageDialog(null,
								"Ce doigt existe d�j�", "Doigt non cr��!",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						try {
							Integer.parseInt(nb);
							System.out.println("C'est un entier");

							if (Integer.parseInt(nb) > 0) {

								String requeteSQL = "INSERT INTO `doigt` (`idDoigt`,`idCompetition`) VALUES ( "
										+ nb + "," + idc + ")";
								BDDupdate(requeteSQL);

								JOptionPane.showMessageDialog(null,
										"Le doigt est " + nb + ".",
										"Nouveau doigt !",
										JOptionPane.INFORMATION_MESSAGE);

							} else {
								JOptionPane.showMessageDialog(null,
										"Ce nombre doit �tre positif",
										"Doigt non cr��!",
										JOptionPane.INFORMATION_MESSAGE);
							}

						} catch (Exception e) {
							System.out.println("Je ne suis pas un entier");
							JOptionPane
									.showMessageDialog(
											null,
											"Attention entrer un entier comme num�ro de doigt",
											"Doigt non cr��!",
											JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		}
	}

	/**
	 * Permet de g�rer les clics du type "Modifier" Recup�ration de la saisie
	 * modification dans la BDD
	 */
	public class EcouteurModif implements ActionListener { // Action du modif

		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
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
								"Veuillez entrer un num�ro positif", "Doigt "
										+ tab.get(i) + " non modifi�!",
								JOptionPane.INFORMATION_MESSAGE);
						System.out.println("cha�ne vide");
						nb = JOptionPane
								.showInputDialog(null,
										"Donner le num�ro de votre doigt !",
										"Nouveau doigt ?",
										JOptionPane.QUESTION_MESSAGE);
					}

					if (!nb.equals("")) {
						for (int j = 0; j < data.length; j++) {
							if (nb.equals(data[j][1])) {
								flagExiste = 1;
							}
						}
						if (flagExiste == 1) {
							JOptionPane.showMessageDialog(null,
									"Ce doigt existe d�j�",
									"Doigt non modifi�!",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							try {
								Integer.parseInt(nb);
								System.out.println("C'est un entier");

								if (Integer.parseInt(nb) > 0) {
									String requeteSQL = "UPDATE `doigt` SET  `idDoigt` = '"
											+ nb
											+ "' WHERE CONCAT(`doigt`.`idDoigt`) = '"
											+ tab.get(i)
											+ "' && `idCompetition` = '"
											+ idc
											+ "'";
									BDDupdate(requeteSQL);

									JOptionPane
											.showMessageDialog(
													null,
													"Le doigt est maintenant : "
															+ nb,
													"Doigt " + tab.get(i)
															+ " modifi�!",
													JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(null,
											"Ce nombre doit �tre positif",
											"Doigt non modifi�!",
											JOptionPane.INFORMATION_MESSAGE);
								}
							} catch (Exception e) {
								System.out.println("Je ne suis pas un entier");
								JOptionPane
										.showMessageDialog(
												null,
												"Attention entrer un entier positif comme num�ro de doigt",
												"Doigt non modifi�!",
												JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Permet de g�rer les clics du type "Supprimer" suppression dans la BDD
	 */
	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			ArrayList<Object> tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.size() == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de doigt � supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.size(); i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer le doigt " + tab.get(i)
								+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `doigt` WHERE CONCAT(`doigt`.`idDoigt`) = '"
							+ tab.get(i)
							+ "' && `idCompetition` = '"
							+ idc
							+ "'";
					BDDupdate(requeteSQL);

					JOptionPane.showMessageDialog(null,
							"Le doigt est maintenant supprim�",
							"Doigt " + tab.get(i) + " Supprim�!",
							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Doigt " + tab.get(i) + " Supprim�");
				}
			}
		}
	}

	/**
	 * Fonction mettant � jour le tableau
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT * FROM doigt WHERE `idCompetition` = '"
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
				ArrayData.add(new Object[] { new Boolean(false),
						res.getString(1) });
				System.out.println("Nom : " + res.getString(1));
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

		if (data.length == 0) {
			modif.setEnabled(false);
			supp.setEnabled(false);
		} else {
			modif.setEnabled(true);
			supp.setEnabled(true);
		}

		System.out.println("MAJ Table");
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
			// System.out.println(tab[i]);
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
		System.out.println(ArrayDataSelect);

		return ArrayDataSelect;
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
			updateTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
