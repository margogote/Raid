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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

public class Inte_Epreuve extends JPanel {

	/* Panels */
	private JPanel thePanel;
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panTitre = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons
	private JPanel panEpL = new JPanel();

	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();
	private JPanel panBoutAqui = new JPanel();
	private JPanel panCombo = new JPanel();
	private JPanel panEpre = new JPanel();
	private JPanel panZoneAquis = new JPanel();

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");
	private JButton aquis = new JButton("Aquisition");

	private JComboBox<Object> comboEpreuv = new JComboBox<>();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "Check", "idEpreuve", "Nom de l'epreuve",
			"Type", "Difficulté", "Heure début", "Durée" };

	private JLabel epreuveL = new JLabel("Epreuve");

	private int idc;

	public Inte_Epreuve(int idC) {

		thePanel = this;
		idc = idC;

		data = updateTable();

		thePanel.removeAll();
		panMega.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));
		aquis.setPreferredSize(new Dimension(100, 30));

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
				.createTitledBorder("Ici vous pouvez gérer vos différentes épreuves"));
		panTitre.setPreferredSize(new Dimension(750, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panEpL.add(epreuveL);
		panEpre.add(epreuveL);
		panCombo.add(comboEpreuv);
		panBoutAqui.add(aquis);
		
		updateCombo(comboEpreuv);

		panZoneAquis
				.setBorder(BorderFactory
						.createTitledBorder("Ici vous pouvez gérer vous aquisitions par épreuve"));
		panZoneAquis.add(panEpre);
		panZoneAquis.add(panCombo);
		panZoneAquis.add(panBoutAqui);

		panMega.setLayout(new BoxLayout(panMega, BoxLayout.PAGE_AXIS));
		panMega.add(panTitre);
		panMega.add(panZoneAquis);

		thePanel.add(panMega);
		/*
		 * EcouteurModif ecoutModif = new EcouteurModif();
		 * modif.addActionListener(ecoutModif);
		 * 
		 * EcouteurSupp ecoutSupp = new EcouteurSupp();
		 * supp.addActionListener(ecoutSupp);
		 */
		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);

	}

	public class EcouteurCreer implements ActionListener { // Action du creer

		public void actionPerformed(ActionEvent arg0) {

			Inte_Epreuve_CreaModif formulaire = new Inte_Epreuve_CreaModif(idc,
					panMega);

			// updateTable();
		}
	}

	public Object[][] updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT * FROM epreuve WHERE `idCompetition` = '"
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
						res.getString(1), res.getString(2), res.getString(3),
						res.getString(4), res.getString(5), res.getString(6) });
				System.out.println("Id : " + res.getInt(1) + " nom : "
						+ res.getString(2) + " Type : " + res.getString(3)
						+ " Diff : " + res.getString(4) + " Date : "
						+ res.getString(5) + " Durée : " + res.getString(6));
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

		panMega.revalidate();

		System.out.println(data);
		System.out.println("MAJ Table");
		return data;
	}

	public void updateCombo(JComboBox<Object> combo) {
		combo.removeAllItems();
		String requeteSQL = "SELECT nomEpreuve FROM epreuve WHERE idCompetition = '"+idc+"'";

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

		}  catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("MAJ Combo");
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
