package stokic;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class DownloaderPanel extends JPanel implements Observer {

	private SoundcloudDownloader sd;
	private JProgressBar progressBar;
	private JButton download, close;
	private JTextField tf;
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItemInfo;

	public DownloaderPanel(JFrame frame) {

		this.frame = frame;

		sd = new SoundcloudDownloader();
		sd.addObserver(this);
		setupPanel();
		setupHandler();
	}

	public void setupPanel() {

		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		JPanel tfPanel = new JPanel();
		JPanel progressBarPanel = new JPanel();
		JPanel closePanel = new JPanel();
		JPanel downloadPanel = new JPanel();

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());


		menuBar = new JMenuBar();
		menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(menu);

		menuItemInfo = new JMenuItem("Info");
		menu.add(menuItemInfo);


		download = new JButton("Download");
		close = new JButton("Close");
		progressBar = new JProgressBar();
		tf = new JTextField("SoundCloud link of the song");

		tf.setPreferredSize(new Dimension(380, 25));
		progressBar.setPreferredSize(new Dimension(380, 25));

		closePanel.add(close);
		downloadPanel.add(download);
		buttonsPanel.add(closePanel, BorderLayout.LINE_START);
		buttonsPanel.add(downloadPanel, BorderLayout.LINE_END);

		tfPanel.add(tf);
		progressBarPanel.add(progressBar);
		panel.add(tfPanel, BorderLayout.PAGE_START);
		panel.add(progressBarPanel, BorderLayout.CENTER);

		frame.setJMenuBar(menuBar);

		this.add(panel, BorderLayout.PAGE_START);
		this.add(buttonsPanel, BorderLayout.CENTER);

	}

	public void setupHandler() {

		ActionHandler ah = new ActionHandler();
		download.addActionListener(ah);
		close.addActionListener(ah);

		menuItemInfo.addActionListener(ah);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		int progress = 0;
		progress = (int)(float)arg1;

		progressBar.setValue(progress);
		progressBar.setStringPainted(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if(progress == 100) {

			setCursor(null);
			download.setEnabled(true);
			close.setEnabled(true);
			JOptionPane.showMessageDialog(frame, "Download finished!", "Info", JOptionPane.INFORMATION_MESSAGE);
			progressBar.setValue(0);
		}
	}

	public class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == download && !tf.getText().isEmpty() && sd.validUrl(tf.getText())) {


				sd.prepareDownload(tf.getText());

				Thread downloadThread = new Thread(sd);
				downloadThread.start();

				download.setEnabled(false);
				close.setEnabled(false);
			}
			else if(e.getSource() == close) {

				frame.dispose();
			}
			else if(e.getSource() == menuItemInfo) {

				JOptionPane.showMessageDialog(frame, "Created by Stefan Stokic\n4AHIT TGM\n\n", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			else {

				JOptionPane.showMessageDialog(frame, "Link is not valid or no link entered!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
