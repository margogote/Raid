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
		    
		   //Retourne le titre de la colonne à l'indice spécifié
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
		    
		   //Retourne la valeur à l'emplacement spécifié
		   public Object getValueAt(int row, int col) {
		      return this.data[row][col];
		   }
		   
		   //Retourner l'id que l'on veut
		   public Object getId(int row){
			   return this.data[row][2];
		   }
		    
		   //Définit la valeur à l'emplacement spécifié
		   public void setValueAt(Object value, int row, int col) {
		      //On interdit la modification sur certaines colonnes !
		      if(!this.getColumnName(col).equals("Age")
		         && !this.getColumnName(col).equals("Suppression"))
		         this.data[row][col] = value;
		   }
		          
		  //Retourne la classe de la donnée de la colonne
		   public Class getColumnClass(int col){
		      //On retourne le type de la cellule à la colonne demandée
		      //On se moque de la ligne puisque les données sont les mêmes
		      //On choisit donc la première ligne
		      return this.data[0][col].getClass();
		   }
		     
		    
		   public boolean isCellEditable(int row, int col){
		      return true;
		   }
		}

