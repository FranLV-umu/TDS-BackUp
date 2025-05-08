package umu.tds.persistencia;

import java.util.List;
import umu.tds.modelo.Mensaje;

public interface IAdaptadorMensajeDAO {

	public void registrarMensaje(Mensaje venta);
	public void borrarMensaje(Mensaje venta);
	public void modificarMensaje(Mensaje venta);
	public Mensaje recuperarMensaje(int codigo);
	public List<Mensaje> recuperarTodosMensajes();
	
}
