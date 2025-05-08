package umu.tds.vista;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import umu.tds.controlador.ControladorAppChat;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.awt.event.ActionEvent;

public class Register extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldApellidos;
	private JTextField textFieldTelefono;
	private JTextField textFieldContraseña;
	private JTextField textFieldContraseñaConfirm;
	private JTextField textFieldURLImagen;
	private JTextField textFieldMail;
	
	// Expresión regular para validar la contraseña:
    // - Al menos una letra minúscula
    // - Al menos una letra mayúscula
    // - Al menos un número
    // - Al menos un símbolo
	// - Sin espacios
    String patronContraseña = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[\\S]+$";
    
    // Expresión regular para validar el email:
    String patronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public Register() {
		setBackground(new Color(255, 255, 255));
		setTitle("APPCHAT");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 740, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{70, 200, 70, 200, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 50, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel LabelNombre = new JLabel("Nombre:");
		LabelNombre.setHorizontalTextPosition(SwingConstants.CENTER);
		LabelNombre.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_LabelNombre = new GridBagConstraints();
		gbc_LabelNombre.anchor = GridBagConstraints.WEST;
		gbc_LabelNombre.insets = new Insets(0, 0, 5, 5);
		gbc_LabelNombre.gridx = 0;
		gbc_LabelNombre.gridy = 0;
		contentPane.add(LabelNombre, gbc_LabelNombre);
		
		textFieldNombre = new JTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.gridwidth = 3;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 1;
		gbc_textFieldNombre.gridy = 0;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel LabelApellidos = new JLabel("Apellidos:");
		GridBagConstraints gbc_LabelApellidos = new GridBagConstraints();
		gbc_LabelApellidos.anchor = GridBagConstraints.WEST;
		gbc_LabelApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_LabelApellidos.gridx = 0;
		gbc_LabelApellidos.gridy = 1;
		contentPane.add(LabelApellidos, gbc_LabelApellidos);
		
		textFieldApellidos = new JTextField();
		GridBagConstraints gbc_textFieldApellidos = new GridBagConstraints();
		gbc_textFieldApellidos.gridwidth = 3;
		gbc_textFieldApellidos.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldApellidos.gridx = 1;
		gbc_textFieldApellidos.gridy = 1;
		contentPane.add(textFieldApellidos, gbc_textFieldApellidos);
		textFieldApellidos.setColumns(10);
		
		JLabel LabelTelefono = new JLabel("Telefono:");
		GridBagConstraints gbc_LabelTelefono = new GridBagConstraints();
		gbc_LabelTelefono.anchor = GridBagConstraints.WEST;
		gbc_LabelTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_LabelTelefono.gridx = 0;
		gbc_LabelTelefono.gridy = 2;
		contentPane.add(LabelTelefono, gbc_LabelTelefono);
		
		textFieldTelefono = new JTextField();
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.gridx = 1;
		gbc_textFieldTelefono.gridy = 2;
		contentPane.add(textFieldTelefono, gbc_textFieldTelefono);
		textFieldTelefono.setColumns(10);
		
		JLabel LabelMail = new JLabel("Mail:");
		GridBagConstraints gbc_LabelMail = new GridBagConstraints();
		gbc_LabelMail.anchor = GridBagConstraints.EAST;
		gbc_LabelMail.insets = new Insets(0, 0, 5, 5);
		gbc_LabelMail.gridx = 2;
		gbc_LabelMail.gridy = 2;
		contentPane.add(LabelMail, gbc_LabelMail);
		
		textFieldMail = new JTextField();
		GridBagConstraints gbc_textFieldMail = new GridBagConstraints();
		gbc_textFieldMail.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldMail.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMail.gridx = 3;
		gbc_textFieldMail.gridy = 2;
		contentPane.add(textFieldMail, gbc_textFieldMail);
		textFieldMail.setColumns(10);
		
		JLabel LabelContraseña = new JLabel("Contraseña:");
		LabelContraseña.setHorizontalTextPosition(SwingConstants.CENTER);
		LabelContraseña.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_LabelContraseña = new GridBagConstraints();
		gbc_LabelContraseña.anchor = GridBagConstraints.WEST;
		gbc_LabelContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_LabelContraseña.gridx = 0;
		gbc_LabelContraseña.gridy = 3;
		contentPane.add(LabelContraseña, gbc_LabelContraseña);
		
		textFieldContraseña = new JTextField();
		GridBagConstraints gbc_textFieldContraseña = new GridBagConstraints();
		gbc_textFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldContraseña.gridx = 1;
		gbc_textFieldContraseña.gridy = 3;
		contentPane.add(textFieldContraseña, gbc_textFieldContraseña);
		textFieldContraseña.setColumns(10);
		
		JLabel LabelContraseñaConfirm = new JLabel("Contraseña:");
		GridBagConstraints gbc_LabelContraseñaConfirm = new GridBagConstraints();
		gbc_LabelContraseñaConfirm.anchor = GridBagConstraints.WEST;
		gbc_LabelContraseñaConfirm.insets = new Insets(0, 0, 5, 5);
		gbc_LabelContraseñaConfirm.gridx = 2;
		gbc_LabelContraseñaConfirm.gridy = 3;
		contentPane.add(LabelContraseñaConfirm, gbc_LabelContraseñaConfirm);
		
		textFieldContraseñaConfirm = new JTextField();
		GridBagConstraints gbc_textFieldContraseñaConfirm = new GridBagConstraints();
		gbc_textFieldContraseñaConfirm.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldContraseñaConfirm.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldContraseñaConfirm.gridx = 3;
		gbc_textFieldContraseñaConfirm.gridy = 3;
		contentPane.add(textFieldContraseñaConfirm, gbc_textFieldContraseñaConfirm);
		textFieldContraseñaConfirm.setColumns(10);
		
		JLabel LabelFecha = new JLabel("Fecha:");
		GridBagConstraints gbc_LabelFecha = new GridBagConstraints();
		gbc_LabelFecha.anchor = GridBagConstraints.WEST;
		gbc_LabelFecha.insets = new Insets(0, 0, 5, 5);
		gbc_LabelFecha.gridx = 0;
		gbc_LabelFecha.gridy = 4;
		contentPane.add(LabelFecha, gbc_LabelFecha);
		
		JDateChooser dateChooserFecha = new JDateChooser();
		GridBagConstraints gbc_dateChooserFecha = new GridBagConstraints();
		gbc_dateChooserFecha.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooserFecha.fill = GridBagConstraints.BOTH;
		gbc_dateChooserFecha.gridx = 1;
		gbc_dateChooserFecha.gridy = 4;
		contentPane.add(dateChooserFecha, gbc_dateChooserFecha);
		
		JLabel LabelSaludo = new JLabel("Saludo:");
		GridBagConstraints gbc_LabelSaludo = new GridBagConstraints();
		gbc_LabelSaludo.anchor = GridBagConstraints.WEST;
		gbc_LabelSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_LabelSaludo.gridx = 0;
		gbc_LabelSaludo.gridy = 5;
		contentPane.add(LabelSaludo, gbc_LabelSaludo);
		
		JTextArea textAreaSaludo = new JTextArea();
		GridBagConstraints gbc_textAreaSaludo = new GridBagConstraints();
		gbc_textAreaSaludo.gridheight = 2;
		gbc_textAreaSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaSaludo.fill = GridBagConstraints.BOTH;
		gbc_textAreaSaludo.gridx = 1;
		gbc_textAreaSaludo.gridy = 5;
		contentPane.add(textAreaSaludo, gbc_textAreaSaludo);
		
		JLabel LabelURLImagen = new JLabel("");
		LabelURLImagen.setIcon(new ImageIcon(Register.class.getResource("/umu/tds/vista/imagenes/usuario.png")));
		GridBagConstraints gbc_LabelURLImagen = new GridBagConstraints();
		gbc_LabelURLImagen.insets = new Insets(0, 0, 5, 0);
		gbc_LabelURLImagen.gridx = 3;
		gbc_LabelURLImagen.gridy = 6;
		contentPane.add(LabelURLImagen, gbc_LabelURLImagen);
		
		JLabel LabelImagen = new JLabel("Imagen:");
		GridBagConstraints gbc_LabelImagen = new GridBagConstraints();
		gbc_LabelImagen.anchor = GridBagConstraints.WEST;
		gbc_LabelImagen.insets = new Insets(0, 0, 5, 5);
		gbc_LabelImagen.gridx = 2;
		gbc_LabelImagen.gridy = 5;
		contentPane.add(LabelImagen, gbc_LabelImagen);
		
		textFieldURLImagen = new JTextField();
		textFieldURLImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String urlText = textFieldURLImagen.getText().trim();
				if(!ControladorAppChat.getUnicaInstancia().comprobarImagen(urlText)) {
					JOptionPane.showMessageDialog(contentPane, "La imagen insertada no es válida o es inaccesible.");
					return;
				}
				URL url;
				try {
					url = new URL(urlText);
					ImageIcon imageIcon = new ImageIcon(url);
					Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
					
					// Verificar si la imagen se carga correctamente
	                if (imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
	                	LabelURLImagen.setIcon(new ImageIcon(img)); // Mostrar la imagen en el JLabel
	                	LabelURLImagen.setText(""); // Quitar el texto en caso de que haya
	                } else {
	                	LabelURLImagen.setIcon(new ImageIcon(Register.class.getResource("/umu/tds/vista/imagenes/usuario.png")));
	                }
				} catch (MalformedURLException ex) {
					if(ControladorAppChat.getUnicaInstancia().debug) {
						System.err.println("-> Error al cargar la imagen. [Registro | TextField]");
						ex.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_textFieldURLImagen = new GridBagConstraints();
		gbc_textFieldURLImagen.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldURLImagen.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldURLImagen.gridx = 3;
		gbc_textFieldURLImagen.gridy = 5;
		contentPane.add(textFieldURLImagen, gbc_textFieldURLImagen);
		textFieldURLImagen.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 7;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton ButtonCancelar = new JButton("Cancelar");
		ButtonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldNombre.setText("");
				textFieldApellidos.setText("");
				textFieldTelefono.setText("");
				textFieldContraseña.setText("");
				textFieldContraseñaConfirm.getText().trim();
				dateChooserFecha.setDate(null);
				textAreaSaludo.setText("");
				textFieldMail.setText("");
				textFieldURLImagen.setText("");
				setVisible(false);
			}
		});
		panel.add(ButtonCancelar);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
		JButton ButtonAceptar = new JButton("Aceptar");
		ButtonAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText().trim();
				String apellidos = textFieldApellidos.getText().trim();
				String telefono = textFieldTelefono.getText().trim();
				String contraseña = textFieldContraseña.getText().trim();
				String contraseñaConfirm = textFieldContraseñaConfirm.getText().trim();
				Date fecha = dateChooserFecha.getDate();
				String saludo = textAreaSaludo.getText().trim();
				String mail = textFieldMail.getText().trim();
				String imagen = textFieldURLImagen.getText().trim();
				
				// Comprobación para ver si un campo está vacío.
				if(nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()
						|| contraseñaConfirm.isEmpty() || fecha == null || mail.isEmpty() || imagen.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Complete correctamente todos los campos (el saludo es opcional).");
					return;
				}
				
				// Comprobaciones para el nº de teléfono.
				if (!telefono.matches("\\d+")) {
					JOptionPane.showMessageDialog(contentPane, "El teléfono solo debe contener números.");
					return;
				}	else if (telefono.length() > 9 || telefono.length() < 9) {
					JOptionPane.showMessageDialog(contentPane, "El teléfono debe tener una longitud de 9 números.");
					return;
				}	else if(!telefono.isEmpty() && ControladorAppChat.getUnicaInstancia().usuarioExistente(telefono))	{
					JOptionPane.showMessageDialog(contentPane, "Ya existe un usuario con ese teléfono.");
					return;
				}
				
				// Comprobaciones para la contraseña.
				if (contraseña.length() < 8) {
					JOptionPane.showMessageDialog(contentPane, "La contraseña es muy débil, ingrese una de al menos 8 carácteres.\n");
					return;
				} else if(!contraseña.matches(patronContraseña)) {
					String mensaje = "La contraseña es muy débil, ingrese una de al menos 8 carácteres.\n"
							+ "De los cuales, tiene que haber al menos:\n"
							+ " - Una letra minúscula. ";
					if (!tieneMinusculas(contraseña)) mensaje +="❌";
					else mensaje +="✅";
					mensaje +="\n - Una letra mayúscula. ";
					if (!tieneMayusculas(contraseña)) mensaje +="❌";
					else mensaje +="✅";
					mensaje +="\n - Un número. ";
					if (!tieneDigitos(contraseña)) mensaje +="❌";
					else mensaje +="✅";
					mensaje +="\n - Un símbolo. ";
					if (!tieneSimbolos(contraseña)) mensaje +="❌";
					else mensaje +="✅";
					mensaje +="\n - Y no contener espacios. ";
					if (noEspacios(contraseña)) mensaje +="❌";
					else mensaje +="✅";
					
					JOptionPane.showMessageDialog(contentPane, mensaje);
					return;
				}	else if(!contraseña.equals(contraseñaConfirm))	{
					JOptionPane.showMessageDialog(contentPane, "Las contraseñas no coinciden.");
					return;
				}
				
				// Comprobación para el Email.
				if(!mail.matches(patronEmail)) {
					JOptionPane.showMessageDialog(contentPane, "Email no válido.\n");
					return;
				}
				
				// Comprobaciones para la imagen.
				if(!ControladorAppChat.getUnicaInstancia().comprobarImagen(imagen)) {
					JOptionPane.showMessageDialog(contentPane, "La imagen insertada no es válida o es inaccesible.");
					return;
				}
				
				ControladorAppChat.getUnicaInstancia().registrarUsuario(nombre, apellidos, telefono, contraseña, fecha, contraseñaConfirm, saludo, mail, imagen);
				textFieldNombre.setText("");
				textFieldApellidos.setText("");
				textFieldTelefono.setText("");
				textFieldContraseña.setText("");
				textFieldContraseñaConfirm.setText("");
				dateChooserFecha.setDate(null);
				textAreaSaludo.setText("");
				textFieldMail.setText("");
				textFieldURLImagen.setText("");
				LabelURLImagen.setIcon(new ImageIcon(Register.class.getResource("/umu/tds/vista/imagenes/usuario.png")));
				
				JOptionPane.showMessageDialog(contentPane, "Se ha registrado correctamente " + nombre + ".");
				
			}
		});
		panel.add(ButtonAceptar);
	}
	
	private Boolean tieneMinusculas(String contraseña) {
		return contraseña.matches(".*[a-z].*");
	}
	private Boolean tieneMayusculas(String contraseña) {
		return contraseña.matches(".*[A-Z].*");
	}
	private Boolean tieneDigitos(String contraseña) {
		return contraseña.matches(".*\\d.*");
	}
	private Boolean tieneSimbolos(String contraseña) {
		return contraseña.matches(".*[\\W_].*");
	}
	private Boolean noEspacios(String contraseña) {
		return contraseña.matches(".*\\s.*");
	}
}
