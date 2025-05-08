package umu.tds.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.controlador.ControladorAppChat;
import umu.tds.hash.Hash;
import umu.tds.persistencia.DAOException;
import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.IAdaptadorUsuarioDAO;

public class RepositorioUsuarios {
	private Map<String,Usuario> usuarios;
	private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	private RepositorioUsuarios() {
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = dao.getUsuarioDAO();
			usuarios = new HashMap<String,Usuario>();
			this.cargarRepositorio();
		}	catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	
	public static RepositorioUsuarios getUnicaInstancia() {
		return unicaInstancia;
	}
	
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u:usuarios.values()) 
			lista.add(u);
		return lista;
	}
	
	public Usuario getUsuario(String telefono) {
		for(Usuario u:usuarios.values()) {
			if(u.getTelefono().equals(telefono)) return u;
		}
		return null;
	}
	
	//public Usuario getUsuario(String nombre){}
	public void addUsuario(Usuario u) {
		usuarios.put(u.getTelefono(), u);
	}
	
	public void removeUsuario(Usuario u) {
		usuarios.remove(u.getTelefono());
	}
	
	private void cargarRepositorio() throws DAOException {
		List<Usuario> usuariosDB = adaptadorUsuario.recuperarTodosUsuarios();
		for(Usuario u:usuariosDB) {
			if(ControladorAppChat.getUnicaInstancia().debug)	System.out.println(u.toString() + "\t" + Hash.descifrarContraseña(u.getContraseña()));
			usuarios.put(u.getTelefono(), u);
		}
	}
}
