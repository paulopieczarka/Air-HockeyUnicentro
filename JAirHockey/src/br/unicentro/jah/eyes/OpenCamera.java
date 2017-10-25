package br.unicentro.jah.eyes;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 * OpenCamera.
 * OpenCV camera frames and basic image processing.
 * 
 * Call {@code OpenCamera.start()} to initialize.
 */
public class OpenCamera
{
	private VideoCapture camera;
	private boolean firstFrame;
	
	/**
	 * JOpenCV initializer. 
	 **/
	public void start()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("opencv_ffmpeg320_64");
		camera = new VideoCapture(1);
//		if(!camera.open("C:\\Users\\paulo\\Desktop\\Air-HockeyUnicentro-master\\JAirHockey\\temp\\video2.mp4")) {
//			System.err.println("Cannot open camera.");
//			System.exit(-1);
//		}
		
		this.firstFrame = true;
	}
	
	/**
	 * JOpenCV destroyer (aka Hulkfier). 
	 **/
	public void close()
	{
		System.out.println("Releasing webcam..");
		camera.release();
	}
	
	public boolean isFirstFrame() {
		return this.firstFrame;
	}
	
	public Mat read()
	{
		Mat mat = new Mat();
		if(camera.isOpened()) 
		{
			this.firstFrame = false;
			if(!camera.read(mat)) 
			{
				camera.set(Videoio.CAP_PROP_POS_AVI_RATIO, 0);
				this.firstFrame = true;
				camera.read(mat);
			}
		}
		
		return mat;
	}
}
