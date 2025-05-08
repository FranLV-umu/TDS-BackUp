package umu.tds.modelo;

import java.util.Date;
import java.util.List;

public class Mensaje {
	
	private String texto;
	private Usuario emisor;
	private Usuario receptor;
	private Date fecha;
	private int emoji;
	private int codigo;
	
	public Mensaje(Usuario emisor, Usuario receptor, String texto, Date fecha, int emoji) {
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.fecha = fecha;
		this.emoji = emoji;
		this.codigo = 0;
	}
	
	public Mensaje(String texto, Date fecha, int emoji) {
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.fecha = fecha;
		this.emoji = emoji;
		this.codigo = 0;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public Usuario getReceptor() {
		return receptor;
	}
	
	public String getNumReceptor() {
		return receptor.getTelefono();
	}
	
	public String getNumEmisor() {
		return emisor.getTelefono();
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public int getEmoji() {
		return emoji;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	public void setReceptor(Usuario receptor) {
		this.receptor = receptor;
	}
	
	void enviarMsj() {
		
	}
	
	void filtrarTexto() {
		
	}
	
	void filtrarTlfn() {
		
	}
	
	void filtrarNombre() {
		
	}
}
