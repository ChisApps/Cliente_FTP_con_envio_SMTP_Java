/** JORGE FIGUEROLA - PRÁCTICA 2 PSP 2ª EVALUACIÓN **/

import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JComboBox;

public class GUI_ClienteFTP {

	private JFrame frame;
	private JTextField hostField;
	private JTextField puertoField;
	private JTextField userField;
	private JTextField passField;
	private JTextField emailField;
	private JTextArea ventanalog;
	
	//Declaramos los botones como variables para tener fácil acceso a habilitar y deshabilitar mediante
	//métodos
	
	private JButton btnSelectSubir; 
	private JButton btnDescargar;
	private JButton btnDesconectar;
	private JButton btnSubir;
	private JButton btnDirectorioDeDescargas;
	private JButton btnCambiarDirectorio;
	private JButton btnVolverHome;
	
	//labels los usaremos para recoger y mostrar rutas del archivo q seleccionemos para subir de la ruta
	// que seleccionamos para recibir las descargas
	private JLabel lblFicheroSubida;
	private JLabel labelRutaDescarga;

	//JComBox
	private JComboBox<String> comboBoxDirectorio;
	private JComboBox<String> comboBoxArchivos;
	
	private ClienteFTP cliente = new ClienteFTP();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_ClienteFTP window = new GUI_ClienteFTP();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_ClienteFTP() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Cliente FTP y SMTP");
		frame.setBounds(100, 100, 738, 667);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_general = new JPanel();
		panel_general.setBackground(SystemColor.inactiveCaption);
		panel_general.setBounds(0, 0, 722, 629);
		frame.getContentPane().add(panel_general);
		panel_general.setLayout(null);
		
		JPanel panel_superior = new JPanel();
		panel_superior.setBackground(Color.WHITE);
		panel_superior.setBounds(10, 11, 702, 306);
		panel_general.add(panel_superior);
		panel_superior.setLayout(null);
		
		JPanel panel_paramtros = new JPanel();
		panel_paramtros.setBounds(0, 0, 702, 85);
		panel_superior.add(panel_paramtros);
		panel_paramtros.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Host:");
		lblNewLabel.setBounds(10, 45, 46, 14);
		panel_paramtros.add(lblNewLabel);
		
		hostField = new JTextField();
		hostField.setBounds(66, 42, 139, 20);
		panel_paramtros.add(hostField);
		hostField.setColumns(10);
		
		puertoField = new JTextField();
		puertoField.setBounds(301, 42, 86, 20);
		panel_paramtros.add(puertoField);
		puertoField.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(245, 45, 46, 14);
		panel_paramtros.add(lblPuerto);
		
		JLabel lblParmetrosDeConexin = new JLabel("Par\u00E1metros de conexi\u00F3n");
		lblParmetrosDeConexin.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblParmetrosDeConexin.setBounds(10, 11, 682, 14);
		panel_paramtros.add(lblParmetrosDeConexin);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.setBounds(474, 36, 89, 23);
		panel_paramtros.add(btnConectar);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setBounds(573, 36, 119, 23);
		panel_paramtros.add(btnDesconectar);
		
		JPanel panel_usuario = new JPanel();
		panel_usuario.setLayout(null);
		panel_usuario.setBounds(0, 92, 702, 214);
		panel_superior.add(panel_usuario);
		
		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(10, 44, 57, 14);
		panel_usuario.add(lblUser);
		
		userField = new JTextField();
		userField.setColumns(10);
		userField.setBounds(77, 41, 139, 20);
		panel_usuario.add(userField);
		
		passField = new JTextField();
		passField.setColumns(10);
		passField.setBounds(307, 41, 139, 20);
		panel_usuario.add(passField);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(226, 44, 71, 14);
		panel_usuario.add(lblContrasea);
		
		JLabel lblDatosDeUsuario = new JLabel("Datos de usuario");
		lblDatosDeUsuario.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDatosDeUsuario.setBounds(10, 11, 682, 19);
		panel_usuario.add(lblDatosDeUsuario);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(456, 44, 46, 14);
		panel_usuario.add(lblEmail);
		
		
		btnSelectSubir = new JButton("Fichero a subir");
		btnSelectSubir.setBounds(10, 72, 185, 23);
		panel_usuario.add(btnSelectSubir);
		
