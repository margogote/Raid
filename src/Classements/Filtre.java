package Classements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import BDD.DataSourceProvider;

/**
 * Permet l'expression de la selection selon des criteres donnes
 * @author philippesmessaert
 *
 */
public class Filtre {
	ArrayList<Integer> listeIdsEquipeFiltre = new ArrayList<>();
	ArrayList<Integer> listeIdsEpreuveFiltre = new ArrayList<>();
	
	public Filtre(String difficultee, String groupe,String typeEquipe,ArrayList<String> epreuves,int idc){
		
		//-----------Renvoi les Equipe a prendre en compte------------
		ArrayList<ArrayList<Integer>> listeDeListe = new ArrayList<>();
		
		listeDeListe.add(equipeDeCeGroupe(groupe, idc));
		listeDeListe.add(equipeDeCeType(typeEquipe, idc));
		listeDeListe.add(equipesDeCetteDificulte(difficultee, idc));
		
		this.listeIdsEquipeFiltre=gardeQueLesBonsIds(listeDeListe); 
		
		//-----------Renvoi les Epreuves a prendre en compte------------
		
		this.listeIdsEpreuveFiltre=idsDeCesEpreuves(epreuves,idc); 
	}
	
	
	/**
	 * Donne la liste des ids correspondant aux epreuves donnees
	 * @param epreuves, la liste des epreuves a explorer
	 * @param idc, toujours l'identifiant de la compet
	 * @return, liste des ids correspondant aux epreuves donnees
	 */
	public ArrayList<Integer> idsDeCesEpreuves(ArrayList<String> epreuves,int idc) {
		ArrayList<Integer> listeIdsOk= new ArrayList<>();
		
		System.out.println("---------------fonc idsDeCesEpreuves --------------");
		
		if(epreuves.size()>0){
			String requeteSQL = "SELECT idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"' AND ";
			requeteSQL+=listeNomsEnReqSQL("nomEpreuve", epreuves);
			
			System.out.println("REQUETE SQL DONNE : "+requeteSQL);
			try {
				Class.forName("com.mysql.jdbc.Driver");

				Connection conn = DataSourceProvider.getDataSource().getConnection();
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);
				
				while (res.next()) {
					listeIdsOk.add(res.getInt(1));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		int i;
		for(i=0;i<listeIdsOk.size();i++){
			System.out.println("idEpreuve "+(i+1)+" = "+listeIdsOk.get(i));
		}
		System.out.println("---------------fin--------------");
		return listeIdsOk;
	}

	/**
	 * Met en forme une partie de requete SQL pour que la clause WHERE selectionne les parametres
	 * @param nomChamp, le nom du champ tel qu'il existe dans la BDD
	 * @param listeNoms, la liste des valeur a garder
	 * @return, la partie de la requete concernee
	 */
	public String listeNomsEnReqSQL(String nomChamp,ArrayList<String> listeNoms){
		String boutDeRequete="";
		int i;
		for(i=0;i<listeNoms.size();i++){
			boutDeRequete+=nomChamp;
			boutDeRequete+="=\"";
			boutDeRequete+=listeNoms.get(i);
			boutDeRequete+="\"";
			if(i+1<listeNoms.size())boutDeRequete+=" OR ";
			
		}
		System.out.println("LE BOUT DE REQUETTE EST : "+boutDeRequete);
		return boutDeRequete;
	}

	/**
	 * Permet d'obtenir la liste des identifiants d'epreuves correspondant a cette difficulte
	 * et cette competition
	 * 
	 * @param difficulte, la difficulte de l'epreuve cherchees
	 * @param idc , identifiant de la competition concernee
	 * @return, liste des identifiants d'épreuves correspondante
	 */
	public static ArrayList<Integer> epreuvesDeCetteDificulte(String difficulte,int idc){
		ArrayList<Integer> epreuves = new ArrayList<>();
			
		String requeteSQL = "SELECT idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"' AND `difficulte` = '"+difficulte+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
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
	 * Permet d'obtenir la liste des identifiants d'equipes correspondant a cette difficulte
	 * et a cette competition
	 * 
	 * @param difficulte, la difficulte de l'equipe a chercher
	 * @param idc, identifiant de la competition concernee
	 * @return, liste des identifiants d'equipes correspondant à cette difficulte
	 */
	public static ArrayList<Integer> equipesDeCetteDificulte(String difficulte,int idc){
		ArrayList<Integer> equipes = new ArrayList<>();
			
		String requeteSQL = "";
		if(difficulte!="")requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"' AND `typeDifficulte` = '"+difficulte+"'";
		else requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"'";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
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
	 * Permet d'obtenir la liste des identifiants d'équipes correspondant à ce groupe
	 * et a cette competition
	 * 
	 * @param groupe le groupe d'equipe a chercher
	 * @param idc, identifiant de la competition concernee
	 * @return, liste des identifiants d'équipes correspondante
	 */
	public static ArrayList<Integer> equipeDeCeGroupe(String groupe,int idc){
		ArrayList<Integer> equipes = new ArrayList<>();
		
		String requeteSQL = "";
		if(groupe!="")requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"' AND `nomGroupe` = '"+groupe+"'";
		else requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
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
	 * Permet d'obtenir la liste des identifiants d'equipes correspondant a ce type
	 * et a cette competition
	 * 
	 * @param type, le type d'equipe recherche
	 * @param idc, identifiant de la compétition concernee
	 * @return,liste des identifiants d'equipes correspondante
	 */
	public static ArrayList<Integer> equipeDeCeType(String type,int idc){
		ArrayList<Integer> equipes = new ArrayList<>();
		

		String requeteSQL = "";
		if(type!="")requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"' AND `typeEquipe` = '"+type+"'";
		else requeteSQL+="SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"'";
		

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
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
	 * Retourne si un entier existe dans une liste
	 * @param liste,
	 * @param id,
	 * @return, true si l'entier existe dans la liste
	 */
	public static Boolean idExisteDansLaListe(ArrayList<Integer> liste,int id){
		Boolean existe=false;
		int i=0;
		while(i<liste.size() && !existe){
			if(liste.get(i)==id)existe=true;
			i++;
		}
		return existe;
	}
	/**
	 * Obtenir tous les entiers distincts present au moins une fois dans une des listes
	 * @param listeDeListes
	 * @return, liste des entiers distincts present au moins une fois dans la liste
	 */
	public static ArrayList<Integer> listeDesIdsQuiExistentDansCesListes(ArrayList<ArrayList<Integer>> listeDeListes){
		ArrayList<Integer> listeDesIdsQuiExistent = new ArrayList<>();
		int i,j;
		for(i=0;i<listeDeListes.size();i++){
			for(j=0;j<listeDeListes.get(i).size();j++){
				if(!idExisteDansLaListe(listeDesIdsQuiExistent,listeDeListes.get(i).get(j))){
					listeDesIdsQuiExistent.add(listeDeListes.get(i).get(j));
				}
			}
		}
		return listeDesIdsQuiExistent;
	}
	/**
	 * Parcours autant de listes d'identifiants que presentes dans la liste de liste en parametre
	 * afin de retourner une liste des identifiants presents dans toutes les listes
	 * @param listesIds, la liste de listes d'entiers a trier
	 * @return, liste des identifiants qui sont presents dans TOUTES les listes
	 */
	public static ArrayList<Integer> gardeQueLesBonsIds(ArrayList<ArrayList<Integer>> listesIds){
		ArrayList<Integer> bonsIds = new ArrayList<>();
		int i,j;
		
		if(listesIds.size()<2){
			if(listesIds.size()==1)bonsIds=listesIds.get(0);
			else System.out.println("ERREUR fonc gardeQueLesBonsIds => Liste à trier vide");
			System.out.println("---- CATASTROPHE => Il y a même pas deux liste à fusionner (fonction gardeQueLesBonsIds classe Filtre)-----");
		}
		else{
			ArrayList<Integer> listeDesIdsQuiExistent = new ArrayList<>();
			listeDesIdsQuiExistent=listeDesIdsQuiExistentDansCesListes(listesIds);
			Boolean idOk;
			
			for(i=0;i<listeDesIdsQuiExistent.size();i++){
				j=0;
				idOk=true;
				while(j<listesIds.size() && idOk){
					if(!idExisteDansLaListe(listesIds.get(j), listeDesIdsQuiExistent.get(i))){
						idOk=false;	
					}
					j++;
				}
				if(idOk){
					bonsIds.add(listeDesIdsQuiExistent.get(i));
				}
			}
		}
		return bonsIds;
	}
	/**
	 * getter de la liste des ids des equipes filtrees
	 * @return, liste des ids des equipes filtrees
	 */
	public ArrayList<Integer> getListeIdsEquipeFiltres(){
		return listeIdsEquipeFiltre;
	}
	/**
	 * getter de la liste des ids des epreuves filtrees
	 * @return, liste des ids des epreuves filtrees
	 */
	public ArrayList<Integer> getListeIdsEpreuveFiltres(){
		return listeIdsEpreuveFiltre;
	}
}
