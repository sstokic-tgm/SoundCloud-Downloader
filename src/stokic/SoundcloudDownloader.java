package stokic;

import de.voidplus.soundcloud.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class SoundcloudDownloader extends Observable implements Runnable {

	private SoundCloud sc;
	private String[] splittedUrl;
	private ArrayList<Track> tracks;
	private String trackUrl;
	private boolean succeed = false;

	public SoundcloudDownloader() {

		sc = new SoundCloud("CLIENT_ID", "CLIENT_SECRET");
	}

	private void analyzeUrl(String trackUrl) {

		splittedUrl = trackUrl.split("[/]+");
	}
	public boolean validUrl(String trackUrl) {
		
		analyzeUrl(trackUrl);
		
		try {
			
			tracks = searchTrack();
			return true;
			
		}
		catch(Exception e) 
		{
			return false;
		}
		
	}
	
	public void prepareDownload(String trackUrl) {

		this.trackUrl = trackUrl;
	}

	private ArrayList<Track> searchTrack() {

		return sc.findTrack(splittedUrl[3]);
	}

	private boolean startDownload() {

		analyzeUrl(trackUrl);

		tracks = searchTrack();

		if(tracks != null) {

			if(download() == true) {

				succeed = true;
			}
			else {

				succeed = false;
			}
		}
		return succeed;
	}

	private boolean download() {

		String track = ((Track)tracks.get(0)).getStreamUrl();
		String fileName = splittedUrl[3] + ".mp3";

		try {

			File f = new File("./Downloads/");
			if(!f.exists()) {
				
				f.mkdir();
			}
			
			URL url = new URL(track);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

			float filesize = connection.getContentLength();
			float totalDataRead = 0.0F;

			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream("./Downloads/" + fileName);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

			byte[] data = new byte[1024];
			int i = 0;
			float progress = 0;

			while((i = in.read(data, 0, 1024)) >= 0) {

				totalDataRead += i;
				bout.write(data, 0, i);
				progress = totalDataRead * 100.0F / filesize;

				this.setChanged();
				this.notifyObservers(progress);
			}
			bout.close();
			fos.close();
			in.close();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public void run() {


		if(startDownload() == true) {
			try {

				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		else
		{
			try {

				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}
}
