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
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private SimpleDateFormat dateFormat;

	private static AdaptadorMensajeTDS unicaInstancia;

	public static AdaptadorMensajeTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorMensajeTDS();
		return unicaInstancia;
	}

	private AdaptadorMensajeTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;
		
		// Si está registrado, no se registra de nuevo.
		try {
			 eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		}	catch (NullPointerException e) {}
		if (eMensaje != null) return;
		
		eMensaje = new Entidad();
		eMensaje.setNombre("mensaje");
		eMensaje.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
						new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
						new Propiedad("fecha", mensaje.getFecha().toString()),
						new Propiedad("texto", mensaje.getTexto()),
						new Propiedad("emoji", String.valueOf(mensaje.getEmoji()))
				)));
		
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		mensaje.setCodigo(eMensaje.getId());
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Mensaje registrado: " + eMensaje.getNombre() );
			eMensaje.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public void borrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		servPersistencia.borrarEntidad(eMensaje);
	}
	
	public void modificarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		for (Propiedad prop : eMensaje.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(mensaje.getCodigo()));
			} else if (prop.getNombre().equals("emisor")) {
				prop.setValor(String.valueOf(mensaje.getEmisor().getCodigo()));
			} else if (prop.getNombre().equals("receptor")) {
				prop.setValor(String.valueOf(mensaje.getReceptor().getCodigo()));
			} else if (prop.getNombre().equals("texto")) {
				prop.setValor(mensaje.getTexto());
			} else if (prop.getNombre().equals("fecha")) {
				prop.setValor(mensaje.getFecha().toString());
			} else if (prop.getNombre().equals("emoji")) {
				prop.setValor(String.valueOf(mensaje.getEmoji()));
			}
			servPersistencia.modificarPropiedad(prop);
		}
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Mensaje modificado: " + eMensaje.getNombre() );
			eMensaje.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
	}
	
	public Mensaje recuperarMensaje(int codigo) {
		// Si la entidad está en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		Entidad eMensaje;
		String emisor;
		String receptor;
		String fechaprov;
		Date fecha = null;
		String texto;
		String emoji;
		
		eMensaje = servPersistencia.recuperarEntidad(codigo);
		
		emisor = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor");
		receptor = servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor");
		texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		emoji = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoji");
		fechaprov = servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha");
		
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		try {
			fecha = format.parse(fechaprov);
		} catch (ParseException e) {
			System.err.println("No se pudo transformar la fecha al leer los datos.");
			e.printStackTrace();
		}
		
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		
		Mensaje mensaje = new Mensaje(texto, fecha, Integer.parseInt(emoji));
		mensaje.setCodigo(codigo);
		
		if(ControladorAppChat.getUnicaInstancia().debug) {
			System.out.println("-> Mensaje recuperado: " + eMensaje.getNombre() + " " + eMensaje.getId());
			eMensaje.getPropiedades().stream().forEach( u -> System.out.print( "\t\t"+ u.getNombre() + " " + u.getValor() + "\n"));
		}
		
		//Metemos el mensaje al pool
		PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);
		
		mensaje.setEmisor(adaptadorU.recuperarUsuario(Integer.parseInt(emisor)));
		mensaje.setReceptor(adaptadorU.recuperarUsuario(Integer.parseInt(receptor)));
		
		return mensaje;
	}
	
	public List<Mensaje> recuperarTodosMensajes(){
		List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
		List<Mensaje> mensajes = new LinkedList<Mensaje>();

		for (Entidad eMensaje : eMensajes) {
			mensajes.add(recuperarMensaje(eMensaje.getId()));
		}
		return mensajes;
	}
	
	/*public List<Mensaje> inicializarMensajes(List<Mensaje> mensajes) {
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		LinkedList<Mensaje> lista = new LinkedList<Mensaje>();
		for(Mensaje m : mensajes) {
			lista.add(new Mensaje(adaptadorU.recuperarUsuario(m.getCodigoEmisor()),
					adaptadorU.recuperarUsuario(m.getCodigoReceptor()),
					m.getTexto(), m.getFecha(), m.getEmoji())); 
		}
		return lista;
	}*/

}
