/*
 * Artcodes recognises a different marker scheme that allows the
 * creation of aesthetically pleasing, even beautiful, codes.
 * Copyright (C) 2013-2016  The University of Nottingham
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.horizon.artcodes.detect;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class ImageBuffers
{
	private byte[] buffer;
	private Mat cameraImage;
	private Mat image;
	private Mat overlay;
	private Mat temp;
	private Bitmap overlayBitmap;
	private boolean overlayReady = false;
	private boolean detected = false;
	private boolean flip = false;
	private int rotations = 0;

	public Mat getImage()
	{
		return image;
	}

	public void setImage(byte[] data)
	{
		overlayReady = false;
		cameraImage.put(0, 0, data);
	}

	public void setImage(Bitmap bitmap)
	{
		Utils.bitmapToMat(bitmap, cameraImage);
	}

	public byte[] createBuffer(int imageWidth, int imageHeight, int imageDepth)
	{
		buffer = new byte[imageWidth * imageHeight * imageDepth / 8];
		// TODO Change depth based on image processors used?
		cameraImage = new Mat(imageHeight, imageWidth, CvType.CV_8UC1);
		return buffer;
	}

	public void setROI(Rect rect)
	{
		if (rect == null)
		{
			image = cameraImage;
		}
		else
		{
			image = cameraImage.submat(rect);
		}
	}

	public boolean hasDetected()
	{
		return detected;
	}

	public void setDetected(boolean detected)
	{
		this.detected = detected;
	}

	public Mat getOverlay()
	{
		return getOverlay(true);
	}

	public Bitmap createOverlayBitmap()
	{
		if (overlayReady)
		{
			if (overlayBitmap == null)
			{
				overlayBitmap = Bitmap.createBitmap(overlay.cols(), overlay.rows(), Bitmap.Config.ARGB_8888);
			}
			Utils.matToBitmap(overlay, overlayBitmap);
			return overlayBitmap;
		}
		else if (overlayBitmap != null)
		{
			overlayBitmap = null;
		}

		return null;
	}

	public void setRotation(int rotation)
	{
		this.rotations = rotation / 90;
	}

	public void setFrontFacing(boolean frontFacing)
	{
		this.flip = frontFacing;
	}

	public Mat getTemp()
	{
		if (temp == null)
		{
			temp = new Mat(image.rows(), image.cols(), CvType.CV_8UC3);
		}
		return temp;
	}

	public Mat getOverlay(boolean clear)
	{
		if (overlayReady)
		{
			return overlay;
		}

		rotate(image);

		if (overlay == null)
		{
			overlay = new Mat(image.rows(), image.cols(), CvType.CV_8UC4);
		}

		if (clear)
		{
			overlay.setTo(new Scalar(0, 0, 0, 0));
		}

		overlayReady = true;

		return overlay;
	}

	private void rotate(Mat image)
	{
		//0 : flip vertical; 1 flip horizontal
		int flip_horizontal_or_vertical = rotations > 0 ? 1 : 0;
		if (flip)
		{
			flip_horizontal_or_vertical = -1;
		}

		for (int i = 0; i != rotations; ++i)
		{
			Core.transpose(image, image);
			Core.flip(image, image, flip_horizontal_or_vertical);
		}
	}
}