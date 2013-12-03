import de.javasoft.plaf.synthetica.*;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DateFormatter;
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import java.io.File;
import java.awt.Graphics;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class MainMenu extends JFrame{
    CardLayout cards;
    JPanel firstCard;
    JPanel cardPanel;
    RegisterStudentForm secondCard;
    SISOForm thirdCard;
    JMenu AdminMenu = new JMenu("Edit");
    JMenuItem importAction;
    String startdate,enddate;

    public static void main(String[] args)
    {
		try
		{
			UIManager.setLookAndFeel(new SyntheticaBlackMoonLookAndFeel());
		}catch (Exception e){
			System.err.println("");
        }
        //———–Look and Feel————-

    	//end main()

        //Use the event dispatch thread for Swing components
        EventQueue.invokeLater(new Runnable()
        {
			@Override
            public void run()
            {
				new MainMenu();
			}
		});
	}

    public MainMenu()
    {
		//create gui
		JFrame guiFrame = new JFrame();

		//set gui settings
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Bloomfield College - CIS Lab Application");
        guiFrame.setSize(1000,600);
        guiFrame.setResizable(false);
		guiFrame.setLocationRelativeTo(null);
        guiFrame.setLayout(new BorderLayout());
        //guiFrame.setAlwaysOnTop(true);

        //set menubar
        JMenuBar menuBar = new JMenuBar();
        guiFrame.setJMenuBar(menuBar);

		//icons
        ImageIcon gd = new ImageIcon("icons/Google_Drive_icon.png");
        ImageIcon ed = new ImageIcon("icons/xl-icon.png");
		ImageIcon eico = new ImageIcon("icons/email_icon.png");
		ImageIcon logico = new ImageIcon("icons/logout_icon.png");

        //create jmenu and jmenitems
        JMenu fileMenu = new JMenu("File");

        //set admin menu invisible
        AdminMenu.setVisible(false);

        JMenu manageAction = new JMenu("Manage Database");
        manageAction.setIcon(new ImageIcon("icons/access_icon.png"));
		JMenuItem commitsubAction = new JMenuItem("Commit (Save)",new ImageIcon("icons/commit_icon.png"));
        JMenuItem rollbacksubAction = new JMenuItem("Rollback (Undo)",new ImageIcon("icons/rollback_icon.png"));

        JMenuItem uploadAction = new JMenuItem("Upload to Google Drive",gd);
        importAction = new JMenuItem("Import Excel Document",ed);
        JMenuItem exportAction = new JMenuItem("Export as Excel Document",ed);
        JMenuItem emailAction = new JMenuItem("E-mail Document", eico);
        JMenuItem logoutAction = new JMenuItem("Logout", logico);

        JMenuItem exitAction = new JMenuItem("Exit");
        JMenuItem adminAction = new JMenuItem("Switch to Administrator Mode ...",new ImageIcon("icons/user-32.gif"));
		JMenuItem employeeAction = new JMenuItem("Switch to Employee Mode ...",new ImageIcon("icons/user-14-32.png"));
		JMenuItem guestAction = new JMenuItem("Switch to Guest Mode ...",new ImageIcon("icons/user-14-32.png"));

		//add jmenu and jmenuitems to jmenubar
        menuBar.add(fileMenu);
        menuBar.add(AdminMenu);
		fileMenu.add(adminAction);
		fileMenu.add(employeeAction);
		fileMenu.add(guestAction);
		//fileMenu.add(exitAction);
		//AdminMenu.add(manageAction);
		AdminMenu.add(importAction);
		AdminMenu.add(exportAction);
		//AdminMenu.add(uploadAction);
		AdminMenu.add(emailAction);
		AdminMenu.add(logoutAction);
		manageAction.add(commitsubAction);
		manageAction.add(rollbacksubAction);

        //creating a border to highlight the JPanel areas
        Border outline = BorderFactory.createLineBorder(Color.black);

		//panel to switch cards
        cards = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(cards);
        cards.show(cardPanel, "main menu");

		//button panel
        JPanel btnpanel = new JPanel();
        btnpanel.setPreferredSize(new Dimension(200, 80));

		//panels inside cards

		firstCard = new JPanel();
		firstCard.setPreferredSize(new Dimension(400, 100));
		firstCard.setBorder(BorderFactory.createTitledBorder("Main Menu"));
        secondCard = new RegisterStudentForm();
        secondCard.setBorder(BorderFactory.createTitledBorder("Student Registration"));
        thirdCard = new SISOForm();

		//how to use information
		String howtouseinfo = "<html><font face= 'courier'><b><font size = '8'>HOW TO USE</font></b>"
		+ "<p><font color = 'grey' size = '5'><u><b>Navigation Panel</b></u></font><p><font color = '' size = '4'>- Located at the top which consist of buttons used to navigate through the application<br>- Click on New Student to register if you are a new student and fill in your information<br>- Click on Registered Student if you are already registered and find your name on the list located on the left</font>"
		+ "<p><font color = 'grey' size = '5'><u><b>New Student</b></u></font><p><font color = '' size = '4'>- Click on the New Student button<br>- Fill in your Student ID Number, First Name, Last Name<br>- Click On Register <br>- Once it confirms that you have been added to database you have successfully registered!</font>"
		+ "<p><font color = 'grey' size = '5'><u><b>Registered Student</b></u></font><p><font color = '' size = '4'>- Click on the Registered Student button<br>- To sign and sign out a registered student must double-click on his/her name in the list and a pop-up dialog will ask him/her to enter his/her ID<br>- If the ID entered matches with the one entered at registration, the student will be able to sign in and sign out</font>"
		+ "<p><font color = 'grey' size = '5'><u><b>How to Sign In and Sign Out</b></u></font><p><font color = '' size = '4'>- It's a simple as clicking on your name, entering your ID, and pressing sign in<br>- When you are ready to sign out double click on the row where you last signed in and click the signout button</font>"
		+ "<p><font color = 'grey' size = '3'><i>Developed by: Miguel Gervasi</i></font></font></html>";

		JTextPane howtoJarea = new JTextPane();
	/*	{
			public void paint(Graphics g)
			{
		        g.setXORMode(Color.white);
		        //g.drawImage(image.getImage(),0, 0, this);
		        super.paint(g);
  			}
		};*/

		howtoJarea.setContentType("text/html");
		JScrollPane infoscroll = new JScrollPane(howtoJarea);
		infoscroll.setPreferredSize(new Dimension(700,400));
		//howtoJarea.setCaretPosition(0);
		//infoscroll.getVerticalScrollBar().setValue(0);
		howtoJarea.setText(howtouseinfo);
		howtoJarea.setHighlighter(null);
		howtoJarea.setEditable(false);
		howtoJarea.setBackground(Color.WHITE);
		howtoJarea.setCaretPosition(0);
		firstCard.add(infoscroll);

        //navigation buttons
        JButton newstudentbtn = new JButton("New Student",new ImageIcon("lubt.jpg"));
		setBCStyle(newstudentbtn); //set color and style
        JButton regstudentbtn = new JButton("Registered Student",new ImageIcon("lubt.jpg"));
		setBCStyle(regstudentbtn); //set color and style
        JButton mainbtn = new JButton("Main Menu",new ImageIcon("lubt.jpg"));
		setBCStyle(mainbtn); //set color and style
        JButton exitbtn = new JButton("Exit",new ImageIcon("lubt.jpg"));
		setBCStyle(exitbtn); //set color and style
        JButton exportbtn = new JButton("Export",new ImageIcon("lubt.jpg"));
		setBCStyle(exportbtn); //set color and style
		JLabel greeting = new JLabel("Welcome to the CIS / NTW Lab");

		btnpanel.add(mainbtn);
        btnpanel.add(newstudentbtn);
		btnpanel.add(regstudentbtn);
		btnpanel.add(exitbtn);
        btnpanel.setBorder(BorderFactory.createTitledBorder("Navigation Panel"));

        //cards add panels
        cardPanel.add(firstCard, "main menu");
        cardPanel.add(secondCard, "new student");
        cardPanel.add(thirdCard, "reg student");


		// BEGIN NAVIGATION ACTIONS

		//when you click on new student switch cardPanel to secondCard
        newstudentbtn.addActionListener(new ActionListener()
        {
			@Override
            public void actionPerformed(ActionEvent event)
            {
				cards.show(cardPanel, "new student");
            }
        });

		//main menu button action
        mainbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
				cards.show(cardPanel, "main menu");
            }
        });

		//register student button action
        regstudentbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
				regstudent();
            }
        });

		//exit button action
        exitbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
				System.exit(0);
            }
        });

		// END NAVIGATION ACTIONS

		//ROLL BACK AND COMMIT ACTIONS
		/*
        rollbacksubAction.addActionListener(new ActionListener()

        {
            @Override
            public void actionPerformed(ActionEvent event)
            {

			   thirdCard.rollback();

            }
        });

        commitsubAction.addActionListener(new ActionListener()

        {
            @Override
            public void actionPerformed(ActionEvent event)
            {

				thirdCard.commit();


            }
        });*/

		//START JMENU ITEM ACTIONS

		//export database data into an excel file in .xls format
		//data is determined by date range, start date to end date
		exportAction.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{

					final SimpleDateFormat dst = new SimpleDateFormat("MM/dd/yyyy");
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					c2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					startdate = dst.format(c1.getTime());
					enddate = dst.format(c2.getTime());

					JPanel p = new JPanel(new GridLayout(3,2));
					p.setPreferredSize(new Dimension(50,20));
					JDateChooser chooser1 = new JDateChooser("MM/dd/yyyy", "####/##/##", '_');
					chooser1.setDate(c1.getTime());
					chooser1.getDateEditor().addPropertyChangeListener(
						new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent e) {
							if ("date".equals(e.getPropertyName())) {
								//System.out.println( dst.format((Date) e.getNewValue()) );
								startdate = dst.format((Date) e.getNewValue());
							}
						}
					});

					p.add(new JLabel("Start Date:  "));
					p.add(chooser1);

					JDateChooser chooser2 = new JDateChooser("MM/dd/yyyy", "####/##/##", '_');
					chooser2.setDate(c2.getTime());
					chooser2.getDateEditor().addPropertyChangeListener(
						new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent e) {
							if ("date".equals(e.getPropertyName())) {
								//System.out.println( dst.format((Date) e.getNewValue()) );
								enddate = dst.format((Date) e.getNewValue());

							}
						}
					});

					p.add(new JLabel("End Date:  "));
					p.add(chooser2);

					Object[] options = {
						"Student Logs",
						"Employee Logs",
					};

					JComboBox optionbox = new JComboBox(options);
					p.add(new JLabel("Data:  "));
					p.add(optionbox);

