package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Interface.Inte_Doigt.EcouteurSupp;

public class Inte_Balise  extends JPanel{
	
	/* Panes */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBalises = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");

	JLabel bjr = new JLabel("Ici vous pouvez gérer vous différents balises");
	
	public Inte_Balise(){
		
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
		
		EcouteurSupp ecoutSupp = new EcouteurSupp();
		supp.addActionListener(ecoutSupp);
		
	}

	
	public class EcouteurSupp implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {
			// on prend le num de la compet , on le stock
			// on demande confirmation, si oui on la supprime

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment supprimer cette balise?",
					"Attention", JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				//Supprimer la comptetition de la BDD
			}

		}
	}
}