		lblFicheroSubida = new JLabel("Ruta:");
		lblFicheroSubida.setBackground(Color.WHITE);
		lblFicheroSubida.setBounds(215, 76, 477, 14);
		panel_usuario.add(lblFicheroSubida);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(512, 41, 180, 20);
		panel_usuario.add(emailField);
		
		btnDirectorioDeDescargas = new JButton("Directorio de descargas");
		btnDirectorioDeDescargas.setEnabled(false);
		btnDirectorioDeDescargas.setBounds(10, 106, 185, 23);
		panel_usuario.add(btnDirectorioDeDescargas);
		
		labelRutaDescarga = new JLabel("Ruta:");
		labelRutaDescarga.setBackground(Color.WHITE);
		labelRutaDescarga.setBounds(215, 110, 477, 14);
		panel_usuario.add(labelRutaDescarga);
		
		comboBoxDirectorio = cliente.combodirectorios;
		comboBoxDirectorio.setToolTipText("");
		comboBoxDirectorio.setBounds(215, 165, 185, 20);
		panel_usuario.add(comboBoxDirectorio);
		
		comboBoxArchivos = cliente.comboarchivos;
		comboBoxArchivos.setToolTipText("");
		comboBoxArchivos.setBounds(461, 165, 185, 20);
		panel_usuario.add(comboBoxArchivos);
		
		JLabel lblNewLabel_1 = new JLabel("Seleccione directorio del servidor");
		lblNewLabel_1.setBounds(215, 140, 231, 14);
		panel_usuario.add(lblNewLabel_1);
		
		JLabel lblSeleccionesArchvoPara = new JLabel("Seleccione archvo para descarga");
		lblSeleccionesArchvoPara.setBounds(461, 140, 231, 14);
		panel_usuario.add(lblSeleccionesArchvoPara);
		
		btnCambiarDirectorio = new JButton("Cambiar directorio");
		btnCambiarDirectorio.setEnabled(false);
		btnCambiarDirectorio.setBounds(10, 140, 185, 23);
		panel_usuario.add(btnCambiarDirectorio);
		
		btnVolverHome = new JButton("Ir a directorio raiz");
		btnVolverHome.setEnabled(false);
		btnVolverHome.setBounds(10, 174, 185, 23);
		panel_usuario.add(btnVolverHome);
		
		
		JPanel panel_botones = new JPanel();
		panel_botones.setBounds(10, 328, 702, 51);
		panel_general.add(panel_botones);
		panel_botones.setLayout(null);
		
		btnSubir = new JButton("Subir");
		btnSubir.setBounds(240, 11, 112, 31);
		panel_botones.add(btnSubir);
		
		btnDescargar = new JButton("Descargar");
		btnDescargar.setBounds(361, 11, 112, 31);
		panel_botones.add(btnDescargar);
		
		
		ventanalog = cliente.log;
		ventanalog.setLineWrap(true); //Saltará de línea al llegar al final del campo en horizonal
		
