package umu.tds.hash;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import umu.tds.controlador.ControladorAppChat;

public class ImageValidator {
	// Patrón para validar la extensión de una imagen
    private static final Pattern IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)");
    private static boolean debug = ControladorAppChat.getUnicaInstancia().debug; 

    public static boolean validarImagenURL(String urlImagen) {
        try {
            // Comprobar si la URL tiene un formato válido
            URL url = new URL(urlImagen);

            // Validar si termina con una extensión de imagen
            if (!IMAGE_PATTERN.matcher(urlImagen).matches()) {
                if(debug) System.err.println("-> La URL no tiene una extensión de imagen válida.");
                return false;
            }

            // Intentar conectar con la URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD"); // Solo obtener metadatos
            connection.setConnectTimeout(5000);  // Tiempo de espera de conexión
            connection.setReadTimeout(5000);     // Tiempo de espera de lectura

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
            	if(debug) System.err.println("-> La URL no es accesible. Código de respuesta: " + responseCode);
                return false;
            }

            // Comprobar el tipo de contenido
            String contentType = connection.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
            	if(debug) System.err.println("-> El contenido no es una imagen. Tipo: " + contentType);
                return false;
            }

            // La URL es válida y apunta a una imagen
            return true;

        } catch (IOException e) {
            if(debug) System.err.println("-> Error al intentar validar la URL: " + e.getMessage());
            return false;
        }
    }
}
