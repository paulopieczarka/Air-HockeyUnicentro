package br.unicentro.jah.eyes;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public class OCVUtil 
{
	public static BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
	
	public static void applyCLAHE(Mat srcArry, Mat dstArry) { 
	    //Function that applies the CLAHE algorithm to "dstArry".

	    if (srcArry.channels() >= 3) {
	        // READ RGB color image and convert it to Lab
	        Mat channel = new Mat();
	        Imgproc.cvtColor(srcArry, dstArry, Imgproc.COLOR_BGR2Lab);

	        // Extract the L channel
	        Core.extractChannel(dstArry, channel, 0);

	        // apply the CLAHE algorithm to the L channel
	        CLAHE clahe = Imgproc.createCLAHE();
	        clahe.setClipLimit(4);
	        clahe.apply(channel, channel);

	        // Merge the the color planes back into an Lab image
	        Core.insertChannel(channel, dstArry, 0);

	        // convert back to RGB
	        Imgproc.cvtColor(dstArry, dstArry, Imgproc.COLOR_Lab2BGR);

	        // Temporary Mat not reused, so release from memory.
	        channel.release();
	    }

	}
	
//	Mat equalizeIntensity(const Mat& inputImage)
//	{
//	    if(inputImage.channels() >= 3)
//	    {
//	        Mat ycrcb;
//	        cvtColor(inputImage,ycrcb,CV_BGR2YCrCb);
//
//	        vector<Mat> channels;
//	        split(ycrcb,channels);
//
//	        equalizeHist(channels[0], channels[0]);
//
//	        Mat result;
//	        merge(channels,ycrcb);
//	        cvtColor(ycrcb,result,CV_YCrCb2BGR);
//
//	        return result;
//	    }
//
//	    return Mat();
	
	public static Mat equalizeIntensity(Mat src)
	{
		Mat dst = new Mat();
		
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2YCrCb);
		
		Mat channel = new Mat();
		Core.extractChannel(dst, channel, 0);
		
		Imgproc.equalizeHist(channel, channel);
		Core.insertChannel(channel, dst, 0);
		
		Imgproc.cvtColor(dst, dst, Imgproc.COLOR_YCrCb2BGR);
		
		return dst;
	}
}
