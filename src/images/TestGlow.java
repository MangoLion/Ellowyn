package images;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestGlow {
	 public static void main(String[] args) throws IOException {
	        int w = 500;
	        int h = 120;
	        Font font = new Font("Lucida Bright", Font.ITALIC, 72);
	        String text = "Shadow Text";

	        BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = image.createGraphics();
	        adjustGraphics(g);
	        
	        //start off all white:
	        g.setPaint(Color.WHITE);
	        g.fillRect(0, 0, w, h);
	        
	        //draw "shadow" text: to be blurred next
	        TextLayout textLayout = new TextLayout(text, font, g.getFontRenderContext());
	        g.setPaint(new Color(128,128,255));
	        textLayout.draw(g, 15, 105);
	        g.dispose();
	        
	        //blur the shadow: result is sorted in image2
	        float ninth = 1.0f / 9.0f;
	        float[] kernel = {ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth};
	        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, kernel), ConvolveOp.EDGE_NO_OP, null);
	        BufferedImage image2 = op.filter(image,null);
	        
	        //write "original" text on top of shadow
	        Graphics2D g2 = image2.createGraphics();
	        adjustGraphics(g2);
	        g2.setPaint(Color.BLACK);
	        textLayout.draw(g2, 10, 110);
	        
	        //save to file
	        //ImageIO.write(image2, "jpeg", new File("ShadowText.jpg"));
	        
	        //show me the result
	        display(image2);
	    }

	    static void adjustGraphics(Graphics2D g) {
	        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    }

	    static void display(BufferedImage im) {
	        JFrame f = new JFrame("ShadowText");
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.getContentPane().add(new JLabel(new ImageIcon(im)));
	        f.pack();
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
	    }
}
