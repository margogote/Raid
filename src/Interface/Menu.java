package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Interface.Inte_Accueil.EcouteurQ;

public class Menu extends JPanel {

	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBalise = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */

	private JButton retour = new JButton("Changer de compétition");
	private JButton quitter = new JButton("Quitter");

	public Menu() {

		retour.setPreferredSize(new Dimension(200, 30));
		quitter.setPreferredSize(new Dimension(200, 30));

		JPanel panBoutRetour = new JPanel();
		JPanel panBoutQuit = new JPanel();

		panBoutRetour.add(retour);
		panBoutQuit.add(quitter);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutRetour);
		panBoutonsListe.add(panBoutQuit);

		panBalise.setLayout(new BoxLayout(panBalise, BoxLayout.PAGE_AXIS));
		panBalise.setLayout(new BorderLayout());
		panBalise.add(panBoutonsListe, BorderLayout.CENTER);

		// panMega.setLayout(new BorderLayout());
		panMega.add(panBalise /*, BorderLayout.CENTER*/);

		this.setLayout(new BorderLayout());
		this.add(panMega /* , BorderLayout.CENTER */);

		EcouteurQ ecoutQ = new EcouteurQ();
		quitter.addActionListener(ecoutQ);

	}

	public class EcouteurQ implements ActionListener { // Action du supprimer

		public void actionPerformed(ActionEvent arg0) {

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment quitter?", "Attention",
					JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				//fen2.dispose();
				System.exit(0);
			}

		}
	}

}
