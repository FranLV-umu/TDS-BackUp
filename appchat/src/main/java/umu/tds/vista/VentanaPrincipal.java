package umu.tds.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Mensaje;
import umu.tds.modelo.Usuario;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Dimension;
import tds.BubbleText;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int TAM_IMAGEN = 30;
	private JPanel contentPane;
	private VentanaContactos ventanaContactos = null;
	private Mensaje mensajeSeleccionado = null;
	private Contacto contactoSeleccionado = null;
	private JTextField textFieldMensaje;
	private String otroUsuario;
	private JPanel panelChatActual = null;
	private JScrollPane scrollPane = null;
	private JComboBox<Contacto> comboBoxContactos;
	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setTitle("APPCHAT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 485);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Archivo");
		menuBar.add(mnMenu);
		
		JMenuItem mntmMenuItemPerfil = new JMenuItem("Perfil");
		mnMenu.add(mntmMenuItemPerfil);
		
		JMenuItem mntmMenuItemSesion = new JMenuItem("Cerrar sesión");
		mnMenu.add(mntmMenuItemSesion);
		
		JMenuItem mntmMenuItemSalir = new JMenuItem("Salir");
		mnMenu.add(mntmMenuItemSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel botonera = new JPanel();
		contentPane.add(botonera, BorderLayout.NORTH);
		botonera.setLayout(new BoxLayout(botonera, BoxLayout.X_AXIS));
		
		DefaultComboBoxModel<Contacto> modelo = new DefaultComboBoxModel<>();
		
		comboBoxContactos = new JComboBox<>(modelo);
		comboBoxContactos.setRenderer(new ContactoCellRenderer());
		cargarContactosSinChat(modelo);
		
		comboBoxContactos.setSelectedIndex(-1);
		comboBoxContactos.setToolTipText("Seleccione un contacto.");
		
        comboBoxContactos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contactoSeleccionado = (Contacto) comboBoxContactos.getSelectedItem();
                if (contactoSeleccionado != null) {
                	mensajeSeleccionado = null;
                	panelChatActual.removeAll();
                	if(ControladorAppChat.getUnicaInstancia().debug)
                		System.out.println("> Contacto seleccionado: " + contactoSeleccionado.getNombre());
                	AbrirChat();
                }
            }
        });
        
        botonera.add(comboBoxContactos);

		
		JButton boton_enviar = new JButton("");
		boton_enviar.setIcon(new ImageIcon(Register.class.getResource("/umu/tds/vista/imagenes/send_message.png")));
		//Image imgEnviar = new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/vista/imagenes/send_message.png")).getImage().getScaledInstance(TAM_IMAGEN, TAM_IMAGEN, Image.SCALE_SMOOTH);
		//boton_enviar.setIcon(new ImageIcon(imgEnviar));
		
		botonera.add(boton_enviar);
		
		JButton boton_buscar = new JButton("Buscar");
		botonera.add(boton_buscar);
		
		JButton boton_contactos = new JButton("Contactos");
		boton_contactos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ventanaContactos == null) {
					ventanaContactos = new VentanaContactos();
				}
				ventanaContactos.setVisible(true);
			}
		});
		botonera.add(boton_contactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		botonera.add(horizontalGlue);
		
		JButton boton_premium = new JButton("Premium");
		botonera.add(boton_premium);
		
		JLabel NombreUsuario = new JLabel("Nombre Usuario");
		NombreUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		String nombre = ControladorAppChat.getUnicaInstancia().getNombreUsuarioActual();
		NombreUsuario.setText(nombre);
		botonera.add(NombreUsuario);
		
		JLabel ImagenUsuario = new JLabel("");
		URL url;
		try {
			url = new URL(ControladorAppChat.getUnicaInstancia().getImgUsuarioActual());
			ImageIcon imageIcon = new ImageIcon(url);
			Image imgUsr = imageIcon.getImage().getScaledInstance(TAM_IMAGEN, TAM_IMAGEN, Image.SCALE_SMOOTH);
			
			// Verificar si la imagen se carga correctamente
            if (imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
            	ImagenUsuario.setIcon(new ImageIcon(imgUsr)); // Mostrar la imagen en el JLabel
            	ImagenUsuario.setText(""); // Quitar el texto en caso de que haya
            } else {
            	ImagenUsuario.setIcon(new ImageIcon(Register.class.getResource("/umu/tds/vista/imagenes/send_mensaje.gif")));
            }
		} catch (MalformedURLException ex) {
			if(ControladorAppChat.getUnicaInstancia().debug) {
				System.err.println("-> Error al cargar la imagen. [Registro | TextField]");
				ex.printStackTrace();
			}
		}
		/*try {
			URL imageUrl = new URL("https://robohash.org/" + ControladorAppChat.getUnicaInstancia().getImgUsuarioActual() + "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			ImagenUsuario.setIcon(imageIcon);
		} catch (IOException e) {
			e.printStackTrace();
			ImagenUsuario.setIcon(null); // Default to no image if there was an issue
		}*/
		botonera.add(ImagenUsuario);
		
		JPanel panel_chat_recientes = new JPanel();
		// TODO Cuidado con esto:
		panel_chat_recientes.setPreferredSize(new Dimension(100, 100));
		panel_chat_recientes.setMinimumSize(new Dimension(40, 40));
		panel_chat_recientes.setBorder(new TitledBorder(null, "mensajes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_chat_recientes, BorderLayout.WEST);
		panel_chat_recientes.setLayout(new BorderLayout(0, 0));
		
		
		DefaultListModel<Mensaje> ModMensajes = new DefaultListModel<Mensaje>(){
			List<Mensaje> mensajes = ControladorAppChat.getUnicaInstancia().getUltimoMensajePorUsuario(); // new LinkedList<Mensaje>();
			
			public int getSize() {
				return mensajes.size();
			}
			public Mensaje getElementAt(int index) {
				return mensajes.get(index);
			}
		};
		//AbstractListModel<Mensaje> modelo = new AbstractListModel<>();
		
		JList<Mensaje> list = new JList<Mensaje>(ModMensajes);
		list.addListSelectionListener(e -> {
		    if (!e.getValueIsAdjusting()) { // Evitar capturar eventos intermedios
		        mensajeSeleccionado = list.getSelectedValue();
		        if (mensajeSeleccionado != null) {
		        	contactoSeleccionado = null;
		            System.out.println("Mensaje seleccionado: " + mensajeSeleccionado.getTexto());
		            
		            otroUsuario = mensajeSeleccionado.getNumEmisor().equals(ControladorAppChat.getUnicaInstancia().getNumUsuarioActual())
		            					? mensajeSeleccionado.getNumReceptor()
		            					: mensajeSeleccionado.getNumEmisor();
					Optional<ContactoIndividual> c = ControladorAppChat.getUnicaInstancia().buscarContactoPorNum(otroUsuario);
					if(c.isPresent()) {
						otroUsuario = c.get().getNombre();
					}
		            AbrirChat();
		        }
		        
		    }
		});
		list.setCellRenderer(new MensajeCellRenderer());
		panel_chat_recientes.add(new JScrollPane(list), BorderLayout.CENTER);
		
		JPanel panelChat = new JPanel();
		panelChat.setLayout(new BorderLayout(0, 0));
		contentPane.add(panelChat, BorderLayout.CENTER);
		
		panelChatActual = new JPanel();
		BubbleText.noZoom();
		panelChatActual.setMinimumSize(new Dimension(500, 700));
		panelChatActual.setLayout(new BoxLayout(panelChatActual, BoxLayout.Y_AXIS));
		
		scrollPane = new JScrollPane(panelChatActual);
		scrollPane.setBorder(new TitledBorder(null, "chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChat.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panelChat.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		textFieldMensaje = new JTextField();
		panel.add(textFieldMensaje, BorderLayout.CENTER);
		textFieldMensaje.setColumns(10);
		
		// Botón para desplegar emojis
		/*
		JButton btnEmojis = new JButton("😀");
		btnEmojis.addActionListener(e -> {
			JPopupMenu emojiMenu = new JPopupMenu();
			String[] emojis = {"😀", "😂", "😍", "😢", "😡", "👍", "👎", "🎉"};
			for (String emoji : emojis) {
				JMenuItem emojiItem = new JMenuItem(emoji);
				emojiItem.addActionListener(event -> textFieldMensaje.setText(textFieldMensaje.getText() + emoji));
				emojiMenu.add(emojiItem);
			}
			emojiMenu.show(btnEmojis, btnEmojis.getWidth() / 2, btnEmojis.getHeight() / 2);
		});
		panel.add(btnEmojis, BorderLayout.WEST);
		*/
		
		// TODO Revisar esto:
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuario u = null;
				if(contactoSeleccionado != null) {
					ContactoIndividual c = (ContactoIndividual) contactoSeleccionado;
					u = c.getUsuario();
					comboBoxContactos.removeItem(contactoSeleccionado);
				}
				else if(mensajeSeleccionado != null) {
					u = mensajeSeleccionado.getEmisor().getTelefono().equals(ControladorAppChat.getUnicaInstancia().getNumUsuarioActual()) 
							? mensajeSeleccionado.getReceptor()
							: mensajeSeleccionado.getEmisor();
				}
				Mensaje m = ControladorAppChat.getUnicaInstancia().enviarMensaje(u, textFieldMensaje.getText().trim());
				BubbleText burbuja = new BubbleText(panelChatActual, m.getTexto(), Color.GREEN, ControladorAppChat.getUnicaInstancia().getNombreUsuarioActual(), BubbleText.SENT);
				panelChatActual.add(burbuja);
				if(contactoSeleccionado != null) {
					ModMensajes.addElement(m);
				}
				revalidate();
				repaint();
			}
		});
		panel.add(btnEnviar, BorderLayout.EAST);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				cargarContactosSinChat(modelo);
			}
		});
	}
	
	private void cargarContactosSinChat(DefaultComboBoxModel<Contacto> modelo) {
		modelo.removeAllElements();
        List<Contacto> contactosSinChat = ControladorAppChat.getUnicaInstancia().getContactosSinChatUsuarioActual();
        for (Contacto contacto : contactosSinChat) {
        	if(contacto != null) {
        		modelo.addElement(contacto);
        		//System.out.println(contacto);
        	}
        		
        }
    }
	
	private void AbrirChat() {
		panelChatActual.removeAll();
		Usuario u;
		if(mensajeSeleccionado != null) {
			// TODO: Cambiar el nº a por el propio usuario
			u = mensajeSeleccionado.getEmisor().getTelefono().equals(ControladorAppChat.getUnicaInstancia().getNumUsuarioActual()) 
					? mensajeSeleccionado.getReceptor()
					: mensajeSeleccionado.getEmisor();
	
			Optional<List<Mensaje>> mensajes = ControladorAppChat.getUnicaInstancia().getMensajesdeUsuario(u);
			if(!mensajes.isEmpty() && mensajes.isPresent()) {
				mensajes.get().stream()
					.sorted(Comparator.comparing(Mensaje::getFecha))
					.forEach(m -> {
						BubbleText burbuja;
						if(m.getEmisor().getTelefono().equals(ControladorAppChat.getUnicaInstancia().getNumUsuarioActual())) {
							burbuja=new BubbleText(panelChatActual, m.getTexto(), Color.GREEN, ControladorAppChat.getUnicaInstancia().getNombreUsuarioActual(), BubbleText.SENT);
						}	else	{
							// TODO CAMBIAR A POR NOMBRE CONTACTO
							
							burbuja=new BubbleText(panelChatActual, m.getTexto(), Color.LIGHT_GRAY, otroUsuario, BubbleText.RECEIVED);
						}
						panelChatActual.add(burbuja);
					});
			}
		}	else if (contactoSeleccionado != null) {
			ContactoIndividual c = (ContactoIndividual) contactoSeleccionado;
			u = c.getUsuario();
		}
		
		
	}

}
