package umu.tds.controlador;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import umu.tds.hash.Hash;
import umu.tds.hash.ImageValidator;
import umu.tds.main.Lanzador;
import umu.tds.modelo.Chat;
import umu.tds.modelo.Contacto;
import umu.tds.modelo.ContactoIndividual;
import umu.tds.modelo.Grupo;
import umu.tds.modelo.Mensaje;
import umu.tds.modelo.RepositorioUsuarios;
import umu.tds.modelo.Usuario;
import umu.tds.persistencia.DAOException;
import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.IAdaptadorChatDAO;
import umu.tds.persistencia.IAdaptadorContactoDAO;
import umu.tds.persistencia.IAdaptadorMensajeDAO;
import umu.tds.persistencia.IAdaptadorUsuarioDAO;


public class ControladorAppChat 
{
	
	private static ControladorAppChat unicaInstancia;
	
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorChatDAO adaptadorChat;
	private IAdaptadorContactoDAO adaptadorContacto;
	
	private RepositorioUsuarios repositorioUsuarios;
		
	private Usuario usuarioActual = null;
	private Boolean modoColor = true;
	
	public Boolean debug = true;
	
	ControladorAppChat(){
		inicializarAdaptadores();
		inicializarRepositorios();
		
	}
	
	public static ControladorAppChat getUnicaInstancia() {
		if(unicaInstancia == null)
			unicaInstancia = new ControladorAppChat();
		return unicaInstancia;
	}

	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			System.err.println("No se ha encontrado el servidor de persistencia.\n");
			System.exit(1);
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorMensaje = factoria.getMensajeDAO();
		adaptadorChat = factoria.getChatDAO();
		adaptadorContacto = factoria.getContactoDAO();
	}
	
	private void inicializarRepositorios() {
		repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}
	
	/*
	 * Comprueba que no exista un usuario con ese teléfono
	 * y luego se comprueba si el resto de elementos cumplen
	 * con ciertos requisitos de la aplicación.
	 */
	public void registrarUsuario(String nombre, String apellidos, String telefono, String contraseña,
			Date bday, String contraseñaConfirmada, String saludo, String mail, String URL) {
		// TODO no crear el usuario aquí
		Usuario usuario = new Usuario(nombre, apellidos, Hash.cifrarContraseña(contraseña), telefono, bday, URL, saludo, mail);
		adaptadorUsuario.registrarUsuario(usuario);
		repositorioUsuarios.addUsuario(usuario);
	}
	
	/*
	 * USUARIO ACTUAL
	 */
	
	private void inicializarUsuarioActual(Usuario usuario) {
		this.usuarioActual = usuario;
	}
	
	public String getNombreUsuarioActual() {
		return this.usuarioActual.getNombre();
	}
	
	public String getNumUsuarioActual() {
		return this.usuarioActual.getTelefono();
	}
	
	public String getImgUsuarioActual() {
		return this.usuarioActual.getImagen();
	}
	
	public List<String> getNombreContactosUsuarioActual(){
		return this.usuarioActual.getContactos().stream()
			.map(contacto -> contacto.getNombre())
			.collect(Collectors.toList());
	}
	
	public List<Contacto> getContactosUsuarioActual(){
		return this.usuarioActual.getContactos();
	}
	
	public Usuario getUsuarioActual() {
		return this.usuarioActual;
	}
	
	public List<Contacto> getContactosSinChatUsuarioActual(){
		return this.usuarioActual.getContactos().stream()
				.filter(contacto -> contacto instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.filter(contacto -> contacto != null && this.getChat(contacto.getUsuario()) == null)
				.collect(Collectors.toList());
		/*
		List<Contacto> contactosSinChat = this.usuarioActual.getContactos().stream()
	            .filter(contacto -> contacto instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .filter(contacto -> this.getChat(contacto.getUsuario()) == null)
	            .collect(Collectors.toList());

	    List<Contacto> grupos = this.usuarioActual.getContactos().stream()
	            .filter(contacto -> contacto instanceof Grupo)
	            .collect(Collectors.toList());

	    contactosSinChat.addAll(grupos);
	    return contactosSinChat;
	    */
	}
	
	public boolean usuarioExistente(String telefono) {
		return repositorioUsuarios.getUsuario(telefono) != null;
	}
	
	public void modificarUsuario() {
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	public void modificarUsuario(Usuario u) {
		adaptadorUsuario.modificarUsuario(u);
	}
	
	/*
	 * Mensajes
	 */
	public void registrarMensaje(Usuario emisor, Usuario receptor, String texto, Date fecha, int emoji) {
		// TODO no crear el mensaje aquí
		Mensaje mensaje = new Mensaje(emisor, receptor, texto, fecha, emoji);
		adaptadorMensaje.registrarMensaje(mensaje);
		//repositorioMensajes.addMensaje(mensaje);
	}
	
	public void registrarMensaje(Mensaje mensaje) {
		adaptadorMensaje.registrarMensaje(mensaje);
	}
	
	public List<Mensaje> getUltimoMensajePorUsuario() {
		return usuarioActual.getUltimoMensajePorUsuario();
	}
	
	public Optional<Mensaje> getUltimoMensajedeUsuario(Usuario u) {
		Optional<Mensaje> m = this.usuarioActual.getChats().stream()
								.filter( chat -> chat.getNumUsr1().equals(u) || chat.getNumUsr2().equals(u))
								.map( msj -> msj.getUltimoMensaje())
								.findFirst();
		return m;
	}
	
	public Optional<List<Mensaje>> getMensajesdeUsuario(Usuario u){
		List<Mensaje> mensajes = this.usuarioActual.getChats().stream()
				.filter( chat -> chat.getNumUsr1().equals(u.getTelefono()) || chat.getNumUsr2().equals(u.getTelefono()))
				.flatMap( msj -> msj.getMensajes().stream() )
				.collect(Collectors.toList());
		return mensajes.isEmpty() ? Optional.empty() : Optional.of(mensajes);
	}
	
	public Mensaje enviarMensaje(Usuario u, String mensaje) {
		return this.usuarioActual.enviarMensaje(u, mensaje);
	}
	
	public Mensaje enviarMensaje(String telefono, String mensaje) {
		Usuario u = repositorioUsuarios.getUsuario(telefono);
		return this.usuarioActual.enviarMensaje(u, mensaje);
	}
	
	public boolean isUsuarioActual(Usuario u) {
		return u.equals(usuarioActual);
	}
	
	
	/*
	 * Chats
	 */
	
	public void registrarChat(Chat chat) {
		adaptadorChat.registrarChat(chat);
	}
	
	public void modificarChat(Chat chat) {
		adaptadorChat.modificarChat(chat);
	}
	
	public Chat getChat(Usuario u) {
		return this.usuarioActual.buscarChat(u);
	}
	
	/*
	 * Contactos
	 */
	
	// Desde ventana
	public Object addContacto(String nombre, String tlf) {
		Usuario u = this.repositorioUsuarios.getUsuario(tlf);
		if(u == null)
			return 1;
		else if(this.usuarioActual.getTelefono().equals(tlf))
			return 2;
		else if(this.usuarioActual.hasContacto(u))
			return 3;
		Contacto c = this.usuarioActual.addContacto(nombre, u);
		this.modificarUsuario();
		return c;
	}
	
	public void addContacto(Contacto contacto) {
		this.usuarioActual.addContacto(contacto);
		this.modificarUsuario();
	}
	
	public void addGrupo(String nombre, String imagen, List<Contacto> lista) {
		this.usuarioActual.addGrupo(nombre, imagen, lista);
		this.modificarUsuario();
	}
	
	public void registrarContacto(Contacto contacto) {
		adaptadorContacto.registrarContacto(contacto);
	}
	
	/*
	 * Comprueba si existe un usuario con ese teléfono
	 * y luego se comprueba si la contraseña es correcta.
	 */
	public boolean login(String telefono, String contraseña) {
		Usuario u = repositorioUsuarios.getUsuario(telefono);
		if(u == null) {
			if(debug)System.err.println("-> No user found in the db.");
			return false;
		}
		if(u.comprobarContraseña(Hash.cifrarContraseña(contraseña)) == true) {
			inicializarUsuarioActual(u);
			return true;
		}
		if(debug)System.err.println("-> Password not matches.");
		return false;
	}
	
	public Optional<ContactoIndividual> buscarContactoPorNum(String telefono) {
		return this.usuarioActual.getContactos().stream()
				.filter(contacto -> contacto instanceof ContactoIndividual)
				.map(contacto -> (ContactoIndividual) contacto)
				.filter(contactoIndividual -> contactoIndividual.getTelefono().equals(telefono))
				.findFirst();
	}
	
	public boolean comprobarImagen(String imagen) {
		return ImageValidator.validarImagenURL(imagen);
	}
	
	public boolean comprobarTelefono(String telefono) {
		return (!telefono.isEmpty()) && telefono.matches("\\d+") && telefono.length() == 9;
	}
	
	public Boolean getModoColor() {
		return modoColor;
	}
	
	public void setModoColor(Boolean modoColor) {
		this.modoColor = modoColor;
	}
	
	public void cambiarModo() {
		Lanzador.cambiarModo();
	}
}
