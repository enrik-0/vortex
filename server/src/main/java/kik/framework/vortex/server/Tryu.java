package kik.framework.vortex.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.sound.sampled.Line;
/**
 * Hello world!
 *
 */
public class Tryu {
	public static void main(String[] args) throws IOException {
		String line;
		byte count = 0;
		List<String> p = new ArrayList<String>();
		try {
			// Crea un servidor en el puerto 8080
			ServerSocket serverSocket = new ServerSocket(8080);
			System.out.println("Servidor HTTP escuchando en el puerto 8080...");

			while (true) {
				// Espera a que un cliente se conecte
				Socket clientSocket = serverSocket.accept();

				// Lee la solicitud del cliente
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				String requestLine = reader.readLine();

				

				// Procesa la solicitud para obtener el método y la ruta
				if (requestLine != null) {
					String[] requestParts = requestLine.split(" ");
					String method = requestParts[0];
					String path = requestParts[1];

					// Prepara la respuesta
					String response = "HTTP/1.1 200 OK\r\n"
							+ "Server: SimpleHTTPServer\r\n" + "Date: "
							+ LocalDate.now() + "\r\n"
							+ "Content-Type: text/plain\r\n" + "\r\n";

					// Agrega información sobre la solicitud a la respuesta
					response += "Método: " + method + "\r\n";
					response += "Ruta: " + path + "\r\n";
					response += "Mensaje: Hello World!\r\n";
					// Envía la respuesta al cliente
					OutputStream outputStream = clientSocket.getOutputStream();
					outputStream
							.write(response.getBytes(StandardCharsets.UTF_8));
					outputStream.flush();
				}

				// Cierra la conexión con el cliente
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void print(String l) {
		System.out.println(l);
	}
	// AnnotationManager.getInstance();
	// ServerHttp.runServer(8080);
}
