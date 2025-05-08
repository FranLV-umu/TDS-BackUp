package umu.tds.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Mensaje;

public class ContactoCellRenderer extends JPanel 
		implements ListCellRenderer<Contacto> {
	private JLabel nameLabel;
	private JLabel imageLabel;
	private static final int TAM_IMAGEN = 50;
	JPanel panel = new JPanel();

	public ContactoCellRenderer() {
		setLayout(new BorderLayout(5, 5));

		nameLabel = new JLabel();
		imageLabel = new JLabel();
		
		add(imageLabel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(5, 5));
		panel.add(nameLabel, BorderLayout.CENTER);
		add(panel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto contacto, int index,
			boolean isSelected, boolean cellHasFocus) {
		if(contacto == null) {
			nameLabel.setText("Selecciona un contacto");
            imageLabel.setIcon(null); 
		}	else {
			String nombre = contacto.getNombre();
			nameLabel.setText(nombre);
			
			// En caso de ser ContactoIndividual también se mostrará el saludo de cada contacto.
			if(contacto instanceof ContactoIndividual) {
				JLabel statusLabel = new JLabel();
				panel.add(statusLabel, BorderLayout.SOUTH);
				ContactoIndividual c = (ContactoIndividual) contacto;
				statusLabel.setText(c.getSaludo());
			}
			
			try {
				URL imageUrl = new URL(contacto.getImagen());
				//URL imageUrl = new URL("https://robohash.org/" + contacto.getNombre() + "?size=50x50");
				Image image = ImageIO.read(imageUrl);
				ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(TAM_IMAGEN, TAM_IMAGEN, Image.SCALE_SMOOTH));
				imageLabel.setIcon(imageIcon);
			} catch (IOException e) {
				e.printStackTrace();
				imageLabel.setIcon(null); // Default to no image if there was an issue
			}
		}
		// Set background and foreground based on selection
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			panel.setBackground(list.getSelectionBackground());
			panel.setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
		}

		return this;
	}
}