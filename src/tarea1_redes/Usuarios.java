package tarea1_redes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
 
 
public class Usuarios {
	final static String filename = "usuarios.txt";
   
    static void Reader() throws IOException{
       
        System.out.println("Working Directory = "+ System.getProperty("user.dir"));
       
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String line = br.readLine();
            String[] data;
           
            while (line != null) {
                data = line.split("\\|",3);
               
                System.out.println(">"+data[0]+"_"+data[1]+"_"+data[2]);
                line = br.readLine();
            }
           
        }
        finally {
            br.close();
        }
    }
    
    static void Writer(String N, String I, String P) throws FileNotFoundException{
    	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
    		out.println(N+"|"+I+"|"+P);
    	}catch (IOException e) {
    	    System.err.println( e.getMessage() );
    	}
    }
}