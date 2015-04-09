/** JORGE FIGUEROLA - PR�CTICA 2 PSP 2� EVALUACI�N **/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ClienteFTP extends Thread{
	// Declaramos variables que utilizaremos para interacturar con la interfaz
	private String host,user,pass,ruta_fichero_subida, destino_descarga, email;
	
	//TextArea que mostrar� en la interfaz los mensajes de LOG que le asignemos en esta clase
	public JTextArea log = new JTextArea("Ventana LOG:\n");
	//JCombox que mostrar�n en la interfaz los items que a�adamos en esta clase en el m�todo listar
	public JComboBox<String> combodirectorios = new JComboBox<String>();
	public JComboBox<String> comboarchivos=new JComboBox<String>();
	private int puerto;//Puerto
	public boolean conectado=false;//Con esta variable vamos a controlar si la conexi�n se realiza correctamente
					  // la usaremos para interactuar con la GUI y que sepa si tiene que activar los botones o no

	//Conexion
	private FTPClient cliente;
	// C�digo de respuesta
	int reply;
	
	//Variables para subida de fichero
    private File fichero_subida;
    private InputStream inputStream;
    
    //Variables para descargar fichero
    private File fichero_descarga;
    private OutputStream outputStream;
	
    //Creamos instancia de EnviarMail para pasarle datos
    private EnviarMail envio_correo = new EnviarMail();
    
    //M�todo para establecer la conexi�n
	public void conectar_Servidor () throws Exception{
	try {
		redirectSystemStreams();//Para agregar los mensajes de la consola al log
		cliente=new FTPClient();
		cliente.connect(host,puerto);
		reply=cliente.getReplyCode();
		System.out.println("Respuesta del servidor:"+reply);
		// FTPReply contiene las respuestas a los c�digos del servidor FTP
	    
		if (!FTPReply.isPositiveCompletion(reply)) {
			cliente.disconnect();
			throw new Exception("Excepci�n conectando al servidor FTP ("+host+":"+puerto+")");
		}
	// Nos identificamos en el Servidor FTP
		if(cliente.login(user,pass)){
			System.out.println("Conectado correctamente...");
			listarFicheros();//Llamamos a m�todo listar ficheros
			mostrarLog();
			prepararServidor();
			conectado=true;
			
		}else{
			System.out.println("Password o Usuario incorrectos...");
			JOptionPane.showMessageDialog(null, "Password o Usuario incorrectos...");
			conectado=false;//pasamos a false y cerramos conexi�n
			desconectar();
		}
	} catch (SocketException e) {
		System.out.println("Error al crear el socket.");
		JOptionPane.showMessageDialog(null, "No ha podido realizarse la conexi�n revise los par�metros...");
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("Error de I/O.");
		JOptionPane.showMessageDialog(null, "No ha podido realizarse la conexi�n revise los par�metros...");
		e.printStackTrace();
	}
	
}

	/**M�todo para listar ficheros **/
	public FTPFile[] listarFicheros() {
		FTPFile archivos[] = null;
		try {
			System.out.println("LISTADO DE FICHEROS de "
			+ cliente.printWorkingDirectory()
			+ "\n*******************************");
			archivos = cliente.listFiles();
			for (FTPFile a : archivos) {
				System.out.println(((a.isDirectory()) ? "Dir" : "Fil") + " ->"+ a.getName());
				if (a.isFile()) { //Si listamos un fichero lo a�adimos al combo de ficheros
			        comboarchivos.addItem(a.getName());
			    } else if (a.isDirectory()) {//Si es un directorio al combo de directorios
			    	combodirectorios.addItem(a.getName());
			    }
			}
			System.out.println("*******************************\n");
			} catch (IOException e) {
			System.out.println("Imposible listar ficheros");
			e.printStackTrace();
			}
			return archivos;
	}
	
	
	/**M�todo para preparar el env�o o descarga  ficheros **/
	private void prepararServidor () throws IOException{
		// Ponemos la conexi�n del cliente en modo Pasivo.
		// Es mas seguro ya que las conexiones se inician desde el Cliente
		// y si hay un firewall nos ahorramos problemas
		cliente.enterLocalPassiveMode();
		// Preparamos el servidor para el env�o de ficheros binarios
		cliente.setFileType(FTPClient.BINARY_FILE_TYPE);
	}
	
	/**M�todo para subir ficheros **/
	public void subirFicheros () throws IOException{
		//Para poder subir un fichero usaremos el m�todo storeFile del objeto FTPClient, este
		//m�todo recibe el nombre del fichero con el que ser� guardado en el servidor y un
		//InputStream que son los datos que leemos en local y enviamos al servidor:

		fichero_subida = new File(ruta_fichero_subida);
		String nombre_archivo = ruta_fichero_subida.substring(ruta_fichero_subida.lastIndexOf('\\')+1);
	    inputStream = new FileInputStream(fichero_subida);
		boolean correcto = cliente.storeFile(nombre_archivo, inputStream);
        if (correcto) {
            System.out.println("Fichero subido a servidor con �xtio.");
            envio_correo.setEmail(email);
            envio_correo.setMensaje("El fichero: "+ nombre_archivo+" ha sido subido al servidor satisfactoriamente.");
            envio_correo.enviarMail();
        }
        
        //Cerramos flujos de datos
        inputStream.close();

	}
	
	/**M�todo para descargar  ficheros**/
	public void descargarFicheros () throws IOException{
		//Para poder descargar un fichero usaremos el m�todo retrieveFile del objeto FTPClient,
		//este m�todo recibe el nombre del fichero que guardaremos en local y un OutputStream
		//para recibir datos del servidor:
		String nombre_archivo= (String)comboarchivos.getSelectedItem();
	    fichero_descarga = new File(destino_descarga+nombre_archivo);
	    outputStream = new BufferedOutputStream(new FileOutputStream(fichero_descarga));
		boolean correcto = cliente.retrieveFile(nombre_archivo, outputStream);//Declaramos booleano para comprobar que se ha descargado el archivo
        if (correcto) {
            System.out.println("Archivo descargado correctamente.");//Mostramos mensaje
        	envio_correo.setEmail(email);
            envio_correo.setMensaje("El fichero: "+ nombre_archivo+" ha sido descargado satisfactoriamente.");
            envio_correo.enviarMail();
        }
        //Cerramos flujo de datos
        outputStream.close();
	}

	/**M�todo para mostrar_log**/
	private void mostrarLog () throws IOException{
		// A�adimos un Listener para los mensajes del servidor y mostrarlos por la salida estandar
		cliente.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**M�todo para cambiar el direcorio**/
	public void cambiarDirectorio () throws IOException{
		// Cambiamos al directorio FTP
		String carpeta= (String)combodirectorios.getSelectedItem();
		cliente.changeWorkingDirectory(carpeta);
		System.out.println("Cambio de carpeta realizado con �xito, ahora mismo se encuentra en la carpeta "+carpeta+ " del servidor");
		combodirectorios.removeAllItems(); //Se limpian los items antes de llenar los combos nuevamente
		comboarchivos.removeAllItems();
		listarFicheros();
	}
	
	/**M�todo para volver al direcorio raiz**/
	public void directorioRaiz () throws IOException{
		cliente.changeToParentDirectory(); //Se vuelve al directorio raiz
		System.out.println("Se ha cambiado al directorio raiz");
		combodirectorios.removeAllItems(); //Se limpian los items antes de llenar los combos nuevamente
		comboarchivos.removeAllItems();
		listarFicheros();
	}
	
	/** M�todo para mostrar resultados de conexi�n por tex�rea log **/
	private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.append(text);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
	
	
	/**M�todo para desconectar del Servidor**/
	public void desconectar () throws Exception{
		conectado=false;
        // Cerramos conexi�n con servidor
		cliente.logout();
		cliente.disconnect();
	}

	
	
	/** METODOS  SET **/
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setHost(String host) {	
		this.host = host;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
	public void setPass(String pass) {	
		this.pass = pass;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setRuta_fichero_subida(String ruta_fichero_subida) {	
		this.ruta_fichero_subida = ruta_fichero_subida;
	}
	
	public void setDestino_descarga(String destino_descarga) {	
		this.destino_descarga = destino_descarga;
	}
	
}
