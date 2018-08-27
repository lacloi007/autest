package tuanpv.autest.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class FR0001 extends JFrame {
	private static final long serialVersionUID = 1L;
	private ActionListener clickNew, clickOpen, clickSave, clickSaveAs;
	private JPanel panel;

	public FR0001() {
		super("AUTest");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// additional action
		createMenuBar();

		// add additional item
		createLayout();
	}

	private void createLayout() {
		panel = new PN0000();

		// add panel to frame
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// create File menu
		JMenu menu = new JMenu("AUTest");
		menu.setMnemonic(KeyEvent.VK_T);

		// add new menu
		JMenuItem mItem = new JMenuItem("Configuration");
		mItem.setMnemonic(KeyEvent.VK_C);
		mItem.addActionListener(null);
		menu.add(mItem);

		// add menu
		menuBar.add(menu);

		setJMenuBar(menuBar);
	}

}
