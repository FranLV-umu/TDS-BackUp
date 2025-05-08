package umu.tds.main;

import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.vista.Login;


public class Lanzador {
	private static boolean modoOscuro = true;
	private static LookAndFeel LAFClaro;
	private static LookAndFeel LAFOscuro;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    LAFClaro = UIManager.getLookAndFeel();
					
					UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
					LAFOscuro = UIManager.getLookAndFeel();
					
					UIManager.setLookAndFeel(modoOscuro ? LAFOscuro : LAFClaro);
					
					// Comprobar que se haya conectado con el servidor antes de arrancar la aplicación y se establece el color:
					ControladorAppChat.getUnicaInstancia().setModoColor(modoOscuro);
					
					// Lanzar la ventana de login:
					Login ventana = new Login();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void cambiarModo() {
        try {
            // Cambiar el modo oscuro
        	modoOscuro = ControladorAppChat.getUnicaInstancia().getModoColor();
        	ControladorAppChat.getUnicaInstancia().setModoColor(!modoOscuro);
        	modoOscuro = !modoOscuro;

            // Aplicar el LookAndFeel correspondiente
            UIManager.setLookAndFeel(modoOscuro ? LAFOscuro : LAFClaro);

            // Actualizar decoraciones de todas las ventanas
            for (Window window : Window.getWindows()) {
                if (window instanceof javax.swing.JFrame) {
                	boolean visible = ((javax.swing.JFrame) window).isVisible();
                    ((javax.swing.JFrame) window).dispose(); // Cierra temporalmente la ventana
                    if(modoOscuro == false)
                    	((javax.swing.JFrame) window).setUndecorated(false); // Restablece decoraciones false
                    else
                    	((javax.swing.JFrame) window).setUndecorated(true); // Restablece decoraciones true
                    ((javax.swing.JFrame) window).setVisible(visible); // Reabre la ventana
                }
            	
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
