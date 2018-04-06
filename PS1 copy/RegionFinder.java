import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Pixel>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public pixelArray<Pixel> = [];
	public Color targetColor;
	
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
		for (x = 0; x < image.getWidth(); x++) {
			for (y = 0; y < image.getHeight(); y++) {
				pixelArray.add(new Pixel(x, y));
			}
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		for (i = 0; i < pixelArray.getSize(); i++) {
			Pixel initializer = pixelArray.get(i);
			if (initializer.visited == false && colorMatch(initializer.getColor(), targetColor)) {
				ArrayList<Pixel> stack = new ArrayList<Pixel>;
				stack.get(0) = pixelArray.get(i);
				while (stack.getSize() > 0) {
					Pixel popped = stack.get(stack.getSize() - 1);
					stack.remove(popped);
					popped.setVisited(true);
					
				}
			}
		}
				
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		if (Math.abs(c1.getRGB() - c2.getRGB()) < maxColorDifference) {
			return true;
		return false;
		}
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Pixel> largestRegion() {
		ArrayList<Pixel> largest = new ArrayList<Pixel>;
		for (i = 0; i < regions.getSize(); i++) {
			if (largest.getSize() < regions.get(i).getSize()) {
				largest = regions.get(i);
			}
		}
		targetColorlargest.get(0).getRGB();
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
	}

}
