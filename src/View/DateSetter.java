package View;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DateSetter extends JPanel {
    JTextField day;
    JTextField month;
    JTextField year;
    JTextField hour;
    JTextField minute;

    DateSetter() {
        var date = new GregorianCalendar();
        day = new JTextField(String.valueOf(date.get(Calendar.DAY_OF_MONTH)), 2);
        month = new JTextField(String.valueOf(date.get(Calendar.MONTH) + 1), 2);
        year = new JTextField(String.valueOf(date.get(Calendar.YEAR)), 4);
        hour = new JTextField(String.valueOf(date.get(Calendar.HOUR_OF_DAY)), 2);
        minute = new JTextField(String.valueOf(date.get(Calendar.MINUTE)), 2);

        add(day);
        add(new JLabel("."));
        add(month);
        add(new JLabel("."));
        add(year);
        add(new JLabel("г. "));
        add(hour);
        add(new JLabel(":"));
        add(minute);

        setLayout(new FlowLayout(FlowLayout.LEADING));
    }

    public GregorianCalendar getDate() {
        return new GregorianCalendar(
                Integer.valueOf(year.getText()),
                Integer.valueOf(month.getText()) - 1,
                Integer.valueOf(day.getText()),
                Integer.valueOf(hour.getText()),
                Integer.valueOf(minute.getText()));
    }
}
