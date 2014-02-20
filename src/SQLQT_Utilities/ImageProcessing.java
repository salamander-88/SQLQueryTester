package SQLQT_Utilities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public final class ImageProcessing 
{
	/**
	 * Scales imgIcon to the specified size (imgWidth, imgHeight) saving image proportions
	 * @param imgIcon - image that is scaled
	 * @param imgWidth - scale width
	 * @param imgHeight - scale height
	 * @return image scaled to the specified imgWidth and imgHeight with saved proportions
	 */
	public static ImageIcon scaleImage(ImageIcon imgIcon, int imgWidth, int imgHeight)
	{
		int height = imgHeight;           
		int width = imgWidth;
		
		if (imgIcon.getIconHeight() > imgHeight || imgIcon.getIconWidth() > imgWidth) 
		{                
			height = imgHeight;                
			int wid = imgWidth;                
			float sum = (float)imgIcon.getIconWidth() / (float)imgIcon.getIconHeight();                
			width = Math.round(height * sum);                
			if(width > wid) 
			{                    
				height = Math.round(wid/sum);                    
				width = wid;                
			}            
		}
		
		Image image = imgIcon.getImage();
		BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufImg.createGraphics();
		
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		
		ImageIcon scaledImage = new ImageIcon(bufImg);
		
		return scaledImage;
	}
}
