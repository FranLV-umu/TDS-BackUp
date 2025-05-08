package umu.tds.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.persistencia.DAOException;
import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.IAdaptadorMensajeDAO;

public class RepositorioMensajes {
	private Map<Integer,Mensaje> mensajes;
	private static RepositorioMensajes unicaInstancia = new RepositorioMensajes();
	
	private FactoriaDAO dao;
	private IAdaptadorMensajeDAO adaptadorMensajes;
	
	private RepositorioMensajes() {
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorMensajes = dao.getMensajeDAO();
			mensajes = new HashMap<Integer,Mensaje>();
			this.cargarRepositorio();
		}	catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	public static RepositorioMensajes getUnicaInstancia() {
		return unicaInstancia;
	}
	
	public List<Mensaje> getMensajes(){
		ArrayList<Mensaje> lista = new ArrayList<Mensaje>();
		for (Mensaje m:mensajes.values()) 
			lista.add(m);
		return lista;
	}
	
	public Mensaje getMensaje(String numEmisor, String numReceptor, LocalDate fecha) {
		for(Mensaje m:mensajes.values()) {
			if(m.getNumReceptor().equals(numReceptor) && m.getNumEmisor().equals(numEmisor) && m.getFecha().equals(fecha)) return m;
		}
		return null;
	}
	
	//public Usuario getUsuario(String nombre){}
	public void addMensaje(Mensaje m) {
		mensajes.put(m.getCodigo(), m);
	}
	
	public void removeMensaje(Mensaje m) {
		mensajes.remove(m.getCodigo());
	}
	
	private void cargarRepositorio() throws DAOException {
		List<Mensaje> mensajesDB = adaptadorMensajes.recuperarTodosMensajes();
		for(Mensaje m:mensajesDB) {
			mensajes.put(m.getCodigo(), m);
		}
	}
}
