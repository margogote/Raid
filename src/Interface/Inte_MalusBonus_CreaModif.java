package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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

import BDD.DataSourceProvider;

/**
 * Formulaire permettant de renseigner les informations pour créer/modifier un Malus/Bonus
 * 
 * @author Margaux
 * 
 */
public class Inte_MalusBonus_CreaModif extends JFrame {

	JFrame thePanel = new JFrame();

	JLabel nomL = new JLabel("Nom");
	JTextField nomT = new JTextField("");

	JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Bonus", "Malus" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	JLabel tpsL = new JLabel("Temps");
	JTextField tpsT = new JTextField("hh:mm:ss");

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private int idc;
	private int modif;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idModif
	 *            , l'id de l'équipe à modifier
	 */
	public Inte_MalusBonus_CreaModif(int idC, int idModif) {

		thePanel = this;
		idc = idC;
		modif = idModif;

		thePanel.setTitle("Raidzultats - Création Malus/Bonus");
		thePanel.setSize(450, 220);
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		if (idModif != -1) {
			thePanel.setTitle("Raidzultats - Modification Malus/Bonus "
					+ idModif);

			String requeteSQL = "SELECT * FROM malusbonus WHERE `idCompetition` = '"
					+ idc + "' && `idMB` = '" + idModif + "'";

			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource()
						.getConnection();
				System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);

				while (res.next()) {
					nomT.setText(res.getString(2));
					typeC.setSelectedIndex(res.getInt(3)+1);
					tpsT.setText(res.getString(4));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(400, 100));
		megaP.setLayout(new GridLayout(3, 2));
		megaP.add(nomL);
		megaP.add(nomT);
		megaP.add(typeL);
		megaP.add(typeC);
		megaP.add(tpsL);
		megaP.add(tpsT);

		JPanel gigaP = new JPanel();
		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez créer/modifier vos différents malus/bonus"));
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

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre de modification
	 * d'épreuve. Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurOK implements ActionListener { // Action du Ok

		public void actionPerformed(ActionEvent arg0) {
			String nom = nomT.getText();
			
			int type = typeC.getSelectedIndex()-1;
			String tps = tpsT.getText();

			if (nom.equals("") || type == -1 || tps.equals("")
					|| tps.equals("hh:mm:ss")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Malus/Bonus non créée!", JOptionPane.WARNING_MESSAGE);

			} else if (modif == -1) {
				String requeteSQL = "INSERT INTO `raidzultat`.`malusbonus` (`nomMalusBonus`, `malus`, `tempsMalusBonus`, `idCompetition`) VALUES ('"
						+ nom
						+ "', '"
						+ type
						+ "', '"
						+ tps
						+ "', '"
						+ idc
						+ "')";
				BDDupdate(requeteSQL);

				thePanel.dispose();
			} else {
				String requeteSQL = "UPDATE`raidzultat`.`malusbonus` SET `nomMalusBonus` = '"
						+ nom
						+ "',`malus` = '"
						+ type
						+ "', `tempsMalusBonus`='"
						+ tps
						+ "' WHERE idMB = '"
						+ modif
						+ "' && `idCompetition`='" + idc + "'";

				BDDupdate(requeteSQL);

				thePanel.dispose();
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter", Ferme la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	/**
     * Effectue une requête de mise à jour et de gestion dans la BDD.
     * 
     * @param requeteSQL
     * 			La requête SQL à saisir dans la BDD
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
