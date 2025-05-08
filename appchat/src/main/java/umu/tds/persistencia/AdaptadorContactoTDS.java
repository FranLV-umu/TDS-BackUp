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
import umu.tds.modelo.Chat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Grupo;
import umu.tds.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorContactoTDS implements IAdaptadorContactoDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private static AdaptadorContactoTDS unicaInstancia;

	public static AdaptadorContactoTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoTDS();
		return unicaInstancia;
	}

	private AdaptadorContactoTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarContacto(Contacto contacto) {
		Entidad eContacto = null;
		
		// Si está registrado, no se registra de nuevo.
		try {
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		}	catch (NullPointerException e) {}
		if (eContacto != null) return;
		
		eContacto = new Entidad();
		if(contacto instanceof ContactoIndividual) {
			ContactoIndividual c = (ContactoIndividual) contacto;
			eContacto.setNombre("contacto_individual");
			eContacto.setPropiedades(new ArrayList<Propiedad>(
					Arrays.asList(
							new Propiedad("nombre", c.getNombre()),
							new Propiedad("usuario", String.valueOf(c.getCodigoUsuario())),
							new Propiedad("imagen", c.getImagen())
					)));
		}
		
		if(contacto instanceof Grupo) {
			Grupo g = (Grupo) contacto;
			eContacto.setNombre("grupo");
			eContacto.setPropiedades(new ArrayList<Propiedad>(
					Arrays.asList(
							new Propiedad("nombre", g.getNombre()),
							new Propiedad("contactos", obtenerCodigosContactos(g.getListaContactos())),
							new Propiedad("imagen", g.getImagen())
					)));
		}
		
		eContacto = servPersistencia.registrarEntidad(eContacto);
		contacto.setCodigo(eContacto.getId());
	}
	
	public void borrarContacto(Contacto contacto) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		servPersistencia.borrarEntidad(eContacto);
	}
	
	public void modificarContacto(Contacto contacto) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		for (Propiedad prop : eContacto.getPropiedades()) {
			/*if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(mensaje.getCodigo()));
			} else if (prop.getNombre().equals("emisor")) {
				prop.setValor(String.valueOf(mensaje.getCodigoEmisor()));
			} */
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public Contacto recuperarContacto(int codigo) {
		// Si la entidad está en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Contacto) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		Entidad eContacto;
		
		eContacto = servPersistencia.recuperarEntidad(codigo);
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		
		
		if(eContacto.getNombre().equals("contacto_individual")) {
			String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
			String codigoUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");
			String imagen = servPersistencia.recuperarPropiedadEntidad(eContacto, "imagen");
			
			ContactoIndividual c = new ContactoIndividual(nombre);
			c.setImagen(imagen);
			c.setCodigo(codigo);
			
			//Metemos el mensaje al pool
			PoolDAO.getUnicaInstancia().addObjeto(codigo, c);
			
			c.setUsuario(adaptadorU.recuperarUsuario(Integer.parseInt(codigoUsuario)));
			
			return c;
		}
		
		if(eContacto.getNombre().equals("grupo")) {
			String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
			String contactos = servPersistencia.recuperarPropiedadEntidad(eContacto, "contactos");
			String imagen = servPersistencia.recuperarPropiedadEntidad(eContacto, "imagen");
			
			Grupo g = new Grupo(nombre, imagen);
			g.setCodigo(codigo);
			
			//Metemos el mensaje al pool
			PoolDAO.getUnicaInstancia().addObjeto(codigo, g);
			
			g.setListaContactos(obtenerContactosDesdeCodigos(contactos));
			
			return g;
			
		}
		return null;
		
	}
	
	public List<Contacto> recuperarTodosContactos(){
		List<Entidad> eContactos = servPersistencia.recuperarEntidades("contacto_individual");
		List<Contacto> contactosInd = new LinkedList<Contacto>();

		for (Entidad eContacto : eContactos) {
			contactosInd.add(recuperarContacto(eContacto.getId()));
		}
		
		eContactos = servPersistencia.recuperarEntidades("grupo");
		
		for (Entidad eContacto : eContactos) {
			contactosInd.add(recuperarContacto(eContacto.getId()));
		}
		
		return contactosInd;
	}
	
	public void inicializarListaContactosGrupo(List<ContactoIndividual> contactos) {
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		for(ContactoIndividual c : contactos) {
			c = new ContactoIndividual(c.getNombre(), adaptadorU.recuperarUsuario(c.getCodigoUsuario()));
		}
	}
	
	/*public List<Contacto> inicializarListaContactos(List<Contacto> contactos) {
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		LinkedList<Contacto> lista = new LinkedList<Contacto>();
		for(Contacto c : contactos) {
			if(c instanceof ContactoIndividual) {
				ContactoIndividual ci = (ContactoIndividual) c;
				ci = new ContactoIndividual(ci.getNombre(), adaptadorU.recuperarUsuario(ci.getCodigoUsuario()));
				lista.add(ci);
			}	else if (c instanceof Grupo) {
				Grupo g = (Grupo) c;
				if(!g.getListaContactos().isEmpty()) {
					g = new Grupo(g.getNombre(), g.getImagen(), g.getListaContactos(), g.getCodigosContactos());
				}	else	{
					g = new Grupo(g.getNombre(), g.getImagen(), g.getCodigosContactos());
				}
				lista.add(g);
			}
		}
		return lista;
	}*/
	// -------------------Funciones auxiliares-----------------------------
			private String obtenerCodigosContactos(List<ContactoIndividual> listaContactos) {
				String aux = "";
				for (ContactoIndividual c : listaContactos) {
					aux += c.getCodigo() + " ";
				}
				return aux.trim();
			}

			private List<ContactoIndividual> obtenerContactosDesdeCodigos(String contactos) {
				List<ContactoIndividual> listaContactos = new LinkedList<ContactoIndividual>();
				StringTokenizer strTok = new StringTokenizer(contactos, " ");
				AdaptadorContactoTDS adaptadorC = AdaptadorContactoTDS.getUnicaInstancia();
				while (strTok.hasMoreTokens()) {
					listaContactos.add((ContactoIndividual) adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
				}
				return listaContactos;
			}

}
