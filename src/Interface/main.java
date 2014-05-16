package Interface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import BDD.DataSourceProvider;
import Classements.Classement;
import Classements.Filtre;
import Models.Equipe;

/**
 * Classe Main qui lance l'application
 * Test de génération de classement
 * 
 * @author Margaux
 * 
 */
public class Main {

	static int idc =1;
	public static void main(String[] args) {
		String requeteSQL = "SELECT tempsRealise FROM scorer ORDER BY tempsRealise ASC";
		Inte_Accueil inte_Accueil = new Inte_Accueil();
		Calendar tempsTotal = Calendar.getInstance();
		try {
			Connection conn = DataSourceProvider.getDataSource().getConnection();
			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);
			while (res.next()) {
				
				System.out.println("Temps : " + res.getTime(1));
				/*String temps;
				temps=res.getTime(1).toString();
				System.out.println("Temps : " + temps);*/
				Date temps=new Date();
				Date temps2=new Date();
				Date duree1et2 = new Date();
				DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
				try {
					temps = dateformat.parse(res.getTime(1).toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				Calendar c = Calendar.getInstance(); 
				Calendar c2 = Calendar.getInstance(); 
				c.setTime(temps); 
				System.out.println("Temps = "+temps.toString());
				c.add(Calendar.SECOND, 1);
				c.add(Calendar.MINUTE, 1);
				c.add(Calendar.HOUR, 1);
				c.add(Calendar.DATE, 1);
				temps2 = c.getTime();
				System.out.println("Temps 2 = "+temps2.toString());
				c2.setTime(temps2);
				
				System.out.println("temps.getDay() = "+temps.getDay());
				System.out.println("temps2.getDay() = "+temps2.getDate());
				//duree1et2=temps2.getTime()-temps.getTime();
				
				String testFonc;
				testFonc=dateToString(temps2);
				System.out.println("temps2 en String : "+testFonc);
				temps2=stringToDate(res.getTime(1).toString());
				System.out.println("res.getTime(1).toString() en Date "+temps2.toString());
				
				
				// Test fonc epreuvesDeCesJournees
				ArrayList<Integer> listeDesIds = new ArrayList<>();
				ArrayList<Date> dates = new ArrayList<Date>();
				
				Date dateTest = new Date();
				DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
				dfm.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
				try {
					dateTest = dfm.parse("2014-02-20");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				dates.add(dateTest);
				listeDesIds=epreuvesDeCesJournees(dates);
				int j;
				
				for(j=0;j<listeDesIds.size();j++){
					System.out.println("Date idEpreuve = "+listeDesIds.get(j));
					}
				
				// Test fonc epreuvesDeCetteDificulte
				listeDesIds = new ArrayList<>();
				listeDesIds=epreuvesDeCetteDificulte("moinsDur");
				int k;
				for(k=0;k<listeDesIds.size();k++){
					System.out.println("Difficulte idEpreuve = "+listeDesIds.get(k));
					}
				
				// Test fonc equipeDeCeGroupe
				listeDesIds = new ArrayList<>();
				listeDesIds=equipeDeCeGroupe("HEI");
				int l;
				for(l=0;l<listeDesIds.size();l++){
					System.out.println("Groupe idEquipe = "+listeDesIds.get(l));
					}

				// Test fonc equipeDeCeType
				listeDesIds = new ArrayList<>();
				listeDesIds=equipeDeCeType("masculine");
				int n;
				for(n=0;n<listeDesIds.size();n++){
					System.out.println("Type idEquipe = "+listeDesIds.get(n));
					}

				// Test fonc gardeQueLesBonsIds
				listeDesIds = new ArrayList<>();
				ArrayList<ArrayList<Integer>> listeDeListe = new ArrayList<>();
				ArrayList<Integer> arr1 = new ArrayList<>();
				ArrayList<Integer> arr2 = new ArrayList<>();
				
				int o,p;
				for(o=0;o<10;o++)arr1.add(o);
				for(p=3;p<5;p++)arr2.add(p);
				
				listeDeListe.add(arr1);
				listeDeListe.add(arr2);
				
				listeDesIds= gardeQueLesBonsIds(listeDeListe);
				
				int q;
				for(q=0;q<listeDesIds.size();q++){
					System.out.println("FusionListeIds id = "+listeDesIds.get(q));
					}
				
				// Test fonc equipeExisteDeja
				ArrayList<Equipe> listeEquipe = new ArrayList<>();
				listeEquipe.add(new Equipe(1,new Date()));
				if(equipeExisteDeja(listeEquipe,1))System.out.println("1 existe");
				else System.out.println("MARCHE PAS");

				if(equipeExisteDeja(listeEquipe,2)==false)System.out.println("2 n'existe pas");
				else System.out.println("MARCHE PAS");
				
				/*Date dt=new Date(0, 0, 0, 0, 0, 0);
				Calendar c = Calendar.getInstance(); 
				c.setTime(dt); 
				System.out.println(dt.toString());
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				System.out.println(dt.toString());*/
				
				//Se reperer avec les index de substring
				//String chaine;
				/*for(int i=0;i<4;i++){
					for(int j=i;j<4;j++){
					    chaine = "abc".substring(i,j);
						System.out.println("chaine = abc.substring("+i+","+j+");chaine = "+chaine);
					}
				}*/
			    /* 
				System.out.println("c = "+c);
			    c = "abc".substring(0,2);
				System.out.println("c = "+c);
			    c = "abc".substring(0,3);
				System.out.println("c = "+c);
			     c = "abc".substring(1,1);
					System.out.println("c = "+c);
			    c = "abc".substring(1,2);
				System.out.println("c = "+c);
			     c = "abc".substring(1,3);
					System.out.println("c = "+c);
			     c = "abc".substring(2,2);
					System.out.println("c = "+c);
			    c = "abc".substring(2,3);
				System.out.println("c = "+c);
			      c = "abc".substring(3,3);
					System.out.println("c = "+c);*/
			}

			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Calendar cTest1 = Calendar.getInstance();
		Calendar cTest2 = Calendar.getInstance();
		
		cTest1.setTime(stringToDate("00:00:01"));
		cTest2.setTime(stringToDate("00:00:02"));
		
		System.out.println("SOMME = "+dateToString(addTwoCal(cTest1,cTest2).getTime()));
		System.out.println("SOUSTRACTION = "+dateToString(substractTwoCal(cTest1,cTest2).getTime()));
		
		Classement testClassement = new Classement(idc);

		ArrayList<Date> dates= new ArrayList<>();
		Date dateTest = new Date();
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
		dfm.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		try {
			dateTest = dfm.parse("2014-04-27");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		dates.add(dateTest);
		//Classement testClassementAvecDate = new Classement(dateTest,idc);
		
		//Classement testClassementAvecFiltre = new Classement(new Filtre("Aventure","Etudiant","Masculin",idc),idc);
		
		//Classement testClassementAvecFiltreEtDate = new Classement(dateTest,new Filtre("Aventure","Etudiant","Masculin",idc),idc);
	}
	public static Date stringToDate(String HHmmss){
		Date temps = new Date();
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		try {
			temps = dateformat.parse(HHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//System.out.println(temps.toString());
		return temps;
	}
	public static String dateToString(Date temps){
		String HHmmss="";
		
		Calendar calTemps = Calendar.getInstance();
		calTemps.setTime(temps);
		
		int h,m,s;
		h=m=s=0;
		if(calTemps.get(Calendar.DAY_OF_MONTH)!=1){
			h+=24*(calTemps.get(Calendar.DAY_OF_MONTH)-1);
		}
		h+=calTemps.get(Calendar.HOUR);
		//System.out.println("h = "+h);
		m+=calTemps.get(Calendar.MINUTE);
		//System.out.println("m = "+m);
		s+=calTemps.get(Calendar.SECOND);
		//System.out.println("s = "+s);
		
		HHmmss+=h+":"+m+":"+s;
		
		//System.out.println("HHmmss = "+HHmmss);
		return HHmmss;
	}
	public static Calendar addTwoCal(Calendar c1,Calendar c2){
		//System.out.println(""+c1.getTimeInMillis());
		//System.out.println(""+c2.getTimeInMillis());
		long sum = c1.getTimeInMillis() + c2.getTimeInMillis();
		Calendar sumCalendar = (Calendar)c1.clone();
		sumCalendar.setTimeInMillis(sum);
		sumCalendar.add(Calendar.HOUR,1);
		
		/*Pb heure d'été
		http://www.geeketfier.fr/index.php/bien-utiliser-la-date-et-lheure-en-java/
		http://www.developpez.net/forums/d1161597/java/general-java/probleme-decalage-horaire-calendar/
		*/
		
		return sumCalendar;
	}
	public static Calendar substractTwoCal(Calendar c1,Calendar c2){

		Calendar subCalendar = Calendar.getInstance();
		subCalendar.setTimeZone(c1.getTimeZone());
		
		//System.out.println(""+c1.getTimeInMillis());
		//System.out.println(""+c2.getTimeInMillis());
		long sub = c1.getTimeInMillis() - c2.getTimeInMillis();
		if(sub<0)subCalendar.setTimeInMillis(0);
		else subCalendar.setTimeInMillis(sub);
		subCalendar.add(Calendar.HOUR,-1);
		
		/*Pb heure d'été
		http://www.geeketfier.fr/index.php/bien-utiliser-la-date-et-lheure-en-java/
		http://www.developpez.net/forums/d1161597/java/general-java/probleme-decalage-horaire-calendar/
		*/
		return subCalendar;
	}/*public Object[][] updateTable() {

		ArrayList<Object[]> ArrayData = new ArrayList<>();*/

	public static ArrayList<Integer> epreuvesDeCesJournees(ArrayList<Date> journees){
		ArrayList<Integer> epreuves = new ArrayList<>();
		int i;
		for(i=0;i<journees.size();i++){
			
			String requeteSQL = "SELECT dateHeureEpreuve,idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"'";

			try {
				Class.forName("com.mysql.jdbc.Driver");
				//System.out.println("Driver O.K.");

				Connection conn = DataSourceProvider.getDataSource().getConnection();
				//System.out.println("Connexion effective !");
				Statement stm = conn.createStatement();
				ResultSet res = stm.executeQuery(requeteSQL);
				
				while (res.next()) {
					if(res.getDate(1).compareTo(journees.get(i))==0)epreuves.add(res.getInt(2));
				}
				conn.close();
				res.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		return epreuves;
	}

	public static ArrayList<Integer> epreuvesDeCetteDificulte(String difficulte){
		ArrayList<Integer> epreuves = new ArrayList<>();
			
		String requeteSQL = "SELECT idEpreuve FROM epreuve WHERE `idCompetition` = '"+idc+"' AND `difficulte` = '"+difficulte+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
			//System.out.println("Connexion effective !");
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
	public static ArrayList<Integer> equipeDeCeGroupe(String groupe){
		ArrayList<Integer> epreuves = new ArrayList<>();
			
		String requeteSQL = "SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"' AND `nomGroupe` = '"+groupe+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
			//System.out.println("Connexion effective !");
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
	public static ArrayList<Integer> equipeDeCeType(String type){
		ArrayList<Integer> epreuves = new ArrayList<>();
			
		String requeteSQL = "SELECT idEquipe FROM equipe WHERE `idCompetition` = '"+idc+"' AND `typeEquipe` = '"+type+"'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("Driver O.K.");

			Connection conn = DataSourceProvider.getDataSource().getConnection();
			//System.out.println("Connexion effective !");
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
	public static ArrayList<Integer> gardeQueLesBonsIds(ArrayList<ArrayList<Integer>> listesIds){
		ArrayList<Integer> bonsIds = new ArrayList<>();
		int i,j,k;
		Boolean nonTrouve;
		//int cmpt=0;
		
		System.out.println("P1");
		
		
		if(listesIds.size()<2)System.out.println("Il y a même pas deux liste à fusionner");
		else{
			System.out.println("P2");
			for(i=0;i<listesIds.size()-1;i++){
				System.out.println("P3 i = "+i);
				for(j=0;j<listesIds.get(i).size();j++){
					/*System.out.println("P4 j = "+j);
					for(k=0;k<listesIds.get(i+1).size();k++){
						//cmpt++;
						System.out.println("P5 k = "+k);
						if(listesIds.get(i).get(j)==listesIds.get(i+1).get(k))bonsIds.add(listesIds.get(i).get(j));
					}*/
					
					System.out.println("P4 j = "+j);
					k=0;
					nonTrouve=true;
					
					if(k<listesIds.get(i+1).size())System.out.println("k<taille = true");
					if(nonTrouve)System.out.println("nonTrouve = true");
					
					
					while(k<listesIds.get(i+1).size() && nonTrouve){
						//cmpt++;
						System.out.println("P5 k = "+k);
						if(listesIds.get(i).get(j)==listesIds.get(i+1).get(k)){
							bonsIds.add(listesIds.get(i).get(j));
							nonTrouve=false;
							System.out.println("P6 : Trouve !");
						}
						k++;
					}
				}
			}
		}
		//System.out.println(cmpt);
		return bonsIds;
	}
	public static Boolean equipeExisteDeja(ArrayList<Equipe> listeEquipe,int idEquipe){
		int i=0;
		Boolean existe=false;
		while(i<listeEquipe.size() && !existe){
			
			if(listeEquipe.get(i).getIdEquipe()==idEquipe)existe=true;
			i++;
		}
		return existe;
	}
	//TriEquipes ArrayList<Integer>
}
