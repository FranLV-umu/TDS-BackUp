package umu.tds.persistencia;

import java.util.List;
import umu.tds.modelo.Chat;

public interface IAdaptadorChatDAO {

	public void registrarChat(Chat chat);
	public void borrarChat(Chat chat);
	public void modificarChat(Chat chat);
	public Chat recuperarChat(int telefono);
	public List<Chat> recuperarTodosChats();
}
