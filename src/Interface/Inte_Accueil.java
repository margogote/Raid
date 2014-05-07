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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import BDD.Connect;

public class Inte_Accueil {

	JFrame fen = new JFrame();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* --- Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panAccueil = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* --- Boutons */
	private JButton entrerCompet = new JButton("Entrer dans la compétition");
	private JButton modifCompet = new JButton("Modifier la compétition");
	private JButton suppCompet = new JButton("Supprimer la compétition");
	private JButton creerCompet = new JButton("Créer une compétition");
	private JButton quitter = new JButton("Quitter");

	/* --- BDD Liste des compet */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	// private String[] competitions = { "compet1", "la 2é", "la 3é" };
	private JComboBox<Object> compets = new JComboBox<Object>();
	// private JComboBox<Object> compets;

	JLabel bjr = new JLabel(
			"Bienvenu(e) sur Raidzultats, l'application qui permet de gérer le classement d'un Raid");

	public Inte_Accueil() {

		updateCombo(compets);

		fen.setTitle("Raidzultats"); // titre
		fen.setSize(800, 600); // taille de la fenetre
		fen.setLocationRelativeTo(null); // centre la fenetre
		fen.setResizable(false);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setLayout(new BorderLayout()); // Pour les placements

		entrerCompet.setPreferredSize(new Dimension(200, 30));
		modifCompet.setPreferredSize(new Dimension(200, 30));
		suppCompet.setPreferredSize(new Dimension(200, 30));
		creerCompet.setPreferredSize(new Dimension(200, 30));
		quitter.setPreferredSize(new Dimension(200, 30));

		JPanel panCompet = new JPanel();
		JPanel panBoutEntrer = new JPanel();
		JPanel panBoutModif = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutQ = new JPanel();

		panCompet.add(compets);
		panBoutEntrer.add(entrerCompet);
		panBoutModif.add(modifCompet);
		panBoutSupp.add(suppCompet);
		panBoutCreer.add(creerCompet);
		panBoutQ.add(quitter);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		// panBoutonsListe.setLayout(new BorderLayout());
		panBoutonsListe.add(panCompet);
		panBoutonsListe.add(panBoutEntrer);
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutQ);

		panAccueil.setLayout(new BoxLayout(panAccueil, BoxLayout.PAGE_AXIS));
		panAccueil.setLayout(new BorderLayout());
		panAccueil.add(bjr, BorderLayout.NORTH);
		panAccueil.add(panBoutonsListe, BorderLayout.CENTER);
		// panAccueil.add(panBoutonsListe, BorderLayout.CENTER);

		panMega.add(panAccueil);
		// fen.add(panAccueil, BorderLayout.CENTER);
		fen.add(panMega, BorderLayout.CENTER);

		fen.setVisible(true);

		/* Ecouteurs */
		EcouteurEntrer ecoutEntrer = new EcouteurEntrer();
		entrerCompet.addActionListener(ecoutEntrer);

		EcouteurModif ecoutModif = new EcouteurModif();
		modifCompet.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		suppCompet.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creerCompet.addActionListener(ecoutCreer);

		EcouteurQ ecoutQ = new EcouteurQ();
		quitter.addActionListener(ecoutQ);

	}

	public class EcouteurEntrer implements ActionListener { // Action du Entrer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet, on le stock
			// on lance la page suivante
			int id = getIndex(compets);

			String nomC = (String) compets.getSelectedItem();

			int idC = getSelectID(compets);
			// Inte_monAppli app = new Inte_monAppli(id);
			Inte_monAppli app = new Inte_monAppli(nomC, idC);
			fen.dispose();

		}
	}

	public class EcouteurCreer implements ActionListener { // Action du creer

		public void actionPerformed(ActionEvent arg0) {
			// String nom="test";
			// JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nom = JOptionPane.showInputDialog(null,
					"Donner le nom de votre compétition !",
					"Nouvelle compétition ?", JOptionPane.QUESTION_MESSAGE);
			if (nom != null) {
				while (nom.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Veuillez entrer un nom", "Compétition non créée!",
							JOptionPane.ERROR_MESSAGE);
					nom = JOptionPane.showInputDialog(null,
							"Donner le nom de votre compétition !",
							"Nouvelle compétition ?",
							JOptionPane.QUESTION_MESSAGE);
				}
					if (!nom.equals("")) {
						String requeteSQL = "INSERT INTO `competition` (`nomCompetition`)VALUES ( '"
								+ nom + "')";
						BDDquery(requeteSQL);

						JOptionPane.showMessageDialog(null,
								"La compétition est " + nom + ".",
								"Nouvelle compétition !",
								JOptionPane.INFORMATION_MESSAGE);

						updateCombo(compets);
					
				}
			}
		}
	}

	public class EcouteurModif implements ActionListener { // Action du modifier

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on lance le formulaire pré-remplit

			int id;
			id = getIndex(compets);
			String nomAv = (String) compets.getSelectedItem();

			// JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nom = JOptionPane.showInputDialog(null,
					"Donner le nouveau nom de votre competition !",
					"Modifier compétition ?", JOptionPane.QUESTION_MESSAGE);

			if (nom != null) {
				while (nom.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Veuillez entrer un nom", "Compétition " + id
									+ " non modifiée!",
							JOptionPane.ERROR_MESSAGE);
					nom = JOptionPane.showInputDialog(null,
							"Donner le nom de votre compétition !",
							"Nouvelle compétition ?",
							JOptionPane.QUESTION_MESSAGE);
				}
				if (!nom.equals("")) {
					String requeteSQL = "UPDATE `raidzultat`.`competition` SET `nomCompetition` = '"
							+ nom
							+ "' WHERE `nomCompetition` = '"
							+ nomAv
							+ "'";
					BDDupdate(requeteSQL);

					JOptionPane.showMessageDialog(null,
							"La competition est maintenant : " + nom,
							"Compétition " + id + " modifiée!",
							JOptionPane.INFORMATION_MESSAGE);

					updateCombo(compets);

				}
			}
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			// int id;
			String nom = (String) compets.getSelectedItem();
			int rep = 0;
			// JOptionPane jop2 = new JOptionPane();
			// id = compets.getSelectedIndex() + 1;

			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer la compétition " + nom
							+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				String requeteSQL = "DELETE FROM `raidzultat`.`competition` WHERE `competition`.`nomCompetition` = '"
						+ nom + "'";
				BDDupdate(requeteSQL);

				JOptionPane.showMessageDialog(null,
						"La competition est maintenant supprimée",
						"Compétition " + nom + " Supprimée!",
						JOptionPane.INFORMATION_MESSAGE);

				System.out.println("Compet " + nom + " Supprimée");
				updateCombo(compets);
			}
		}
	}

	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment quitter?", "Attention",
					JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				fen.dispose();
			}

		}
	}

	public void updateCombo(JComboBox<Object> combo) {

		compets.removeAllItems();
		String requeteSQL = "SELECT nomCompetition FROM competition";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				compets.addItem(res.getString(1));
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

		System.out.println("MAJ Combo");

	}

	public int getIndex(JComboBox<Object> combo) {
		int id;
		id = combo.getSelectedIndex() + 1;
		return id;
	}

	public int getSelectID(JComboBox<Object> combo) {
		String nom = (String) combo.getSelectedItem();
		int iDSelect = -1;
		try {
			String requeteSQL = "SELECT idCompetition FROM competition WHERE `nomCompetition` = '"
					+ nom + "'";
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				iDSelect = res.getInt(1);
				System.out.println("Id Compet select : " + iDSelect);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iDSelect;
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