		//Añadimos ventana log a scrollPane y declaramos que solo haga scroll hacía abajo
		JScrollPane panel_log = new JScrollPane(ventanalog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel_log.setBackground(SystemColor.scrollbar);
		panel_log.setBounds(10, 390, 702, 228);
		panel_general.add(panel_log);
		
		deshabilitarBotones ();//Deshabilitamos los botones por defecto
		
		/** AÑADIMOS ACCIÓN A LOS BOTONES **/
		
		// Añadimos acción a botón conectar
		btnConectar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					recoge_Datos(); //Pasamos datos a la instancia de cliente
					cliente.conectar_Servidor();
				//Habilitamos botones de archivos y directorios si hay conexión
					if(cliente.conectado){
						habilitarBotones ();
					}
				} catch (Exception e1) {//Capturamos excepción y añadimos mensaje a log
					// TODO Auto-generated catch block
					//Deshabilitamos todos los botones de selección de archivos y directorios
					ventanalog.setText(ventanalog.getText()+"No se ha podido conectar con servidor, revise datos de puerto y host\n");
					e1.printStackTrace();
				}
			}
		});
	
		// Añadimos acción para el botón que selecciona el fichero que vamos a subir
		btnSelectSubir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Declaramos filechooser con características para que solo se puedan seleccionar ficheros
					JFileChooser chooser;
					String ruta = "";
					chooser = new JFileChooser("SELECCIONE ARCHIVO PARA ENVIAR"); 
				    chooser.setCurrentDirectory(new java.io.File("."));
				    chooser.setDialogTitle(ruta);
				    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				    chooser.setAcceptAllFileFilterUsed(false);
				    //Lanzamos filechooser
				    chooser.showOpenDialog(btnSelectSubir);
				    //Agregamos movimiento en el log
				    ventanalog.setText(ventanalog.getText()+"Archivo seleccionado para subir al servidor: " +chooser.getSelectedFile()+"\n");
				    //La etiqueta de la ruta de archivo coge el valor de la ruta seleccionada
				    lblFicheroSubida.setText(""+chooser.getSelectedFile());
					cliente.setRuta_fichero_subida(lblFicheroSubida.getText());
		         }
		});
		
		//Acción para selección del directorio donde descargar el archivo
		btnDirectorioDeDescargas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					JFileChooser chooser;
					String ruta = "";
					chooser = new JFileChooser(""); 
				    chooser.setDialogTitle(ruta);
					// Declaramos filechooser con características para que solo muestre directorios
				    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				    chooser.setAcceptAllFileFilterUsed(false);
				    //Lanzamos filechooser
				    chooser.showOpenDialog(btnDirectorioDeDescargas);
				    //Agregamos movimiento en el log
				    ventanalog.setText(ventanalog.getText()+"Ruta seleccionada para descarga del archivo: " +chooser.getSelectedFile()+"\n");
				    //La etiqueta de la ruta de directorio coge el valor se la ruta seleccionada
				    labelRutaDescarga.setText(""+chooser.getSelectedFile()+"\\");//Añadimos carácter \\ al final de la etiqueta para que coja la ruta
					cliente.setDestino_descarga(labelRutaDescarga.getText());
		         }
		});
		
		// Añadimos acción a botón cambiar directorio
		btnCambiarDirectorio.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					cliente.cambiarDirectorio();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// Añadimos acción a botón cambiar directorio
		btnVolverHome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					cliente.directorioRaiz ();//Retornamos al directorio raiz
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		// Añadimos acción a botón subir fichero
		btnSubir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					if(compruebaEmail()){// Comprobamos email y si se ha rellnado el campo comenzamos trasferencia
						cliente.subirFicheros();
						ventanalog.setText(ventanalog.getText()+"Subiendo fichero.......\n");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					try {//En  caso de error desconectamos y deshabilitamos los botones y limpiamos los campos
						cliente.desconectar();
						cliente.conectado=false;
						deshabilitarBotones ();
						JOptionPane.showMessageDialog(frame, "No se puede realizar la subida del archivo, vuelva a conectarse y compruebe la ruta del archivo");
						ventanalog.setText(ventanalog.getText()+"Se ha producido un error en la subida del archivo, se desconecta del servidor\n");
						comboBoxDirectorio.removeAllItems(); //Se limpian los items de los combobox
						comboBoxArchivos.removeAllItems();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					e1.printStackTrace();
				}
			}
		});
		
		// Añadimos acción a botón para descargar fichero
		btnDescargar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					if(compruebaEmail ()){// Comprobamos email y si se ha rellnado el campo comenzamos trasferencia
						cliente.descargarFicheros();
						ventanalog.setText(ventanalog.getText()+"Descargando fichero.......\n");
					}
				} catch (Exception e1) {
					try {//En  caso de error desconectamos y deshabilitamos los botones
						cliente.desconectar();
						cliente.conectado=false;
						deshabilitarBotones ();
						JOptionPane.showMessageDialog(frame, "No se puede realizar la descarga, vuelva a conectarse e intentelo de nuevo");
						ventanalog.setText(ventanalog.getText()+"Se ha producido un error en la descarga del archivo, se desconecta el servidor\n");
						comboBoxDirectorio.removeAllItems(); //Se limpian los items de los combobox
						comboBoxArchivos.removeAllItems();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					e1.printStackTrace();
				}
			}
		});
		
		// Añadimos acción a botón para descargar fichero
		btnDesconectar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					cliente.desconectar();
				//Deshabilitamos todos los botones de selección de archivos y directorios y vaciamos campos
					deshabilitarBotones ();
					limpiarCampos ();
					JOptionPane.showMessageDialog(frame, "Se ha desconectado correctamente");
					ventanalog.setText(ventanalog.getText()+"Conexión finalizada\n");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//Añadimos acción al presionar sobre el aspa para ccerrar el programa
	    frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
					close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//Llamamos al método que que muestra un diálogo preguntando si se desea cerrar
            }
        });
		
	}	
	/** METODOS **/
	
	// Método para recoger datos de conexión desde la interfaz y se los pasa a la instancia cliente
	public void recoge_Datos (){
		
		try{
			String puerto_texto = puertoField.getText();
			int puerto_num = Integer.parseInt(puerto_texto);
			cliente.setPuerto(puerto_num);
		}catch (NumberFormatException e){//Si el campo puerto no se puede pasar a INT capturamos excepción y motramos diálogo y añadimos mensaje en log
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "El campo puerto debe contener un valor numérico valido");
			ventanalog.setText(ventanalog.getText()+"No se ha podido realizar la conexión campo puerto no valido\n");
		}
		// Recogemos Host, usuario y password
		cliente.setHost(hostField.getText());
		cliente.setUser(userField.getText());;
		cliente.setPass(passField.getText());
	}
    
	//Método para deshabilitar todos los botones de selección de archivos y directorios
    private void deshabilitarBotones (){
    	btnDesconectar.setEnabled(false);
    	btnDirectorioDeDescargas.setEnabled(false);
    	btnCambiarDirectorio.setEnabled(false);
		btnVolverHome.setEnabled(false);
		btnSelectSubir.setEnabled(false); 
		btnDescargar.setEnabled(false);
		btnSubir.setEnabled(false);
    }
    
	//Método para habilitar todos los botones de selección de archivos y directorios
    private void habilitarBotones (){
    	btnDesconectar.setEnabled(true);
		btnDirectorioDeDescargas.setEnabled(true);
		btnSelectSubir.setEnabled(true); 
		btnCambiarDirectorio.setEnabled(true);
		btnVolverHome.setEnabled(true);
		btnDescargar.setEnabled(true);
		btnSubir.setEnabled(true);
    }
    
	//Método que usaremos para limpiar los campos texfield y establecer solo valores relacionados de ClienteFTP en blacno
    private void limpiarCampos (){
    	puertoField.setText("");
		hostField.setText("");
		userField.setText("");
		passField.setText("");
		emailField.setText("");
		lblFicheroSubida.setText("");
		labelRutaDescarga.setText("");
		cliente.setPuerto(0);
		cliente.setHost(hostField.getText());
		cliente.setUser(userField.getText());;
		cliente.setPass(passField.getText());
		cliente.setEmail(emailField.getText());
		comboBoxDirectorio.removeAllItems(); //Se limpian los items de los combobox
		comboBoxArchivos.removeAllItems();
    }
    
    //Método para comprobar si se ha escrito algo en el campo mail, lo pondremos como requisito para subir o descargar ficheros
    //devuelve un booleano
    private boolean compruebaEmail (){
		if(emailField.getText().equalsIgnoreCase("")){//Lanzamos mensaje si no se ha recogido ningún valoe en campo de correo electrónico
			JOptionPane.showMessageDialog(null, "Es necesario que introduzca una dirección de correo eléctronico");
			System.out.println("Es necesario que introduzca una dirección de correo eléctronico para el envío de confirmación de transferencia\n");
			return false;
		}else{
			cliente.setEmail(emailField.getText());//Si no le pasamos la dirección de mail a la clase ClienteFTP
			return true;
		}
    }
    
	//Método que pregunta antes de cerrar la ventana y cierra conexión
    private void close() throws SQLException{
        if (JOptionPane.showConfirmDialog(frame, "¿Desea realmente salir de la aplicación?",
                "Salir del programa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)//Pregunta
			try {
				cliente.desconectar();//Llama a método desconectar de cliente
	            System.exit(0);//Se cierra apk
			} catch (Exception e) {
	            System.exit(0);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }  
}
