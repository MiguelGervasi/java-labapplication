


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;


import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.FontPolicy;
import com.jgoodies.looks.FontSet;
import com.jgoodies.looks.FontSets;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JLocaleChooser;
import com.toedter.components.JSpinField;
import com.toedter.components.JTitlePanel;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.util.*;

public class JDateExample
{
	String startdate;
	String enddate;

	public JDateExample()
	{


		SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		startdate = date_format.format(c1.getTime());
		enddate = date_format.format(c2.getTime());

		JFrame f = new JFrame();
		f.setVisible(true);
		f.setSize(400,400);
		JPanel p = new JPanel();
		//p.setLayout(new FlowLayout());
		p.setPreferredSize(new Dimension(50,20));

		final SimpleDateFormat dst = new SimpleDateFormat("MM/dd/yyyy");
		JDateChooser chooser = new JDateChooser("MM/dd/yyyy", "####/##/##", '_');
		chooser.setDate(c1.getTime());
		//chooser.setPreferredSize(new Dimension(150,25));
		chooser.getDateEditor().addPropertyChangeListener(
    new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if ("date".equals(e.getPropertyName())) {
                System.out.println( dst.format((Date) e.getNewValue()) );
                startdate = dst.format((Date) e.getNewValue());
            }
        }
    });
		p.add(new JLabel("Start Date:  "));
		p.add(chooser);


		JDateChooser chooser2 = new JDateChooser("MM/dd/yyyy", "####/##/##", '_');
		chooser2.setDate(c2.getTime());
		chooser2.getDateEditor().addPropertyChangeListener(
    	new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if ("date".equals(e.getPropertyName())) {
                System.out.println( dst.format((Date) e.getNewValue()) );
                enddate = dst.format((Date) e.getNewValue());

            }
        }
    });

		p.add(new JLabel("End Date:  "));
		p.add(chooser2);
		f.add(p);


	}

	public static void main(String[] args)
	{
		new JDateExample();
	}


}