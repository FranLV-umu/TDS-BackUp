package umu.tds.persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {
	public TDSFactoriaDAO () {
	}
	
	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		return AdaptadorMensajeTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}
	
	@Override
	public IAdaptadorChatDAO getChatDAO() {
		return AdaptadorChatTDS.getUnicaInstancia();
	}
	
	@Override
	public IAdaptadorContactoDAO getContactoDAO() {
		return AdaptadorContactoTDS.getUnicaInstancia();
	}

}
