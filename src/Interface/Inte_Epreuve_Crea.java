package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import BDD.DataSourceProvider;
import Interface.Inte_Epreuve_Modif.EcouteurOK;
import Interface.Inte_Epreuve_Modif.EcouteurQ;
import Models.TabModel;

public class Inte_Epreuve_Crea extends JFrame {

	JFrame thePanel = new JFrame();

	/* Champs */
	private JLabel nomL = new JLabel("Nom");
	private JTextField nomT = new JTextField("");

	private JLabel typeL = new JLabel("Type");
	private String[] typeS = { "CHOISIR", "Course d`orientation", "Course",
			"Orientshow" };
	private JComboBox<Object> typeC = new JComboBox<Object>(typeS);

	private JLabel dateL = new JLabel("Date de début");
	private JTextField dateT = new JTextField("AAAA-MM-JJ HH:MM:SS.0");

	private JLabel dureeL = new JLabel("Durée");
	private JTextField dureeT = new JTextField("hh:mm:ss");

	private JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);

	/* Boutons */
	private JButton supp = new JButton(" - ");
	private JButton creer = new JButton(" + ");

	private JButton oK = new JButton("Suivant");
	private JButton oK2 = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");

	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons
	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	private JPanel panTitre = new JPanel();

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "idBalise", "Type", "Valeur" };

	private int idc;
	private int modif;

	public Inte_Epreuve_Crea(int idC) {
		thePanel = this;
		idc = idC;

		InterfaceEp();
	}

	/**
	 * Fonction gérant l'interface de la fenetre4
	 * Partie 1 : création de l'epreuve
	 */
	public void InterfaceEp() {
		System.out.println("InterfaceEp");
		thePanel.setTitle("Raidzultats Création Epreuve");
		thePanel.setSize(400, 300);
		thePanel.setLocationRelativeTo(null);
		// thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));

		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel labelP = new JPanel();
		labelP.setLayout(new GridLayout(6, 2));
		labelP.setPreferredSize(new Dimension(300, 170));
		labelP.add(nomL);
		labelP.add(nomT);
		labelP.add(typeL);
		labelP.add(typeC);
		labelP.add(dateL);
		labelP.add(dateT);
		labelP.add(dureeL);
		labelP.add(dureeT);
		labelP.add(difficL);
		labelP.add(difficC);

		JPanel megaP = new JPanel();
		megaP.setBorder(BorderFactory
				.createTitledBorder("Créer/modifier votre épreuve"));
		megaP.add(labelP);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(megaP);
		ultraP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		thePanel.add(panPan);

		thePanel.setVisible(true);

		EcouteurOKEp ecoutOKEp = new EcouteurOKEp();
		oK.addActionListener(ecoutOKEp);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);

	}

	/**
	 * Fonction gérant l'interface de la fenetre4
	 * Partie 2 : l'association de balises dans l'epreuve
	 */
	public void InterfaceBa() {
		System.out.println("InterfaceBa");
		thePanel.setTitle("Raidzultats Attribution de balises");
		thePanel.setSize(500, 500);

		oK2.setPreferredSize(new Dimension(100, 30));

		creer.setPreferredSize(new Dimension(50, 30));
		supp.setPreferredSize(new Dimension(50, 30));

		panBoutCreer.add(creer);
		panBoutSupp.add(supp);

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
		jScroll.setPreferredSize(new Dimension(400, 350));

		panTitre.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les balises de l'epreuve"));
		// panTitre.setPreferredSize(new Dimension(350, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		JPanel btnP = new JPanel();
		btnP.add(oK2);
		btnP.add(annuler);

		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(panTitre);
		ultraP.add(btnP);

		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		System.out.println("panpan");
		thePanel.add(panPan);

		thePanel.setContentPane(ultraP);
		thePanel.revalidate();
		thePanel.repaint();
		thePanel.setVisible(true);

		EcouteurOKBa ecoutOKBa = new EcouteurOKBa();
		oK2.addActionListener(ecoutOKBa);
		/*
		 * EcouteurQ ecoutQ = new EcouteurQ();
		 * annuler.addActionListener(ecoutQ);
		 */

	}

	public class EcouteurOKEp implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Ok Ep");

			String nom = nomT.getText();
			String type = (String) typeC.getSelectedItem();
			String difficulte = (String) difficC.getSelectedItem();
			String date = (String) dateT.getText();
			String duree = (String) dureeT.getText();

			InterfaceBa();
			String requeteSQL = "INSERT INTO `raidzultat`.`epreuve` (`nomEpreuve`, `typeEpreuve`, `difficulte`, `dateHeureEpreuve`, `dureeEpreuve`, `idCompetition`) VALUES ('"
					+ nom
					+ "', '"
					+ type
					+ "', '"
					+ difficulte
					+ "', '"
					+ date
					+ "', '" + duree + "', '" + idc + "')";
			System.out.println(requeteSQL);
			BDDupdate(requeteSQL);
		}
	}

	public class EcouteurOKBa implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

		}
	}

	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
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
