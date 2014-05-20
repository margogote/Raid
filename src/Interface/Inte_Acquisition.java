package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import BDD.DataSourceProvider;
import Models.TabModel;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class Inte_Acquisition extends JFrame {

	private JFrame theFrame;

	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panTitre = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	private JPanel panBoutAcq = new JPanel();
	private JPanel panBoutQ = new JPanel();

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");
	private JButton acqA = new JButton("Acquisition Auto");
	private JButton quit = new JButton("J'ai fini !");

	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data;
	private String title[] = { "", "id", "Nom équipe", "Dossard",
			"Heure pointage", "Balise" };

	int idc;
	int idep;
	String nomEp;

	/**
	 * Classe principale
	 * 
	 * @param idC
	 *            , l'id de la compétition étudiée
	 * @param idEpreuve
	 *            , l'id de l' épreuve étudiée
	 * @param nomEpreuve
	 *            , le nom de l' épreuve étudiée
	 */
	Inte_Acquisition(int idC, int idEpreuve, String nomEpreuve) {

		theFrame = this;
		idc = idC;
		idep = idEpreuve;
		nomEp = nomEpreuve;

		updateTable();

		EcouteurModif ecoutModif = new EcouteurModif();
		modif.addActionListener(ecoutModif);

		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);

		EcouteurCreer ecoutCreer = new EcouteurCreer();
		creer.addActionListener(ecoutCreer);

		EcouteurA ecoutA = new EcouteurA();
		acqA.addActionListener(ecoutA);

		EcouteurFin ecoutFin = new EcouteurFin();
		quit.addActionListener(ecoutFin);
	}

	/**
	 * Fonction gérant l'interface du panel
	 */
	public void Interface() {
		theFrame.setTitle("Raidzultats - Acquisition - " + nomEp); // titre
		theFrame.setSize(800, 550); // taille de la fenetre
		theFrame.setLocationRelativeTo(null); // centre la fenetre
		theFrame.setResizable(false);
		theFrame.setLayout(new BorderLayout()); // Pour les placements

		// theFrame.removeAll();
		panMega.removeAll();
		panTitre.removeAll();

		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));
		acqA.setPreferredSize(new Dimension(140, 50));
		quit.setPreferredSize(new Dimension(140, 50));

		panBoutAcq.add(acqA);
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);
		panBoutQ.add(quit);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutAcq);
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutQ);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(600, 400));

		panTitre.setPreferredSize(new Dimension(700, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);

		panMega.setBorder(BorderFactory
				.createTitledBorder("Ici vous pouvez gérer les acquisitions pour cette epreuve"));
		panMega.setLayout(new BoxLayout(panMega, BoxLayout.PAGE_AXIS));
		panMega.add(panTitre);
		panMega.add(panBoutQ);

		theFrame.add(panMega);

		theFrame.setVisible(true);
	}

	/**
	 * Permet de gérer les clics du type "Créer" Lancement du formulaire associé
	 * mise à jour du tableau lors de la fermeture du formulaire
	 */
	public class EcouteurCreer implements ActionListener, WindowListener { // Action
																			// du
																			// creer
		public void actionPerformed(ActionEvent arg0) {

			Inte_Acquisition_Crea formulaire = new Inte_Acquisition_Crea(idc,
					idep);
			formulaire.addWindowListener(this);
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			updateTable();
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}

	/**
	 * Permet de gérer les clics du type "Modifier" pour une épreuve
	 * Recupérations des données entrées et insertion dans la BDD
	 */
	public class EcouteurModif implements ActionListener, WindowListener { // Action
																			// du
																			// modif
		public void actionPerformed(ActionEvent arg0) {
			int flagExiste = 0;
			int[][] tab = getIndexSelectTab(data);

			if (tab[0].length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'acquisition à modifier!",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				for (int i = 0; i < tab.length; i++) {

					Inte_Acquisition_Modif formulaire = new Inte_Acquisition_Modif(
							idc, idep, tab[i][0], tab[i][1]);

					formulaire.addWindowListener(this);
				}
			}
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			updateTable();
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}

	/**
	 * Permet de gérer les clics du type "Supprimer" pour une épreuve
	 * Suppression de la BDD
	 */
	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int[][] tab = getIndexSelectTab(data);
			int rep = 0;

			if (tab[0].length == 0) {
				JOptionPane.showMessageDialog(null, "Veuillez cochez une case",
						"Pas d'acquisition à supprimer!",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				for (int i = 0; i < tab.length; i++) {
					rep = JOptionPane.showConfirmDialog(null,
							"Voulez vous vraiment supprimer l'acquisition "
									+ tab[i][0] + " ?", "Attention",
							JOptionPane.YES_NO_OPTION);

					if (rep == 0) {
						String requeteSQL = "DELETE FROM `pointer` WHERE CONCAT(`pointer`.`idBalise`) ='"
								+ tab[i][1]
								+ "' AND `pointer`.`idEpreuve` = '"
								+ idep
								+ "' AND CONCAT(`pointer`.`idDoigt`) = (SELECT `idDoigt` FROM `posséder` WHERE `idEquipe`='"
								+ tab[i][0]
								+ "')  AND `pointer`.`idCompetition` = '"
								+ idc
								+ "'";

						BDDupdate(requeteSQL);

						JOptionPane.showMessageDialog(null,
								"L'acquisition est maintenant supprimée",
								"Acquisition " + tab[i][0] + " Supprimée!",
								JOptionPane.INFORMATION_MESSAGE);

						System.out.println("Acquisition " + tab[i][0]
								+ " Supprimée");
					}
				}
			}
		}
	}

	/**
	 * Permet de gérer les clics du type "Acquisition" pour une épreuve
	 * Récupération de l'épreuve séléctionnée et lancement du formulaire associé
	 */
	public class EcouteurA implements ActionListener { // Action
		// du
		// creer
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Bouton en chantier",
					"Chantier!", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Permet de gérer les clics du type "Quitter" Ferme la fenêtre
	 */
	public class EcouteurFin implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {
			String typeCourse = "", dateCourse = "", dureeCourse = "";

			BDDupdate("DELETE FROM `scorer` WHERE `idEpreuve` = '" + idep
					+ "' && `idCompetition` = '" + idc + "'");
			// Pour connaitre le type d'épreuve, sa date de départ et sa durée
			try {
				String requeteSQL = "SELECT `typeEpreuve`, `dateHeureEpreuve`, `dureeEpreuve` FROM `epreuve` WHERE `idEpreuve` = '"
						+ idep + "' && `idCompetition` = '" + idc + "'";
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource()
						.getConnection();
				System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);

				while (res.next()) {
					typeCourse = res.getString(1);
					dateCourse = res.getString(2);
					dureeCourse = res.getString(3);
					System.out.println("type: " + typeCourse + " date : "
							+ dateCourse + " durée : " + dureeCourse);
				}

				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			ArrayList<Integer> lisDoigt = new ArrayList<>();
			// Pour savoir quelles équipes ont participées
			try {
				String requeteDoigt = "SELECT DISTINCT `idDoigt` FROM `pointer` WHERE `pointer`.`idCompetition`='"
						+ idc + "' && `pointer`.`idEpreuve` = '" + idep + "'";

				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver O.K.");

				Connection connType = DataSourceProvider.getDataSource()
						.getConnection();
				System.out.println("Connexion effective !");
				Statement stmType = connType.createStatement();
				ResultSet resType = stmType.executeQuery(requeteDoigt);

				while (resType.next()) {
					lisDoigt.add(resType.getInt(1));
				}
				connType.close();
				resType.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * --- Si le type de l'épreuve est une Course
			 */
			if (typeCourse.equals("Course")) {

				System.out.println(" ----- C'est une course");
				try {

					for (int i = 0; i < lisDoigt.size(); i++) {
						String dateHDeb = "";
						String dateHFin = "";

						String requeteBalDeb = "SELECT `pointer`.`dateHeurePointage` FROM `pointer` INNER JOIN `valoir` ON `pointer`.`idBalise` = `valoir`.`idBalise` && `pointer`.`idEpreuve` = `valoir`.`idEpreuve` WHERE `valoir`.`type`= 'Debut' && `pointer`.`idCompetition`='"
								+ idc
								+ "' && `valoir`.`idCompetition`='"
								+ idc
								+ "' && `pointer`.`idEpreuve` = '"
								+ idep
								+ "' && `pointer`.`idDoigt`='"
								+ lisDoigt.get(i) + "'";

						String requeteBalFin = "SELECT `pointer`.`dateHeurePointage` FROM `pointer` INNER JOIN `valoir` ON `pointer`.`idBalise` = `valoir`.`idBalise` && `pointer`.`idEpreuve` = `valoir`.`idEpreuve` WHERE `valoir`.`type`= 'Arrivé' && `pointer`.`idCompetition`='"
								+ idc
								+ "' && `valoir`.`idCompetition`='"
								+ idc
								+ "' && `pointer`.`idEpreuve` = '"
								+ idep
								+ "' && `pointer`.`idDoigt`='"
								+ lisDoigt.get(i) + "'";
						System.out.println(requeteBalDeb);
						System.out.println(requeteBalFin);

						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DataSourceProvider.getDataSource()
								.getConnection();
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();

						ResultSet resBalDeb = stm.executeQuery(requeteBalDeb);
						while (resBalDeb.next()) {
							dateHDeb = resBalDeb.getString(1);
							System.out.println(dateHDeb);
						}

						ResultSet resBalFin = stm.executeQuery(requeteBalFin);

						/*
						 * Verifier si requeteBalFin existe, sinon, M/B absent
						 */
						if (!resBalFin.next()) {
							System.out
									.println("   ----- Balise de fin non pointée ! -----");

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "',(SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus`='abs"
									+ nomEp
									+ "' &&`idCompetition`='"
									+ idc
									+ "'),'" + idc + "')";

							BDDupdate(requeteScorer);

						} else {
							resBalFin.beforeFirst();
							while (resBalFin.next()) {
								dateHFin = resBalFin.getString(1);
								System.out.println(dateHFin);
							}

							Date temps = substractTwoDates(
									stringToDate(dateHFin),
									stringToDate(dateHDeb));

							System.out.println("Temps course tot : " + temps);

							String attribMB = "SELECT `malusbonus`.`tempsMalusBonus`, `malusbonus`.`malus` FROM `avoir` INNER JOIN `malusbonus` ON `avoir`.`idMB`=`malusbonus`.`idMB` WHERE `avoir`.`idEquipe` =(SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "' && `idCompetition`= '"
									+ idc
									+ "') && `avoir`.`idEpreuve` ='"
									+ idep
									+ "' &&`avoir`.`idCompetition`= '"
									+ idc
									+ "'";
							System.out.println(attribMB);
							ResultSet resattribMB = stm.executeQuery(attribMB);
							while (resattribMB.next()) {
								String tpsMBstr = resattribMB.getString(1);
								Date tpsMB = stringToDuree(resattribMB
										.getString(1));
								int mal = resattribMB.getInt(2);
								System.out.println("tpsStr : " + tpsMBstr
										+ " tpsMB : " + tpsMB + " malus? : "
										+ mal);

								if (mal == 0) {
									temps = substractTwoDates(temps, tpsMB);
								} else {
									temps = addTwoDates(temps, tpsMB);
								}
							}
							System.out.println("Tps course +- MB" + temps);

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "','"
									+ dateToString(temps) + "','" + idc + "')";

							BDDupdate(requeteScorer);
						}
						conn.close();
						resBalDeb.close();
						resBalFin.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			/*
			 * --- Si le type de l'épreuve est un MassStart
			 */
			else if (typeCourse.equals("MassStart")) {
				System.out.println(" ----- C'est un MassStart");
				try {

					for (int i = 0; i < lisDoigt.size(); i++) {
						String dateHDeb = dateCourse;
						String dateHFin = "";

						String requeteBalFin = "SELECT `pointer`.`dateHeurePointage` FROM `pointer` INNER JOIN `valoir` ON `pointer`.`idBalise` = `valoir`.`idBalise` && `pointer`.`idEpreuve` = `valoir`.`idEpreuve` WHERE `valoir`.`type`= 'Arrivé' && `pointer`.`idCompetition`='"
								+ idc
								+ "' && `valoir`.`idCompetition`='"
								+ idc
								+ "' && `pointer`.`idEpreuve` = '"
								+ idep
								+ "' && `pointer`.`idDoigt`='"
								+ lisDoigt.get(i) + "'";

						System.out.println(requeteBalFin);

						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DataSourceProvider.getDataSource()
								.getConnection();
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();

						ResultSet resBalFin = stm.executeQuery(requeteBalFin);

						/*
						 * --- Vérifier si la balise de fin est pointée
						 */
						if (!resBalFin.next()) {
							System.out
									.println("   ----- Balise de fin non pointée ! -----");

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "',(SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus`='abs"
									+ nomEp
									+ "' &&`idCompetition`='"
									+ idc
									+ "'),'" + idc + "')";

							BDDupdate(requeteScorer);

						}
						/*
						 * --- Si la balise de fin est pointée
						 */
						else {
							resBalFin.beforeFirst();

							while (resBalFin.next()) {
								dateHFin = resBalFin.getString(1);
								System.out.println("Fin : " + dateHFin);
							}
							System.out.println("Début : " + dateHDeb);

							Date temps = substractTwoDates(
									stringToDate(dateHFin),
									stringToDate(dateHDeb));

							System.out.println(temps);

							String attribMB = "SELECT `malusbonus`.`tempsMalusBonus`, `malusbonus`.`malus` FROM `avoir` INNER JOIN `malusbonus` ON `avoir`.`idMB`=`malusbonus`.`idMB` WHERE `avoir`.`idEquipe` =(SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "' && `idCompetition`= '"
									+ idc
									+ "') && `avoir`.`idEpreuve` ='"
									+ idep
									+ "' &&`avoir`.`idCompetition`= '"
									+ idc
									+ "'";
							System.out.println(attribMB);
							ResultSet resattribMB = stm.executeQuery(attribMB);
							while (resattribMB.next()) {
								String tpsMBstr = resattribMB.getString(1);
								Date tpsMB = stringToDuree(resattribMB
										.getString(1));
								int mal = resattribMB.getInt(2);
								System.out.println("tpsStr : " + tpsMBstr
										+ " tpsMB : " + tpsMB + " malus? : "
										+ mal);

								if (mal == 0) {
									temps = substractTwoDates(temps, tpsMB);
								} else {
									temps = addTwoDates(temps, tpsMB);
								}
							}
							System.out.println("Tps course +- MB" + temps);

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "','"
									+ dateToString(temps) + "','" + idc + "')";

							BDDupdate(requeteScorer);

						}

						conn.close();
						resBalFin.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * --- Si le type de l'épreuve est une Course d'Orientation
			 */
			else if (typeCourse.equals("Course d`orientation")) {
				System.out.println(" ----- C'est une CO");

				try {
					for (int i = 0; i < lisDoigt.size(); i++) {
						System.out.println("... Doigt n°" + lisDoigt.get(i));
						String dateHDeb = dateCourse;
						String dateHFin = "";

						String requeteBalFin = "SELECT `pointer`.`dateHeurePointage` FROM `pointer` INNER JOIN `valoir` ON `pointer`.`idBalise` = `valoir`.`idBalise` && `pointer`.`idEpreuve` = `valoir`.`idEpreuve` WHERE `valoir`.`type`= 'Arrivé' && `pointer`.`idCompetition`='"
								+ idc
								+ "' && `valoir`.`idCompetition`='"
								+ idc
								+ "' && `pointer`.`idEpreuve` = '"
								+ idep
								+ "' && `pointer`.`idDoigt`='"
								+ lisDoigt.get(i) + "'";

						Class.forName("com.mysql.jdbc.Driver");
						System.out.println("Driver O.K.");

						Connection conn = DataSourceProvider.getDataSource()
								.getConnection();
						System.out.println("Connexion effective !");
						Statement stm = conn.createStatement();

						System.out.println(requeteBalFin);

						ResultSet resBalFin = stm.executeQuery(requeteBalFin);

						/*
						 * --- Vérifier si la balise de fin n'est pas pointée
						 */
						if (!resBalFin.next()) {
							System.out
									.println("   ----- Balise de fin non pointée ! -----");

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "',(SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus`='abs"
									+ nomEp
									+ "' &&`idCompetition`='"
									+ idc
									+ "'),'" + idc + "')";

							BDDupdate(requeteScorer);

						}
						/*
						 * --- Si la balise de fin est pointée
						 */
						else {
							resBalFin.beforeFirst();

							while (resBalFin.next()) {
								dateHFin = resBalFin.getString(1);
								System.out.println("Fin : " + dateHFin);
							}
							System.out.println("Début : " + dateHDeb);

							Date temps = substractTwoDates(
									stringToDate(dateHFin),
									stringToDate(dateHDeb));

							System.out.println("Tps de course : " + temps);

							String tempsStr = dateToString(temps);
							System.out.println("Tps de course : " + tempsStr);

							int totBalNonP = 0;
							String requeteBalNonP = "SELECT `valeurBalise` FROM `valoir` WHERE `idEpreuve`='"
									+ idep
									+ "' &&`idBalise`NOT IN (SELECT `idBalise` FROM `pointer` WHERE `idEpreuve`='"
									+ idep
									+ "'&& `idDoigt`='"
									+ lisDoigt.get(i)
									+ "' && `idCompetition`='" + idc + "')";
							ResultSet resBalNonP = stm
									.executeQuery(requeteBalNonP);
							System.out.println(requeteBalNonP);
							while (resBalNonP.next()) {
								totBalNonP += resBalNonP.getInt(1);
								System.out.println("Tot pts malus : "
										+ totBalNonP);
							}
							System.out.println("Tot pts malus : " + totBalNonP);
							String tpsMBBalNonPString = "00:00:00";

							String requeteMBBalNonP = "SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus`='nonPointageBalise"
									+ nomEp
									+ "' && `idCompetition`='"
									+ idc
									+ "'";
							ResultSet resMBBalNonP = stm
									.executeQuery(requeteMBBalNonP);
							while (resMBBalNonP.next()) {
								tpsMBBalNonPString = resMBBalNonP.getString(1);
								System.out.println("Tps malus si non pointée: "
										+ tpsMBBalNonPString);
							}

							Date tpsMBBalNonP = stringToDuree(tpsMBBalNonPString);
							System.out.println("Temps total malus BnonP : "
									+ tpsMBBalNonP);

							Date tpsTotBalNonP = multiDate(tpsMBBalNonP,
									totBalNonP);

							System.out
									.println("Tps tot pour balise non pointées : "
											+ tpsTotBalNonP);
							String tpsTotBalNonPStr = dateToString(tpsTotBalNonP);
							System.out
									.println("Tps tot pour balise non pointées en String : "
											+ tpsTotBalNonPStr);

							Date finEpreuve = addTwoDates(
									stringToDate(dateCourse),
									stringToDuree(dureeCourse));
							System.out.println("Heure début : " + dateCourse
									+ " Duree : " + dureeCourse
									+ " Heure fin epreuve : " + finEpreuve);

							Date tpsDepassement = substractTwoDates(
									stringToDate(dateHFin), finEpreuve);
							System.out
									.println("Heure d'arrivée : " + dateHFin
											+ " Tps de dépassement : "
											+ tpsDepassement);

							String requeteMBDepas = "SELECT `tempsMalusBonus` FROM `malusbonus` WHERE `nomMalusBonus`='tempsSupp"
									+ nomEp
									+ "' && `idCompetition`='"
									+ idc
									+ "'";
							ResultSet resMBDepas = stm
									.executeQuery(requeteMBDepas);
							System.out.println(requeteMBDepas);
							String tpsMBDepas = "00:00:00";
							while (resMBDepas.next()) {
								tpsMBDepas = resMBDepas.getString(1);
								System.out.println("Tps malus si depassement: "
										+ tpsMBDepas);
							}

							Date tpsMBDepasDate = stringToDuree(tpsMBDepas);
							int coeffDepass = tpsMBDepasDate.getSeconds();

							System.out.println("Coeff dépassement : "
									+ coeffDepass);

							Date tpsTotDepass = multiDate(tpsDepassement,
									coeffDepass);

							System.out.println("Temps tot de dépassement : "
									+ tpsTotDepass);

							String attribMB = "SELECT `malusbonus`.`tempsMalusBonus`, `malusbonus`.`malus` FROM `avoir` INNER JOIN `malusbonus` ON `avoir`.`idMB`=`malusbonus`.`idMB` WHERE `avoir`.`idEquipe` =(SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "' && `idCompetition`= '"
									+ idc
									+ "') && `avoir`.`idEpreuve` ='"
									+ idep
									+ "' &&`avoir`.`idCompetition`= '"
									+ idc
									+ "'";
							System.out.println(attribMB);
							ResultSet resattribMB = stm.executeQuery(attribMB);
							while (resattribMB.next()) {
								String tpsMBstr = resattribMB.getString(1);
								Date tpsMB = stringToDuree(resattribMB
										.getString(1));
								int mal = resattribMB.getInt(2);
								System.out.println("tpsStr : " + tpsMBstr
										+ " tpsMB : " + tpsMB + " malus? : "
										+ mal);

								if (mal == 0) {
									temps = substractTwoDates(temps, tpsMB);
								} else {
									temps = addTwoDates(temps, tpsMB);
								}
							}
							System.out.println("Tps course +- MB" + temps);

							Date tpsTot = addTwoDates(temps,
									addTwoDates(tpsTotBalNonP, tpsTotDepass));
							System.out.println("Tps epreuve : " + temps
									+ " tps balnnP : " + tpsTotBalNonP
									+ " tpsTotal : " + tpsTot);

							String requeteScorer = "INSERT INTO `scorer`(`idEquipe`, `idEpreuve`, `tempsRealise`, `idCompetition`) VALUES ((SELECT `idEquipe` FROM `posséder` WHERE `idDoigt`='"
									+ lisDoigt.get(i)
									+ "'),'"
									+ idep
									+ "','"
									+ dateToString(tpsTot) + "','" + idc + "')";

							BDDupdate(requeteScorer);

							resBalNonP.close();
							resMBBalNonP.close();

							conn.close();
							resBalFin.close();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Problèèèèèèèèèèèèèèèèèèèèèèèème");
			}

			theFrame.dispose();
		}
	}

	/**
	 * Met à jour du tableau pour le remplir avec les équipes de la compétition
	 * à partir de la BDD.
	 */
	public void updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();

		String requeteSQL = "SELECT `equipe`.idEquipe, `equipe`.nomEquipe, `equipe`.dossard, `pointer`.`dateHeurePointage`, `pointer`.`idBalise` FROM `pointer` INNER JOIN (`posséder` INNER JOIN `equipe` ON `posséder`.idEquipe = `equipe`.idEquipe) ON `pointer`.`idDoigt` = `posséder`.`idDoigt` WHERE `pointer`.`idCompetition` = '"
				+ idc
				+ "'&& `equipe`.`idCompetition` = '"
				+ idc
				+ "' && `posséder`.`idCompetition` = '"
				+ idc
				+ "' && `pointer`.`idEpreuve`='" + idep + "'";
		int i = 1;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			while (res.next()) {
				ArrayData.add(new Object[] { new Boolean(false), res.getInt(1),
						res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5) });
				System.out.println(" nom : " + res.getString(2) + " dossard : "
						+ res.getInt(3) + " tps : " + res.getString(4)
						+ " balise : " + res.getInt(5));
				i++;
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

		data = ArrayToTab(ArrayData);

		Interface();

		System.out.println("MAJ Table equipe");
	}

	/**
	 * Fonction transformant une ArrayList en tableau
	 * 
	 * @param array
	 *            , l'arrayList à transformer
	 * 
	 * @return tab, le tableau correspondant à l'arrayList prise en parametre
	 * 
	 */
	public Object[][] ArrayToTab(ArrayList<Object[]> array) {

		int lengthLig = array.size();
		int lengthCol;
		if (lengthLig > 0) {
			lengthCol = array.get(0).length;
		} else {
			lengthCol = 0;
		}
		Object[][] tab = new Object[lengthLig][lengthCol];
		for (int i = 0; i < lengthLig; i++) {
			tab[i] = array.get(i);
		}
		return tab;
	}

	/**
	 * Fonction permettant de renvoyer les différentes lignes cochées dans un
	 * tableau
	 * 
	 * @param table
	 *            , le tableau à analyser
	 * 
	 * @return ArrayDataSelect, l'arrayList contenant les indices de chaque
	 *         ligne cochée
	 * 
	 */
	public int[][] getIndexSelectTab(Object[][] table) {
		ArrayList<Integer[]> ArrayDataSelect = new ArrayList<Integer[]>();
		int lig = table.length;
		int col;

		if (lig > 0) {
			col = table[0].length;
		} else {
			col = 0;
		}

		System.out.println(lig);
		System.out.println(col);

		for (int i = 0; i < lig; i++) {
			if ((boolean) table[i][0] == (true)) {
				System.out.println(ArrayDataSelect);
				ArrayDataSelect.add(new Integer[] { (int) table[i][1],
						(int) table[i][5] });
			}

		}

		int[][] tab = { {} };
		System.out.println(ArrayDataSelect);
		if (ArrayDataSelect.size() != 0) {
			tab = new int[ArrayDataSelect.size()][ArrayDataSelect.get(0).length];
			for (int i = 0; i < ArrayDataSelect.size(); i++) {
				tab[i][0] = ArrayDataSelect.get(i)[0];
				tab[i][1] = ArrayDataSelect.get(i)[1];
			}
		}

		return tab;
	}

	/**
	 * Effectue une requête de mise à jour et de gestion dans la BDD.
	 * 
	 * @param requeteSQL
	 *            La requête SQL à saisir dans la BDD
	 */
	public void BDDupdate(String requeteSQL) {
		try {
			System.out.println(requeteSQL);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			int res = stm.executeUpdate(requeteSQL);

			System.out.println("Nb enregistrement : " + res);

			conn.close();
			updateTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'extraire de la base de donees un temps au format de chaine de
	 * caractere pour pouvoir en simplifier le traitement et l'interpretation
	 * 
	 * @param HHmmss
	 *            chaine de characteres extraites au format HH:mm:ss
	 * @return temps
	 */
	public static Date stringToDate(String HHmmss) {
		Date temps = new Date();
		DateFormat dateformat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
		try {
			temps = dateformat.parse(HHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temps;
	}

	/**
	 * Permet d'extraire de la base de donees un temps au format de chaine de
	 * caractere pour pouvoir en simplifier le traitement et l'interpretation
	 * 
	 * @param HHmmss
	 *            chaine de characteres extraites au format HH:mm:ss
	 * @return temps
	 */
	public static Date stringToDuree(String HHmmss) {
		Date temps = new Date();
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		try {
			temps = dateformat.parse(HHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temps;
	}

	/**
	 * Permet d'inserer dans la base de donees un temps au format de chaÃ®ne de
	 * caractere en renseignant un temps au format Date de Java pour pouvoir en
	 * simplifier le traitement et l'insertion.
	 * 
	 * @param temps
	 *            date a convertir au format `hh:mm:ss pour insertion plus
	 *            simple dans BDD
	 * @return HHmmss
	 */
	public static String dateToString(Date temps) {
		String HHmmss = "";

		Calendar calTemps = Calendar.getInstance();
		calTemps.setTime(temps);

		int h, m, s;
		h = m = s = 0;
		if (calTemps.get(Calendar.DAY_OF_MONTH) != 1) {
			h += 24 * (calTemps.get(Calendar.DAY_OF_MONTH) - 1);
		}
		h += calTemps.get(Calendar.HOUR_OF_DAY);
		m += calTemps.get(Calendar.MINUTE);
		s += calTemps.get(Calendar.SECOND);

		String hh, mm, ss = "";

		if (h < 10) {
			hh = "0" + h;
		} else {
			hh = "" + h;
		}
		if (m < 10) {
			mm = "0" + m;
		} else {
			mm = "" + m;
		}
		if (s < 10) {
			ss = "0" + s;
		} else {
			ss = "" + s;
		}

		HHmmss += hh + ":" + mm + ":" + ss;
		return HHmmss;
	}

	/**
	 * Manipulation (addition) de Calendrier
	 * 
	 * @param c1
	 *            , un calandrier Ã  ajouter
	 * @param c2
	 *            , un autre calandrier Ã  ajouter
	 * @return
	 */
	public static Calendar addTwoCal(Calendar c1, Calendar c2) {
		long sum = c1.getTimeInMillis() + c2.getTimeInMillis();
		Calendar sumCalendar = (Calendar) c1.clone();
		sumCalendar.setTimeInMillis(sum);
		sumCalendar.add(Calendar.HOUR, 1);

		return sumCalendar;
	}

	public Date multiDate(Date d1, int coef) {
		Date multi = stringToDuree("00:00:00");
		// Date res = addTwoDates(d1, d1);
		if (coef != 0) {
			for (int i = 0; i < coef; i++) {
				multi = addTwoDates(d1, multi);
				System.out.println("durée : " + d1 + "Total : " + multi);
			}
		} else {
			multi = stringToDuree("00:00:00");
		}
		return multi;
	}

	public Date addTwoDates(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(d1);
		c2.setTime(d2);

		return addTwoCal(c1, c2).getTime();
	}

	public Date substractTwoDates(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(d1);
		c2.setTime(d2);

		return substractTwoCal(c1, c2).getTime();
	}

	/**
	 * Manipulation (soustraction) de Calendrier
	 * 
	 * @param c1
	 *            , un calendrier
	 * @param c2
	 *            , un autre calandrier a soustraire
	 * @return
	 */
	public static Calendar substractTwoCal(Calendar c1, Calendar c2) {

		Calendar subCalendar = Calendar.getInstance();
		subCalendar.setTimeZone(c1.getTimeZone());

		long sub = c1.getTimeInMillis() - c2.getTimeInMillis();
		if (sub < 0)
			subCalendar.setTimeInMillis(0);
		else
			subCalendar.setTimeInMillis(sub);
		subCalendar.add(Calendar.HOUR, -1);

		return subCalendar;
	}
}
