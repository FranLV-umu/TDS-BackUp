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
import umu.tds.modelo.Chat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Mensaje;
import umu.tds.modelo.Usuario;

public class ChatCellRenderer extends JPanel 
		implements ListCellRenderer<Chat> {
	private JLabel nameLabel;
	private JLabel imageLabel;
	private JTextField messaje;
	JPanel panel = new JPanel();

	public ChatCellRenderer() {
		setLayout(new BorderLayout(5, 5));

		nameLabel = new JLabel();
		imageLabel = new JLabel();
		messaje = new JTextField();
		
		add(imageLabel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(5, 5));
		panel.add(nameLabel, BorderLayout.CENTER);
		panel.add(messaje, BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Chat> list, Chat chat, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		Usuario u = ControladorAppChat.getUnicaInstancia().getUsuarioActual().equals(chat.getUsr1()) ? chat.getUsr2() : chat.getUsr1();
		String nombre = u.getNombre();
		
		nameLabel.setText(nombre);
		messaje.setText(chat.getUltimoMensaje().getTexto());

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + u.getNombre() + "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
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