package umu.tds.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;

public class Grupo extends Contacto {
	
	private List<ContactoIndividual> listaContactos = null;

	public Grupo(String nombre, String imagen){
		super(nombre);
		super.setImagen(imagen);
		this.listaContactos = new LinkedList<ContactoIndividual>();
	}
	
	public Grupo(String nombre, String imagen, List<ContactoIndividual> contactos){
		super(nombre);
		super.setImagen(imagen);
		this.listaContactos = contactos;
	}
	
	@Override
	public int getCodigo() {
		return super.getCodigo();
	}
	
	@Override
	public void setCodigo(int codigo) {
		super.setCodigo(codigo);
	}
	
	@Override
	public void setNombre(String nombre) {
		super.setNombre(nombre);
	}
	
	public void setListaContactos(List<ContactoIndividual> listaContactos) {
		this.listaContactos = listaContactos;
	}
	
	void crearGrupo() {
	}
	
	void addContacto(ContactoIndividual contacto) {
		if(this.listaContactos == null) {
			this.listaContactos = new LinkedList<ContactoIndividual>();
		}
		listaContactos.add(contacto);
		// Registrar contacto
	}
	
	public List<ContactoIndividual> getListaContactos() {
		return listaContactos;
	}

	public String getNombre() {
		return super.getNombre();
	}

}
