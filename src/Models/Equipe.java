package Models;

import java.util.Calendar;
import java.util.Date;

/**
 * Modele d'une equipe dans Raidzultats (differe de celui de la BDD)
 * @author philippesmessaert
 *
 */
public class Equipe {

	private int idEquipe;
	private String nomEquipe;
	private String nomGroupe;
	private String typeDifficulte;
	private String typeEquipe;
	private int idCompetition;
	private int dossard;
	private Date score;
	/**
	 * Constructeur qui permet de creer une equipe avec un id, un score,
	 * un numero de dossard, un nom et un type
	 * @param idEquipe, identifiant de l'equipe
	 * @param score, score de l'equipe
	 * @param dossard, numero de dossard de l'equipe
	 * @param nomEquipe, nom de l'equipe
	 * @param typeEquipe, type d'equipe (masculine, feminine, mixte)
	 */
	public Equipe(int idEquipe,Date score, int dossard, String nomEquipe,String typeEquipe){
		setIdEquipe(idEquipe);
		setScore(score);
		setDossard(dossard);
		setTypeEquipe(typeEquipe);
		setNomEquipe(nomEquipe);
	}
	public int getDossard(){
		return this.dossard;
	}
	public void setDossard(int dossard){
		this.dossard=dossard;
	}
	public int getIdEquipe() {
		return idEquipe;
	}
	public void setIdEquipe(int idEquipe) {
		this.idEquipe = idEquipe;
	}
	public String getNomEquipe() {
		return nomEquipe;
	}
	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}
	public String getNomGroupe() {
		return nomGroupe;
	}
	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}
	public String getTypeDifficulte() {
		return typeDifficulte;
	}
	public void setTypeDifficulte(String typeDifficulte) {
		this.typeDifficulte = typeDifficulte;
	}
	public String getTypeEquipe() {
		return typeEquipe;
	}
	public void setTypeEquipe(String typeEquipe) {
		this.typeEquipe = typeEquipe;
	}
	public int getIdCompetition(){
		return idCompetition;
	}
	public void setIdCompetition(int idCompetition){
		this.idCompetition=idCompetition;
	}
	public void setScore(Date score){
		this.score=score;
	}
	public Date getScore(){
		return score;
	}
	/**
	 * Permet de mettre à jour le temps d'une équipe
	 * @param, scoreToAdd le temps correspondant au score a ajouter
	 */
	public void addToScore(Date scoreToAdd){
		this.score=addTwoDates(score, scoreToAdd);
	}
	/**
	 * Fonction utilisée pour pour ajouter deux dates (temps)
	 * dans la fonction addToScore(Date scoreToAdd)
	 * 
	 * @param, d1 la premiere date a ajouter
	 * @param, d2 la deuxieme date a ajouter
	 * @return
	 */
	public Date addTwoDates(Date d1,Date d2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d2);
		
		return addTwoCal(c1, c2).getTime();
	}
	/**
	 * Fonction utilisée pour ajouter deux calendrier
	 * Fonction utilisée dans la fonction addTwoDates pour faciliter la manipulation des dates
	 * S'inscrit dans l'évolution actuelle du java
	 * @param, c1 le calendrier a ajouter
	 * @param, c2 le calendrier a ajouter
	 * @return
	 */
	public Calendar addTwoCal(Calendar c1,Calendar c2){
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
	/**
	 * Soustrait un score à une équipe (utilisée pour les bonus)
	 * @param, scoreToGet le score a soustraire du score actuel de l'equipe
	 */
	public void getFromScore(Date scoreToGet) {
		this.score=substractTwoDates(score,scoreToGet);
		
	}
	/**
	 * Permet la soustraction de 2 dates en gérant le cas où a-b avec b>a ici dans ce cas a-b=0
	 * @param, d1 une date
	 * @param, d2 la date a soustraire a d1
	 * @return, affecte de 0 si d1<d2
	 */
	public Date substractTwoDates(Date d1,Date d2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d2);
		
		return substractTwoCal(c1, c2).getTime();
	}
	/**
	 * Méthode utilisée pour faciliter la manipulation des dates.
	 * @param, c1 un calendrier
	 * @param, c2 le calendrier a soustraire a c1
	 * @return, affecte de 0 si c1<c2
	 */
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
	}
}
