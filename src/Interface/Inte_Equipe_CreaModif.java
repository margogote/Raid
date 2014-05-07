package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import Interface.Inte_Menu.EcouteurQ;

public class Inte_Equipe_CreaModif extends JFrame {

	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	JFrame thePanel = new JFrame();

	JLabel bjr = new JLabel(
			"Ici vous pouvez créer/modifier vous différentes équipes");

	JLabel nomL = new JLabel("Nom");
	JTextField nomT = new JTextField("");

	JLabel doigtL = new JLabel("Doigt");
	private JComboBox<Object> doigtC = new JComboBox<Object>();

	JLabel dossartL = new JLabel("Dossart");
	JTextField dossartT = new JTextField("");

	JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	JLabel grpL = new JLabel("Groupe");
	private String[] grpS = { "CHOISIR", "HEI", "Entreprise1", "Entreprise2" };
	private JComboBox<Object> grpC = new JComboBox<Object>(grpS);

	JLabel catL = new JLabel("Catégorie");
	private String[] categorie = { "CHOISIR", "Masculin", "Feminin", "Mixte" };
	private JComboBox<Object> catC = new JComboBox<Object>(categorie);

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private int idc;
	private JPanel panel;

	Inte_Equipe_CreaModif(int idC, JPanel pan) {
		thePanel = this;
		idc = idC;
		panel=pan;

		thePanel.setTitle("Raidzultats"); // titre
		thePanel.setSize(800, 600);
		// taille de la fenetre
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel nomP = new JPanel();
		nomP.add(nomL);
		nomP.add(nomT);

		JPanel doigtP = new JPanel();
		doigtP.add(doigtL);
		updateDoigt();

		JPanel dossartP = new JPanel();
		dossartP.add(dossartL);

		JPanel diffP = new JPanel();
		diffP.add(difficL);
		diffP.add(difficC);

		JPanel grpP = new JPanel();
		grpP.add(grpL);

		JPanel catP = new JPanel();
		catP.add(catL);
		catP.add(catC);

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(100, 100));
		megaP.setLayout(new GridLayout(6, 2));
		// megaP.setLayout(new BoxLayout(megaP, BoxLayout.PAGE_AXIS));
		megaP.add(nomL);
		megaP.add(nomT);
		megaP.add(doigtL);
		megaP.add(doigtC);
		megaP.add(dossartL);
		megaP.add(dossartT);
		megaP.add(difficL);
		megaP.add(difficC);
		megaP.add(grpL);
		megaP.add(grpC);
		megaP.add(catL);
		megaP.add(catC);

		/*
		 * megaP.add(nomP); megaP.add(doigtP); megaP.add(dossartP);
		 * megaP.add(diffP); megaP.add(grpP); megaP.add(catP);
		 */

		JPanel gigaP = new JPanel();
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(bjr);
		gigaP.add(megaP);
		gigaP.add(btnP);

		thePanel.add(gigaP);

		thePanel.setVisible(true);

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

	}

	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String nom = nomT.getText();
			String doigt = (String) doigtC.getSelectedItem();
			String dossart = dossartT.getText();
			String difficulte = (String) difficC.getSelectedItem();
			String groupe = (String) grpC.getSelectedItem();
			String categorie = (String) catC.getSelectedItem();
			int idE = -1;

			if (nom.equals("") || doigt.equals("CHOISIR") || dossart.equals("")
					|| difficulte.equals("CHOISIR") || groupe.equals("CHOISIR")
					|| categorie.equals("CHOISIR")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Equipe non créée!", JOptionPane.INFORMATION_MESSAGE);
			} else {
				String requeteSQL = "INSERT INTO `raidzultat`.`equipe` (`idEquipe`, `nomEquipe`, `nomGroupe`, `typeDifficulte`, `typeEquipe`, `idCompetition`) VALUES (NULL, '"
						+ nom
						+ "', '"
						+ groupe
						+ "', '"
						+ difficulte
						+ "', '"
						+ categorie + "', '" + idc + "')";
				BDDquery(requeteSQL);

				try {
					String requeteSQL2 = "SELECT `idEquipe` FROM `equipe` WHERE `nomEquipe`= '"
							+ nom + "' && `idCompetition`='" + idc + "'";
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DriverManager.getConnection(url, user,
							passwd);
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					ResultSet res = stm.executeQuery(requeteSQL2);

					while (res.next()) {
						idE = res.getInt(1);
						System.out.println("Num Equipe : " + res.getInt(1));
					}

					conn.close();
					res.close();

				} catch (CommunicationsException com) {
					JOptionPane.showMessageDialog(null,
							"Pas de connection avec la Base de Données",
							"Attention", JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e) {
					e.printStackTrace();
				}

				String requeteSQL3 = "INSERT INTO `raidzultat`.`posséder` (`idDoigt`, `idEquipe`, `dateHeureAttribution`,`idCompetition`) VALUES ('"
						+ doigt + "', '" + idE + "', NULL,'" + idc + "')";
				BDDquery(requeteSQL3);

				//Inte_Equipe equ = new Inte_Equipe(idc);
				//equ.updateTable();
				panel.revalidate();
				panel.repaint();
				thePanel.dispose();
			}

		}
	}

	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	public void updateDoigt() {
		String requeteSQL = "SELECT `idDoigt` FROM `doigt` WHERE `idDoigt` NOT IN ( SELECT `idDoigt` FROM `posséder` WHERE `idCompetition` = '"
				+ idc + "') && `idCompetition` = '" + idc + "'";

		doigtC.removeAllItems();
		doigtC.addItem("CHOISIR");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				doigtC.addItem(res.getString(1));
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
