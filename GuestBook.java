import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Main worker class. Sets up GUI for the Guest book and allows for
 * communication with the specified database.
 *
 * @author Ivar Lund
 * ivarnilslund@gmail.com
 * 
 */
@SuppressWarnings("serial")
public class GuestBook extends JPanel {

	JTextArea display;
	JTextField name = new JTextField(15);
	JTextField email = new JTextField(15);
	JTextField web = new JTextField(15);
	JTextField comment = new JTextField(15);

	Statement myStmt;

	/**
	 * Class constructor. Sets up GUI and connection and updates text field to
	 * display DB data.
	 */
	public GuestBook() {
		JPanel top = new JPanel();
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		JPanel bottom = new JPanel();
		
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		bottom.setLayout(new BorderLayout());
		
		display = new JTextArea(20, 60);
		display.setEditable(false);
		JScrollPane pane = new JScrollPane(display);

		JButton insert = new JButton("Click to insert!");
		insert.addActionListener(new InsertListener());

		left.add(new JLabel("Name: "));
		left.add(new JLabel("Email: "));
		left.add(new JLabel("Web: "));
		left.add(new JLabel("Comment: "));
		right.add(name);
		right.add(email);
		right.add(web);
		right.add(comment);
		bottom.add(insert);

		top.add(left);
		top.add(right);
		top.add(bottom);

		add(top, BorderLayout.NORTH);
		add(pane, BorderLayout.SOUTH);

		setupConn();
		getResult();
	}

	/**
	 * Getter for JTextField 'name'
	 */
	public String getName() {
		return name.getText();
	}

	/**
	 * Getter for JTextField 'email'
	 */
	public String getEmail() {
		return email.getText();
	}

	/**
	 * Getter for JTextField 'web'
	 */
	public String getWeb() {
		return web.getText();
	}

	/**
	 * Getter for JTextField 'comment'
	 */
	public String getComment() {
		return comment.getText();
	}

	/**
	 * Sets up connection to server and initiates statement object for use.
	 */
	public void setupConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
			Connection conn = DriverManager.getConnection("SQL SRV URL", "USERNAME",
					"PASSWORD");
			myStmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets entry data from connected db. Calls testResult for pattern of HTML code
	 * in every entry.
	 */
	public void getResult() {
		ResultSet result;
		try {
			result = myStmt.executeQuery("select * from SQLGuestbook");
			ResultSetMetaData data = result.getMetaData();

			while (result.next()) {
				updateDisplay("Entry: " + result.getRow());
				for (int i = 1; i < data.getColumnCount() + 1; i++) {
					String column = data.getColumnName(i);
					String value = testResult(result.getString(i));

					updateDisplay(column + ": " + value);
				}
				updateDisplay("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method testing Strings for HTML code patterns. If the string fails the test
	 * it is censured.
	 * 
	 * @param str element to be tested
	 * @return String of test result
	 */
	public String testResult(String str) {
		Pattern p = Pattern.compile("<.*>");
		Matcher m = p.matcher(str);
		String result = m.find() ? "CENSUR" : str;

		return result;
	}

	/**
	 * Updates display.
	 * 
	 * @param txt String to update with.
	 */
	public void updateDisplay(String txt) {
		display.append(txt + "\n");
		display.setCaretPosition(display.getDocument().getLength());
	}

	/**
	 * Insert an entry to the connected db. Uses data from JTextFields.
	 */
	public void insertStmt() {
		try {
			myStmt.executeUpdate("INSERT INTO SQLGuestbook (`Namn`, `Epost`, `Hemsida`, `Kommentar`)" + "VALUES ('"
					+ getName() + "', '" + getEmail() + "', '" + getWeb() + "', '" + getComment() + "')");
			getResult();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ActionListener for insert button.
	 * 
	 * @author Ivar Lund
	 *
	 */
	class InsertListener implements ActionListener {
		/**
		 * Method call for ActionListener.
		 */
		public void actionPerformed(ActionEvent e) {
			insertStmt();
		}
	}

}
