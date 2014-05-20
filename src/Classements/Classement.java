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

	
	private ArrayList<Equipe> classement;
	
	/**
	 * setter qui permet de stocker le classement realise 	
	 * @param classement sous fourme de liste d'equipes
	 */
	public void setClassement (ArrayList<Equipe> classement){
		this.classement=classement;
	}
	/**
	 * getter qui permet de recuperer le classement
	 * @return sous fourme de liste d'equipes
	 */
	public ArrayList<Equipe> getClassement (){
		return this.classement;
	}
	
	/**
	 * Constructeur de la classe Classement
	 * 
	 * On l'appelle avec les parametres ci-dessous qui permettent de definir 
	 * les conditions a prendre en compte pour la realisation du classement
	 * 
	 * Les conditions sont exprimees par :
	 * 		- la liste des dates sur lesquelles il faut faire le classement (peut être vide
	 * 		et dans ce cas ignoree)
	 * 		- un filtre (voir comment est structure un filtre dans la classe eponyme)
	 * 		- et toujours la compet
	 * 
	 * Il realise ensuite le classement (selon ces conditions)
	 * 
	 * Puis l'affecte au parametre de la classe (classement) sous forme de liste d'equipe
	 * 
	 * @param journees, liste des journees en format dates sur lesquelles on réalise les classement
	 * @param filtre, donne les autres conditions pour realiser les classements
	 * @param idc, l'identifiant de la competition
	 */
	public Classement(ArrayList<Date> journees,Filtre filtre,int idc){
		
	ArrayList<Equipe> equipes=new ArrayList<>();
	ArrayList<Integer> idEpreuvesOk = new ArrayList<>();
	ArrayList<Integer> idEquipesOk = new ArrayList<>();
	
	//Selection des equipes à prendre en compte
	idEquipesOk=filtre.getListeIdsEquipeFiltres();
	
	//Selection des epreuves à prendre en compte
	if(filtre.getListeIdsEpreuveFiltres().size()!=0){
		idEpreuvesOk=filtre.getListeIdsEpreuveFiltres();
	}
	else idEpreuvesOk = epreuvesDeCesJournees(journees, idc);
	
	
	//Traduction en requête SQL
	String reqSQLequipesOk=listeIdsEnReqSQL("scorer.idEquipe", idEquipesOk);
	String reqSQLepreuvesOk=listeIdsEnReqSQL("scorer.idEpreuve", idEpreuvesOk);
	
	if(reqSQLepreuvesOk!="" &&reqSQLequipesOk!=""){
		/*
		 * SI il y a bien 2 listes non vides de conditions (equipes et epreuves)
		 * On les integrent dans la clause WHERE de la requete et on fait le classement
		 * SINON PAS LA PEINE DE FAIRE LE CLASSEMENT car il n'y aura de toutes manieres pas de resultats
		 */
		
		String requeteSQL = "SELECT scorer.idEquipe,scorer.idEpreuve,scorer.tempsRealise,equipe.dossard,equipe.nomEquipe,equipe.typeEquipe FROM scorer INNER JOIN equipe ON scorer.idEquipe=equipe.idEquipe WHERE scorer.idCompetition = '";
		requeteSQL+= idc;
		requeteSQL+= "' AND ((";
		requeteSQL+= reqSQLepreuvesOk;
		requeteSQL+= ") AND (";
		requeteSQL+= reqSQLequipesOk;
		requeteSQL+= "))";
		
		System.out.println(requeteSQL);
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
	
			Connection conn = DataSourceProvider.getDataSource().getConnection();
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			
			System.out.println("----------------Temps REALISES----------------");
			
			while (res.next()) {
				//SI l'equipe existe deja dans le classement on additionne son temps a son score
				if(equipeExisteDeja(equipes, res.getInt(1))){
				System.out.println("L'équipe existe DEJA !");
				int i;
					for(i=0;i<equipes.size();i++){
						if(equipes.get(i).getIdEquipe()==res.getInt(1)){
							equipes.get(i).addToScore(stringToDate(res.getString(3)));
							System.out.println("Score "+res.getString(3)+" ajoute a l'equipe : "+equipes.get(i).getIdEquipe());
						}
					}
				}
				else{
					//SINON on cree une nouvelle equipe qu'on ajoute a la liste avec ce score
					equipes.add(new Equipe(res.getInt(1),stringToDate(res.getString(3)),res.getInt(4),res.getString(5),res.getString(6)));
					System.out.println("Nouvelle equipe (id="+res.getInt(1)+") ajoutee avec un premier score de "+res.getString(3));
				}
			}
			conn.close();
			res.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		// On recupere ensuite les malus et bonus
		requeteSQL = "SELECT avoir.idEquipe,avoir.idMB,malusbonus.tempsMalusBonus,malusbonus.malus,equipe.dossard,equipe.nomEquipe,equipe.typeEquipe FROM avoir INNER JOIN malusbonus ON avoir.idMB = malusbonus.idMB INNER JOIN equipe ON avoir.idEquipe=equipe.idEquipe WHERE avoir.idCompetition = "+idc+" ORDER BY malusbonus.malus DESC";
		try {
			Class.forName("com.mysql.jdbc.Driver");
	
			Connection conn = DataSourceProvider.getDataSource().getConnection();
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
	
			System.out.println("----------------Malus bonus----------------");
			
			while (res.next()) {
				if(equipeExisteDeja(equipes, res.getInt(1))){
				//SI l'equipe est deja dans la liste c'est qu'elle doit etre prise en compte
				System.out.println("L'équipe (id="+res.getInt(1)+") existe DEJA !");
				
				//On recherche ensuite l'equipe dans la liste et on modifie son score
				int i;
				for(i=0;i<equipes.size();i++){
					if(equipes.get(i).getIdEquipe()==res.getInt(1)){
						//SI c'est un malus on ajoute le malus au score
						if(res.getBoolean(4)){
							System.out.println("Boolean True => C'est un MALUS");
							equipes.get(i).addToScore(stringToDate(res.getString(3)));
						}
						else{
							//SINON c'est un bonus et on l'enleve au score
							System.out.println("Boolean False => C'est un BONUS");
							equipes.get(i).getFromScore(stringToDate(res.getString(3)));
						}
					}
				}
				}// SINON on ne prend pas l'equipe en compte => on ne fait rien de plus
			}
			conn.close();
			res.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	*/	
	}
	/*
	 * Derniere etape
	 * On trie la liste des equipes du meilleur score au plus mauvais
	 * Puis on le stock grace au setter dans la variable de ce classement
	 */
	setClassement(classeLesEquipes(equipes,idc));
	
	}
	
	
	/**
	 * Cette fonction reordonne les equipes d'une liste du meilleur au plus mauvais score
	 * @param equipes, la liste des equipes a reordonner
	 * @param idc, toujours l'identifiant de la competition
	 * @return, la liste d'equipe reordonnee
	 */
	ArrayList<Equipe> classeLesEquipes(ArrayList<Equipe> equipes,int idc){
		ArrayList<Equipe> equipesClassees = new ArrayList<>();
		Equipe eqTemp; // variable tempon qui permet d'interchanger deux elements de la liste
		int i,j;
		equipesClassees=equipes;
		
		/*
		 * Il y a de meilleurs algorythme de tri.
		 * Ici on parcours la liste de taille n factorielle n fois
		 * Cependant pas de problemes de ralentissements
		 */
		if(equipes.size()>1){
			for(i=0;i<equipesClassees.size()-1;i++){
				for(j=0;j<equipesClassees.size()-1;j++){
					if(equipesClassees.get(j).getScore().after(equipesClassees.get(j+1).getScore())){
						/*
						 * SI le temps (score) de l'equipe situee après dans la liste est plus petit (meilleur)
						 * On inverse les deux equipes
						 */
						eqTemp=equipesClassees.get(j);
						equipesClassees.set(j, equipesClassees.get(j+1));
						equipesClassees.set(j+1,eqTemp);
					}
					
				}
			}
			
		}
		else{
			System.out.println("Fonction classeLesEquipes => ca sert a rien, on tri pas une liste avec une seule equipe :P");
		}
		return equipesClassees;
	}
	
	
	/**
	 * Converti une date en chaine de charactère format HH:mm:ss en objet type Date
	 * Permet de lire dans la base de donnees des durrees en format texte
	 * 
	 * @param HHmmss chaine de characteres extraites au format HH:mm:ss
	 * @return, la date correspondante
	 */
	public static Date stringToDate(String HHmmss){
		Date temps = new Date();
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		try {
			//L'utilisation de HH permet les heures jusqu'a 24 au lieu de 12 (hh)
			temps = dateformat.parse(HHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temps;
	}
	/**
	 * Converti un objet de type Date en chaine de charactère format HH:mm:ss
	 * Permet d'ecrire dans la base de donnees des durrees en format texte
	 * 
	 * @param temps, Date a convertir au format HH:mm:ss pour insertion plus simple dans BDD
	 * @return, chaine de charactere format HH:mm:ss
	 */
	public static String dateToString(Date temps){
		String HHmmss="";
		
		//On met la date dans un calendrier
		Calendar calTemps = Calendar.getInstance();
		calTemps.setTime(temps);
		
		//On récupère le nombre d'heure de minute et de seconde necessaire
		int h,m,s;
		h=m=s=0;
		if(calTemps.get(Calendar.DAY_OF_MONTH)!=1){
			h+=24*(calTemps.get(Calendar.DAY_OF_MONTH)-1);
		}
		h+=calTemps.get(Calendar.HOUR_OF_DAY);
		m+=calTemps.get(Calendar.MINUTE);
		s+=calTemps.get(Calendar.SECOND);

		//Si on a des valeur < 10 on ajoute un 0 devant pour toujours avoir 2 chiffres
		String hh, mm, ss="";
		
		if(h<10){
			hh = "0"+h;
		}
		else{hh=""+h;}
		
		if(m<10){
			mm = "0"+m;
		}else{mm=""+m;}
		
		if(s<10){
			ss = "0"+s;
		}else{ss=""+s;}
		
		//On les présentent dans une chaîne de charactère selon le format voulu
		HHmmss += hh + ":" + mm + ":" + ss;
		
		return HHmmss;
	}
	
	/**
	 * Manipulation (addittion) de Calendrier
	 * @param c1 un calandrier a ajouter
	 * @param c2 un autre calandrier a ajouter
	 * @return, le calendrier additionne
	 */
	public static Calendar addTwoCal(Calendar c1,Calendar c2){
		long sum = c1.getTimeInMillis() + c2.getTimeInMillis();
		Calendar sumCalendar = (Calendar)c1.clone();
		sumCalendar.setTimeInMillis(sum);
		sumCalendar.add(Calendar.HOUR,1);//Pb heure d'ete/hiver pour le 1/01/1970

		return sumCalendar;
	}
	/**
	 * Manipulation (soustraction) de Calendrier
	 * @param c1 un calendrier
	 * @param c2 un autre calandrier a soustraire
	 * @return
	 */
	public static Calendar substractTwoCal(Calendar c1,Calendar c2){

		Calendar subCalendar = Calendar.getInstance();
		subCalendar.setTimeZone(c1.getTimeZone());
		
		long sub = c1.getTimeInMillis() - c2.getTimeInMillis();
		if(sub<0)subCalendar.setTimeInMillis(0);
		else subCalendar.setTimeInMillis(sub);
		subCalendar.add(Calendar.HOUR,-1);//Pb heure d'ete/hiver pour le 1/01/1970
		
		return subCalendar;
	}
	/**
	 * Permet la verification facile de la presence d'une equipe dans une liste d'equipes
	 * @param listeEquipe liste d'equipe dans laquelle on veut savoir si equipe existe
	 * @param idEquipe l'equipe dont veut connapitre l'existance
	 * @return
	 */
	Boolean equipeExisteDeja(ArrayList<Equipe> listeEquipe,int idEquipe){
		int i=0;
		Boolean existe=false;
		//TANT qu'on a pas parcouru toute la liste ou trouve ce qu'on veut on cherche
		while(i<listeEquipe.size() && !existe){
			
			//SI on trouve on dit qu'on a trouve et du coup tout est fini
			if(listeEquipe.get(i).getIdEquipe()==idEquipe)existe=true;
			i++;
		}
		return existe;
	}
	
	
	/**
	 * Permet de recuperer la liste des Ids d'epreuves ayant lieu pendant une de ces journees en parametre
	 * Ou tous les ids is on ne specifie pas de date
	 * 
	 * @param journees, listes des journees pendant lesquelles on cherche quelles epreuves ont eu lieu
	 * @param idc , identifiant de la competition concernee
	 * @return,
	 * 			liste des identifiants des epreuves qui ont lieu pendant les journees demandees
	 * 			liste les identifiants de TOUTES les epreuves si la liste de Date(s) est vide
	 */
	public static ArrayList<Integer> epreuvesDeCesJournees(ArrayList<Date> journees,int idc){
		ArrayList<Integer> epreuves = new ArrayList<>();
		int i;
		//Si on ne specifie pas de dates on renvoi toute les epreuves
		if(journees.size()==0){
			String requeteSQL = "SELECT dateHeureEpreuve,idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"'";

			try {
				Class.forName("com.mysql.jdbc.Driver");

				Connection conn = DataSourceProvider.getDataSource().getConnection();
				//System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);
				
				while (res.next()) {
					epreuves.add(res.getInt(2));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			//SINON que celles qui correspondent
			for(i=0;i<journees.size();i++){
				
				String requeteSQL = "SELECT dateHeureEpreuve,idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"'";
	
				try {
					Class.forName("com.mysql.jdbc.Driver");
	
					Connection conn = DataSourceProvider.getDataSource().getConnection();
					Statement stm = conn.createStatement();
					ResultSet res = stm.executeQuery(requeteSQL);
					
					while (res.next()) {
						//Si cette epreuve est a la date demandee on la sauvegarde
						if(res.getDate(1).compareTo(journees.get(i))==0)epreuves.add(res.getInt(2));
					}
					conn.close();
					res.close();
	
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
		}
		
		return epreuves;
	}
	/**
	 * Permet de compléter une clause WHERE
	 * pour faire correspondre la liste d'identifiants avec l'extraction de donnees d'une base.
	 * @param nomChamp, le nom du champ de la table de la bdd dans lequel on veut effectuer la clause
	 * @param listeIds, la listes des identifiants a recuperer dans la requetes donc dans la clause WHERE
	 * @return
	 */
	public String listeIdsEnReqSQL(String nomChamp,ArrayList<Integer> listeIds){
		String boutDeRequete="";
		int i;
		for(i=0;i<listeIds.size();i++){
			boutDeRequete+=nomChamp;
			boutDeRequete+="=";
			boutDeRequete+="'";
			boutDeRequete+=listeIds.get(i);
			boutDeRequete+="'";
			if(i+1<listeIds.size())boutDeRequete+=" OR ";
			
		}
		System.out.println("LE BOUT DE REQUETTE EST : "+boutDeRequete);
		return boutDeRequete;
	}
}