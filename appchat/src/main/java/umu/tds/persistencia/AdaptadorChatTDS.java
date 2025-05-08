package umu.tds.persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.controlador.ControladorAppChat;
import umu.tds.modelo.Chat;
import umu.tds.modelo.Mensaje;
import umu.tds.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;


public class AdaptadorChatTDS implements IAdaptadorChatDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private SimpleDateFormat dateFormat;

	private static AdaptadorChatTDS unicaInstancia;

	public static AdaptadorChatTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorChatTDS();
		return unicaInstancia;
	}

	private AdaptadorChatTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarChat(Chat chat) {
		Entidad eChat = null;
		
		// Si está registrado, no se registra de nuevo.
		try {
			 eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		}	catch (NullPointerException e) {}
		if (eChat != null) return;
		
		// registrar todos los mensajes
		if(chat.getMensajes() != null && !chat.getMensajes().isEmpty()) {
			AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
			for(Mensaje m : chat.getMensajes()) {
				adaptadorMensaje.registrarMensaje(m);
			}
		}
		
		// registramos el chat
		eChat = new Entidad();
		eChat.setNombre("Chat");
		eChat.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("usr1", String.valueOf(chat.getCodigoUsr1())),
						new Propiedad("usr2", String.valueOf(chat.getCodigoUsr2())),
						new Propiedad("mensajes", obtenerCodigosMensajes(chat.getMensajes()))
				)));
		
		eChat = servPersistencia.registrarEntidad(eChat);
		chat.setCodigo(eChat.getId());
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Chat registrado: " + eChat.getNombre() );
			eChat.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public void borrarChat(Chat chat) {
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		servPersistencia.borrarEntidad(eChat);
	}
	
	public void modificarChat(Chat chat) {
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		for (Propiedad prop : eChat.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(chat.getCodigo()));
			} else if (prop.getNombre().equals("usr1")) {
				prop.setValor(String.valueOf(chat.getCodigoUsr1()));
			} else if (prop.getNombre().equals("usr2")) {
				prop.setValor(String.valueOf(chat.getCodigoUsr2()));
			} else if (prop.getNombre().equals("mensajes")) {
				String mensajes = obtenerCodigosMensajes(chat.getMensajes());
				prop.setValor(mensajes);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Chat modificado: " + eChat.getNombre() );
			eChat.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public Chat recuperarChat(int codigo) {
		// Si la entidad está en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Chat) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		Entidad eChat;
		int CodigoUsr1, CodigoUsr2;
		List<Mensaje> mensajes = new LinkedList<Mensaje>();
		
		eChat = servPersistencia.recuperarEntidad(codigo);
		
		CodigoUsr1 = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "usr1"));
		CodigoUsr2 = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "usr2"));
		
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		
		Chat chat = new Chat();
		chat.setCodigo(codigo);
		
		//Metemos el Chat al pool
		PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);
		
		chat.setUsrs(adaptadorU.recuperarUsuario(CodigoUsr1), adaptadorU.recuperarUsuario(CodigoUsr2));
		
		String codigoMensajes = servPersistencia.recuperarPropiedadEntidad(eChat, "mensajes");
		mensajes = obtenerMensajesDesdeCodigos(codigoMensajes);
		for (Mensaje m : mensajes) {
			chat.addMensaje(m);
		}
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Chat recuperado: " + eChat.getNombre() );
			eChat.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
		
		return chat;
	}
	
	public List<Chat> recuperarTodosChats(){
		List<Entidad> eChats = servPersistencia.recuperarEntidades("Chat");
		List<Chat> chats = new LinkedList<Chat>();

		for (Entidad eChat : eChats) {
			chats.add(recuperarChat(eChat.getId()));
		}
		return chats;
	}
	
	// -------------------Funciones auxiliares-----------------------------
	
	private String obtenerCodigosMensajes(List<Mensaje> lineasMensaje) {
		if(lineasMensaje == null) return "";
		String lineas = "";
		for (Mensaje lineaMensaje : lineasMensaje) {
			lineas += lineaMensaje.getCodigo() + " ";
		}
		return lineas.trim();
	}
	
	private List<Mensaje> obtenerMensajesDesdeCodigos(String lineas) {
		List<Mensaje> lineasMensaje = new LinkedList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		AdaptadorMensajeTDS adaptadorM = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			lineasMensaje.add(adaptadorM.recuperarMensaje(Integer.valueOf((String) strTok.nextElement())));
		}
		return lineasMensaje;
	}
	
	/*public List<Chat> inicializarChats(List<Chat> chats) {
		AdaptadorMensajeTDS adaptadorM = AdaptadorMensajeTDS.getUnicaInstancia();
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		LinkedList<Chat> lista = new LinkedList<Chat>();
		for(Chat c : chats) {
			lista.add(new Chat(adaptadorU.recuperarUsuario(c.getCodigoUsr1()),
					adaptadorU.recuperarUsuario(c.getCodigoUsr2()),
					c.getCodigo(), adaptadorM.inicializarMensajes(c.getMensajes())));
		}
		return lista;
	}*/
}
