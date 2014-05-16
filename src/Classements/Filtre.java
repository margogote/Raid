package Classements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import BDD.DataSourceProvider;

/**
 * Permet l'expression de la séléction selon des critères donnés
 * 
 * @author philippesmessaert
 * 
 */
public class Filtre {
	ArrayList<Integer> listeIdsFiltre = new ArrayList<>();

	public Filtre(String difficultee, String groupe, String typeEquipe, int idc) {
		ArrayList<ArrayList<Integer>> listeDeListe = new ArrayList<>();
		listeDeListe.add(equipeDeCeGroupe(groupe, idc));
		listeDeListe.add(equipeDeCeType(typeEquipe, idc));
		listeDeListe.add(equipesDeCetteDificulte(difficultee, idc));
		this.listeIdsFiltre = gardeQueLesBonsIds(listeDeListe);
	}

	public ArrayList<Integer> getListeIdsFiltres() {
		return listeIdsFiltre;
	}

	/**
	 * Permet d'obtenir la liste des identifiants d'épreuves correspondant à
	 * cette difficulté et cette compétition
	 * 
	 * @param difficulte
	 *            , la difficulte de l'epreuve cherchees
	 * @param idc
	 *            , identifiant de la compétition concernee
	 * @return
	 */
	public static ArrayList<Integer> epreuvesDeCetteDificulte(
			String difficulte, int idc) {
		ArrayList<Integer> epreuves = new ArrayList<>();

		String requeteSQL = "SELECT idEpreuve FROM epreuve WHERE `idCompetition` = '"
				+ idc + "' AND `difficulte` = '" + difficulte + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				epreuves.add(res.getInt(1));
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return epreuves;
	}

	/**
	 * Permet d'obtenir la liste des identifiants d'équipes correspondant à
	 * cette difficulté et à cette compétition
	 * 
	 * @param difficulte
	 *            , la difficulte de l'equipe a chercher
	 * @param idc
	 *            , identifiant de la compétition concernee
	 * @return equipes , les �quipes correspondantes
	 */
	public static ArrayList<Integer> equipesDeCetteDificulte(String difficulte,
			int idc) {
		ArrayList<Integer> equipes = new ArrayList<>();

		String requeteSQL = "SELECT idEquipe FROM equipe WHERE `idCompetition` = '"
				+ idc + "' AND `typeDifficulte` = '" + difficulte + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				equipes.add(res.getInt(1));
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipes;
	}

	/**
	 * Permet d'obtenir la liste des identifiants d'équipes correspondant à ce
	 * groupe et à cette compétition
	 * 
	 * @param groupe
	 *            le groupe d'auipe a cherhcer
	 * @param idc
	 *            , identifiant de la compétition concernee
	 * @return equipes , les �quipes correspondantes
	 */
	public static ArrayList<Integer> equipeDeCeGroupe(String groupe, int idc) {
		ArrayList<Integer> equipes = new ArrayList<>();

		String requeteSQL = "SELECT idEquipe FROM equipe WHERE `idCompetition` = '"
				+ idc + "' AND `nomGroupe` = '" + groupe + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				equipes.add(res.getInt(1));
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipes;
	}

	/**
	 * Permet d'obtenir la liste des identifiants d'équipes correspondant à ce
	 * type et a cette compétition
	 * 
	 * @param type
	 *            , le type d'equipe recherche
	 * @param idc
	 *            , identifiant de la compétition concernee
	 * @return equipes , les �quipes correspondantes
	 */
	public static ArrayList<Integer> equipeDeCeType(String type, int idc) {
		ArrayList<Integer> equipes = new ArrayList<>();

		String requeteSQL = "SELECT idEquipe FROM equipe WHERE `idCompetition` = '"
				+ idc + "' AND `typeEquipe` = '" + type + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource()
					.getConnection();
			// System.out.println("Connexion effective !");
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				equipes.add(res.getInt(1));
			}
			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipes;
	}

	/**
	 * Parcours autant de listes d'identifiants que presentes dans la liste de
	 * liste en parametre afin de retourner une liste des identifiants presents
	 * dans toutes les listes
	 * 
	 * @param listesIds
	 *            la liste de listes d'entiers a trier
	 * @return bonsIds , contenant les id des �quipes coressondant aux filtres
	 */
	public static ArrayList<Integer> gardeQueLesBonsIds(
			ArrayList<ArrayList<Integer>> listesIds) {
		ArrayList<Integer> bonsIds = new ArrayList<>();
		int i, j, k;
		Boolean nonTrouve;
		// int cmpt=0;

		System.out.println("P1");

		if (listesIds.size() < 2)
			System.out.println("Il y a même pas deux liste à fusionner");
		else {
			System.out.println("P2");
			for (i = 0; i < listesIds.size() - 1; i++) {
				System.out.println("P3 i = " + i);
				for (j = 0; j < listesIds.get(i).size(); j++) {
					/*
					 * System.out.println("P4 j = "+j);
					 * for(k=0;k<listesIds.get(i+1).size();k++){ //cmpt++;
					 * System.out.println("P5 k = "+k);
					 * if(listesIds.get(i).get(j
					 * )==listesIds.get(i+1).get(k))bonsIds
					 * .add(listesIds.get(i).get(j)); }
					 */

					System.out.println("P4 j = " + j);
					k = 0;
					nonTrouve = true;

					if (k < listesIds.get(i + 1).size())
						System.out.println("k<taille = true");
					if (nonTrouve)
						System.out.println("nonTrouve = true");

					while (k < listesIds.get(i + 1).size() && nonTrouve) {
						// cmpt++;
						System.out.println("P5 k = " + k);
						if (listesIds.get(i).get(j) == listesIds.get(i + 1)
								.get(k)) {
							bonsIds.add(listesIds.get(i).get(j));
							nonTrouve = false;
							System.out.println("P6 : Trouve !");
						}
						k++;
					}
				}
			}
		}
		// System.out.println(cmpt);
		return bonsIds;
	}
}
