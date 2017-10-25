package br.unicentro.jah.eyes;

import java.awt.Color;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

public class Trackable 
{
	private String name;
	
	private int posX;
	private int posY;
	
	private Scalar hsvLower;
	private Scalar hsvUpper;
	
	private Color displayColor;
	
	private Rect startPoint;
	
	private Mat roiHist;
	private TermCriteria termCriteria;
	
	public Trackable(String name, Rect startPoint, Color color) 
	{
		this.name = name;
		this.displayColor = color;
		this.posX = 0;
		this.posY = 0;
		this.displayColor = Color.WHITE;
		this.startPoint = startPoint;
	}
	
	public void setup(Mat frame)
	{
		Mat roi = frame.submat(startPoint);
		
		Mat hsvRoi = new Mat();
		Imgproc.cvtColor(roi, hsvRoi, Imgproc.COLOR_BGR2HSV);
		
		Mat mask = new Mat();
		
		Core.inRange(hsvRoi, 
				getHSVLower(),
				getHSVUpper(), 
				mask
			);
		
		this.roiHist = new Mat();
		Imgproc.calcHist(
				Arrays.asList(hsvRoi), 
				new MatOfInt(0), 
				mask, 
				roiHist, 
				new MatOfInt(180), 
				new MatOfFloat(0, 180),
				true
			);
		
		Core.normalize(roiHist, roiHist, 0, 255, Core.NORM_MINMAX);
		this.termCriteria = new TermCriteria(TermCriteria.EPS | TermCriteria.COUNT, 80, 1);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPosition(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	
	public int getX() {
		return this.posX;
	}
	
	public int getY() {
		return this.posY;
	}
	
	public void setHSVBounds(Scalar lower, Scalar upper) {
		this.hsvLower = lower;
		this.hsvUpper = upper;
	}
	
	public Scalar getHSVLower() {
		return this.hsvLower;
	}
	
	public Scalar getHSVUpper() {
		return this.hsvUpper;
	}
	
	public Color getDisplayColor() {
		return this.displayColor;
	}
	
	public TermCriteria getTermCriteria() {
		return this.termCriteria;
	}
	
	public Mat getRoiHist() {
		return this.roiHist;
	}
	
	public Rect getStartPoint() {
		return this.startPoint;
	}
}
