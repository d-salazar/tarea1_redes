/*Ejemplo de Servidor Web en Java
 *
 * Autor: Roberto Canales
 * rcanales@adictosaltrabajo.com
 * rcanales@autentia.com
 *
 **/


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class servidorWeb{
	int puerto = 5003;
	
	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;
	
		
	// funcion para centralizar los mensajes de depuracion

	void depura(String mensaje){
		depura(mensaje,DEBUG);
	}	

	void depura(String mensaje, int gravedad){
		System.out.println("Mensaje: " + mensaje);
	}	
		
	// punto de entrada a nuestro programa
	public static void main(String [] array) throws IOException	{
		servidorWeb instancia = new servidorWeb(array);	
		instancia.arranca();
	}
	
	// constructor que interpreta los parameros pasados
	servidorWeb(String[] param){
		procesaParametros();	
	}
	
	// parsearemos el fichero de entrada y estableceremos las variables de clase
	boolean procesaParametros(){
		return true;	
	}
	
	boolean arranca() throws IOException{
		depura("Arrancamos nuestro servidor",DEBUG);
		ServerSocket s = null;
		try{
			s = new ServerSocket(puerto);
			depura("Quedamos a la espera de conexion");
			
			while(true)  // bucle infinito .... ya veremos como hacerlo de otro modo
			{
				Socket entrante = s.accept();
				peticionWeb pCliente = new peticionWeb(entrante);
				pCliente.start();
			}
		}
		catch(Exception e){
			depura("Error en servidor\n" + e.toString());
		}
		finally{
			s.close();
		}
		return true;
	}
}



class peticionWeb extends Thread{
	int contador = 0;

	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;

	private Socket scliente 	= null;		// representa la petición de nuestro cliente
   	private PrintWriter out 	= null;		// representa el buffer donde escribimos la respuesta
   	
   	
	void depura(String mensaje){
		depura(mensaje,DEBUG);
	}	

	void depura(String mensaje, int gravedad){
		System.out.println(currentThread().toString() + " - " + mensaje);
	}	

   	peticionWeb(Socket ps){
		depura("El contador es " + contador);
		
		contador ++;
		
		scliente = ps;
		setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja
   	}

	public void run(){
		depura("Procesamos conexion");

		try{
			BufferedReader in = new BufferedReader (new InputStreamReader(scliente.getInputStream()));
  			out = new PrintWriter(new OutputStreamWriter(scliente.getOutputStream(),"8859_1"),true) ;


			String cadena = "";		// cadena donde almacenamos las lineas que leemos
			int i=0;				// lo usaremos para que cierto codigo solo se ejecute una vez
	
			do{
				cadena = in.readLine();

				if (cadena != null ){
					depura("--" + cadena + "-");
				}

				if(i == 0){
					i++;
			        
			        StringTokenizer st = new StringTokenizer(cadena);
                    
                    if ((st.countTokens() >= 2) && st.nextToken().equals("GET")){
                    	retornaFichero(st.nextToken()) ;
                    }
                    else{
                    	out.println("400 Petición Incorrecta") ;
                    }
				}
				
			}
			while (cadena != null && cadena.length() != 0);

		}
		catch(Exception e){
			depura("Error en servidor\n" + e.toString());
		}
			
		depura("Hemos terminado");
	}
	
	
	void retornaFichero(String sfichero){
		depura("Recuperamos el fichero _" + sfichero + "_");
		
		// comprobamos si tiene una barra al principio
		if (sfichero.startsWith("/")){
			sfichero = sfichero.substring(1) ;
		}
        
        // no especifica direccion retornamos el index.html
        if (sfichero.endsWith("/") || sfichero.equals(""))
        {
        	sfichero = sfichero + "index.html" ;
        }
        
        try{
		    // Ahora leemos el fichero y lo retornamos
		    File mifichero = new File(sfichero) ;
		        
		    if ( mifichero.exists() ){
	      		out.println("HTTP/1.0 200 OK");
				out.println("Server: localhost/1.0");
				out.println("Date: " + new Date());
				//out.println("Content-Type: text/html");
				out.println("Content-Type: " + Files.probeContentType(Paths.get(sfichero)));
				// problema con imagenes
				out.println("Content-Length: " + mifichero.length());
				//out.println("Content-Length: " + Files.size(Paths.get(sfichero)));
				out.println("\n");
			
				BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
				
				
				String linea = "";
				
				do{
					linea = ficheroLocal.readLine();
	
					if ( linea != null ){
						// sleep(500);
						out.println(linea);
					}
				}
				while (linea != null);
				
				depura("fin envio fichero");
				
				ficheroLocal.close();
				out.close();
				
			}  // fin de si el fiechero existe 
			else{
				depura("No encuentro el fichero " + mifichero.toString());	
	      		out.println("HTTP/1.0 400 OK");
	      		out.close();
			}
			
		}
		catch(Exception e)
		{
			depura("Error al retornar fichero");	
		}

	}
}