package Classements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import BDD.DataSourceProvider;
import Models.Equipe;

/**
 * Permet de calculer et produire le classement voulu à l'instant t
 * 
 * @author philippesmessaert
 * 
 */
public class Classement {
	/**
	 * Permet de generer le classement des équipes filtrees selon le contenu du
	 * parametre filtre et uniquement pour les journees appartenant à la liste
	 * des journées en parametre
	 * 
	 * @param journees
	 *            ,la liste des journees a prendre en compte
	 * @param filtre
	 *            ,objet de type filtre permetant la selection des uniques
	 *            identifiants d'equipe à traiter
	 * @param idc
	 *            , identifiant de la competition concernee
	 */
	public Classement(ArrayList<Date> journees, Filtre filtre, int idc) {
		ArrayList<Equipe> equipes = new ArrayList<>();
		ArrayList<Integer> idEpreuvesOk = new ArrayList<>();
		ArrayList<Integer> idEquipesOk = new ArrayList<>();

		idEquipesOk = filtre.getListeIdsFiltres();

		idEpreuvesOk = epreuvesDeCesJournees(journees, idc);
		// int jj;
		// for(jj=0;jj<idEpreuvesOk.size();jj++)System.out.println("PPPPPPPPPPPPPPPPPPPPPPP idEpreuve OK => "+idEpreuvesOk.get(jj));

		// "' AND `idEquipe` = '"+idEq+"' AND `idEpreuve` = '"+idEp+
		String requeteSQL = "SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE `idCompetition` = '"
				+ idc
				+ "' AND (("
				+ listeIdsEnReqSQL("idEpreuve", idEpreuvesOk)
				+ ") AND ("
				+ listeIdsEnReqSQL("idEquipe", idEquipesOk) + "))";
		// SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE
		// `idCompetition` = 1 AND (idEpreuve= OR idEpreuve = OR idEpreuve =)

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							equipes.get(i).addToScore(
									stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					equipes.add(new Equipe(res.getInt(1), stringToDate(res
							.getString(3))));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		requeteSQL = "SELECT idEquipe,avoir.idMB,tempsMalusBonus,malusbonus.malus FROM avoir INNER JOIN malusbonus ON avoir.idMB = malusbonus.idMB WHERE avoir.idCompetition = "
				+ idc + " ORDER BY malusbonus.malus DESC";
		// SELECT idEquipe,avoir.idMB,tempsMalusBonus FROM avoir INNER JOIN
		// malusbonus ON avoir.idMB = malusbonus.idMB WHERE
		// avoir.idCompetition=1 ORDER BY malusbonus.malus DESC
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							if (res.getBoolean(4)) {
								equipes.get(i).addToScore(
										stringToDate(res.getString(3)));
							} else
								equipes.get(i).getFromScore(
										stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					if (res.getBoolean(4)) {
						equipes.add(new Equipe(res.getInt(1), stringToDate(res
								.getString(3))));
					} else
						equipes.add(new Equipe(res.getInt(1),
								stringToDate("00:00:00")));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		equipes = classeLesEquipes(equipes, idc);
		int j;
		for (j = 0; j < equipes.size(); j++) {
			System.out.println("Id equipe " + j + " au classement => "
					+ equipes.get(j).getIdEquipe() + " score = "
					+ equipes.get(j).getScore().toString());
		}
	}

	/**
	 * /** Permet de generer le classement des equipes filtrees selon le contenu
	 * du parametre filtre
	 * 
	 * @param filtre
	 *            , objet de type filtre permetant la selection des uniques
	 *            identifiants d'équipe à traiter
	 * @param idc
	 *            , identifiant de la compétition concernee
	 */
	public Classement(Filtre filtre, int idc) {
		ArrayList<Equipe> equipes = new ArrayList<>();
		ArrayList<Integer> idEquipesOk = new ArrayList<>();
		idEquipesOk = filtre.getListeIdsFiltres();
		int ii;
		for (ii = 0; ii < idEquipesOk.size(); ii++)
			System.out.println("PPPPPPPPPPPPPPPPPPPPPPP idEquipes OK => "
					+ idEquipesOk.get(ii));
		// "' AND `idEquipe` = '"+idEq+"' AND `idEpreuve` = '"+idEp+
		String requeteSQL = "SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE `idCompetition` = '"
				+ idc
				+ "' AND ("
				+ listeIdsEnReqSQL("idEquipe", idEquipesOk)
				+ ")";
		// SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE
		// `idCompetition` = 1 AND (idEpreuve= OR idEpreuve = OR idEpreuve =)

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							equipes.get(i).addToScore(
									stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					equipes.add(new Equipe(res.getInt(1), stringToDate(res
							.getString(3))));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		requeteSQL = "SELECT idEquipe,avoir.idMB,tempsMalusBonus,malusbonus.malus FROM avoir INNER JOIN malusbonus ON avoir.idMB = malusbonus.idMB WHERE avoir.idCompetition = "
				+ idc
				+ " /*AND ("
				+ listeIdsEnReqSQL("avoir.idEquipe", idEquipesOk)
				+ ")*/ ORDER BY malusbonus.malus DESC";
		// SELECT idEquipe,avoir.idMB,tempsMalusBonus FROM avoir INNER JOIN
		// malusbonus ON avoir.idMB = malusbonus.idMB WHERE
		// avoir.idCompetition=1 ORDER BY malusbonus.malus DESC
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							if (res.getBoolean(4)) {
								equipes.get(i).addToScore(
										stringToDate(res.getString(3)));
							} else
								equipes.get(i).getFromScore(
										stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					if (res.getBoolean(4)) {
						equipes.add(new Equipe(res.getInt(1), stringToDate(res
								.getString(3))));
					} else
						equipes.add(new Equipe(res.getInt(1),
								stringToDate("00:00:00")));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		equipes = classeLesEquipes(equipes, idc);
		int j;
		for (j = 0; j < equipes.size(); j++) {
			System.out.println("Id equipe POMPOM " + j + " au classement => "
					+ equipes.get(j).getIdEquipe() + " score = "
					+ equipes.get(j).getScore().toString());
		}
	}

	/**
	 * Classement uniquement pour les journées appartenant a la liste des
	 * journees en parametre
	 * 
	 * @param journees
	 *            , liste des journees dont il faut compatbiliser les points
	 * @param idc
	 *            , l'id de la competition
	 */
	public Classement(ArrayList<Date> journees, int idc) {
		ArrayList<Equipe> equipes = new ArrayList<>();
		ArrayList<Integer> idEpreuvesOk = new ArrayList<>();

		idEpreuvesOk = epreuvesDeCesJournees(journees, idc);
		// "' AND `idEquipe` = '"+idEq+"' AND `idEpreuve` = '"+idEp+
		String requeteSQL = "SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE `idCompetition` = '"
				+ idc
				+ "' AND ("
				+ listeIdsEnReqSQL("idEpreuve", idEpreuvesOk)
				+ ")";
		// SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE
		// `idCompetition` = 1 AND (idEpreuve= OR idEpreuve = OR idEpreuve =)

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							equipes.get(i).addToScore(
									stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					equipes.add(new Equipe(res.getInt(1), stringToDate(res
							.getString(3))));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		requeteSQL = "SELECT idEquipe,avoir.idMB,tempsMalusBonus,malusbonus.malus FROM avoir INNER JOIN malusbonus ON avoir.idMB = malusbonus.idMB WHERE avoir.idCompetition = "
				+ idc + " ORDER BY malusbonus.malus DESC";
		// SELECT idEquipe,avoir.idMB,tempsMalusBonus FROM avoir INNER JOIN
		// malusbonus ON avoir.idMB = malusbonus.idMB WHERE
		// avoir.idCompetition=1 ORDER BY malusbonus.malus DESC
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							if (res.getBoolean(4)) {
								equipes.get(i).addToScore(
										stringToDate(res.getString(3)));
							} else
								equipes.get(i).getFromScore(
										stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					if (res.getBoolean(4)) {
						equipes.add(new Equipe(res.getInt(1), stringToDate(res
								.getString(3))));
					} else
						equipes.add(new Equipe(res.getInt(1),
								stringToDate("00:00:00")));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		equipes = classeLesEquipes(equipes, idc);
		int j;
		for (j = 0; j < equipes.size(); j++) {
			System.out.println("Id equipe " + j + " au classement => "
					+ equipes.get(j).getIdEquipe() + " score = "
					+ equipes.get(j).getScore().toString());
		}
	}

	/**
	 * Classement général de la compétition idc en paramètre sans aucun
	 * filtres
	 * 
	 * @param idc
	 *            , identifiant de la compétition concernee
	 */
	public Classement(int idc) {
		ArrayList<Equipe> equipes = new ArrayList<>();

		// "' AND `idEquipe` = '"+idEq+"' AND `idEpreuve` = '"+idEp+
		String requeteSQL = "SELECT idEquipe,idEpreuve,tempsRealise FROM scorer WHERE `idCompetition` = '"
				+ idc + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							equipes.get(i).addToScore(
									stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					equipes.add(new Equipe(res.getInt(1), stringToDate(res
							.getString(3))));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		requeteSQL = "SELECT idEquipe,avoir.idMB,tempsMalusBonus,malusbonus.malus FROM avoir INNER JOIN malusbonus ON avoir.idMB = malusbonus.idMB WHERE avoir.idCompetition = "
				+ idc + " ORDER BY malusbonus.malus DESC";
		// SELECT idEquipe,avoir.idMB,tempsMalusBonus FROM avoir INNER JOIN
		// malusbonus ON avoir.idMB = malusbonus.idMB WHERE
		// avoir.idCompetition=1 ORDER BY malusbonus.malus DESC
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				if (equipeExisteDeja(equipes, res.getInt(1))) {
					int i;
					for (i = 0; i < equipes.size(); i++) {
						if (equipes.get(i).getIdEquipe() == res.getInt(1)) {
							if (res.getBoolean(4)) {
								equipes.get(i).addToScore(
										stringToDate(res.getString(3)));
							} else
								equipes.get(i).getFromScore(
										stringToDate(res.getString(3)));

							// System.out.println("Ajout de : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
						}
						// else
						// System.out.println("NON PAS PU ETRE AJOUTES : "+stringToDate(res.getString(3)).toString()+" a l'equipe "+res.getInt(1));
					}
				} else {
					if (res.getBoolean(4)) {
						equipes.add(new Equipe(res.getInt(1), stringToDate(res
								.getString(3))));
					} else
						equipes.add(new Equipe(res.getInt(1),
								stringToDate("00:00:00")));
					// System.out.println("Ajout Equipe "+res.getInt(1)+" avec un score de : "+stringToDate(res.getString(3)).toString());
				}
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * Test de la fonction classeLesEquipes
		 */
		ArrayList<Equipe> equipes2 = classeLesEquipes(equipes, idc);
		int j;
		for (j = 0; j < equipes2.size(); j++) {
			System.out.println("L'équipe : " + equipes2.get(j).getNomEquipe()
					+ " dossard " + equipes2.get(j).getDossard() + " termine "
					+ (j + 1)
					+ "ème au classement GENERAL avec un temps de => "
					+ equipes2.get(j).getScore().toString());
		}

		// Test de la fonc listeIdsEnReqSQL
		ArrayList<Integer> listeDentiers = new ArrayList<>();
		listeDentiers.add(1);
		listeDentiers.add(3);
		System.out.println(listeIdsEnReqSQL("idEpreuve", listeDentiers));
	}

	/**
	 * Test de la fonction classeLesEquipes
	 */
	ArrayList<Equipe> classeLesEquipes(ArrayList<Equipe> equipes, int idc) {
		ArrayList<Equipe> equipesClassees = new ArrayList<>();
		Equipe eqTemp;
		int i, j;
		equipesClassees = equipes;

		if (equipes.size() > 1) {
			// System.out.println("Taille ok");
			for (i = 0; i < equipesClassees.size() - 1; i++) {
				// System.out.println("Passsage "+i);
				for (j = 0; j < equipesClassees.size() - 1; j++) {
					// System.out.println("Element "+j);
					if (equipesClassees.get(j).getScore()
							.after(equipesClassees.get(j + 1).getScore())) {
						// System.out.println("Echange");
						eqTemp = equipesClassees.get(j);
						equipesClassees.set(j, equipesClassees.get(j + 1));
						equipesClassees.set(j + 1, eqTemp);
					}
					if (equipesClassees.get(j).getScore()
							.before(equipesClassees.get(j + 1).getScore())) {
						// System.out.println("Before ?");
					}
				}
			}

		} else {
			System.out
					.println("On tri pas une liste avec une seule équipe :)");
		}/*
		 * ArrayList<Equipe> equipesClasseesEtRemplies = new ArrayList<>();
		 * equipesClasseesEtRemplies = rempliPlusCesEquipes(equipesClassees,
		 * idc);
		 * 
		 * return equipesClasseesEtRemplies;
		 */
		return equipesClassees;
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
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		try {
			temps = dateformat.parse(HHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println(temps.toString());
		return temps;
	}

	/**
	 * Permet d'inserer dans la base de donees un temps au format de chaîne de
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
		h += calTemps.get(Calendar.HOUR);
		// System.out.println("h = "+h);
		m += calTemps.get(Calendar.MINUTE);
		// System.out.println("m = "+m);
		s += calTemps.get(Calendar.SECOND);
		// System.out.println("s = "+s);

		HHmmss += h + ":" + m + ":" + s;

		// System.out.println("HHmmss = "+HHmmss);
		return HHmmss;
	}

	/**
	 * Manipulation (addittion) de Calendrier
	 * 
	 * @param c1
	 *            , un calandrier à ajouter
	 * @param c2
	 *            , un autre calandrier à ajouter
	 * @return
	 */
	public static Calendar addTwoCal(Calendar c1, Calendar c2) {
		// System.out.println(""+c1.getTimeInMillis());
		// System.out.println(""+c2.getTimeInMillis());
		long sum = c1.getTimeInMillis() + c2.getTimeInMillis();
		Calendar sumCalendar = (Calendar) c1.clone();
		sumCalendar.setTimeInMillis(sum);
		sumCalendar.add(Calendar.HOUR, 1);

		/*
		 * Pb heure d'été
		 * http://www.geeketfier.fr/index.php/bien-utiliser-la-date
		 * -et-lheure-en-java/
		 * http://www.developpez.net/forums/d1161597/java/general
		 * -java/probleme-decalage-horaire-calendar/
		 */

		return sumCalendar;
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

		// System.out.println(""+c1.getTimeInMillis());
		// System.out.println(""+c2.getTimeInMillis());
		long sub = c1.getTimeInMillis() - c2.getTimeInMillis();
		if (sub < 0)
			subCalendar.setTimeInMillis(0);
		else
			subCalendar.setTimeInMillis(sub);
		subCalendar.add(Calendar.HOUR, -1);

		/*
		 * Pb heure d'été
		 * http://www.geeketfier.fr/index.php/bien-utiliser-la-date
		 * -et-lheure-en-java/
		 * http://www.developpez.net/forums/d1161597/java/general
		 * -java/probleme-decalage-horaire-calendar/
		 */
		return subCalendar;
	}

	/**
	 * Permet la verification facile de la presence d'une equipe dans une liste
	 * d'equipes
	 * 
	 * @param listeEquipe
	 *            , liste d'equipe dans laquelle on veut savoir si equipe existe
	 * @param idEquipe
	 *            , l'equipe dont veut connapitre l'existance
	 * @return
	 */
	Boolean equipeExisteDeja(ArrayList<Equipe> listeEquipe, int idEquipe) {
		int i = 0;
		Boolean existe = false;
		while (i < listeEquipe.size() && !existe) {

			if (listeEquipe.get(i).getIdEquipe() == idEquipe)
				existe = true;
			i++;
		}
		return existe;
	}

	/*
	 * public Object[][] updateTable() {
	 * 
	 * ArrayList<Object[]> ArrayData = new ArrayList<>();
	 * 
	 * String idc; String requeteSQL =
	 * "SELECT * FROM balise WHERE `idCompetition` = '"+idc+"'";
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver");
	 * System.out.println("Driver O.K.");
	 * 
	 * Connection conn = DataSourceProvider.getDataSource().getConnection();
	 * System.out.println("Connexion effective !"); Statement stm =
	 * conn.createStatement(); ResultSet res = stm.executeQuery(requeteSQL);
	 * 
	 * while (res.next()) { ArrayData.add(new Object[] { new Boolean(false),
	 * res.getString(1) }); System.out.println("Nom : " + res.getString(1));
	 * 
	 * }
	 * 
	 * conn.close(); res.close();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * Object[][] data = ArrayToTab(ArrayData);
	 * 
	 * Inte_Resultat inteResultat = new Inte_Resultat(1); inteResultat.;
	 * 
	 * System.out.println("MAJ Table"); return data; } public Object[][]
	 * ArrayToTab(ArrayList<Object[]> array) {
	 * 
	 * int lengthLig = array.size(); int lengthCol; if(lengthLig>0){ lengthCol =
	 * array.get(0).length; }else{lengthCol=0;} Object[][] tab = new
	 * Object[lengthLig][lengthCol]; for (int i = 0; i < lengthLig; i++) {
	 * tab[i] = array.get(i); // System.out.println(tab[i]); } return tab; }
	 */
	/**
	 * Permet de récupérer la liste des Ids d'épreuves ayant lieu pendant une
	 * de ces journées en paramètre
	 * 
	 * @param journees
	 *            , listes des journees pendant lesquelles on cherche quelles
	 *            epreuves ont eu lieu
	 * @param idc
	 *            , identifiant de la compétition concernee
	 * @return
	 */
	public static ArrayList<Integer> epreuvesDeCesJournees(
			ArrayList<Date> journees, int idc) {
		ArrayList<Integer> epreuves = new ArrayList<>();
		int i;
		for (i = 0; i < journees.size(); i++) {

			String requeteSQL = "SELECT dateHeureEpreuve,idEpreuve FROM epreuve WHERE `idCompetition` = '"
					+ idc + "'";

			try {
				Class.forName("com.mysql.jdbc.Driver");
				// System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource()
						.getConnection();
				// System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);

				while (res.next()) {
					if (res.getDate(1).compareTo(journees.get(i)) == 0)
						epreuves.add(res.getInt(2));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return epreuves;
	}

	/**
	 * Permet de compléter une clause WHERE pour faire correspondre la liste
	 * d'identifiants avec l'extraction de deonnees d'une base.
	 * 
	 * @param nomChamp
	 *            , le nom du champ de la table de la bdd dans lequel on veut
	 *            effectuer la clause
	 * @param listeIds
	 *            , la listes des identifiants a recuperer dans la requetes donc
	 *            dans la clause WHERE
	 * @return
	 */
	public String listeIdsEnReqSQL(String nomChamp, ArrayList<Integer> listeIds) {
		String boutDeRequete = "";
		int i;
		for (i = 0; i < listeIds.size(); i++) {
			boutDeRequete += nomChamp;
			boutDeRequete += "=";
			boutDeRequete += listeIds.get(i);
			if (i + 1 < listeIds.size())
				boutDeRequete += " OR ";

		}
		return boutDeRequete;
	}
	/*
	 * public ArrayList<Equipe> rempliPlusCesEquipes(ArrayList<Equipe>
	 * equipes,int idc){ ArrayList<Equipe> newEquipes = equipes;`idCompetition`
	 * = '"+idc+"' AND /*Equipe newEquipe=new Equipe(0,new Date());
	 * //System.out.println("equipe.getIdEquipe() => "+equipe.getIdEquipe())
	 * String requeteSQL =
	 * "SELECT * FROM equipe WHERE  "+listeIdsEnReqSQL("idEquipe"
	 * ,listerLesIds(equipes));
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver");
	 * //System.out.println("Driver O.K.");
	 * 
	 * Connection conn = DataSourceProvider.getDataSource().getConnection();
	 * //System.out.println("Connexion effective !"); Statement stm =
	 * conn.createStatement(); ResultSet res = stm.executeQuery(requeteSQL);
	 * System.out.println("ICI CA MARCHE"); while (res.next()) {
	 * newEquipe.setNomEquipe(res.getString(2));
	 * System.out.println("Nom Equipe : "+res.getString(2));
	 * newEquipe.setTypeEquipe(res.getString(5));
	 * System.out.println("Type Equipe : "+res.getString(5));
	 * newEquipe.setDossard(res.getInt(6));
	 * System.out.println("Num dossard : "+res.getInt(6));
	 * newEquipes.add(newEquipe); } conn.close(); res.close();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return newEquipes; }
	 * ArrayList<Integer> listerLesIds(ArrayList<Equipe> equipes){ int i;
	 * ArrayList<Integer> listesDesIds = new ArrayList<>();
	 * for(i=0;i<equipes.size();i++){
	 * listesDesIds.add(equipes.get(i).getIdEquipe()); } return listesDesIds; }
	 */
}
