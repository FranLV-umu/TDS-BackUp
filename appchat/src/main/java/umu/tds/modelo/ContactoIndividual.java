package umu.tds.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;

public class ContactoIndividual extends Contacto{

	private Usuario usuario;
	
	public ContactoIndividual(String nombre, Usuario usuario) {
		super(nombre);
		this.usuario = usuario;
		super.setImagen(usuario.getImagen());
	}
	
	public ContactoIndividual(String nombre){
		super(nombre);
		this.usuario = null;
	}
	
	@Override
	public String getNombre() {
		return super.getNombre();
	}
	
	@Override
	public int getCodigo() {
		return super.getCodigo();
	}
	
	public String getTelefono(){
		return usuario.getTelefono();
	}
	
	public int getCodigoUsuario() {
		return usuario.getCodigo();
	}
	
	public String getSaludo(){
		return usuario.getSaludo();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
