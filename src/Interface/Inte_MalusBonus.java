package Interface;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import BDD.DataSourceProvider;
import Interface.Inte_Equipe.EcouteurCreer;
import Interface.Inte_Equipe.EcouteurModif;
import Interface.Inte_Equipe.EcouteurSupp;
import Models.TabModel;

public class Inte_MalusBonus extends JPanel {

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
	private JButton creer = new JButton("Créer");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "", "nomMalusBonus", "tempsMalusBonus", "idEpreuve" };

	int idc;

	public Inte_MalusBonus(int idC) {
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

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(600, 400));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer vos différentes Malus & Bonus"));
		panTitre.setPreferredSize(new Dimension(750, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.add(panTitre);

		/*
		 * panMega.setLayout(new BorderLayout()); panMega.add(bjr,
		 * BorderLayout.NORTH); panMega.add(panTitre, BorderLayout.WEST);
		 * panMega.add(new JScrollPane(tableau), BorderLayout.CENTER);
		 */

		thePanel.add(panMega);
	}

	public class EcouteurCreer implements ActionListener, WindowListener { // Action
																			// du
																			// creer
		public void actionPerformed(ActionEvent arg0) {

			Inte_Equipe_CreaModif formulaire = new Inte_Equipe_CreaModif(idc,
					-1);
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

	public class EcouteurModif implements ActionListener, WindowListener { // Action
																			// du
																			// modif

		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
			int[] tab = getIndexSelectTab(data);

			if (tab.length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas de malus/bonus à modifier!",
						JOptionPane.INFORMATION_MESSAGE);
			}
			for (int i = 0; i < tab.length; i++) {

				Inte_Equipe_CreaModif formulaire = new Inte_Equipe_CreaModif(
						idc, tab[i]);

				formulaire.addWindowListener(this);
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

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int[] tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab.length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'équipe à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (int i = 0; i < tab.length; i++) {
				rep = JOptionPane.showConfirmDialog(null,
						"Voulez vous vraiment supprimer l'équipe " + tab[i]
								+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

				if (rep == 0) {
					String requeteSQL = "DELETE FROM `raidzultat`.`malusbonus` WHERE CONCAT(`malusbonus`.`nomMalusBonus`) = '"
							+ tab[i] + "' && `idCompetition` = '" + idc + "'";
					BDDupdate(requeteSQL);

					JOptionPane.showMessageDialog(null,
							"L'équipe est maintenant supprimée", "Equipe "
									+ tab[i] + " Supprimée!",
							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Equipe " + tab[i] + " Supprimée");
				}
			}
		}
	}

	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		// String requeteSQL =
		// "SELECT * FROM equipe WHERE `idCompetition` = '"+idc+"'";
		String requeteSQL = "SELECT malusbonus.`idEquipe`, malusbonus.`nomEquipe`, malusbonus.`nomGroupe` FROM equipe WHERE malusbonus.`idCompetition` = '"
				+ idc + "' && malusbonus.`idEquipe` = '" + idc + "'";

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
						res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7) });
				System.out.println("Id : " + res.getInt(1) + " nom : "
						+ res.getString(2) + " Grp : " + res.getString(3)
						+ " Diff : " + res.getString(4) + " Type : "
						+ res.getString(5) + " Dossart : " + res.getInt(6)
						+ " Doigt : " + res.getString(7));
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

	public int[] getIndexSelectTab(Object[][] table) {
		ArrayList<Integer> ArrayDataSelect = new ArrayList<Integer>();
		int lig = table.length;
		int col = table[0].length;

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

	public void BDDquery(String requeteSQL) {
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
