package umu.tds.persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Chat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;

//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia;

	public static AdaptadorUsuarioTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuarioTDS();
		return unicaInstancia;
	}

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		
		// Comprobar si está ya registrado
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		}	catch (NullPointerException e) {}
		if (eUsuario != null) return;
		
		// Registrar los contactos que son objetos. (Contactos y chats)
		 AdaptadorChatTDS adaptadorChat = AdaptadorChatTDS.getUnicaInstancia();
		 if(usuario.getChats() != null && !usuario.getChats().isEmpty()) {
			 for (Chat c : usuario.getChats())
				 adaptadorChat.registrarChat(c);
		 }
		 
		 AdaptadorContactoTDS adaptadorContacto = AdaptadorContactoTDS.getUnicaInstancia();
		 if(!usuario.getContactos().isEmpty())
			 for (Contacto c : usuario.getContactos())
				 adaptadorContacto.registrarContacto(c);

		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("nombre", usuario.getNombre()),
						new Propiedad("apellidos", usuario.getApellidos()),
						new Propiedad("contraseña", usuario.getContraseña()),
						new Propiedad("telefono", usuario.getTelefono()),
						new Propiedad("fechaNacimiento", usuario.getFechaDeNacimiento().toString()),
						new Propiedad("email", usuario.getEmail()),
						new Propiedad("imagen", usuario.getImagen()),
						new Propiedad("saludo", usuario.getSaludo()),
						new Propiedad("contactos", usuario.getContactos().isEmpty() ? "" : obtenerCodigosContactos(usuario.getContactos())),
						new Propiedad("chats", usuario.getChats().isEmpty() ? "" : obtenerCodigosChats(usuario.getChats()))
				)));
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Usuario registrado: " + eUsuario.getNombre() );
			eUsuario.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public void borrarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		servPersistencia.borrarEntidad(eUsuario);
	}
	
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		for(Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(usuario.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals("apellidos")) {
				prop.setValor(usuario.getApellidos());
			} else if (prop.getNombre().equals("telefono")) {
				prop.setValor(usuario.getTelefono());
			} else if (prop.getNombre().equals("fechaNacimiento")) {
				prop.setValor(usuario.getFechaDeNacimiento().toString());
			} else if (prop.getNombre().equals("email")) {
				prop.setValor(usuario.getEmail());
			} else if (prop.getNombre().equals("imagen")) {
				prop.setValor(usuario.getImagen());
			} else if (prop.getNombre().equals("saludo")) {
				prop.setValor(usuario.getSaludo());
			} else if (prop.getNombre().equals("contactos")) {
				 String contacto = obtenerCodigosContactos(usuario.getContactos());
				 prop.setValor(contacto);
			} else if (prop.getNombre().equals("chats")) {
				String chats = obtenerCodigosChats(usuario.getChats());
				prop.setValor(chats);
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Usuario modificado: " + eUsuario.getNombre() );
			eUsuario.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public Usuario recuperarUsuario(int codigo) {
		// Si la entidad está en el pool la devuele directamente
		if(PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		
		Entidad eUsuario;
		eUsuario = servPersistencia.recuperarEntidad(codigo);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String fechaprov = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		String contactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
		String chats = servPersistencia.recuperarPropiedadEntidad(eUsuario, "chats");
		Date fecha = null;
		
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		try {
			fecha = format.parse(fechaprov);
		} catch (ParseException e) {
			System.err.println("No se pudo transformar la fecha al leer los datos.");
			e.printStackTrace();
		}
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println(" -> Usuario recuperado: "+ eUsuario.getNombre());
			eUsuario.getPropiedades().stream()
				.forEach(prop -> System.out.println("\t"+prop.getNombre()+ " "+ prop.getValor()));
		}
		
		Usuario usuario = new Usuario(nombre, apellidos, contraseña, telefono, fecha, imagen, saludo, email);
		usuario.setCodigo(codigo);
		
		// Metemos el usuario al pool
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);
		
		List<Chat> chatsRecuperados = obtenerChatsDesdeCodigos(chats);
		for (Chat c : chatsRecuperados)
			usuario.addChat(c);
		
		List<Contacto> contactosRecuperados = obtenerContactosDesdeCodigos(contactos);
		for (Contacto c : contactosRecuperados)
			usuario.addContacto(c);
		
		return usuario;
	}
	
	public List<Usuario> recuperarTodosUsuarios(){
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();
		
		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}
	
	// -------------------Funciones auxiliares-----------------------------

		private String obtenerCodigosChats(List<Chat> lista) {
			String aux = "";
			if(lista == null) return aux;
			for (Chat v : lista) {
				aux += v.getCodigo() + " ";
			}
			return aux.trim();
		}
	
		private List<Chat> obtenerChatsDesdeCodigos(String chats) {
			List<Chat> listaChats = new LinkedList<Chat>();
			if(chats == null) return listaChats;
			StringTokenizer strTok = new StringTokenizer(chats, " ");
			AdaptadorChatTDS adaptadorC = AdaptadorChatTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				listaChats.add(adaptadorC.recuperarChat(Integer.valueOf((String) strTok.nextElement())));
			}
			return listaChats;
		}
		
		private String obtenerCodigosContactos(List<Contacto> lista) {
			String aux = "";
			if(lista == null) return aux;
			for (Contacto v : lista) {
				aux += v.getCodigo() + " ";
			}
			return aux.trim();
		}
		
		private List<Contacto> obtenerContactosDesdeCodigos(String contactos) {
			List<Contacto> listaContactos = new LinkedList<Contacto>();
			if(contactos == null) return listaContactos;
			StringTokenizer strTok = new StringTokenizer(contactos, " ");
			AdaptadorContactoTDS adaptadorC = AdaptadorContactoTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				listaContactos.add(adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
			}
			return listaContactos;
		}

		/*public Usuario inicializarUsuario(Usuario u) {
			AdaptadorChatTDS adaptadorC = AdaptadorChatTDS.getUnicaInstancia();
			AdaptadorContactoTDS adaptadorContacto = AdaptadorContactoTDS.getUnicaInstancia();
			//if(u.getCodigosChats()!=null && !u.getCodigosChats().isEmpty()) {
				List<Chat> listaChats = obtenerChatsDesdeCodigos(u.getCodigosChats());
//if(ControladorAppChat.getUnicaInstancia().debug)listaChats.stream().forEach(chat -> System.out.println(" Chat " + chat + " " + chat.getCodigo()));
				listaChats = adaptadorC.inicializarChats(listaChats);
			//}
				List<Contacto> listaContactos = obtenerContactosDesdeCodigos(u.getCodigosContactos());
//if(ControladorAppChat.getUnicaInstancia().debug)listaContactos.stream().forEach(contact -> System.out.println(" Contacto " + contact + " " + contact.getCodigo() + " " + contact.getNombre()));
				listaContactos = adaptadorContacto.inicializarListaContactos(listaContactos);
				
				return new Usuario(u, listaChats, listaContactos, u.getCodigo());
		}*/
		
}
