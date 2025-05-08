package umu.tds.modelo;

import beans.Entidad;

public abstract class Contacto {
	
	private String nombre;
	private int codigo;
	private String imagen;
	
	Contacto(String nombre){
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	//public abstract void creaEntidad(Entidad eContacto);
}
