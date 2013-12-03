import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;

public class AutoCompleteJList extends JPanel{
    JTextField city = new JTextField(10);
    String enteredName = null;
    ArrayList<String> cities = new <String>ArrayList();
    JList dbList = new JList();
    JList searchResult;
    JScrollPane pane = new JScrollPane(dbList);

//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
    public AutoCompleteJList(ArrayList<String> arr){
        setLayout(new java.awt.FlowLayout());
        cities = new <String>ArrayList(arr);
        dbList.setListData(cities.toArray());
        add(city);
        add(pane);
        pane.setPreferredSize(new Dimension(200,200));
	    dbList.setVisibleRowCount(5);
     	dbList.setFixedCellHeight(30);
     	dbList.setFixedCellWidth(230);
     	dbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

       //pack();
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        city.addKeyListener(new TextHandler());
    }
//------------------------------------------------------------------------------
public void initiateSearch(String lookFor){

    //add the following two statements to set the width of the list.
    //int newWidth = AutoCompleteTest.this.getSize().width;
    //dbList.setPreferredSize(new Dimension(newWidth, dbList.getPreferredSize().height));

        Vector<String> matches = new Vector<String>();
        lookFor = lookFor.toString();
        for(String each : cities){
            if(each.contains(lookFor)){
                matches.add(each);
                //System.out.println("Match: " + each);
            }
        }
        this.repaint();

        if(matches.size()!=0){
            dbList.setListData(matches);
            this.searchResult = dbList;
            this.pane = pane;
        }else{
            matches.add("No Match Found");
            dbList.setListData(matches);
            this.searchResult = dbList;
            this.pane = pane;
        }

    }

//------------------------------------------------------------------------------

    class TextHandler implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e){

        }

        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyChar() == '\n'){
                initiateSearch(city.getText());
            }
        }

        @Override
        public void keyReleased(KeyEvent e){

        }
    }
//------------------------------------------------------------------------------
}