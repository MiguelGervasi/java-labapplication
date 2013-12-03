import de.javasoft.plaf.synthetica.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

public class Main{
	public Main()
	{
		//add fullscreen frame behind mainmenu
		JFrame frame = new JFrame();
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//ImagePanel panel = new ImagePanel(new ImageIcon("icons/bg.jpg").getImage());

		try
		{
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("icons/bg3.jpg")))));
		}catch (IOException e) {
			e.printStackTrace();
    	}

    	//frame.add(panel);

		frame.setFocusableWindowState(false);
    	frame.setUndecorated(true);
		frame.setVisible(true);

		try
		{
			UIManager.setLookAndFeel(new SyntheticaBlackMoonLookAndFeel());
		}catch (Exception e){
			System.err.println("");
        }

		new MainMenu();

	}

	public static void main(String[] args)
	{
		new Main();

	}

	class ImagePanel extends JComponent {
		private Image image;
		public ImagePanel(Image image) {
			this.image = image;
		}
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}
}