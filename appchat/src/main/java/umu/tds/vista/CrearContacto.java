package umu.tds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Contacto;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class CrearContacto extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldTlf;


	/**
	 * Create the frame.
	 */
	public CrearContacto(DefaultListModel<Contacto> lista) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Añadir nuevo contacto");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel LabelNombre = new JLabel("Nombre:");
		GridBagConstraints gbc_LabelNombre = new GridBagConstraints();
		gbc_LabelNombre.insets = new Insets(0, 0, 5, 5);
		gbc_LabelNombre.anchor = GridBagConstraints.EAST;
		gbc_LabelNombre.gridx = 1;
		gbc_LabelNombre.gridy = 1;
		panel.add(LabelNombre, gbc_LabelNombre);
		
		textFieldNombre = new JTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 2;
		gbc_textFieldNombre.gridy = 1;
		panel.add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel LabelTlf = new JLabel("Teléfono:");
		GridBagConstraints gbc_LabelTlf = new GridBagConstraints();
		gbc_LabelTlf.anchor = GridBagConstraints.EAST;
		gbc_LabelTlf.insets = new Insets(0, 0, 0, 5);
		gbc_LabelTlf.gridx = 1;
		gbc_LabelTlf.gridy = 3;
		panel.add(LabelTlf, gbc_LabelTlf);
		
		textFieldTlf = new JTextField();
		GridBagConstraints gbc_textFieldTlf = new GridBagConstraints();
		gbc_textFieldTlf.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldTlf.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTlf.gridx = 2;
		gbc_textFieldTlf.gridy = 3;
		panel.add(textFieldTlf, gbc_textFieldTlf);
		textFieldTlf.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton ButtonCancelar = new JButton("Cancelar");
		ButtonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldNombre.setText("");
				textFieldTlf.setText("");
				setVisible(false);
			}
		});
		panel_1.add(ButtonCancelar);
		
		JButton ButtonAñadir = new JButton("Añadir");
		ButtonAñadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText().trim();
				String tlf = textFieldTlf.getText().trim();
				Object salida = ControladorAppChat.getUnicaInstancia().addContacto(nombre, tlf);
				if(salida instanceof Number) {
					int add = ((Number) salida).intValue();
					if(add == 1)
						JOptionPane.showMessageDialog(contentPane, "No se ha encontrado ningún usuario con ese teléfono.");
					else if(add == 2)
						JOptionPane.showMessageDialog(contentPane, "Has intentado añadirte a tí mismo.");
					else if(add == 3)
						JOptionPane.showMessageDialog(contentPane, "Ya tiene un usuario registrado con este teléfono.");
				} else if (salida instanceof Contacto) {
					Contacto c = (Contacto) salida;
					lista.addElement(c);
					JOptionPane.showMessageDialog(contentPane, "Se ha agregado al contacto " + nombre + ".");
				}
			}
		});
		panel_1.add(ButtonAñadir);
	}

}
