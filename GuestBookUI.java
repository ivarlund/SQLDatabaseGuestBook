
/**
 * GUI implementation of a Guest book for name, email, website, and comment entries.
 * Is connected to a MySQL server and collects data from server and presents it in the GUI.
 * Censures any entries containing HTML code.
 * 
 * @author Ivar Lund
 * ivarnilslund@gmail.com
 * 
 */
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Main class and JFrame container for guest book.
 * 
 * @author Ivar Lund
 * 
 */
@SuppressWarnings("serial")
public class GuestBookUI extends JFrame {

	private GuestBook gb = new GuestBook();

	/**
	 * Class constructor. Sets up JFrame container for guest book and title.
	 */
	public GuestBookUI() {
		super("Connected to: 'MySQL' on: 'atlas.dsv.su.se'");
		add(gb, BorderLayout.CENTER);

		setSize(720, 540);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		new GuestBookUI();
	}
}
