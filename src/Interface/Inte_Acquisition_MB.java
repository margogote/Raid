package Interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import BDD.DataSourceProvider;
import Interface.Inte_Acquisition_Crea.EcouteurQ;

public class Inte_Acquisition_MB extends JFrame {

	/* Champs */
	private JLabel mbL = new JLabel("Malus/Bonus");
	private JComboBox<Object> mbC = new JComboBox<Object>();
	private ArrayList<Integer> mbLis = new ArrayList<>();

	/* Boutons */
	private JButton oK = new JButton("Ajouter");
	private JButton annuler = new JButton("Annuler");

	/* panels */
	private JPanel panTitre = new JPanel();
	private JFrame theFrame = new JFrame();

	private int idc;
	private int idep;
	private int ideq;

	Inte_Acquisition_MB(int idC, int idEp, int idEq) {
		idc = idC;
		idep = idEp;
		ideq = idEq;
		theFrame = this;

		theFrame.setTitle("Raidzultats - Ajout d'acquisition");
		theFrame.setSize(450, 150);
		theFrame.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Attribuer un Malus/Bonus à une équipe"));
		panTitre.add(mbL);
		panTitre.add(mbC);

		String requeteSQL = "SELECT nomMalusBonus, idMB FROM malusbonus WHERE idCompetition = '"
				+ idc + "'";
		mbLis = updateCombo(mbC, requeteSQL);

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panTitre);
		ultraP.add(btnP);

		theFrame.add(ultraP);

		theFrame.setVisible(true);

		EcouteurOk ecoutOk = new EcouteurOk();
		oK.addActionListener(ecoutOk);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

	}

	/**
	 * Permet de gérer les clics du type "Ajout" pour la fenêtre d'attribution
	 * de Maluse/Bonus à une épreuve Fermer la fenêtre
	 */
	public class EcouteurOk implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			if (mbC.getSelectedItem().equals("CHOISIR")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Malus/Bonus non créé!", JOptionPane.WARNING_MESSAGE);
			} else {

				int idMB = mbLis.get(mbC.getSelectedIndex() - 1);
				System.out.println("idMB : " + idMB);
				String requeteSQL = "INSERT INTO `avoir`(`idMB`, `idEquipe`, `tempsTotalMalusBonus`, `idCompetition`) VALUES ('"
						+ idMB + "','" + ideq + "','00:00:01','" + idc + "')";
				System.out.println(requeteSQL);
				BDDupdate(requeteSQL);

				theFrame.dispose();
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter" Ferme la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			theFrame.dispose();
		}
	}

	/**
	 * Met à jour la comboBox pour la remplir selon la requete en parametre à
	 * partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox à mettre à jour
	 * @param requeteSQL
	 *            , la requete SQL à effectuer
	 * @return comboList , la liste des id associés aux noms dans la combo dans
	 *         la BDD
	 */
	public ArrayList<Integer> updateCombo(JComboBox<Object> combo,
			String requeteSQL) {
		combo.removeAllItems();
		ArrayList<Integer> comboList = new ArrayList<Integer>();

		combo.addItem("CHOISIR");

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
				comboList.add(res.getInt(2));
				System.out.println("Nom : " + res.getString(1) + " Id : "
						+ res.getInt(2));
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (combo.getItemCount() == 0) {
			oK.setEnabled(false);
		} else {
			oK.setEnabled(true);
		}
		System.out.println("MAJ Combo");
		return (comboList);
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
