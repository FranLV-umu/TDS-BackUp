package umu.tds.vista;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Mensaje;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class NuevoChat extends JFrame {
	private JTextField textFieldTelefono;
	private JTextField textFieldMensaje;

	private Contacto contactoSeleccionado = null;
	
	/**
	 * Create the frame.
	 */
	public NuevoChat(List<Contacto> lista) {
		setBackground(new Color(255, 255, 255));
		setTitle("Crear Grupo");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 740, 330);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldTelefono.setText("");
				textFieldMensaje.setText("");
				contactoSeleccionado = null;
				setVisible(false);
			}
		});
		panel.add(btnCancelar);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_2);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblImagen = new JLabel("Mensaje:");
		lblImagen.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 1;
		gbc_lblImagen.gridy = 1;
		panel_1.add(lblImagen, gbc_lblImagen);
		
		textFieldMensaje = new JTextField();
		GridBagConstraints gbc_textFieldMensaje = new GridBagConstraints();
		gbc_textFieldMensaje.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMensaje.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMensaje.gridx = 3;
		gbc_textFieldMensaje.gridy = 1;
		panel_1.add(textFieldMensaje, gbc_textFieldMensaje);
		textFieldMensaje.setColumns(10);
		
		JLabel lblTelefono = new JLabel("Teléfono");
		lblTelefono.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 3;
		panel_1.add(lblTelefono, gbc_lblTelefono);
		
		textFieldTelefono = new JTextField();
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.gridx = 3;
		gbc_textFieldTelefono.gridy = 3;
		panel_1.add(textFieldTelefono, gbc_textFieldTelefono);
		textFieldTelefono.setColumns(10);
		
		JPanel panel_chat_contactos = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 3;
		gbc_panel_2.gridy = 5;
		panel_1.add(panel_chat_contactos, gbc_panel_2);
		
		DefaultListModel<Contacto> ModelListContactos = new DefaultListModel<Contacto>() {
			List<Contacto> contactos = ControladorAppChat.getUnicaInstancia().getContactosSinChatUsuarioActual();
			
			public int getSize() {
				return contactos.size();
			}
			public Contacto getElementAt(int index) {
				return contactos.get(index);
			}
		};
		
		JList<Contacto> listContactos = new JList<Contacto>(ModelListContactos);
		listContactos.addListSelectionListener(e -> {
		    if (!e.getValueIsAdjusting()) { // Evitar capturar eventos intermedios
		    	contactoSeleccionado = listContactos.getSelectedValue();
		    	if(ControladorAppChat.getUnicaInstancia().debug) {
		        	if (contactoSeleccionado != null) {
	        			System.out.println("> Contacto seleccionado: " + contactoSeleccionado.getNombre());	
	        		}	else	{
	        			System.out.println("> No se ha seleccionado ningún contacto.");	
	        		}
		        }
		    }
		});
		
		listContactos.setCellRenderer(new ContactoCellRenderer());
		panel_chat_contactos.add(new JScrollPane(listContactos), BorderLayout.CENTER);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textFieldMensaje.getText().trim().isEmpty()) {
					String mensaje = textFieldMensaje.getText().trim();
					String telefono = textFieldTelefono.getText().trim();
					
					if(textFieldTelefono.getText().trim().isEmpty() && contactoSeleccionado != null) {
						ContactoIndividual c = (ContactoIndividual) contactoSeleccionado;
						ControladorAppChat.getUnicaInstancia().enviarMensaje(c.getUsuario(), mensaje);
					}	else if(ControladorAppChat.getUnicaInstancia().buscarContactoPorNum(telefono).isEmpty() &&
								ControladorAppChat.getUnicaInstancia().comprobarTelefono(telefono)) {
						ControladorAppChat.getUnicaInstancia().enviarMensaje(telefono, mensaje);
					}
				}
			}
		});
		panel.add(btnAceptar);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_1);

	}

}
