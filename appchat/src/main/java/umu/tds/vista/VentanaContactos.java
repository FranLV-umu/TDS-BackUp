package umu.tds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Contacto;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class VentanaContactos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CrearContacto ventanaAddContacto = null;
	private CrearGrupo ventanaAddGrupo = null;
	private Contacto contactoSeleccionado = null;
	private List<Contacto> listaContactosSeleccionados = null;


	/**
	 * Create the frame.
	 */
	public VentanaContactos() {
		setMinimumSize(new Dimension(700, 310));
		setTitle("Contactos");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 699, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		contentPane.add(horizontalGlue_1);
		
		JPanel Global = new JPanel();
		Global.setBorder(new TitledBorder(null, "lista contactos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(Global);
		GridBagLayout gbl_Global = new GridBagLayout();
		gbl_Global.columnWidths = new int[]{0, 270, 0, 270, 0, 0};
		gbl_Global.rowHeights = new int[]{0, 0, 0, 0};
		gbl_Global.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_Global.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		Global.setLayout(gbl_Global);
		
		DefaultListModel<Contacto> ModelListContactos = new DefaultListModel<Contacto>() {
			List<Contacto> contactos = ControladorAppChat.getUnicaInstancia().getContactosUsuarioActual();
			
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
		GridBagConstraints gbc_listContactos = new GridBagConstraints();
		gbc_listContactos.insets = new Insets(0, 0, 5, 5);
		gbc_listContactos.fill = GridBagConstraints.BOTH;
		gbc_listContactos.gridx = 1;
		gbc_listContactos.gridy = 1;
		listContactos.setCellRenderer(new ContactoCellRenderer());
		JScrollPane pane = new JScrollPane(listContactos);
		Global.add(pane, gbc_listContactos);
		
		
		DefaultListModel<Contacto> ModelListSelec = new DefaultListModel<>();
		JList<Contacto> listSeleccionados = new JList<Contacto>(ModelListSelec);
		GridBagConstraints gbc_listSeleccionados = new GridBagConstraints();
		gbc_listSeleccionados.insets = new Insets(0, 0, 5, 5);
		gbc_listSeleccionados.fill = GridBagConstraints.BOTH;
		gbc_listSeleccionados.gridx = 3;
		gbc_listSeleccionados.gridy = 1;
		listSeleccionados.setCellRenderer(new ContactoCellRenderer());
		listSeleccionados.addListSelectionListener(e -> {
		    if (!e.getValueIsAdjusting()) { // Evitar capturar eventos intermedios
		    	contactoSeleccionado = listSeleccionados.getSelectedValue();
		        
	        	if(ControladorAppChat.getUnicaInstancia().debug) {
		        	if (contactoSeleccionado != null) {
	        			System.out.println("> Contacto seleccionado: " + contactoSeleccionado.getNombre());	
	        		}	else	{
	        			System.out.println("> No se ha seleccionado ningún contacto.");	
	        		}
		        }
        	}
		});
		Global.add(listSeleccionados, gbc_listSeleccionados);
		//JScrollPane scrollPane = new JScrollPane(listSeleccionados);
		//Global.add(scrollPane, gbc_listSeleccionados);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 1;
		Global.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnInsertar = new JButton(">>");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(contactoSeleccionado != null && !ModelListSelec.contains(contactoSeleccionado)) {
					ModelListSelec.addElement(contactoSeleccionado);
					if(listaContactosSeleccionados == null) {
						listaContactosSeleccionados = new LinkedList<Contacto>();
					}
					listaContactosSeleccionados.add(contactoSeleccionado);
					listSeleccionados.revalidate();
					listSeleccionados.repaint();
				}	else if(ModelListSelec.contains(contactoSeleccionado)) {
					JOptionPane.showMessageDialog(contentPane, "El usuario seleccionado ya está en la lista.");
				}	else if(contactoSeleccionado == null)	{
					JOptionPane.showMessageDialog(contentPane, "No hay ningún usuario seleccionado.");
				}
			}
		});
		panel.add(btnInsertar);
		
		JButton btnQuitar = new JButton("<<");
		btnQuitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(contactoSeleccionado != null && ModelListSelec.contains(contactoSeleccionado)) {
					ModelListSelec.removeElement(contactoSeleccionado);
					listaContactosSeleccionados.remove(contactoSeleccionado);
					listSeleccionados.revalidate();
					listSeleccionados.repaint();
				}	else if(!ModelListSelec.contains(contactoSeleccionado)) {
					JOptionPane.showMessageDialog(contentPane, "El usuario seleccionado no está en la lista.");
				}	else	{
					JOptionPane.showMessageDialog(contentPane, "No hay ningún usuario seleccionado.");
				}
			}
		});
		panel.add(btnQuitar);
		
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 2;
		Global.add(panel_1, gbc_panel_1);
		
		JButton btnAddContacto = new JButton("Añadir contacto");
		btnAddContacto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ventanaAddContacto == null) ventanaAddContacto = new CrearContacto(ModelListContactos);
				ventanaAddContacto.setVisible(true);
			}
		});
		panel_1.add(btnAddContacto);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.gridx = 3;
		gbc_panel_2.gridy = 2;
		Global.add(panel_2, gbc_panel_2);
		
		JButton btnAddGrupo = new JButton("Añadir grupo");
		btnAddGrupo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ModelListSelec.isEmpty()) {
					ModelListSelec.removeAllElements();
					
					if(ventanaAddGrupo == null) {
						ventanaAddGrupo = new CrearGrupo(listaContactosSeleccionados);
					}
					ventanaAddGrupo.setVisible(true);
					listSeleccionados.revalidate();
					listSeleccionados.repaint();
				}	else if(!ModelListSelec.contains(contactoSeleccionado)) {
					JOptionPane.showMessageDialog(contentPane, "El usuario seleccionado no está en la lista.");
				}	else	{
					JOptionPane.showMessageDialog(contentPane, "No hay ningún usuario seleccionado.");
				}
			}
		});
		panel_2.add(btnAddGrupo);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		contentPane.add(horizontalGlue);
		
		
	}

}
