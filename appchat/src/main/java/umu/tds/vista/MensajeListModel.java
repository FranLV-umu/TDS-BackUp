package umu.tds.vista;

import javax.swing.*;

import umu.tds.modelo.Mensaje;

import java.util.LinkedList;
import java.util.List;

public class MensajeListModel extends AbstractListModel<Mensaje> {
    private static final int CARGA_INICIAL = 20; // Cantidad de mensajes cargados inicialmente
    private static final int CARGA_INCREMENTAL = 10; // Cantidad de mensajes cargados al desplazar
    private List<Mensaje> mensajes;
    private int mensajesCargados;

    public MensajeListModel(List<Mensaje> todosMensajes) {
        this.mensajes = new LinkedList<>(todosMensajes);
        this.mensajesCargados = Math.min(CARGA_INICIAL, mensajes.size());
    }

    @Override
    public int getSize() {
        return mensajesCargados;
    }

    @Override
    public Mensaje getElementAt(int index) {
        return mensajes.get(mensajes.size() - mensajesCargados + index);
    }

    public void cargarMasMensajes() {
        int nuevosMensajes = Math.min(mensajesCargados + CARGA_INCREMENTAL, mensajes.size());
        if (nuevosMensajes > mensajesCargados) {
            int prevCargados = mensajesCargados;
            mensajesCargados = nuevosMensajes;
            fireIntervalAdded(this, 0, mensajesCargados - prevCargados - 1);
        }
    }

    public boolean hayMasMensajes() {
        return mensajesCargados < mensajes.size();
    }
}
