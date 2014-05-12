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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import BDD.DataSourceProvider;
import Interface.Inte_Menu.EcouteurQ;

public class Inte_Equipe_CreaModif extends JFrame {

	JFrame thePanel = new JFrame();

	JLabel nomL = new JLabel("Nom");
	JTextField nomT = new JTextField("");

	JLabel doigtL = new JLabel("Doigt");
	private JComboBox<Object> doigtC = new JComboBox<Object>();

	JLabel dossardL = new JLabel("dossard");
	JTextField dossardT = new JTextField("");

	JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	JLabel grpL = new JLabel("Groupe");
	private String[] grpS = { "CHOISIR", "Etudiant", "Salarié" };
	private JComboBox<Object> grpC = new JComboBox<Object>(grpS);

	JLabel catL = new JLabel("Catégorie");
	private String[] categorie = { "CHOISIR", "Masculin", "Feminin", "Mixte" };
	private JComboBox<Object> catC = new JComboBox<Object>(categorie);

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private int idc;
	private int modif;

	Inte_Equipe_CreaModif(int idC, int idModif) {
		thePanel = this;
		idc = idC;
		modif = idModif;

		thePanel.setTitle("Raidzultats - Création équipe");
		thePanel.setSize(500, 350);
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		// thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		if (idModif != -1) {
			thePanel.setTitle("Raidzultats - Modification équipe " + idModif);

			String requeteSQL = "SELECT equipe.`nomEquipe`, equipe.`nomGroupe`, equipe.`typeDifficulte`, equipe.`typeEquipe`, equipe.`dossard`, posséder.`idDoigt` FROM equipe INNER JOIN posséder ON equipe.`idEquipe`=posséder.`idEquipe` WHERE equipe.`idCompetition` = '"
					+ idc
					+ "' && posséder.`idCompetition` = '"
					+ idc
					+ "' && equipe.`idEquipe` = '" + idModif + "'";

			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource()
						.getConnection();
				System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);

				while (res.next()) {
					nomT.setText(res.getString(1));
					grpC.setSelectedItem(res.getString(2));
					difficC.setSelectedItem(res.getString(3));
					catC.setSelectedItem(res.getString(4));
					dossardT.setText(res.getString(5));
					doigtC.addItem(res.getString(6));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel nomP = new JPanel();
		nomP.add(nomL);
		nomP.add(nomT);

		JPanel doigtP = new JPanel();
		doigtP.add(doigtL);
		updateDoigt();

		JPanel dossardP = new JPanel();
		dossardP.add(dossardL);

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
		megaP.setPreferredSize(new Dimension(450, 225));
		megaP.setLayout(new GridLayout(6, 2));
		megaP.add(nomL);
		megaP.add(nomT);
		megaP.add(doigtL);
		megaP.add(doigtC);
		megaP.add(dossardL);
		megaP.add(dossardT);
		megaP.add(difficL);
		megaP.add(difficC);
		megaP.add(grpL);
		megaP.add(grpC);
		megaP.add(catL);
		megaP.add(catC);

		JPanel gigaP = new JPanel();
		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez créer/modifier vos différentes équipes"));
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(megaP);
		gigaP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(gigaP);

		thePanel.add(panPan);

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
			String dossard = dossardT.getText();
			String difficulte = (String) difficC.getSelectedItem();
			String groupe = (String) grpC.getSelectedItem();
			String categorie = (String) catC.getSelectedItem();
			int idE = -1;

			if (nom.equals("") || doigt.equals("CHOISIR") || dossard.equals("")
					|| difficulte.equals("CHOISIR") || groupe.equals("CHOISIR")
					|| categorie.equals("CHOISIR")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Equipe non créée!", JOptionPane.WARNING_MESSAGE);

			} else if (modif == -1) {
				try {
					Integer.parseInt(dossard);
					System.out.println("C'est un entier");

					String requeteSQL = "INSERT INTO `raidzultat`.`equipe` (`idEquipe`, `nomEquipe`, `nomGroupe`, `typeDifficulte`, `typeEquipe`,`dossard`, `idCompetition`) VALUES (NULL, '"
							+ nom
							+ "', '"
							+ groupe
							+ "', '"
							+ difficulte
							+ "', '"
							+ categorie
							+ "', '"
							+ dossard
							+ "', '"
							+ idc + "')";
					BDDupdate(requeteSQL);

					try {
						String requeteSQL2 = "SELECT `idEquipe` FROM `equipe` WHERE `nomEquipe`= '"
								+ nom + "' && `idCompetition`='" + idc + "'";
						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DataSourceProvider.getDataSource()
								.getConnection();
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
					BDDupdate(requeteSQL3);

					thePanel.dispose();
				} catch (Exception e) {
					System.out
							.println("Je ne suis pas un entier");
					JOptionPane
							.showMessageDialog(
									null,
									"Attention entrer un entier comme numéro de dossard",
									"Equipe non créée!",
									JOptionPane.WARNING_MESSAGE);
				}
			} else {
				try {
					Integer.parseInt(dossard);
					System.out.println("C'est un entier");
					String requeteSQL = "UPDATE`raidzultat`.`equipe` SET `nomEquipe` = '"
							+ nom
							+ "',`nomGroupe` = '"
							+ groupe
							+ "', `typeDifficulte`='"
							+ difficulte
							+ "', `typeEquipe`='"
							+ categorie
							+ "',`dossard`=  '"
							+ dossard
							+ "' WHERE idEquipe = '"
							+ modif
							+ "' && `idCompetition`='" + idc + "'";

					BDDupdate(requeteSQL);

					String requeteSQL3 = "UPDATE `raidzultat`.`posséder` SET `idDoigt` = '"
							+ doigt
							+ "', `dateHeureAttribution`= NULL WHERE idEquipe = '"
							+ modif + "' && `idCompetition`='" + idc + "'";
					BDDupdate(requeteSQL3);

					thePanel.dispose();
				}

				catch (Exception e) {
					System.out
							.println("Je ne suis pas un entier");
					JOptionPane
							.showMessageDialog(
									null,
									"Attention entrer un entier comme numéro de dossard",
									"Equipe non modifiée!",
									JOptionPane.WARNING_MESSAGE);
				}
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

		doigtC.addItem("CHOISIR");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
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

		System.out.println("MAJ Combo doigt");
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
