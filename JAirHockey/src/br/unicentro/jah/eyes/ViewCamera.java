package br.unicentro.jah.eyes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import br.unicentro.jah.client.TrackWindow;

public class ViewCamera extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static PieceBall ball = new PieceBall();
	
	private OpenCamera camera;
	private Mat image;
	
	private static Point[] tablePoints; 
	private static Point disk;
	private static Point player1;
	
	private static Trackable trackedDisk;
	private static Trackable trackedPlayer;
	
	public ViewCamera(OpenCamera camera)
	{
		this.camera = camera;
		this.image = new Mat();
		
		tablePoints = new Point[] {
				new Point(48, 126),
				new Point(290, 96),
				new Point(535, 100),
				
				new Point(55, 348),
				new Point(305, 344),
				new Point(553, 324)
			};
		
		disk = new Point(295, 218);
		
		Helper.setDefaultCalibration(tablePoints);
		
		Point[] newPoints = new Point[tablePoints.length+3];
		for(int i=0; i < tablePoints.length; i++) {
			newPoints[i] = Helper.translatePoint(tablePoints[i].x, tablePoints[i].y);
		}
		
		tablePoints = Arrays.copyOf(newPoints, tablePoints.length);
		
		// Object to track.
		trackedDisk = new TrackableDisk(new Rect(270, 190, 60, 60));
		trackedPlayer = new TrackablePlayer(new Rect(540, 275, 70, 80));
		
		Thread loop = new Thread(this);
		loop.start();
	}
	
	@Override
	public void paint(Graphics g) 
	{
		g.setColor(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(!image.empty())
		{
			BufferedImage rawImage1 = OCVUtil.MatToBufferedImage(image);
			g.drawImage(rawImage1, 0, 0, null);
			
			g.translate(650, 10);
			
			g.setColor(Color.RED);
			g.drawLine(tablePoints[0].x, tablePoints[0].y, tablePoints[1].x, tablePoints[1].y);
			g.drawLine(tablePoints[1].x, tablePoints[1].y, tablePoints[2].x, tablePoints[2].y);
			g.drawLine(tablePoints[0].x, tablePoints[0].y, tablePoints[3].x, tablePoints[3].y);
			g.drawLine(tablePoints[3].x, tablePoints[3].y, tablePoints[4].x, tablePoints[4].y);
			g.drawLine(tablePoints[4].x, tablePoints[4].y, tablePoints[5].x, tablePoints[5].y);
			g.drawLine(tablePoints[5].x, tablePoints[5].y, tablePoints[2].x, tablePoints[2].y);

			for(int i=0; i < tablePoints.length; i++)
			{
				if(tablePoints != null)
				{
					Point p = tablePoints[i];
					g.setColor(Color.BLUE);
					g.fillRect(p.x-4, p.y-4, 8, 8);
				}
			}
			
			g.setColor(Color.GREEN);
//			g.drawRect(270, 190, 60, 60);
			
			g.fillRect(disk.x-4, disk.y-4, 8, 8);
			
			g.setColor(Color.YELLOW);
			g.fillRect(player1.x-4, player1.y-4, 8, 8);
			
			g.translate(-650, -10);
		}
		
//		g.setColor(Color.blue);
//		g.drawRect(540, 275, 70, 80);
	}
	
	@Override
	public void run() 
	{
		Mat frame = camera.read();
//		frame = frame.submat(new Rect(70, 100, 520, 220));
		
		OCVUtil.applyCLAHE(frame, frame);
		frame = OCVUtil.equalizeIntensity(frame);
		
//		Mat roi = frame.submat(new Rect(540, 275, 70, 80/*270, 190, 60, 60*/));
//		
//		Mat hsvRoi = new Mat();
//		Imgproc.cvtColor(roi, hsvRoi, Imgproc.COLOR_BGR2HSV);
//		
//		Mat mask = new Mat();
//		
//		Core.inRange(hsvRoi, 
//				TrackWindow.HSV_LOWER_BLUE,
//				TrackWindow.HSV_UPPER_BLUE, 
//				mask
//			);
//		
//		Mat roiHist = new Mat();
//		Imgproc.calcHist(
//				Arrays.asList(hsvRoi), 
//				new MatOfInt(0), 
//				mask, 
//				roiHist, 
//				new MatOfInt(180), 
//				new MatOfFloat(0, 180),
//				true
//			);
//		
//		Core.normalize(roiHist, roiHist, 0, 255, Core.NORM_MINMAX);
//		TermCriteria termCrit = new TermCriteria(TermCriteria.EPS | TermCriteria.COUNT, 80, 1);
//		
//		image = mask;
		
		
		trackedDisk.setup(frame.clone());
		trackedPlayer.setup(frame.clone());
		
		while(true)
		{			
			frame = camera.read();
//			frame = frame.submat(new Rect(540, 275, 70, 80));
			
			Mat hsv = new Mat();
			Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
			
			
			trackObject(frame, hsv, trackedDisk);
			trackObject(frame, hsv, trackedPlayer);
			
			image = frame;
//			Core.inRange(hsv, 
//					TrackWindow.lower,
//					TrackWindow.upper, 
//					image
//				);
			
			disk = new Point(trackedDisk.getX(), trackedDisk.getY());
			player1 = new Point(trackedPlayer.getX(), trackedPlayer.getY());
			
			repaint();
			
			try { Thread.sleep(1000/30); } catch(Exception e) {}
		}
	}
	
	private void trackObject(Mat frame, Mat hsv, Trackable trackableObject)
	{
		Mat dst = new Mat();
		Imgproc.calcBackProject(
				Arrays.asList(hsv), 
				new MatOfInt(0), 
				trackableObject.getRoiHist(), 
				dst, 
				new MatOfFloat(0, 180), 
				1
			);
		
		Rect objectPos = trackableObject.getStartPoint();
		Video.meanShift(dst, objectPos, trackableObject.getTermCriteria());
		
		Imgproc.rectangle(frame, objectPos.tl(), objectPos.br(), new Scalar(255), 2);
		Point trackPos = Helper.translatePoint(objectPos.tl().x+objectPos.width/2, objectPos.tl().y+objectPos.height/2);
		trackableObject.setPosition(trackPos.x, trackPos.y);
	}
	
	public static int getRailMaxY() {
		
		if(tablePoints == null) {
			return 0;
		}
		
		return tablePoints[5].y;
	}
	
	public static Trackable getDisk() {
		return trackedDisk;
	}
	
	public static Trackable getPlayer1() {
		return trackedPlayer;
	}
	
	public void stop() 
	{

	}
}
