/** JORGE FIGUEROLA - PRÁCTICA 2 PSP 2ª EVALUACIÓN **/

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarMail {
	
private String email, mensaje; //Recibirán email y mensaje de la clase ClienteFTP

	public void enviarMail() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");//Se va a usar gmail
		props.put("mail.smtp.port", "587"); // PUERTO POR EL QUE CORRE LA
		// APLICACIÓN SMTP
		Session session = Session.getInstance(props,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// proporcionamos el usuario y la clave
				return new PasswordAuthentication("smtpdeprueba1@gmail.com", "adminAdmin");
			}
		});
		try {
			Message message = new MimeMessage(session);
			// e-mail del destinatario
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(email));
			message.setSubject("Mensaje servidor FTP"); // texto del asunto
			message.setText(mensaje); // texto del mensaje
			Transport.send(message);
			System.out.println("Se le ha enviado un mensaje de confirmación a su correo electrónico");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

	//Métodos SET
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public static void main(String[] args) {
		EnviarMail correo = new EnviarMail();
		correo.enviarMail();
	}


}