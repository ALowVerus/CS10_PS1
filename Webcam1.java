import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Class to handle Webcam1 capture and processing, packaging up JavaCV stuff.
 * Subclasses can conveniently process Webcam1 video by extending this and overriding the processImage methods.
 * Since it's an extension of DrawingGUI, they can also override draw(), handleMousePress(), etc.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015 -- updated for JavaCV 0.10
 * @author CBK, Spring 2015, integrated with DrawingGUI
 * @author CBK, updated to JavaCV 1.1, Spring 2016
 *
 */
public class Webcam1 extends DrawingGUI {
	protected boolean mac = true;					// TODO: set to true for mac, false for windows
	private static final double scale = 0.5;		// to downsize the image (for speed), set this to a fraction <= 1
	private static final boolean mirror = true;		// make true in order to mirror left<->right so your left hand is on the left side of the image

	protected BufferedImage image;					// image grabbed from Webcam1 (if any)

	private Grabby grabby;							// handles Webcam1 grabbing
	private FrameGrabber grabber;					// JavaCV

	public Webcam1() {
		super("Webcam1");

		try {
			if (mac) grabber = new OpenCVFrameGrabber(0); // this seems to work for Mac people
			else grabber = FrameGrabber.createDefault(0);  // this seems to work for Windows people
			grabber.start();
			System.out.println("Started!");
		} catch (Exception e) {
			System.err.println("Failed to start frame grabber");
			System.err.println(e);
			System.exit(-1);
		}

		// Get size and figure out scaling
		int width = grabber.getImageWidth();
		int height = grabber.getImageHeight();
		System.out.println("Native camera size "+width+"*"+height);
		if (scale != 1) {
			width = (int)(width*scale);
			height = (int)(height*scale);
			System.out.println("Scaled to "+width+"*"+height);
		}
		initWindow(width,height);

		// Spawn a separate thread to handle grabbing.
		grabby = new Grabby();
		grabby.execute();
	}

	/**
	 * Processes the grabbed image.
	 */
	public void processImage() {
		// Default: nothing
	}
	
	/**
	 * DrawingGUI method, here showing the current image.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Handles grabbing an image from the Webcam1 (following JavaCV examples)
	 * storing it in image, and telling the canvas to repaint itself.
	 */
	private class Grabby extends SwingWorker<Void, Void> {
		protected Void doInBackground() throws Exception {
		    OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		    while (!isCancelled()) {
				IplImage grabbed = null;
				while (grabbed == null) {
					try {
						grabbed = grabberConverter.convert(grabber.grab());
					}	
					catch (Exception e) {
						Thread.sleep(100); // wait a bit
					}
				}
				if (mirror) {
					cvFlip(grabbed, grabbed, 1);
				}
				if (scale != 1) {
					IplImage resized = IplImage.create(width, height, grabbed.depth(), grabbed.nChannels());
					cvResize(grabbed, resized);
					grabbed = resized;
				}
				Frame frame = grabberConverter.convert(grabbed);
				image = paintConverter.getBufferedImage(frame);
				try {
					processImage();
				}
				catch (Exception e) {
					// Bail out if problems processing image
					System.err.println("Exception in processImage!");
					e.printStackTrace();
					System.exit(-1);
				}
				canvas.repaint();
				Thread.sleep(100); // slow it down
			}
			// All done; clean up
			grabber.stop();
			grabber.release();
			grabber = null;
			return null;
		}
	}
}