package tuanpv.autest.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;

public class PN0000 extends JPanel {
	private int seperate = 5;
	private static final long serialVersionUID = 1L;

	private JLabel lblFile;
	private JButton button;

	private Border borderBlue = BorderFactory.createLineBorder(Color.BLUE, 1);
	private Border borderBlack = BorderFactory.createLineBorder(Color.BLACK, 1);

	public PN0000() {
		super(null);
		setPreferredSize(new Dimension(640, 480));

		// add additional item
		createLayout();
	}

	private void createLayout() {
		// create lable file
		lblFile = createLabel("", seperate * 2 + 150, seperate, 400, 30, true);
		lblFile.setBackground(Color.WHITE);
		lblFile.setOpaque(true);
		add(lblFile);

		// create button choose file
		button = new JButton("Browse");
		button.setBounds(seperate * 3 + 150 + 400, seperate, 100, 30);
		button.addActionListener((ActionEvent event) -> {
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int status = fileChooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				lblFile.setText(selectedFile.getAbsolutePath());
			} else if (status == JFileChooser.CANCEL_OPTION) {
				System.out.println("calceled");
			}
		});
		add(button);

		// create button
		button = new JButton("New");
		button.setBounds(seperate * 3 + 150 + 400, seperate, 100, 30);

		// add to panel
		add(createLabel("AUTest Configuration", seperate, seperate, 150, 30, false));

		// create file choose

	}

	private JLabel createLabel(String content, int x, int y, int width, int height, boolean isBorder) {
		JLabel label = new JLabel(content);
		label.setVisible(true);
		label.setBounds(x, y, width, height);
		if (isBorder)
			label.setBorder(borderBlack);
		else
			label.setBorder(borderBlue);
		return label;
	}
}
