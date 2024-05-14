package View;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.User;

public class StatePanel extends JPanel {
    private boolean isOnline = false;
    private User user;
    private JLabel isOnlineLabel = new JLabel();
    private JLabel userLabel = new JLabel();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        userLabel.setText(user == null ? "Войдите в аккаунт" : user.name);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        isOnlineLabel.setText(isOnline ? "Онлайн" : "Офлайн");
    }

    public StatePanel(boolean isOnline, User user) {
        isOnlineLabel.setVisible(true);
        userLabel.setVisible(true);

        setOnline(isOnline);
        setUser(user);

        setBorder(new EmptyBorder(100, 500, 100, 500));
        setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
        add(isOnlineLabel);
        add(userLabel);
    }
}
