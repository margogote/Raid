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

public class Inte_Balise  extends JPanel{
	
	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBalises = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");

	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";
	
	/* Tableau */
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idBalise" };
	private ArrayList<Object> ArrayDataLig;
	
	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents balises");
	
	public Inte_Balise(){
		
		data = updateTable();
		
		modif.setPreferredSize(new Dimension(200, 30));
		creer.setPreferredSize(new Dimension(200, 30));
		supp.setPreferredSize(new Dimension(200, 30));
		
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutModif = new JPanel();
		
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);
		
		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutCreer);

		panBalises.setLayout(new BoxLayout(panBalises, BoxLayout.PAGE_AXIS));
		panBalises.setLayout(new BorderLayout());
		panBalises.add(bjr, BorderLayout.NORTH);
		panBalises.add(panBoutonsListe, BorderLayout.CENTER);
		
		panMega.add(panBalises);

		TabModel tabModel = new TabModel(data, title);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !

		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);

		panMega.add(new JScrollPane(tableau));
		
		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);
		
		this.add(panMega);

		
	}

	
	public class EcouteurCreer implements ActionListener { // Action du creer

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {

			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nb = jop.showInputDialog(null,
					"Donner le numéro de votre balise !", "Nouvelle balise ?",
					JOptionPane.QUESTION_MESSAGE);

			while (nb.equals("")) {
				jop2.showMessageDialog(null, "Veuillez entrer un nombre",
						"Balise non créée!", JOptionPane.INFORMATION_MESSAGE);
				nb = jop.showInputDialog(null,
						"Donner le numéro de votre balise !", "Nouvelle balise ?",
						JOptionPane.QUESTION_MESSAGE);
			}

			// ---- Contrôle utilisateur ----
			// "Cette balise existe déjà, veuillez entrer un autre numéro"

			if (!nb.equals("")) {
				try {
					String requeteSQL = "INSERT INTO `balise` (`idBalise`) VALUES ( "
							+ nb + ")";
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DriverManager.getConnection(url, user,
							passwd);
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					int res = stm.executeUpdate(requeteSQL);

					System.out.println("Nb enregistrement : " + res);

					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				updateTable();
				/*// tableau.repaint();
				panMega.repaint();
*/
				jop2.showMessageDialog(null, "La balise est " + nb + ".",
						"Nouvelle compétition !",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public class EcouteurModif implements ActionListener { // Action du quitter

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {
			ArrayList<Object> tab = getIndexSelectTab(data);
			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();

			for (int i = 0; i < tab.size(); i++) {
				String nb = jop.showInputDialog(null,
						"Donner le nouveau numéro de votre balise !",
						"Transformation de la balise " + tab.get(i) + "?",
						JOptionPane.QUESTION_MESSAGE);

				while (nb.equals("")) {
					jop2.showMessageDialog(null, "Veuillez entrer un numéro",
							"Balise " + tab.get(i) + " non modifiée!",
							JOptionPane.INFORMATION_MESSAGE);
					nb = jop.showInputDialog(null,
							"Donner le numéro de votre balise !",
							"Nouvelle balise ?", JOptionPane.QUESTION_MESSAGE);
				}
				if (!nb.equals("")) {
					try {
						// System.out.println(tab.get(0));
						String requeteSQL = "UPDATE `balise` SET  `idBalise` = '"
								+ nb + "' WHERE CONCAT(`balise`.`idBalise`) = '"
								+ tab.get(i) + "'";
						// System.out.println(requeteSQL);
						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DriverManager.getConnection(url,
								user, passwd);
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();
						int res = stm.executeUpdate(requeteSQL);

						System.out.println("Nb enregistrement : " + res);

						conn.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

					jop2.showMessageDialog(null, "La balise est maintenant : "
							+ nb, "Balise " + tab.get(i) + " modifiée!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				updateTable();
			}
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			ArrayList<Object> tab = getIndexSelectTab(data);
			int rep = 0;
			JOptionPane jop2 = new JOptionPane();

			for (int i = 0; i < tab.size(); i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer la balise " + tab.get(i)
								+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {

					try {

						String requeteSQL = "DELETE FROM `raidzultat`.`balise` WHERE CONCAT(`balise`.`idBalise`) = '"
								+ tab.get(i) + "'";
						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DriverManager.getConnection(url,
								user, passwd);
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();
						int res = stm.executeUpdate(requeteSQL);

						System.out.println("Nb enregistrement : " + res);

						conn.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

					jop2.showMessageDialog(null,
							"La balise est maintenant supprimée",
							"Balise " + tab.get(i) + " Supprimée!",
							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Balise " + tab.get(i) + " Supprimée");
					updateTable();
				}
			}
		}
	}

	public Object[][] updateTable() {
		ArrayList<Object[]> ArrayData = new ArrayList<>();
		;
		String requeteSQL = "SELECT * FROM balise";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			int i = 0;
			while (res.next()) {
				ArrayData.add(new Object[] { new Boolean(false),
						res.getString(1) });
				System.out.println("Nom : " + res.getString(1));
				i++;
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		data = ArrayToTab(ArrayData, title.length - 1);
		// TabModel tabModel = new TabModel(data, title);
		// panMega.removeAll();
		// panMega.validate();
		panMega.repaint();
		System.out.println("MAJ Table");
		return data;
	}

	public Object[][] ArrayToTab(ArrayList<Object[]> array, int lengthCol) {

		int lengthLig = array.size();
		// int lengthCol = ArrayData.get(1).length();
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
		/*
		 * for (int i = 0; i < ArrayDataSelect.size(); i++) { tab[i] =
		 * ArrayDataSelect.get(i); } //System.out.println(tab); return tab;
		 */
		return ArrayDataSelect;
	}
}
