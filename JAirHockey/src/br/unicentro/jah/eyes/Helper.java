package br.unicentro.jah.eyes;

import java.awt.Point;
import java.util.Arrays;

public class Helper 
{
	private static int virtualHeight = 480/2;
	private static int virtualWidth = 640/2;
	
	private static double[] topQuadCoeff;
	private static double[] bottomQuadCoeff;
	private static double[] leftQuadCoeff;
	private static double[] rightQuadCoeff;
	private static double[] centerHorizontalQuadCoeff;
	private static double[] centerVerticalQuadCoeff;
	
	public static Point translatePoint(double x, double y)
	{
		Point newPoint = new Point(0, 0);
		
        double centerY = centerHorizontalQuadCoeff[0] * Math.pow(x, 2) + centerHorizontalQuadCoeff[1] * x + centerHorizontalQuadCoeff[2];
        double centerX = centerVerticalQuadCoeff[0] * Math.pow(y, 2) + centerVerticalQuadCoeff[1] * y + centerVerticalQuadCoeff[2];

        if(y < centerY)
        {
        	double topY = topQuadCoeff[0] * Math.pow(x, 2) + topQuadCoeff[1] * x + topQuadCoeff[2];
        	double ratioY = (y - topY) / (centerY - topY);
            newPoint.y = (int)(virtualHeight / 2 * ratioY); 
        }
        else
        {
        	double bottomY = bottomQuadCoeff[0] * Math.pow(x, 2) + bottomQuadCoeff[1] * x + bottomQuadCoeff[2];
        	double ratioY = (bottomY - y) / (bottomY - centerY);
            newPoint.y = (int)(virtualHeight - (virtualHeight / 2 * ratioY)); 
        }

        if(x < centerX)
        {
        	double leftX = leftQuadCoeff[0] * Math.pow(y, 2) + leftQuadCoeff[1] * y + leftQuadCoeff[2];
        	double ratioX = (x - leftX) / (centerX - leftX);
            newPoint.x = (int)(virtualWidth / 2 * ratioX); 
        }
        else
        {
        	double rightX = rightQuadCoeff[0] * Math.pow(y, 2) + rightQuadCoeff[1] * y + rightQuadCoeff[2];
        	double ratioX = (rightX - x) / (rightX - centerX);
            newPoint.x = (int)(virtualWidth - (virtualWidth / 2 * ratioX)); 
        }

        return newPoint;
	}
	
	public static void setDefaultCalibration(Point[] pts)
	{
		pts = Arrays.copyOf(pts, pts.length+3);
		pts[pts.length-3] = calcCenterPoint(pts, 0, 3); // 6
		pts[pts.length-2] = calcCenterPoint(pts, 1, 4); // 7
		pts[pts.length-1]   = calcCenterPoint(pts, 2, 5); // 8
		
		topQuadCoeff = computeCoefficents(
				threePointToMatX(pts, 0, 1, 2),
				new double[] { pts[0].y, pts[1].y, pts[2].y }
			);
		
		bottomQuadCoeff = computeCoefficents(
				threePointToMatX(pts, 3, 4, 5),
				new double[] { pts[3].y, pts[4].y, pts[5].y }
			);
		
		leftQuadCoeff = computeCoefficents(
				threePointToMatY(pts, 0, 7, 3),
				new double[] { pts[0].x, pts[7].x, pts[3].x }
			);
		
		rightQuadCoeff = computeCoefficents(
				threePointToMatY(pts, 2, 8, 5),
				new double[] { pts[2].x, pts[8].x, pts[5].x }
			);
		
		centerHorizontalQuadCoeff = computeCoefficents(
				threePointToMatX(pts, 6, 7, 8),
				new double[] { pts[6].y, pts[7].y, pts[8].y }
			);
		
		centerVerticalQuadCoeff = computeCoefficents(
				threePointToMatY(pts, 1, 6, 4),
				new double[] { pts[1].x, pts[6].x, pts[4].x }
			);
		
		System.out.println("dsfadsafsda");
	}
	
	private static Point calcCenterPoint(Point[] pts, int i, int j)
	{
		int cx = (pts[i].x+pts[j].x) / 2;
		int cy = (pts[i].y+pts[j].y) / 2;
		return new Point(cx, cy);
	}
	
	private static double[][] threePointToMatX(Point[] pts, int i, int j, int k)
	{
		return new double[][] {
			{ Math.pow(pts[i].x, 2), pts[i].x, 1 },
			{ Math.pow(pts[j].x, 2), pts[j].x, 1 },
			{ Math.pow(pts[k].x, 2), pts[k].x, 1 }
		};
	}
	
	private static double[][] threePointToMatY(Point[] pts, int i, int j, int k)
	{
		return new double[][] {
			{ Math.pow(pts[i].y, 2), pts[i].y, 1 },
			{ Math.pow(pts[j].y, 2), pts[j].y, 1 },
			{ Math.pow(pts[k].y, 2), pts[k].y, 1 }
		};
	}
	
	private static double[] computeCoefficents(double[][] x, double[] y)
	{
		int i, j, k, k1;
		int n = y.length;
		
		for(k=0; k < n; k++)
        {
            k1 = k+1;
            for(i=k; i < n; i++)
            {
                if(x[i][k] != 0)
                {
                    for(j=k1; j < n; j++) {
                        x[i][j] /= x[i][k];
                    }

                    y[i] /= x[i][k];
                }
            }

            for(i=k1; i < n; i++)
            {
                if(x[i][k] != 0)
                {
                    for(j=k1; j < n; j++) {
                        x[i][j] -= x[k][j];
                    }
    
                    y[i] -= y[k];
                }
            }
        }

        for(i=n-2; i >= 0; i--)
        {
            for(j=n-1; j >= i+1; j--) {
                y[i] -= x[i][j] * y[j];
            }
        }
		
		
		return y;
	}
}
