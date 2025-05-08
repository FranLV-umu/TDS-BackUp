package umu.tds.controlador;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import umu.tds.modelo.Usuario;


public class BaseDatosUsr {

	private static LinkedList<Usuario> lista = new LinkedList<Usuario>();
	
	/* 
	 * Comprueba que exista el usuario, si no existe, devuelve -1
	 * Comprueba si la contraseña coincide, devuelve 0
	 * Si la contraseña no coincide, devuelve 1
	 */
	public static void testañadir() {
		//lista.add(new Usuario("Pepe", "Perez", "123", "123", , "img", "hola", "mail"));
		//lista.add(new Usuario("Juan", "Perez", "123", "312", LocalDate.now(), "img", "hola", "mail"));
	}
	
	public static int comprobarContraseña(String telefono, String contraseña) {
		Optional<Usuario> Usuario = lista.stream()
			.filter(usuario -> usuario.getTelefono().equals(telefono))
			.findAny();
			
		if(Usuario.isEmpty()) {
			return -1;
		}
		Usuario u = Usuario.get();
		if(u == null) {
			return -1;
		}
		return u.comprobarContraseña(contraseña) ? 0 : 1;
	}
	
	public static int comprobarExistente(String telefono) {
		Optional<Usuario> Usuario = lista.stream()
			.filter(usuario -> usuario.getTelefono().equals(telefono))
			.findAny();
		
		return Usuario.isEmpty() ? 0 : 1;
	}
	
	// String usuario, String contraseña, String telefono, LocalDate fechaDeNacimiento, String imagen, String saludo, String email
	
	public static void añadirUsuario(String nombre, String apellidos, String telefono, String contraseña, LocalDate bday, String saludo , String mail, String URL) {
		//lista.add(new Usuario(nombre, apellidos, contraseña, telefono, bday, URL, saludo, mail));
	}
}
