package br.unicentro.jah.eyes;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * OpenCamera.
 * OpenCV camera frames and basic image processing.
 * 
 * Call {@code OpenCamera.start()} to initialize.
 */
public class OpenCamera
{
	private VideoCapture camera;
	
	/**
	 * JOpenCV initializer. 
	 **/
	public void start()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		camera = new VideoCapture(0);
	}
	
	/**
	 * JOpenCV destroyer (aka Hulkfier). 
	 **/
	public void close()
	{
		System.out.println("Releasing webcam..");
		camera.release();
	}
	
	public Mat read()
	{
		Mat mat = new Mat();
		camera.read(mat);
		return mat;
	}
}