/*
				    MaskFormatter mask = new MaskFormatter("##/##/####");
            		mask.setPlaceholderCharacter('_');



				    //format field startField, endField for date range of exported excel file
				    JFormattedTextField startField = new JFormattedTextField(mask);
				    JFormattedTextField endField = new JFormattedTextField(mask);
			  		JPanel myPanel = new JPanel(new GridLayout(2,2));
			  		myPanel.add(new JLabel("Start Date:"));
			  		myPanel.add(startField);
			  		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			  		myPanel.add(new JLabel("End Date:"));
			  		myPanel.add(endField);
*/
			  		//create JOptionPane buttons
			  		String[] buttons = { "Export", "Cancel"};

					//if user clicks Export, then popup Filechooser so they can save file with whatevername they please
					//must end in .xls
					int result = JOptionPane.showOptionDialog(null, p,"Export", JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE,null,buttons,buttons[0]);
			  		if (result == 0)
			  		{

						JFileChooser chooser = new JFileChooser() //custom JFileChooser
						{
							@Override //override method to check if file exist if it does ask user to overwrite or not!
							public void approveSelection()
							{
								File f = getSelectedFile();
								if(f.exists() && getDialogType() == SAVE_DIALOG) // if file exists popup message
								{
									int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
									switch(result)
									{
										case JOptionPane.YES_OPTION:
										super.approveSelection();
										return;

										case JOptionPane.NO_OPTION:
										return;

										case JOptionPane.CLOSED_OPTION:
										return;

										case JOptionPane.CANCEL_OPTION:
										cancelSelection();
										return;
									}
								}

							super.approveSelection(); //approve if file does not exist

							}
						}; //end of JFileChooser

						//give suggestion when prompt for export
				 		chooser.setSelectedFile(new File("../timesheets/" + startdate.replaceAll("/", "-")  + "-" + enddate.replaceAll("/", "-") + "___SignInSheet.xls"));
				 		chooser.setFileFilter(new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls"));

				        int option = chooser.showSaveDialog(MainMenu.this);
				        if(option == JFileChooser.APPROVE_OPTION)
				        {
							File f = chooser.getSelectedFile();
							String filePath = f.getPath();
							if(!filePath.toLowerCase().endsWith(".xls"))
							{
								f = new File(filePath + ".xls");
							}

							if(chooser.accept(f))
							{
								//new CreateExcelFile(f.toString(), startField.getText(), endField.getText());
					    		new CreateExcelFile(f.toString(), startdate, enddate, optionbox.getSelectedItem().toString());

					    	}
					    	else
					    	{
								JOptionPane.showMessageDialog(null,"File extension must be .xls!","Error Message",JOptionPane.ERROR_MESSAGE);
							}
				       }
				      if(option == JFileChooser.CANCEL_OPTION)
				      {
						  // display.setText("You canceled.");
					  }
					}

				}catch(Exception e){}
			}
		});


		//switch to admin mode turn adminModeOn = true
		//requestpassword
		//give commands to users such as managing user profiles and logs without entering ID
        adminAction.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
				try{
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(15);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[]{"Login", "Cancel"};
					int option = JOptionPane.showOptionDialog(null, panel, "Admin Login",
                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                         null, options, options[0]);
                    if(option == 0) // && adminsecurity.equals("adminpass")
      				{
						String password = new String(pass.getPassword());
  						if(password.equals("d"))
  						{
  							JOptionPane.showMessageDialog(null,"You have Successfully logged in!","Success",JOptionPane.INFORMATION_MESSAGE);
							thirdCard.adminMode();
							//AdminMenu.setVisible(true);
						}
						else{
							JOptionPane.showMessageDialog(null,"Incorrect Password! Login Failed.","Error",JOptionPane.ERROR_MESSAGE);
							thirdCard.guestMode();
							//AdminMenu.setVisible(false);
						}
					}

				}catch(Exception e){}

			}
		});


        employeeAction.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {


				try{
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(15);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[]{"Login", "Cancel"};
					int option = JOptionPane.showOptionDialog(null, panel, "Employee Login",
                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                         null, options, options[0]);
                    if(option == 0) // && adminsecurity.equals("adminpass")
      				{
						String password = new String(pass.getPassword());
  						if(password.equals("emppass"))
  						{
  							JOptionPane.showMessageDialog(null,"You have Successfully logged in!","Success",JOptionPane.INFORMATION_MESSAGE);
							thirdCard.employeeMode();
							//AdminMenu.setVisible(true);
						}
						else{
							JOptionPane.showMessageDialog(null,"Incorrect Password! Login Failed.","Error",JOptionPane.ERROR_MESSAGE);
							thirdCard.guestMode();
							//AdminMenu.setVisible(false);
						}
					}

				}catch(Exception e){}
			}

		});
		//switch to guestmode and lose all admin privilages
		//set adminMenu invisible and adminModeOn = false
        guestAction.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {

				thirdCard.guestMode();
				AdminMenu.setVisible(false);

			}

		});


        logoutAction.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {

				JOptionPane.showMessageDialog(null,"You have successfully logged out!","Logged Out",JOptionPane.INFORMATION_MESSAGE);
				thirdCard.guestMode();
				AdminMenu.setVisible(false);

			}

		});

		// email .xls file to anyone, make sure the email is valid which is checked in sendmail TLS
		// FileChooser checks if file even exists
		emailAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try
				{
					//input formatters for the startdate and end date
					JPanel myPanel = new JPanel();
					JTextField toField = new JTextField(15);

					//add to myPanel
			  		myPanel.add(new JLabel("To:"));
					myPanel.add(toField);
			  		JPanel attachpanel = new JPanel();

			  		//JoptionPane attach & send mail buttons
			  		String[] buttons = {"Attach & Send Mail", "Cancel"};

					//if press yes, give filechooser option to choose file and then click ok, it will send if all input is true
					int result = JOptionPane.showOptionDialog(null, myPanel,"Send Mail...", JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE,null,buttons,buttons[0]);
			  		if (result == 0)
			  		{
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls"));
				        int option = chooser.showOpenDialog(MainMenu.this);
				        if(option == JFileChooser.APPROVE_OPTION)
				        {
							File f = chooser.getSelectedFile();
							String filePath = f.getPath();
							if(!filePath.toLowerCase().endsWith(".xls"))
							{
								f = new File(filePath + ".xls");
							}
							if(chooser.accept(f) && f.exists())
							{
								new SendMailTLS(toField.getText(), f.toString(), f.getName());
							}
							else if(!chooser.accept(f))
							{
								JOptionPane.showMessageDialog(null,"File extension must be .xls!","Error Message",JOptionPane.ERROR_MESSAGE);
							}
							else if(!f.exists())
							{
								JOptionPane.showMessageDialog(null,"File does not exist!","Error Message",JOptionPane.ERROR_MESSAGE);
							}
				       }

				       if(option == JFileChooser.CANCEL_OPTION) {
					   }

					}

				}catch(Exception e){}


		   	}
		});

		importAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try
				{
					//input formatters for the startdate and end date
					JPanel myPanel = new JPanel();
     				Object[] options = {
	                    "Students Only",
	                    "Employees Only",
	                    "Logs Only",
						"Employee Logs Only",
	                    "Both Students / Logs",
	                    "Both Employees / Employee Logs",
	                };
					JComboBox optionbox = new JComboBox(options);
					//add to myPanel
			  		myPanel.add(new JLabel("Data:  "));
					myPanel.add(optionbox);
			  		//JPanel attachpanel = new JPanel();

			  		//JoptionPane attach & send mail buttons
			  		String[] buttons = {"Import File", "Cancel"};

					//if press yes, give filechooser option to choose file and then click ok, it will send if all input is true
					int result = JOptionPane.showOptionDialog(null, myPanel,"Import .xls File...", JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE,null,buttons,buttons[0]);
			  		if (result == 0)
			  		{
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls"));
				        int option = chooser.showOpenDialog(MainMenu.this);
				        if(option == JFileChooser.APPROVE_OPTION)
				        {
							File f = chooser.getSelectedFile();
							String filePath = f.getPath();
							if(!filePath.toLowerCase().endsWith(".xls"))
							{
								f = new File(filePath + ".xls");
							}
							if(chooser.accept(f) && f.exists())
							{
								new xlsToAccess(f.toString(),optionbox.getSelectedItem().toString());
							}
							else if(!chooser.accept(f))
							{
								JOptionPane.showMessageDialog(null,"File extension must be .xls!","Error Message",JOptionPane.ERROR_MESSAGE);
							}
							else if(!f.exists())
							{
								JOptionPane.showMessageDialog(null,"File does not exist!","Error Message",JOptionPane.ERROR_MESSAGE);
							}
				       }

				       if(option == JFileChooser.CANCEL_OPTION) {
					   }

					}

				}catch(Exception e){}
				//after the import refresh the new student list, update filter array
				try{
					new dBhandler().selectStudents(thirdCard.model,thirdCard.dbList);
				}catch(Exception err){}
				thirdCard.updateArray();
				//end refresh
		   	}
		});

        secondCard.x = this; //giving this instance to the child process which is SISO
        thirdCard.x = this; //giving this instance to the child process which is SISO
        guiFrame.add(cardPanel,BorderLayout.CENTER);
        guiFrame.add(btnpanel,BorderLayout.NORTH);
        guiFrame.setVisible(true);

        //Call Scheduler
        //try{ new TaskScheduler();}catch(Exception e){System.out.println(e);}
    }

    //All the buttons are following the same pattern
    //so create them all in one place.
    public JButton setBCStyle(JButton button)
    {

	    button.setPreferredSize(new Dimension(200, 50));
		button.setMaximumSize(new Dimension(200, 50));
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.CENTER);

		return button;
	}

	public void regstudent() //just copy and paste this inside registerstudentbutton if it gives problems
	{
		cards.show(cardPanel, "reg student");
		try{
			new dBhandler().selectStudents(thirdCard.model,thirdCard.dbList);
		}catch(Exception err){}
		thirdCard.updateArray();
	}

	public CardLayout getcardlayout()
	{
		return this.cards;
	}

	public JPanel getcardpanel()
	{
		return this.cardPanel;
	}

}