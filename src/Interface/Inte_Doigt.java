package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Interface.Inte_Accueil.EcouteurCreer;
import Interface.Inte_Accueil.EcouteurModif;
import Interface.Inte_Accueil.EcouteurSupp;
import Models.TabModel;

public class Inte_Doigt extends JPanel{

	/* Panes */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panDoigts = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");
	
	/* BDD */
	String url = "jdbc:mysql://localhost/raidzultat";
	String user = "root";
	String passwd = "";

	private JTable tableau;
	private Object[][] data = new Object[50][2];	    
	private String title[] = {"Check","idDoigt"};
	
	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents doigts");

	public Inte_Doigt() {

		updateTable();
		
		modif.setPreferredSize(new Dimension(200, 30));
		creer.setPreferredSize(new Dimension(200, 30));
		supp.setPreferredSize(new Dimension(200, 30));
		
		JPanel panBoutCreer = new JPanel();
		JPanel panBoutSupp = new JPanel();
		JPanel panBoutModif = new JPanel();
		
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);
		
		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		panDoigts.setLayout(new BoxLayout(panDoigts, BoxLayout.PAGE_AXIS));
		panDoigts.setLayout(new BorderLayout());
		panDoigts.add(bjr, BorderLayout.NORTH);
		panDoigts.add(panBoutonsListe, BorderLayout.WEST);
		
		panMega.add(panDoigts);
		
	    /*Object[][] data = {   
	    	      {"Cysboy", new JButton("6boy"), new Double(1.80), new Boolean(true)},
	    	      {"BZHHydde", new JButton("BZH"), new Double(1.78), new Boolean(false)},
	    	      {"IamBow", new JButton("BoW"), new Double(1.90), new Boolean(false)},
	    	      {"FunMan", new JButton("Year"), new Double(1.85), new Boolean(true)}
	    	    };
	   	
	    String  title[] = {"Pseudo", "Age", "Taille", "OK ?"};
	   	*/
	
		
	    TabModel tabModel = new TabModel(data, title);
	    //Nous ajoutons notre tableau à notre contentPane dans un scroll
	    //Sinon les titres des colonnes ne s'afficheront pas !
	    
	    tableau = new JTable(tabModel);     
	    tableau.setRowHeight(30);
	    
	    
	    
	    panMega.add(new JScrollPane(tableau));
	  
		
		
		

		
		this.add(panMega);
		//this.add(panDoigts);
		
		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);
		
	}
	
	public class EcouteurCreer implements ActionListener { // Action du creer

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {

			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nb = jop.showInputDialog(null,
					"Donner le numéro de votre doigt !",
					"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);

			while (nb.equals("") ) {
				jop2.showMessageDialog(null, "Veuillez entrer un nombre",
						"Doigt non créée!",
						JOptionPane.INFORMATION_MESSAGE);
				nb = jop.showInputDialog(null,
						"Donner le numéro de votre doigt !",
						"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);
			}
			
			// ---- Contrôle utilisateur ----
			//"Ce doigt existe déjà, veuillez entrer un autre numéro"
			
			if (!nb.equals("")) {
				try {
					String requeteSQL = "INSERT INTO `doigt` (`idDoigt`) VALUES ( "
							+ nb + ")";
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DriverManager.getConnection(url, user,
							passwd);
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					int res = stm.executeUpdate(requeteSQL);

					System.out.println("Nb enregistrement : " + res);

					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				updateTable();
				tableau.revalidate();
				panMega.revalidate();
				
				jop2.showMessageDialog(null, "Le doigt est " + nb + ".",
						"Nouvelle compétition !",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public class EcouteurModif implements ActionListener { // Action du quitter

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on lance le formulaire pré-remplit

			//int id;
			//id = getIndex(compets);
			String id = "id Test";
			String nbAv = "NbAv test";
			//String nbAv = (String) compets.getSelectedItem();

			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String 	nb = jop.showInputDialog(null,
					"Donner le numéro de votre doigt !",
					"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);

			while (nb.equals("")) {
				jop2.showMessageDialog(null, "Veuillez entrer un numéro",
						"Doigt " + id + " non modifié!",
						JOptionPane.INFORMATION_MESSAGE);
				nb = jop.showInputDialog(null,
						"Donner le numéro de votre doigt !",
						"Nouveau doigt ?", JOptionPane.QUESTION_MESSAGE);
			}
			if (!nb.equals("")) {
				try {
					String requeteSQL = "UPDATE `doigt` SET  `idDoigt` = "
							+ nb + " WHERE `idDoigt = '" + nbAv + "'";
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DriverManager.getConnection(url, user,
							passwd);
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					int res = stm.executeUpdate(requeteSQL);

					System.out.println("Nb enregistrement : " + res);

					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				jop2.showMessageDialog(null, "Le doigt est maintenant : "
						+ nb, "Doigt " + id + " modifié!",
						JOptionPane.INFORMATION_MESSAGE);

				//updateCombo(compets);

			}
		}
	}

	public class EcouteurSupp implements ActionListener { // Action du supprimer

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			// int id;
			//String nb = (String) compets.getSelectedItem();
			String nb ="Numéro test";
			int rep = 0;
			JOptionPane jop2 = new JOptionPane();
			// id = compets.getSelectedIndex() + 1;

			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer le doigt " + nb
							+ " ?", "Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {

				try {
					String requeteSQL = "DELETE FROM `raidzultat`.`competition` WHERE `competition`.`nomCompetition` = '"
							+ nb + "'";
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver O.K.");

					Connection conn = DriverManager.getConnection(url, user,
							passwd);
					System.out.println("Connexion effective !");
					Statement stm = conn.createStatement();
					int res = stm.executeUpdate(requeteSQL);

					System.out.println("Nb enregistrement : " + res);

					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				jop2.showMessageDialog(null,
						"Le doigt est maintenant supprimé",
						"Doigt " + nb + " Supprimé!",
						JOptionPane.INFORMATION_MESSAGE);

				System.out.println("Doigt " + nb + " Supprimé");
				//updateCombo(compets);
			}
		}
	}
	
	public void updateTable(){
		//data = new Object[50][2];
		
		String requeteSQL = "SELECT * FROM doigt";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			int i=0;
			while (res.next()) {
				data[i][0] = new Boolean(false);
				data[i][1] = res.getString(1);
				System.out.println("Nom : " + res.getString(1));
				i++;
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("MAJ Table");
		
	}
}
