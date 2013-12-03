import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.border.*;

public class RegisterStudentForm extends JPanel
{
	private JPanel panel;
 	private JButton clockin;
 	private JButton clockout;
 	private JTextField fname,lname,idtxt;
 	private JLabel fnlbl,lnlbl,idlbl;
 	private JRadioButton teacher;
 	private JRadioButton employer;
 	private JRadioButton student;
 	private ButtonGroup radioButtonGroup;
 	private final int WINDOW_WIDTH = 400; // WINDOW WIDTH
 	private final int WINDOW_HEIGHT = 400;// WINDOW HEIGHT
	MainMenu x;

    public RegisterStudentForm()
    {
		setLayout(new GridLayout(1,1));
		JPanel masterpanel = new JPanel();
    	masterpanel.setLayout(new BorderLayout());
        JButton regbtn = new JButton("Register", new ImageIcon("blubt.jpg"));
        JButton clearbtn = new JButton("Clear", new ImageIcon("blubt.jpg"));
        setBCStyle(regbtn); //set color and style
		//setBCStyle(clearbtn); //set color and style

        fname = new JTextField(10);
        lname = new JTextField(10);
        idtxt = new JTextField(10);
        fnlbl = new JLabel("<html><font face= 'courier'><b><font size ='3'>First Name:  </b></font></html>");
        lnlbl = new JLabel("<html><font face= 'courier'><b><font size ='3'>Last Name:  </b></font></html>");
        idlbl = new JLabel("<html><font face= 'courier'><b><font size ='3'>Enter ID:  </b></font></html>");

        JLabel greeting = new JLabel("<html><font face= 'courier'><b><font size = '4'>Welcome to the CIS / NTW Lab<b></font></html>");
        JLabel headerimg = new JLabel(new ImageIcon("icons/bc_ico.jpg"));
        JLabel header = new JLabel("<html><font face= 'courier'><b><font size = '2'><u>Please fill in the following information.</u></b></font></html>");

        JPanel greetingpanel = new JPanel();
        JPanel instr = new JPanel();
        JPanel north = new JPanel();
        JPanel south = new JPanel();
        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel(new GridLayout(3,3));
        JPanel p3 = new JPanel();
	    p0.setBackground(Color.WHITE);
		p1.setOpaque(false);
        p2.setOpaque(false);
        p3.setOpaque(false);
	    instr.setOpaque(false);

 	    p0.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
	    p0.setPreferredSize(new Dimension(440, 380));
	    p1.setPreferredSize(new Dimension(140, 150));
	    p2.setPreferredSize(new Dimension(280, 80));
	    p3.setPreferredSize(new Dimension(480, 80));
	    north.setPreferredSize(new Dimension(300, 20));
		south.setPreferredSize(new Dimension(300, 90));
		instr.setPreferredSize(new Dimension(480, 50));

//alternate layout
/*
        JPanel greetingpanel = new JPanel();
        JPanel instr = new JPanel();
        JPanel north = new JPanel();
        JPanel south = new JPanel();
        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel(new GridLayout(6,3));
        JPanel p3 = new JPanel();
	    p0.setBackground(Color.WHITE);
		p1.setOpaque(false);
        p2.setOpaque(false);
        p3.setOpaque(false);
	    instr.setOpaque(false);
		//JLabel bcsmall = new JLabel(new ImageIcon("bcsmall3.jp"));
		//bcsmall.setPreferredSize(new Dimension(275, 250));

    	p0.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
	    p0.setPreferredSize(new Dimension(440, 380));
	    //p1.setPreferredSize(new Dimension(140, 150));
	    p2.setPreferredSize(new Dimension(200, 130));
	    p3.setPreferredSize(new Dimension(280, 80));
	    north.setPreferredSize(new Dimension(300, 20));
		south.setPreferredSize(new Dimension(300, 100));
		instr.setPreferredSize(new Dimension(480, 50));
*/
		p1.add(headerimg);
		instr.add(header);
        p2.add(idlbl);
        p2.add(idtxt);
        p2.add(fnlbl);
        p2.add(fname);
        p2.add(lnlbl);
        p2.add(lname);
        p3.add(regbtn);
        //p3.add(clearbtn);
        p0.add(new JLabel("<html><font face = 'courier' size = '5'><b>Registration Form</b></font></html>"));
        p0.add(instr);
        p0.add(p1);
        p0.add(p2);
        p0.add(p3);


		regbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
		    {
				try
			    {
					dBhandler d = new dBhandler();
					String errormsg = "";
					if (!(Pattern.matches("^[a-zA-Z]+$", fname.getText())) || !(Pattern.matches("^[a-zA-Z]+$", lname.getText())) || !(Pattern.matches("(\\d){1,9}", idtxt.getText()))) {
						if (!(Pattern.matches("^[a-zA-Z]+$", fname.getText())) || !(Pattern.matches("^[a-zA-Z]+$", lname.getText())))
							errormsg += "Please enter a valid First Name and/or Last Name.\n";
						if (!(Pattern.matches("(\\d){1,9}", idtxt.getText())))
							errormsg += "Please enter a valid Student ID. \n";
					    JOptionPane.showMessageDialog(null, errormsg, "Input Validation", JOptionPane.ERROR_MESSAGE);
					}else{
						d.insertStudent(Integer.parseInt(idtxt.getText()), fname.getText().toString(), lname.getText().toString());
						JOptionPane.showMessageDialog(null, "Your information has been added to the Database!", "Information", JOptionPane.INFORMATION_MESSAGE);
						fname.setText("");
						lname.setText("");
						idtxt.setText("");
						x.regstudent();

					}

			   }catch(Exception error)
			   {
				   JOptionPane.showMessageDialog(null, "An unexpected error has occured! Either the ID Number is already in the system or you have entered invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
			   }

			}
        });

		clearbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
		    {
				fname.setText("");
				lname.setText("");
				idtxt.setText("");
			}
        });

        JPanel eastp = new JPanel();
        eastp.setPreferredSize(new Dimension(250,200));
        JPanel westp = new JPanel();
		westp.setPreferredSize(new Dimension(250,200));

        masterpanel.add(north,BorderLayout.NORTH);
        masterpanel.add(eastp,BorderLayout.EAST);
        masterpanel.add(westp,BorderLayout.WEST);
        masterpanel.add(p0,BorderLayout.CENTER); // change to center, WEST
        masterpanel.add(south,BorderLayout.SOUTH);
        add(masterpanel);
    }

    public JButton setBCStyle(JButton button)
    {

	    button.setPreferredSize(new Dimension(215, 50));
		button.setMaximumSize(new Dimension(200, 50));
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.CENTER);

		return button;
	}

	public boolean isValid(JTextField txt)
	{
		try
		{
			Integer.parseInt(txt.getText());
			return true;
    	}catch (NumberFormatException e){
			return false;
    	}
    }

}