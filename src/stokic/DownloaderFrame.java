package stokic;

import javax.swing.*;

public class DownloaderFrame extends JFrame {

	private DownloaderPanel panel;

	public DownloaderFrame() {

		super("SoundCloud Downloader");
		panel = new DownloaderPanel(this);
		this.add(panel);
	}
}
