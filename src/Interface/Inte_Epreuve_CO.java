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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import BDD.DataSourceProvider;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class Inte_Epreuve_CO extends JFrame {

	/* Fenêtre */
	private JFrame theFrame = new JFrame();

	/* Champs */
	private JLabel mBL = new JLabel("Temps malus par balise non pointée");
	private JTextField mBT = new JTextField("hh:mm:ss");

	private JLabel dureeL = new JLabel(
			"Coefficient multiplicateur par minute de dépassement");
	private JTextField dureeT = new JTextField("");

	private JButton oK = new JButton("Suivant");
	private JButton annuler = new JButton("Annuler");

	int idc, ide;
	String nomEp;

	Inte_Epreuve_CO(int idC, int idEpreuve, String nomEpreuve) {

		theFrame = this;
		idc = idC;
		ide = idEpreuve;
		nomEp = nomEpreuve;

		theFrame.setTitle("Raidzultats - Parametrage Course d'Orientation");
		theFrame.setSize(350, 250);
		theFrame.setLocationRelativeTo(null);
		// centre la fenetre theFrame.setResizable(false);
		// theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		JPanel megaP = new JPanel();
		megaP.setPreferredSize(new Dimension(300, 150));
		megaP.setLayout(new GridLayout(4, 1));
		megaP.add(mBL);
		megaP.add(mBT);
		megaP.add(dureeL);
		megaP.add(dureeT);

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel gigaP = new JPanel();
		gigaP.setBorder(BorderFactory
				.createTitledBorder("Ici vous paramétrer votre Course d'Orientation"));
		gigaP.setLayout(new BoxLayout(gigaP, BoxLayout.PAGE_AXIS));
		gigaP.add(megaP);
		gigaP.add(btnP);

		theFrame.add(gigaP);

		theFrame.setVisible(true);

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

	}

	/**
	 * Permet de gérer les clics du type "Ok" pour la fenêtre de modification
	 * d'épreuve. Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String mb = mBT.getText();
			String tps = dureeT.getText();

			if (mb.equals("") || mb.equals("hh:mm:ss") || tps.equals("")) {
				JOptionPane.showMessageDialog(null,
						"Veuillez remplir tous les champs",
						"Paramètres non créés!", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					String testTpsduree[] = mb.split(":");
					Integer.parseInt(tps);
					for (int i = 0; i < testTpsduree.length; i++) {
						System.out.println(testTpsduree[i]);
						Integer.parseInt(testTpsduree[i]);
					}
					
					String requeteSQL = "INSERT INTO `malusbonus` (`idMB`, `nomMalusBonus`, `malus`, `tempsMalusBonus`, `idCompetition`) VALUES (NULL, 'nonPointageBalise"
							+ nomEp
							+ "', '1', '"
							+ mb
							+ "', '"
							+ idc
							+ "'), (NULL, 'tempsSupp"
							+ nomEp
							+ "', '1', '00:00:" + tps + "', '" + idc + "')";

					System.out.println("Création MB : " + requeteSQL);
					BDDupdate(requeteSQL);

					JOptionPane.showMessageDialog(null,
							"Les paramètres sont rentrés dans Malus/Bonus",
							nomEp + " paramétrée!",
							JOptionPane.INFORMATION_MESSAGE);

					theFrame.dispose();
				} catch (Exception e) {
					System.out.println("Je ne suis pas un entier");
					JOptionPane
							.showMessageDialog(
									null,
									"Veuillez entrer une heure de type hh:mm:ss et un coefficient entier",
									"Paramètres non créée!",
									JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter", Ferme la fenêtre
	 */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			theFrame.dispose();
		}
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
