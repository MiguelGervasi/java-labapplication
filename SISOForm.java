import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import com.toedter.calendar.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class SISOForm extends JPanel
{
	//Initializing program components
	DefaultListModel model;
	private DefaultTableModel tablemodel;
	private JButton buttons[];
	JList dbList;
	private JPanel p1,p2,p3;
	private String bLabel[] = {"Student Sign In","Sign Out"};
	private JButton EmployeeSignInbtn;
	String studentid,idsecurity,stringa,stringb;
	//idsecurity is the variable that is used to verify who is logged in
	JTable table;
	boolean adminModeOn = false;
	boolean employeeModeOn = false;
	JPopupMenu popupMenu, popupmenutable;
	MainMenu x;

    //filter vars
    HintTextField filter;
    JList searchResult;
    JScrollPane pane2;
    String [] array; //create string array

	//jcalender vars
	String datestamptxt;

	//db conn vars
	Connection con;
	Statement st;
	ResultSet rs;
	String db;

 	//Setting up Panel
    public SISOForm()
    {
		setLayout(new BorderLayout());

		//Constructing JButtons, JList, and DefaultListModel
		model = new DefaultListModel();
		EmployeeSignInbtn = new JButton("Employee Sign In");
		buttons = new JButton[2];
		dbList = new JList(model);

 		// names of table columns
 		String[] col = {"id","date","signin","signout","action"};
 		Vector<String> columnNames = new Vector<String>();
		int columnCount = 4;

		// popMenu for dbList edit profile
    	popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Manage Student Profile");
		menuItem.setIcon(new ImageIcon("icons/edit_icon.png"));
		popupMenu.add(menuItem);

		//popupMenu for table
		popupmenutable = new JPopupMenu();
		JMenuItem updaterow = new JMenuItem("Update Selected Row");
		JMenuItem deleterow = new JMenuItem("Delete Selected Row");
		popupmenutable.add(updaterow);
		popupmenutable.add(deleterow);

		//set icons for right click pop up table menu
		updaterow.setIcon(new ImageIcon("icons/update_icon.png"));
		deleterow.setIcon(new ImageIcon("icons/delete_icon.png"));

		//Add listener to components that can bring up popup menus.
		MouseListener popupListener = new PopupListener();
		dbList.addMouseListener(popupListener);

		//add columnnames
		for (int column = 0; column <= 4; column++) {
			columnNames.add(col[column]);
		}

		tablemodel = new DefaultTableModel();

		//set table to tablemodel and table settings
		table = new JTable(tablemodel)
		{
			{
			//	setOpaque(false);
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//	setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
			//	{
			//		{
			//			//setOpaque(false);
			// 		}
	     	//	});

		 	}
			//set cells uneditable
		 	public boolean isCellEditable(int row, int column) { return false; }

    	};

    	JTableHeader header = table.getTableHeader();
	    header.setForeground(Color.BLACK);
		table.setRowSelectionAllowed(true);
	    dbList.setVisibleRowCount(5);

     	dbList.setFixedCellHeight(30);
     	dbList.setFixedCellWidth(230);
     	dbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     	//Creating a connection to MS Access and fetch errors using "try-catch" to check if it is successfully connected or not.
     	try {
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//final String fileName = "db\\lab_db_official.accdb";
			//String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+fileName;
			//db = "jdbc:odbc:mdbTEST";
			//con = DriverManager.getConnection(db,"","");
			//con = DriverManager.getConnection(url,"","");
			String url = "jdbc:mysql://localhost/lab_db";
			String username = "lab_admin";
			String password = "mypassword";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			//System.out.println("Connection Success");
			st = con.createStatement();
   		}catch (Exception e)
   		{
			JOptionPane.showMessageDialog(null,"Failed to Connect to Database","Error Connection", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
   		}

   		//select * from database
     	try {
			model.clear();
     		rs=st.executeQuery("select * from students ORDER BY firstName");
     		while (rs.next()) {
         		model.addElement(rs.getString("firstName") + " " + rs.getString("lastName"));
     		}
     		//dbList.setListData(studentarray.toArray());
     		array =  new String[model.getSize()]; //create string array
			model.copyInto(array); //put elements from datalistmodel into string array (update the array)


    	}catch (Exception e) {
     		System.out.println(e);
    	}

	  	//Constructing JPanel 1 and its property
	  	p1 = new JPanel();
	  	filter = new HintTextField("Search Filter...");
	  	filter.setPreferredSize(new Dimension(255,25));
		filter.addKeyListener(new TextHandler());
	  	pane2 = new JScrollPane(dbList);
	  	p1.add(filter);
	  	p1.add(pane2);
	  	p1.setBorder(BorderFactory.createTitledBorder("Student Database: "));
	  	p1.setPreferredSize(new Dimension(280,200));
	  	pane2.setPreferredSize(new Dimension(255,372));


	  	//Constructing JPanel 2 and its property
	  	p2 = new JPanel();
	  	p2.setPreferredSize(new Dimension(240, 280));
	  	p2.setBorder(BorderFactory.createTitledBorder("Controls: "));
	  	String howtouseinfo = "<html><font size = '5' face= 'courier'><b><u>Need Help?</u></b></font><font size = '4' face= 'courier'><p>- The list of registered students are located on the left.  If your name is not on the list you must make an account by clicking on the New Student button.<p>- Double click on your name and you will be prompted to enter your ID, enter your ID.<br>- On the right panel click Sign In<p>- When you are ready to sign out, double click on the row you last signed in<p>- The sign out button will then be enabled and you will be able to signout.</font></html>";
	  	JTextPane signinhelp = new JTextPane();
	  	signinhelp.setContentType("text/html");
	  	signinhelp.setText(howtouseinfo);
		JScrollPane signinhelpscroll = new JScrollPane(signinhelp);
		signinhelpscroll.setPreferredSize(new Dimension(225,238));
		signinhelp.setHighlighter(null);
		signinhelp.setEditable(false);
		signinhelp.setBackground(Color.WHITE);
		signinhelp.setCaretPosition(0);

	  	p3 = new JPanel();
	  	p3.setBorder(BorderFactory.createTitledBorder("Log Database: "));

	  	//Constructing all 5 JButtons using "for loop" and add it in JPanel 2
	  	p2.add(EmployeeSignInbtn);
	  	setBCStyle(EmployeeSignInbtn);
	  	EmployeeSignInbtn.setEnabled(false);

	  	for(int count=0; count<buttons.length; count++) {
			buttons[count] = new JButton(bLabel[count],new ImageIcon("blubt.jpg"));
	  	 	setBCStyle(buttons[count]);
			buttons[count].setEnabled(false);
			p2.add(buttons[count]);

 		}

 		p2.add(signinhelpscroll);

		//when you right click and click on table mouse event
		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)  {if(adminModeOn)check(evt);}
			public void mouseReleased(MouseEvent evt) {if(adminModeOn)check(evt);}

    		public void check(MouseEvent evt)
    		{
				try
				{
					if (evt.isPopupTrigger() && adminModeOn)
        			{
						int row = table.rowAtPoint( evt.getPoint() );
            			int column = table.columnAtPoint( evt.getPoint() );

            			if (! table.isRowSelected(row))
                			table.changeSelection(row, column, false, false);

            			popupmenutable.show(table, evt.getX(), evt.getY());
        			}
            	} catch (NullPointerException ex) {            }

    		}

  			public void mouseClicked(MouseEvent me)
  			{
  		 		if (me.getClickCount() == 1 && table.getSelectedRow() > -1)
  		 		{
					int selectedRowIndex = table.getSelectedRow();
					int selectedColumnIndex = table.getSelectedColumn();

					String signout = (String) tablemodel.getValueAt(selectedRowIndex, 3);
					if(signout==""|| signout==null)
					{
						buttons[1].setEnabled(true);
					}
					else
					{
						buttons[1].setEnabled(false);
					}
   				}
			}
		});


		//doubleclick jlist
		dbList.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2)
				{
					tablemodel.setRowCount(0);

					try
					{
						int count = 0;

/*						if(!adminModeOn)
						{
							idsecurity = (JOptionPane.showInputDialog(null, "Enter your Student ID:  ", "Security", JOptionPane.INFORMATION_MESSAGE));
						}else
						{
							idsecurity = studentid;
						}*/
						if(adminModeOn || employeeModeOn)
						{
							idsecurity = studentid;
						}
						else
						{
							idsecurity = (JOptionPane.showInputDialog(null, "Enter your Student ID:  ", "Security", JOptionPane.INFORMATION_MESSAGE));
						}

						PreparedStatement s = con.prepareStatement("SELECT * FROM STUDENTS WHERE id = ? and firstName = ? and lastName = ?");
						s.setString(1, idsecurity);
						s.setString(2, stringa);
						s.setString(3, stringb);
						s.execute();
						rs = s.getResultSet();

						while (rs.next()) {
							count++;
						}

      					if(count == 0)
      					{
							JOptionPane.showMessageDialog(null,"ID and Name Do Not Match! Try Again.","Error",JOptionPane.ERROR_MESSAGE);
						}
						else if(count == 1)
						{
							rs=st.executeQuery("select * from logs where id = '"+idsecurity+"' order by datestamp, signin");
							//rs=st.executeQuery("select * from logs where id = '"+idsecurity+"' UNION select * from employeelogs where id = '"+idsecurity+"'");

		                    tablemodel = buildTableModel(rs);
		                    table.setModel(tablemodel);
							int ctr = 0;

							//check if is employee
							boolean isEmployee = new dBhandler().checkEmployeeStatus(idsecurity);

							rs=st.executeQuery("select * from logs where signout is null and id = '"+idsecurity+"'");

							while(rs.next())
							{
								ctr++;
							}

							//check if employee log signout is null if not don't allow
							int ise = 0;
							rs=st.executeQuery("select * from employeelogs where signout is null and id = '"+idsecurity+"'");

							while(rs.next())
							{
								ise++;
							}

							//may have to remove ise counter and employeelog query works well for now :)
							if(ctr == 0 && ise == 0)
							{
								buttons[0].setEnabled(true);

								//check if employee if not disable
								if(isEmployee)
								{
									EmployeeSignInbtn.setEnabled(true);
								}
								else
								{
									EmployeeSignInbtn.setEnabled(false);
								}

								buttons[1].setEnabled(false);
							}
							else if(ctr > 0 || ise > 0)
							{
								buttons[0].setEnabled(false);
								EmployeeSignInbtn.setEnabled(false);
								//buttons[1].setEnabled(true);
							}
						}

					}
					catch (Exception err) {
						//JOptionPane.showMessageDialog(null,"ID and Name Do Not Match! Try Again.","Error",JOptionPane.ERROR_MESSAGE);
						System.out.println(err);
					}

     			me.consume();
    			}
			}
		});

		//make sure they can't scroll up or scroll down
        dbList.addKeyListener(new KeyListener()
        {
			//disable up and down keys security reasons (same reason when selection changed)
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }

            @Override
            public void keyTyped(KeyEvent e) { }

		});

		dbList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting())
				{
					if(dbList.getSelectedIndex() == -1)
					{
						buttons[0].setEnabled(false);
						buttons[1].setEnabled(false);
						EmployeeSignInbtn.setEnabled(false);
					}
					else
					{
						String[] str_array = dbList.getSelectedValue().toString().split(" "); // split full name so then you can search for logs
						stringa = str_array[0];
						stringb = str_array[1];

						//split fullname chose from list in order to search for id from students table, then display logs of sign in and outs when clicked on

						try
						{
							rs=st.executeQuery("select * from students where firstname = '"+stringa+"' and lastname = '"+stringb+"'");
							// String studentid="";
						 	while (rs.next())
						 	{
								//when you click on student get their id
						 		studentid = rs.getString("id");
						 	}
						 }catch(Exception errorsss){}
					}
				}
				else
				{
					//if selection changes make everything false and clear table to prevent signin bug
					buttons[0].setEnabled(false);
					buttons[1].setEnabled(false);
					EmployeeSignInbtn.setEnabled(false);
					//tablemodel.setRowCount(0);
				}
			}
		});


  		//Setting up the container ready for the components to be added.
     	JPanel pane = new JPanel();

     	//Setting up the container layout
     	GridLayout grid = new GridLayout(1,2);
     	pane.setLayout(new BorderLayout());


		//Implementing Event-Listener for Sign In
		EmployeeSignInbtn.addActionListener(new ActionListener()
		{

   			//Handle JButton event if it is clicked
   			public void actionPerformed(ActionEvent event)
   			{
   		  		try
   		  		{
					//call signin
					new dBhandler().Signin(table,tablemodel,idsecurity,adminModeOn);
					//copy logsignin into employeelogs
					EmployeeSignInbtn.setEnabled(false); //close connection
					buttons[0].setEnabled(false); //also disable it is uneeded to have dimmed
				}catch (Exception e) {
					System.out.println(e);
				}

				//employee signin
				try
				{
					new dBhandler().EmployeeSignin(table,tablemodel,idsecurity,adminModeOn);
					buttons[0].setEnabled(false); //also disable it is uneeded to have dimmed
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
  	 		}
  		});

		//Implementing Event-Listener for Sign In
		buttons[0].addActionListener(new ActionListener()
		{

   			//Handle JButton event if it is clicked
   			public void actionPerformed(ActionEvent event)
   			{
   		  		try
   		  		{
					new dBhandler().Signin(table,tablemodel,idsecurity,adminModeOn);
					buttons[0].setEnabled(false); //close connection
					EmployeeSignInbtn.setEnabled(false); //close connection
				}catch (Exception e) {
					System.out.println(e);
				}

  	 		}
  		});

  		//Implemeting Even-Listener on JButton buttons[3] which is Sign Out
  		buttons[1].addActionListener(new ActionListener()
  		{
			//Handle JButton event if it is clicked
   			public void actionPerformed(ActionEvent event)
   			{
				try
				{
					int selectedRowIndex = table.getSelectedRow();
					int selectedColumnIndex = table.getSelectedColumn();

					//String idc = studentid;
					String datec = (String) tablemodel.getValueAt(selectedRowIndex, 1);
					String signinc = (String) tablemodel.getValueAt(selectedRowIndex, 2);

					new dBhandler().Signout(table,tablemodel,idsecurity,datec,signinc);

					buttons[0].setEnabled(false); //close connection
					buttons[1].setEnabled(false); //close connection
					EmployeeSignInbtn.setEnabled(false); //close connection

    			}catch(Exception e){
     				System.out.println(e);
    			}
   			}
  		});


  		//updatemenuitem on rightclick action, take in new values and pass them to updatefunction inside of database
  		updaterow.addActionListener(new ActionListener()
  		{
			//Handle JButton event if it is clicked
   			public void actionPerformed(ActionEvent event)
   			{
     			try
     			{
					int selectedRowIndex = table.getSelectedRow();
					int selectedColumnIndex = table.getSelectedColumn();

					//hold old values
					//String idco = (String) table.getValueAt(selectedRowIndex, 0);
					String dateco = (String) table.getValueAt(selectedRowIndex, 1);
					String signinco = (String) table.getValueAt(selectedRowIndex, 2);
					String signouto = (String) table.getValueAt(selectedRowIndex, 3);

     				JTextField idField = new JTextField(10);
     				idField.setText(idsecurity);
     				/*JTextField dateField = new JTextField(10);
	 				dateField.setText(dateco);
	 				JTextField siField = new JTextField(10);
     				siField.setText(signinco);
     				JTextField soField = new JTextField(10);
	 				soField.setText(signouto);*/

					//date chooser
					final SimpleDateFormat dst = new SimpleDateFormat("MM/dd/yyyy");
					Calendar c1 = Calendar.getInstance();
					c1.setTime(dst.parse(dateco));
					datestamptxt = dst.format(c1.getTime());

					JDateChooser chooser1 = new JDateChooser("MM/dd/yyyy", "####/##/##", '_');
					chooser1.setDate(c1.getTime());
					chooser1.getDateEditor().addPropertyChangeListener(
						new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent e) {
							if ("date".equals(e.getPropertyName())) {
								//System.out.println( dst.format((Date) e.getNewValue()) );
								datestamptxt = dst.format((java.util.Date) e.getNewValue());
							}
						}
					});

					//time format
					SimpleDateFormat tst = new SimpleDateFormat("hh:mm a");
					java.util.Date x = tst.parse(signinco);
					java.util.Date y = tst.parse(signouto);

					//signin
					JSpinner timeSpinner1 = new JSpinner( new SpinnerDateModel() );
					JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(timeSpinner1, "hh:mm a");
					timeSpinner1.setEditor(timeEditor1);
					timeSpinner1.setValue(x); // will only show the current time

					//signout
					JSpinner timeSpinner2 = new JSpinner( new SpinnerDateModel() );
					JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(timeSpinner2, "hh:mm a");
					timeSpinner2.setEditor(timeEditor2);
					timeSpinner2.setValue(y); // will only show the current time

      				Object[] options =
      				{
						"Update Log",
						"Add to Employee Logs",
						"Remove from Employee Logs",
						"",
						"",
					};

      				JComboBox optionbox = new JComboBox(options);
      				JPanel myPanel = new JPanel(new GridLayout(5,5));
					myPanel.add(new JLabel("ID:"));
					myPanel.add(idField);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("Date:"));
					myPanel.add(chooser1);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("Sign In:"));
					myPanel.add(timeSpinner1);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("Sign Out:"));
					myPanel.add(timeSpinner2);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("Action:"));
					myPanel.add(optionbox);

      				int result = JOptionPane.showConfirmDialog(null, myPanel,"Update Row...", JOptionPane.OK_CANCEL_OPTION);
      				if (result == JOptionPane.OK_OPTION)
      				{
						int choice = optionbox.getSelectedIndex();
						String olddata [] = {idsecurity,dateco,signinco,signouto};
						String newdata [] = {idField.getText(),datestamptxt.toString(),tst.format(timeSpinner1.getValue()),tst.format(timeSpinner2.getValue())};
						new dBhandler().ManageLogs(table,tablemodel,olddata,newdata,choice);
     				}

    			} catch (Exception e) {
    				System.out.println(e);
    			}
   			}
  		});

  		//deleterowmenuitem on rightclick
  		deleterow.addActionListener(new ActionListener()
  		{
			//Handle JButton event if it is clicked
   			public void actionPerformed(ActionEvent event)
   			{
				try
				{
					int selectedRowIndex = table.getSelectedRow();
					int selectedColumnIndex = table.getSelectedColumn();

					//String idc = (String) tablemodel.getValueAt(selectedRowIndex, 0);
					String datec = (String) tablemodel.getValueAt(selectedRowIndex, 1);
					String signinc = (String) tablemodel.getValueAt(selectedRowIndex, 2);
					String signoutc = (String) tablemodel.getValueAt(selectedRowIndex, 3);
					//String log = "" + idc + " " + datec + " " + signinc + " " + signoutc + "";
					//int result = JOptionPane.showConfirmDialog(null,"Are you sure you would like to delete this log from the database?\n\n" + "" + "","Deleting..." , JOptionPane.YES_NO_OPTION);
					int result = JOptionPane.showConfirmDialog(null,"Are you sure you would like to delete this log from the database?","Deleting..." , JOptionPane.YES_NO_OPTION);
					if(result == 0)
					{
						new dBhandler().deleteLogs(selectedRowIndex,table,tablemodel,idsecurity,datec,signinc);
					}

    			}catch (Exception e){
     				System.out.println(e);
    			}
   			}
  		});

		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					int selectedRowIndex = table.getSelectedRow();
					int selectedColumnIndex = table.getSelectedColumn();

					JTextField idField = new JTextField(10);
					idField.setText(studentid);
					JTextField fField = new JTextField(10);
					fField.setText(stringa);
					JTextField lField = new JTextField(10);
					lField.setText(stringb);
					JPanel myPanel = new JPanel(new GridLayout(4,4));
					myPanel.add(new JLabel("ID:"));
					myPanel.add(idField);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("First Name:"));
					myPanel.add(fField);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("Last Name:"));
					myPanel.add(lField);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer

      				Object[] options = {
	                    "Update Profile",
	                    "Update Profile and Logs",
	                    "Delete Profile",
	                    "Delete Profile and Logs",
	                    "Add to Employee List",
	                    "Remove from Employee List",
	                };

	                JComboBox optionList = new JComboBox(options);
					myPanel.add(new JLabel("Action:  "));
	                myPanel.add(optionList);

					int result = JOptionPane.showOptionDialog(null, myPanel,"Student Profile Management", JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE,null,null,null);
      				if (result == 0)
      				{
						int choice = optionList.getSelectedIndex();
						//System.out.println(choice);
						String olddata [] = {studentid,stringa,stringb};
						String newdata [] = {idField.getText(),fField.getText(),lField.getText()};
						new dBhandler().ManageStudents(table,tablemodel,dbList,model,olddata,newdata,choice);
					}
				}catch(Exception e){}

				updateArray();
			}
 	 	});

		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);

  		//create table and add logs
		try
		{
			//add table to p3
			//ResultSet rs = st.executeQuery("select * from logs");
			p3.add(new JScrollPane(table)
			{
				{
					setOpaque(false);
					setViewportView(table);
					getViewport().setBackground(Color.WHITE);
					//getViewport().setOpaque(false);
				}

	    	});

    	table.setBackground(Color.WHITE);
    	//tablemodel.setBackground(Color.WHITE);

		}catch(Exception e){
		}

	//multiselection option possibly
	/*	dbList.setSelectionModel(new DefaultListSelectionModel() {
    private static final long serialVersionUID = 1L;

    boolean gestureStarted = false;

    @Override
    public void setSelectionInterval(int index0, int index1) {
        if(!gestureStarted){
            if (isSelectedIndex(index0)) {
                super.removeSelectionInterval(index0, index1);
            } else {
                super.addSelectionInterval(index0, index1);
            }
        }
        gestureStarted = true;
    }

    @Override
    public void setValueIsAdjusting(boolean isAdjusting) {
        if (isAdjusting == false) {
            gestureStarted = false;
        }
    }

});*/
     	//Adding components to the container
  		pane.add(p1,BorderLayout.WEST);
  		pane.add(p3,BorderLayout.CENTER);
  		pane.add(p2,BorderLayout.EAST);
  		add(pane);
    }

	public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException //static
	{
		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
        	columnNames.add(metaData.getColumnName(column));
    	}

    	// data of the table
    	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    	while (rs.next())
    	{
        	Vector<Object> vector = new Vector<Object>();
        	for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
         	   if(columnIndex == 1 && !adminModeOn)
         	   //if(columnIndex == 1)
         	   	 vector.add(MaskID(rs.getString(columnIndex)).toString());
         	   else
         	   	 vector.add(rs.getString(columnIndex)); //original rs.getObject(columnIndex)
        	}
        	data.add(vector);
    	}
    	return new DefaultTableModel(data, columnNames);
	}

    public JButton setBCStyle(JButton button)
    {
		button.setPreferredSize(new Dimension(220, 50));
		button.setMaximumSize(new Dimension(200, 50));
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.CENTER);

		return button;
	}

	public DefaultListModel get_jlistmodel()
	{
		return model;
	}

	public JList get_jlist()
	{
		return dbList;
	}

  	public void adminMode()
  	{
		adminModeOn = true;
		x.AdminMenu.setVisible(true); //get adminmenu from parentclass which is mainmenu and set it to true
		x.importAction.setVisible(true);
		tablemodel.setRowCount(0);
	  	table.setModel(tablemodel);
	  	buttons[0].setEnabled(false);
	  	buttons[1].setEnabled(false);
	  	EmployeeSignInbtn.setEnabled(false);

	  	//check if idle for 5 minutes, if true LogOutAction
	  	Action logout = new LogOutAction();
		InactivityListener listener = new InactivityListener(logout, 5); //get adminmenu from parentclass which is mainmenu and set it to false after 5 mins
		listener.start();
  	}

  	public void employeeMode()
  	{
		adminModeOn = false;
		employeeModeOn = true;
		x.AdminMenu.setVisible(true);
		x.importAction.setVisible(false);
		tablemodel.setRowCount(0);
	  	table.setModel(tablemodel);

	  	buttons[0].setEnabled(false);
	  	buttons[1].setEnabled(false);
	  	EmployeeSignInbtn.setEnabled(false);

  		Action logout = new LogOutAction();
		InactivityListener listener = new InactivityListener(logout, 5); //get adminmenu from parentclass which is mainmenu and set it to false after 5 mins
		listener.start();
  	}

  	class LogOutAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			guestMode();
			JOptionPane.showMessageDialog(null,"You have successfully logged out!","Logged Out",JOptionPane.INFORMATION_MESSAGE);
		}
	}


  	public void guestMode()
  	{
		adminModeOn = false;
		employeeModeOn = false;
		x.AdminMenu.setVisible(false);
	  	tablemodel.setRowCount(0);
	  	table.setModel(tablemodel);

	  	buttons[0].setEnabled(false);
	  	buttons[1].setEnabled(false);
	  	EmployeeSignInbtn.setEnabled(false);

  	}

  	public void rollback()
  	{
		try{
			new dBhandler().rollback(table,tablemodel);
	  	}catch(Exception e){}
	}

	public void commit()
	{
		try{
			new dBhandler().commit(table,tablemodel);
	  	}catch(Exception e){}
	}

	//popopclass when rightclick on jlist
	class PopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)  {if(adminModeOn)check(e);}
		public void mouseReleased(MouseEvent e) {if(adminModeOn)check(e);}

		public void check(MouseEvent e)
		{
			if (e.isPopupTrigger() && adminModeOn) { //if the event shows the menu
        		dbList.setSelectedIndex(dbList.locationToIndex(e.getPoint())); //select the item
        		popupMenu.show(dbList, e.getX(), e.getY()); //and show the menu
    		}
    	}
	}

	public void initiateSearch(String lookFor){
		Vector<String> matches = new Vector<String>();
        //String [] array =  new String[model.getSize()]; //create string array
        lookFor = lookFor.toString();
        //model.copyInto(array); //put elements from datalistmodel into string array (update the array)
        for(String each : array){
            if(each.contains(lookFor)){
                matches.add(each);
                //System.out.println("Match: " + each);
            }
        }
        this.repaint();

        if(matches.size()!=0){
            dbList.setListData(matches);
            this.searchResult = dbList;
            this.pane2 = pane2;
        }else{
            matches.add("No Match Found");
            dbList.setListData(matches);
            this.searchResult = dbList;
            this.pane2 = pane2;
        }

    }

    class TextHandler implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e){

        }

        @Override
        public void keyPressed(KeyEvent e){
            //if(e.getKeyChar() == '\n'){
                initiateSearch(filter.getText());
            //}
        }

        @Override
        public void keyReleased(KeyEvent e){

        }
    }
	class HintTextField extends JTextField implements FocusListener {

		private final String hint;

		public HintTextField(final String hint) {
			super(hint);
			this.hint = hint;
			super.addFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if(this.getText().isEmpty()) {
				super.setText("");
			}
		}
		@Override
		public void focusLost(FocusEvent e) {
			if(this.getText().isEmpty()) {
				super.setText(hint);
			}
		}

		@Override
		public String getText() {
			String typed = super.getText();
			return typed.equals(hint) ? "" : typed;
		}
	}

	public String MaskID(String in)
	{
		final char[] ca = in.toCharArray();
		Arrays.fill(ca, 0, in.length(), 'X');
		return new String(ca);
	}

	public void updateArray() //update autosuggest for dbList stuff elements from jlist into array
	{
		//filter.setText(""); //possible solution to updates
		//dbList.setModel(model); //possible solution to updates
		array = new String[model.getSize()];
		model.copyInto(array);
		//for(int i = 0; i < model.getSize(); i++)
		//{
		//	System.out.println(array[i]);
		//}

	}//there is a bug in autocomplete where when you update the last update does not become removed it is still there, fix this by clicking on the registered button

}
