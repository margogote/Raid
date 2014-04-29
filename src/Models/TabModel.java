package Models;

import javax.swing.table.AbstractTableModel;

public class TabModel extends AbstractTableModel{
		 
		   private Object[][] data;
		   private String[] title;
		    
		   //Constructeur
		   public TabModel(Object[][] data, String[] title){
		      this.data = data;
		      this.title = title;
		   }
		    
		   //Retourne le titre de la colonne � l'indice sp�cifi�
		   public String getColumnName(int col) {
		     return this.title[col];
		   }
		 
		   //Retourne le nombre de colonnes
		   public int getColumnCount() {
		      return this.title.length;
		   }
		    
		   //Retourne le nombre de lignes
		   public int getRowCount() {
		      return this.data.length;
		   }
		    
		   //Retourne la valeur � l'emplacement sp�cifi�
		   public Object getValueAt(int row, int col) {
		      return this.data[row][col];
		   }
		   
		   //Retourner l'id que l'on veut
		   public Object getId(int row){
			   return this.data[row][2];
		   }
		    
		   //D�finit la valeur � l'emplacement sp�cifi�
		   public void setValueAt(Object value, int row, int col) {
		      //On interdit la modification sur certaines colonnes !
		      if(!this.getColumnName(col).equals("Age")
		         && !this.getColumnName(col).equals("Suppression"))
		         this.data[row][col] = value;
		   }
		          
		  //Retourne la classe de la donn�e de la colonne
		   public Class getColumnClass(int col){
		      //On retourne le type de la cellule � la colonne demand�e
		      //On se moque de la ligne puisque les donn�es sont les m�mes
		      //On choisit donc la premi�re ligne
		      return this.data[0][col].getClass();
		   }
		     
		    
		   public boolean isCellEditable(int row, int col){
		      return true;
		   }
		}

