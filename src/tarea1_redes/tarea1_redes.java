package tarea1_redes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class tarea1_redes {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Usuarios.Reader();
		
		ServerSocket s = null;
		int puerto = 8080;
		try{
			s = new ServerSocket(puerto);
			s.setReuseAddress(true);
		}
		catch (IOException e){
			System.err.println("No ha sido posible conectar con el puerto "+puerto);
			System.err.println( e.getMessage() );
			System.exit(1);
		}
		
		Socket c = null;
		
		try{
			c = s.accept();
			if( c != null ){
				System.out.println("Conectado!");
			}
		}
		catch( IOException e ){
			System.err.println("El cliente no ha aceptado la conexion");
			System.err.println( e.getMessage() );
			System.exit(1);
		}
		
		PrintWriter out = new PrintWriter( c.getOutputStream() );
		
		final String vista;
		 vista = "<html>"
		         + "<head>"
		         + "     <title>Avioncito de Papel</title>"
		         + "</head>"
		         + "<body>"
		         + "     <h1>VISTA 1</h1>"
		         + "     <p>ASDF</p>"
		         + "</body>"
		         + "</html>";
		 
		 
		 out.println("GET HTTP/1.1");
		 out.println("Content-Type: text/html");
		 out.flush();
		 
		 out.println(vista);
		 out.println("");
		 
		 out.close();
		 c.close();
		 s.close();
	 }

}
