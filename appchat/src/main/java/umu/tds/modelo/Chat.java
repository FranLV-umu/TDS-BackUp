package umu.tds.modelo;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import umu.tds.controlador.ControladorAppChat;

public class Chat{
	private int codigo;
	private Usuario usr1, usr2;
	private List<Mensaje> mensajes = null;
	
	public Chat() {
		this.codigo = 0;
		mensajes = new LinkedList<Mensaje>();
	}
	
	public Chat(Usuario usr1, Usuario usr2) {
		this.usr1 = usr1;
		this.usr2 = usr2;
		this.codigo = 0;
		mensajes = new LinkedList<Mensaje>();
	}
	
	public Chat(Usuario usr1, Usuario usr2, int codigo, List<Mensaje> mensajes) {
		this.usr1 = usr1;
		this.usr2 = usr2;
		this.codigo = 0;
		this.mensajes = mensajes;
	}
	
	public Usuario getUsr1() {
		return usr1;
	}
	
	public Usuario getUsr2() {
		return usr2;
	}
	
	public int getCodigoUsr1() {
		return usr1.getCodigo();
	}
	
	public int getCodigoUsr2() {
		return usr2.getCodigo();
	}
	
	public String getNumUsr1(){
		return usr1.getTelefono();
	}
	
	public String getNumUsr2(){
		return usr2.getTelefono();
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public List<Mensaje> getMensajes() {
		return mensajes;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public void setUsrs(Usuario usr1, Usuario usr2) {
		this.usr1 = usr1;
		this.usr2 = usr2;
	}
	
	public void addMensaje(Mensaje mensaje) {
		if(this.mensajes == null) {
			this.mensajes = new LinkedList<Mensaje>();
		}
		mensajes.add(mensaje);
	}
	
	public Mensaje buscarMensaje(String texto) {
		for(Mensaje m : mensajes) {
			if(m.getTexto().contains(texto)) {
				return m;
			}
		}
		return null;
	}
	
	public List<Mensaje> buscarMensajes(String texto) {
		List<Mensaje> lista = new LinkedList<Mensaje>();
		for(Mensaje m : mensajes) {
			if(m.getTexto().contains(texto)) {
				lista.add(m);
			}
		}
		if(lista.isEmpty())
			return null;
		return lista;
	}
	
	public Mensaje buscarMensaje(Date fecha) {
		for(Mensaje m : mensajes) {
			if(m.getFecha().equals(fecha)) {
				return m;
			}
		}
		return null;
	}
	
	public List<Mensaje> buscarMensajes(Date fecha) {
		List<Mensaje> lista = new LinkedList<Mensaje>();
		for(Mensaje m : mensajes) {
			if(m.getFecha().equals(fecha)) {
				lista.add(m);
			}
		}
		if(lista.isEmpty())
			return null;
		return lista;
	}
	
	public Mensaje getUltimoMensaje(){
		Optional<Mensaje> mensaje =  mensajes.stream()
										.sorted(Comparator.comparing(Mensaje::getFecha).reversed())
										.findFirst();
		if(mensaje.isEmpty()) {
			return null;
		}
		
		return mensaje.get();
	}
	
	public Mensaje enviarMensaje(Usuario emisor, Usuario receptor, String texto) {
		Mensaje m = new Mensaje(emisor, receptor, texto, new Date(), 0);
		this.mensajes.add(m);
		//TODO ENVIAR (hilo)
		ControladorAppChat.getUnicaInstancia().registrarMensaje(m);
		ControladorAppChat.getUnicaInstancia().modificarChat(this);
		return m;
		
	}

}
