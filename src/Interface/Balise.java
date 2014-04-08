package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Balise  extends JPanel{
	
	/* Panes */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBalises = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Creer");

	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents balises");
	
	public Balise(){
		
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
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);
		panBoutonsListe.add(panBoutCreer);

		panBalises.setLayout(new BoxLayout(panBalises, BoxLayout.PAGE_AXIS));
		panBalises.setLayout(new BorderLayout());
		panBalises.add(bjr, BorderLayout.NORTH);
		panBalises.add(panBoutonsListe, BorderLayout.CENTER);

		panMega.add(panBalises);
		this.add(panMega);
		
	}

}
