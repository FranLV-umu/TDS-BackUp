package umu.tds.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.persistencia.DAOException;
import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.IAdaptadorChatDAO;

public class RepositorioChats {
	private Map<String,Chat> Chats;
	private static RepositorioChats unicaInstancia = new RepositorioChats();
	
	private FactoriaDAO dao;
	private IAdaptadorChatDAO adaptadorChat;
	
	private RepositorioChats() {
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorChat = dao.getChatDAO();
			Chats = new HashMap<String,Chat>();
			//this.cargarRepositorio();
		}	catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	public static RepositorioChats getUnicaInstancia() {
		return unicaInstancia;
	}
	
	public List<Chat> getChats(){
		ArrayList<Chat> lista = new ArrayList<Chat>();
		for (Chat u:Chats.values()) 
			lista.add(u);
		return lista;
	}
	
	public Chat getChat(String usr1, String usr2) {
		Chat chat = Chats.get(usr1+usr2);
		if(chat == null) {
			chat = Chats.get(usr2+usr1);
		}	else	{
			return chat;
		}
		if(chat == null) {
			return null;
		}	else	{
			return chat;
		}
	}
	/*
	//public Chat getChat(String nombre){}
	public void addChat(Chat u) {
		Chats.put(u.getUsr1()+u.getUsr2(), u);
	}
	
	public void removeChat(Chat u) {
		Chats.remove(u.getUsr1()+u.getUsr2());
	}
	
	private void cargarRepositorio() throws DAOException {
		List<Chat> ChatsDB = adaptadorChat.recuperarTodosChats();
		for(Chat u:ChatsDB) {
			System.out.println(u.toString());
			Chats.put(u.getUsr1()+u.getUsr2(), u);
		}
	}*/
}
