package Interface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
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

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import Models.TabModel;

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
	private JButton creer = new JButton("Créer");

	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idDoigt" };

	int idc;

	public Inte_Doigt(int idC) {

		thePanel = this;
		idc=idC;

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

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);

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
		jScroll.setPreferredSize(new Dimension(600, 400));
		
		panTitre.setBorder(BorderFactory.createTitledBorder("Ici vous pouvez gérer vous différents balises"));
		panTitre.setPreferredSize(new Dimension(750, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.add(panTitre);

		thePanel.add(panMega);
	}

	public class EcouteurCreer implements ActionListener { // Action du creer

		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
			String nb = JOptionPane.showInputDialog(null,
					"Donner le numéro de votre doigt !", "Nouveau doigt ?",
					JOptionPane.QUESTION_MESSAGE);

			if (nb != null) {
				while (nb.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Veuillez entrer un numéro", "Doigt non créé!",
							JOptionPane.INFORMATION_MESSAGE);
					nb = JOptionPane.showInputDialog(null,
							"Donner le numéro de votre doigt !",
							"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);
				}

				// ---- Contrôle utilisateur ----
				// "Ce doigt existe déjà, veuillez entrer un autre numéro"

				if (!nb.equals("")) {
					for (int i = 0; i < data.length; i++) {
						if (nb.equals(data[i][1])) {
							flagExiste = 1;
						}
					}
					if (flagExiste == 1) {
						JOptionPane.showMessageDialog(null,
								"Ce doigt existe déjà", "Doigt non créé!",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						
						String requeteSQL = "INSERT INTO `doigt` (`idDoigt`,`idCompetition`) VALUES ( "
								+ nb +","+ idc+ ")";
						BDDquery(requeteSQL);

						JOptionPane.showMessageDialog(null, "Le doigt est "
								+ nb + ".", "Nouvelle compétition !",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	public class EcouteurModif implements ActionListener { // Action du modif

		public void actionPerformed(ActionEvent arg0) {
			int flagExiste=0;
			ArrayList<Object> tab = getIndexSelectTab(data);

			if (tab.size() == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de doigt à modifier!",
						JOptionPane.INFORMATION_MESSAGE);
			}
			
			for (int i = 0; i < tab.size(); i++) {
				String nb = JOptionPane.showInputDialog(null,
						"Donner le nouveau numéro de votre doigt !",
						"Transformation du doigt " + tab.get(i) + "?",
						JOptionPane.QUESTION_MESSAGE);
				System.out.println("Donner num doigt : " + nb);
				if (nb != null) {
					while (nb.equals("")) {
						JOptionPane.showMessageDialog(null,
								"Veuillez entrer un numéro",
								"Doigt " + tab.get(i) + " non modifié!",
								JOptionPane.INFORMATION_MESSAGE);
						System.out.println("chaîne vide");
						nb = JOptionPane
								.showInputDialog(null,
										"Donner le numéro de votre doigt !",
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
									"Ce doigt existe déjà", "Doigt non modifié!",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							String requeteSQL = "UPDATE `doigt` SET  `idDoigt` = '"
									+ nb
									+ "' WHERE CONCAT(`doigt`.`idDoigt`) = '"
									+ tab.get(i) + "' && `idCompetition` = '"+idc+"'";
							BDDupdate(requeteSQL);

							JOptionPane.showMessageDialog(null,
									"Le doigt est maintenant : " + nb, "Doigt "
											+ tab.get(i) + " modifié!",
									JOptionPane.INFORMATION_MESSAGE);
						}
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
						"Pas de doigt à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.size(); i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer le doigt " + tab.get(i)
								+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `raidzultat`.`doigt` WHERE CONCAT(`doigt`.`idDoigt`) = '"
							+ tab.get(i) + "' && `idCompetition` = '"+idc + "'";
					BDDupdate(requeteSQL);
				
					JOptionPane.showMessageDialog(null,
							"Le doigt est maintenant supprimé",
							"Doigt " + tab.get(i) + " Supprimé!",
							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Doigt " + tab.get(i) + " Supprimé");
				}
			}
		}
	}

	public Object[][] updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT * FROM doigt WHERE `idCompetition` = '"+idc+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
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
					"Pas de connection avec la Base de Données", "Attention",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		data = ArrayToTab(ArrayData/* , title.length - 1 */);

		Interface();

		System.out.println("MAJ Table");
		return data;
	}

	public Object[][] ArrayToTab(ArrayList<Object[]> array) {

		int lengthLig = array.size();
		
		int lengthCol;
		if(lengthLig>0){
		lengthCol = array.get(0).length;
		}else{lengthCol=0;}
		
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
		System.out.println(ArrayDataSelect);

		return ArrayDataSelect;
	}

	public void BDDupdate(String requeteSQL){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url,
					user, passwd);
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
	
	public void BDDquery(String requeteSQL){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url,
					user, passwd);
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
