package tuanpv.autest.frame;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PN0002 extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel label;

	public PN0002() {
		super(null);
		setPreferredSize(new Dimension(640, 480));

		// add additional item
		createLayout();
	}

	private void createLayout() {
		label = new JLabel("AUTest Suite");
		label.setVisible(true);

		add(label);
		label.setLocation(100, 100);
	}
}
