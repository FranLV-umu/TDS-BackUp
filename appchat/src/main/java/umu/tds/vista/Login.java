package umu.tds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import umu.tds.controlador.ControladorAppChat;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Login {

	private Register ventanaRegistro = null;
	
	private JFrame frame;
	private JPanel panel_1;
	private JPanel panel_2;
	private JToggleButton tglbtnModoOscuro;
	private JLabel LabelContraseña;
	private JPasswordField passwordFieldContraseña;
	private JLabel LabelTelefono;
	private JTextField textFieldTelefono;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblNewLabel = new JLabel("");
		ImageIcon imageIcon = new ImageIcon(Login.class.getResource("/umu/tds/vista/imagenes/logoPeq.jpg"));
		Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(img));
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		LabelTelefono = new JLabel("Telefono:");
		GridBagConstraints gbc_LabelTelefono = new GridBagConstraints();
		gbc_LabelTelefono.anchor = GridBagConstraints.EAST;
		gbc_LabelTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_LabelTelefono.gridx = 1;
		gbc_LabelTelefono.gridy = 2;
		panel.add(LabelTelefono, gbc_LabelTelefono);
		
		textFieldTelefono = new JTextField();
		textFieldTelefono.setToolTipText("Telefono");
		textFieldTelefono.setColumns(10);
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.gridx = 2;
		gbc_textFieldTelefono.gridy = 2;
		panel.add(textFieldTelefono, gbc_textFieldTelefono);
		
		LabelContraseña = new JLabel("Contraseña:");
		GridBagConstraints gbc_LabelContraseña = new GridBagConstraints();
		gbc_LabelContraseña.anchor = GridBagConstraints.EAST;
		gbc_LabelContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_LabelContraseña.gridx = 1;
		gbc_LabelContraseña.gridy = 4;
		panel.add(LabelContraseña, gbc_LabelContraseña);
		
		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setToolTipText("Contraseña");
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 4;
		panel.add(passwordFieldContraseña, gbc_passwordFieldContraseña);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 7;
		panel.add(panel_2, gbc_panel_2);
		
		tglbtnModoOscuro = new JToggleButton("ModoOscuro");
		tglbtnModoOscuro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControladorAppChat.getUnicaInstancia().cambiarModo();
				
			}
		});
		tglbtnModoOscuro.setSelected(true);
		panel_2.add(tglbtnModoOscuro);
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("Register");
		panel_1.add(btnNewButton);
		
		JButton btnLogin = new JButton("Login");
		panel_1.add(btnLogin);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ventanaRegistro == null) {
					ventanaRegistro = new Register();
				}
				ventanaRegistro.setVisible(true);
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String contraseña = passwordFieldContraseña.getText();
				String telefono = textFieldTelefono.getText();
				
				if(!telefono.matches("\\d+")) {
					JOptionPane.showMessageDialog(frame, "El teléfono solo debe contener números.");
					return;
				}	else if (telefono.length() > 9 || telefono.length() < 9) {
					JOptionPane.showMessageDialog(frame, "El teléfono debe tener una longitud de 9 números.");
					return;
				}
				
				boolean estaLogueado = ControladorAppChat.getUnicaInstancia().login(telefono, contraseña);
				if(estaLogueado) {
					VentanaPrincipal principal = new VentanaPrincipal();
					principal.setVisible(true);
					frame.setVisible(false);
				}	else	{
					JOptionPane.showMessageDialog(frame, "El teléfono o la contraseña no son válidos.");
				}
			}
		});
		frame.setVisible(true);
	}

}
