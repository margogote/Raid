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
 * Formulaire permettant de renseigner les informations pour associer une balise
 * à une épreuve
 * 
 * @author Margaux
 * 
 */
public class Inte_Epreuve_Balise_Crea extends JFrame {

	JFrame thePanel = new JFrame();

	JLabel baliseL = new JLabel("Balise");
	private JComboBox<Object> baliseC = new JComboBox<Object>();

	JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Debut", "Pause Midi","Intermediaire",
			"Fin Pause Midi", "Arrivé" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	JLabel valL = new JLabel("Valeur");
	JTextField valT = new JTextField("1");

	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private int idc;
	private int modif=-1;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idModif
	 *            , l'id de l'épreuve à modifier
	 */
	Inte_Epreuve_Balise_Crea(int idC, int idModif) {

		thePanel = this;
		idc = idC;
		modif = idModif;

		thePanel.setTitle("Raidzultats - Ajout de balise");
		thePanel.setSize(450, 220);
		thePanel.setLocationRelativeTo(null);
		// centre la fenetre thePanel.setResizable(false);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		updateCombo(baliseC);

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(400, 100));
		megaP.setLayout(new GridLayout(3, 2));
		megaP.add(baliseL);
		megaP.add(baliseC);
		megaP.add(typeL);
		megaP.add(typeC);
		megaP.add(valL);
		megaP.add(valT);

		JPanel gigaP = new JPanel();
		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez ajouter une balise à votre épreuve"));
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
	 * Permet de gérer les clics du type "Ok"
	 * Récupération des valeurs
	 * Insertion dans la BDD
	 */
	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String bal = (String) baliseC.getSelectedItem();
			String type = (String) typeC.getSelectedItem();
			String val = valT.getText();

			if (bal.equals("CHOISIR") || type.equals("CHOISIR")
					|| val.equals("")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Balise non ajoutée!", JOptionPane.WARNING_MESSAGE);

			} else {
				String requeteSQL = "INSERT INTO `valoir` (`idBalise`,`idEpreuve`, `type`, `valeurBalise`, `idCompetition`) VALUES ('"
						+ bal
						+ "', '"
						+ modif
						+ "', '"
						+ type
						+ "', '"
						+ val
						+ "', '" + idc + "')";
				System.out.println(requeteSQL);
				BDDupdate(requeteSQL);

				thePanel.dispose();
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter"
	 * Ferme la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}

	/**
	 * Met à jour la comboBox pour la remplir avec les balises de la compétition
	 * qui n'ont pas encore été utilisées à partir de la BDD.
	 * 
	 * @param combo
	 *            , La comboBox à mettre à jour
	 */
	public void updateCombo(JComboBox<Object> combo) {
		combo.removeAllItems();
		String requeteSQL = "SELECT idBalise FROM balise WHERE `idBalise` NOT IN ( SELECT `idBalise` FROM `valoir` WHERE `idCompetition` = '"
				+ idc + "' && `idEpreuve`='"+modif+"') && idCompetition = '" + idc + "'";

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

	/**
	 * Effectue une requête de mise à jour et de gestion dans la BDD.
	 * 
	 * @param requeteSQL
	 *            La requête SQL à saisir dans la BDD
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
