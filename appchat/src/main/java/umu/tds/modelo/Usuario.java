package umu.tds.modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import umu.tds.controlador.ControladorAppChat;

public class Usuario {

	private String nombre;
	private String apellidos;
	private String contraseña;
	private String telefono;
	private Date fechaDeNacimiento;
	// ListaContactos
	private String imagen;
	private String saludo;
	private String email;
	private int codigo;
	
	// Contactos
	private List<Contacto> contactos = null;
	
	// Mensajes
	private List<Chat> chats = null;
	
	
	public Usuario(String usuario, String apellidos, String contraseña, String telefono, Date fechaDeNacimiento, String imagen,
			String saludo, String email) {
		this.nombre = usuario;
		this.apellidos = apellidos;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.fechaDeNacimiento = fechaDeNacimiento;
		this.imagen = imagen;
		this.saludo = saludo;
		this.email = email;
		this.codigo = 0;
		this.chats = new LinkedList<Chat>();
		this.contactos = new LinkedList<Contacto>();
		
	}
	
	public Usuario(String usuario, String apellidos, String contraseña, String telefono, Date fechaDeNacimiento, String imagen,
			String saludo, String email, List<Chat> chats, List<Contacto> contactos, int codigo) {
		this(usuario, apellidos, contraseña, telefono, fechaDeNacimiento, imagen, saludo, email);
		this.chats = chats;
		this.contactos = contactos;
		this.codigo = codigo;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	Contacto getContactoIndividual(Usuario otroUsuario) {
		// Retorna el contacto que es otroUsuario, si lo hay.
		Optional<Contacto> contacto= this.contactos.stream()
										.filter(c -> c.equals(otroUsuario))
										.findFirst();
		if(contacto.isPresent()) {
			return contacto.get();
		}
		// Si no lo hay, retorna null.
		return null;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public List<Contacto> getContactos() {
		return contactos;
	}
	
	public String getContraseña() {
		return contraseña;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Date getFechaDeNacimiento() {
		return fechaDeNacimiento;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public String getSaludo() {
		return saludo;
	}
	
	public String getTelefono() {
		return new String(telefono);
	}
	
	public List<Chat> getChats() {
		return chats;
	}
	
	public boolean comprobarContraseña(String contraseña) {
		return contraseña.equals(this.contraseña);
	}
	
	public void addChat(Chat chat) {
		if(chats == null) {
			chats = new LinkedList<Chat>();
		}
		chats.add(chat);
		ControladorAppChat.getUnicaInstancia().registrarChat(chat);
	}
	
	public Contacto addContacto(String nombre, Usuario u) {
		if(contactos == null) {
			contactos = new LinkedList<Contacto>();
		}
		ContactoIndividual contacto = new ContactoIndividual(nombre, u);
		contactos.add(contacto);
		ControladorAppChat.getUnicaInstancia().registrarContacto(contacto);
		return contacto;
	}
	
	public void addContacto(Contacto contacto) {
		if(contactos == null) {
			contactos = new LinkedList<Contacto>();
		}
		contactos.add(contacto);
		ControladorAppChat.getUnicaInstancia().registrarContacto(contacto);
	}
	
	public boolean hasContacto(Usuario u) {
		if(this.contactos.isEmpty()) {
			return false;
		}
		
		boolean contacto = this.contactos.stream()
			.filter(c -> c instanceof ContactoIndividual)
			.map(c -> (ContactoIndividual) c)
			.anyMatch(c -> c.getUsuario().equals(u));
		return contacto;
	}
	
	public void addGrupo(String nombre, String imagen, List<Contacto> lista) {
		if(contactos == null) {
			contactos = new LinkedList<Contacto>();
		}
		// TODO pasar de Contacto a ContactoIndividual
		List<ContactoIndividual> list = lista.stream()
											.map(c -> (ContactoIndividual) c)
											.collect(Collectors.toList());
		Grupo contacto = new Grupo(nombre, imagen, list);
		contactos.add(contacto);
		ControladorAppChat.getUnicaInstancia().registrarContacto(contacto);
	}
	
	
	public Chat buscarChat(Usuario u) {
		for (Chat c : chats) {
			if(c.getUsr1().equals(u) && c.getUsr2().equals(this) || 
					c.getUsr1().equals(this) && c.getUsr2().equals(u)) {
				return c;
			}
		}
		return null;
	}
	
	public List<Mensaje> getUltimoMensajePorUsuario() {
		if(this.chats == null || this.chats.isEmpty()) {
			return new LinkedList<Mensaje>();
		}
		List<Mensaje> ultMensajes = new LinkedList<Mensaje>();
		for(Chat c : chats) {
			ultMensajes.add(c.getUltimoMensaje());
		}
		return ultMensajes;
	}
	
	public Mensaje enviarMensaje(Usuario u, String mensaje) {
		Optional<Chat> chat = this.chats.stream()
			.filter(c -> c.getUsr1().equals(u) || c.getUsr2().equals(u))
			.findFirst();
		Mensaje m = null;
		if(chat.isPresent()) {
			m = chat.get().enviarMensaje(this, u, mensaje);
		}
		else {
			Chat c = new Chat(u, this);
			ControladorAppChat.getUnicaInstancia().registrarChat(c);
			this.addChat(c);
			u.addChat(c);
			m = c.enviarMensaje(this, u, mensaje);
			ControladorAppChat.getUnicaInstancia().modificarUsuario();
			ControladorAppChat.getUnicaInstancia().modificarUsuario(u);
		}
		return m;
	}
	
	public String toString() {
		return new String(this.getNombre()+" "+this.getApellidos()+" "+this.getCodigo()+" "+this.getContraseña()+" "+this.getTelefono());
	}
	
}
