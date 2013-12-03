import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.swing.JOptionPane;

public class AutomatedSendMail
{
	public static void send(String to, String fileAttachment)
	{
		System.out.println("Sending Mail...");
		if(isValidEmailAddress(to)) //validate email that that was entered for to:
		{
			//authentication username, password
			final String username = "BCPNCLabTek@gmail.com";
			final String password = "teksupport";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			});

			try
			{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("BCPNCLabTek@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));

				//message subject
				message.setSubject("Lab Sign-In Sheet");

				//message body
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("The following attachment contains Lab Sign In logs in .xls format.");
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				//attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(fileAttachment);
				messageBodyPart.setDataHandler(new DataHandler(source));
				String filename = fileAttachment.substring(11, fileAttachment.length());
				System.out.println(filename);
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);

				//sendmail
				Transport.send(message);
				System.out.println("Mail Sent Successfully...");
				//JOptionPane.showMessageDialog(null,"Mail was Successfully Sent","Confirmation Message",JOptionPane.INFORMATION_MESSAGE);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}

		}else if(!isValidEmailAddress(to))
		{
			JOptionPane.showMessageDialog(null,"Could not send e-mail because there was an error with the E-mail address you have entered.\nPlease Enter a Valid E-mail Address.","Error Message",JOptionPane.ERROR_MESSAGE);
		}

	}

	public static boolean isValidEmailAddress(String email)
	{
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		}catch (AddressException ex){result = false;}
  		return result;
	}
}