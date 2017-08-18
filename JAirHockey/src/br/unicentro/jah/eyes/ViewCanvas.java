package br.unicentro.jah.eyes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import br.unicentro.jah.client.TrackWindow;

public class ViewCanvas extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;
	private volatile boolean running = true;
	
	private OpenCamera camera;
	private Mat image[] = new Mat[3];
	
	public static final double RESOLUTION_FACTOR = 2;
	
	public static PiecePlayer player1 = new PiecePlayer(
			"Player1", 
			TrackWindow.HSV_LOWER_BLUE, 
			TrackWindow.HSV_UPPER_BLUE,
			Color.BLUE
		);
	
	public static PiecePlayer player2 = new PiecePlayer(
			"Player2", 
			TrackWindow.HSV_LOWER_RED, 
			TrackWindow.HSV_UPPER_RED,
			Color.RED
		);
			
	public static PieceBall ball = new PieceBall();
	public static PieceTable table = new PieceTable();
	
	public ViewCanvas(OpenCamera camera) 
	{
		this.camera = camera;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void paint(Graphics g) 
	{
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(image[0] != null && image[0].width() > 0) {
			BufferedImage rawImage1 = OCVUtil.MatToBufferedImage(image[0]);
			g.drawImage(rawImage1, 0, 250, null);
			
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 250, 150, 90);
			
			g.setColor(Color.RED);
			g.drawRect(0, 250+100, image[0].width(), image[0].height()-140);
			
//			g.setColor(player1.getBorderColor());
//			g.drawString(player1.toString(), 10, 270);
//			g.drawOval((int)player1.getX()-8, 250+(int)player1.getY()-8, 16, 16);
//			g.fillOval((int)player1.getX()-3, 250+(int)player1.getY()-3, 6, 6);
			
			g.setColor(player2.getBorderColor());
			g.drawString(player2.toString(), 10, 290);
			g.drawOval((int)player2.getX()-8, 250+(int)player2.getY()-8, 16, 16);
			g.fillOval((int)player2.getX()-3, 250+(int)player2.getY()-3, 6, 6);
			
			g.setColor(ball.getBorderColor());
			g.drawString(ball.toString(), 10, 310);
			g.drawOval((int)ball.getX()-8, 250+(int)ball.getY()-8, 16, 16);
			g.fillOval((int)ball.getX()-3, 250+(int)ball.getY()-3, 6, 6);
			
			g.setColor(table.getBorderColor());
			g.drawString(table.toString(), 10, 330);
			g.drawOval((int)table.getX()-8, 250+(int)table.getY()-8, 16, 16);
			g.fillOval((int)table.getX()-3, 250+(int)table.getY()-3, 6, 6);
		}
		
		if(image[1] != null && image[1].width() > 0) {
			BufferedImage rawImage2 = OCVUtil.MatToBufferedImage(image[1]);
			g.drawImage(rawImage2, 0, 0, rawImage2.getWidth(), rawImage2.getHeight(), null);
		}
		
		if(image[2] != null && image[2].width() > 0) {
			BufferedImage rawImage2 = OCVUtil.MatToBufferedImage(image[2]);
			g.drawImage(rawImage2, 314, 0, rawImage2.getWidth(), rawImage2.getHeight(), null);
		}
	}

	
	@Override
	public void run() 
	{
		image[0] = new Mat(); // raw
		image[1] = new Mat(); // hsv
		image[2] = new Mat(); // mask
		
		while(running)
		{
			image[0] = camera.read();
			image[1] = OCVUtil.equalizeIntensity(image[0]);
			
			Imgproc.resize(image[1], image[1], new Size(image[0].width()/RESOLUTION_FACTOR, image[0].height()/RESOLUTION_FACTOR));
			image[1] = image[1].submat(new Rect(0, 50, image[1].width(), image[1].height()-70));
			
			Imgproc.GaussianBlur(image[1], image[1], new Size(11, 11), 0);
			Imgproc.cvtColor(image[1], image[1], Imgproc.COLOR_BGR2HSV);
			
			Core.inRange(image[1].clone(), TrackWindow.lower, TrackWindow.upper, image[2]);
			
			trackObject(image[1], player1);
			trackObject(image[1], player2);
			trackObject(image[1], ball);
			//trackObject(image[1], table);
			
			
			repaint();
			try { Thread.sleep(1000/60); } catch(Exception e) {}
		}
	}
	
	protected void trackObject(Mat cameraFeed, Piece object)
	{
		// should move.
		Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
		Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8));
		
		// Find.
		Mat mask = new Mat();
		Core.inRange(image[1].clone(), object.getHSVLower(), object.getHSVUpper(), mask);
		
		// Morph.
		Imgproc.erode(mask, mask, erodeKernel);
		Imgproc.erode(mask, mask, erodeKernel);
		Imgproc.dilate(mask, mask, dilateKernel);
		Imgproc.dilate(mask, mask, dilateKernel);
		
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		
		// Track.
		Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		
		if(contours.size() > 0)
		{
			Moments moment = Imgproc.moments(contours.get(0), false);
			double area = moment.m00;
			double x = moment.m10 / area;
			double y = moment.m01 / area;
			
			object.setPosition(x, y);
		}
	}
	
	public void stop()
	{
		this.running = false;
	}
}
