package Interface;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;

import BDD.DataSourceProvider;
import Interface.Inte_Acquisition_Crea.EcouteurQ;

public class Inte_Acquisition_MB extends JFrame {
	
	/* Champs */
	private JLabel mbL = new JLabel("Malus/Bonus");
	private JComboBox<Object> mbC = new JComboBox<Object>();
	
	/* Boutons */
	private JButton oK = new JButton("Ajouter");
	private JButton annuler = new JButton("Annuler");

	/* panels */
	private JPanel panTitre = new JPanel();
	private JFrame theFrame = new JFrame();
	
	private int idc;
	private int ide;

	Inte_Acquisition_MB(int idC, int idE){
		idc = idC;
		ide = idE;
		
		theFrame.setTitle("Raidzultats - Ajout d'acquisition");
		theFrame.setSize(450, 220);
		theFrame.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		theFrame.setLayout(new BorderLayout()); // Pour les placements
		
		panTitre.setBorder(BorderFactory
				.createTitledBorder("Attribuer un Malus/Bonus à une équipe"));
		panTitre.add(mbL);
		panTitre.add(mbC);
		
		String requeteSQL = "SELECT nomMalusBonus FROM malusbonus WHERE idCompetition = '"
				+ idc + "'";
		updateCombo(mbC, requeteSQL);
		
		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panTitre);
		ultraP.add(btnP);
		
		theFrame.add(ultraP);

		theFrame.setVisible(true);
		
		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
		
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
	 * Met à jour du tableau pour le remplir avec les épreuves de la compétition
	 * à partir de la BDD.
	 */
	public void updateCombo(JComboBox<Object> combo, String requeteSQL) {
		combo.removeAllItems();

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
				System.out.println("Nom : " + res.getString(1));
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
	}
}
