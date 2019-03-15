package org.daniel.imagepixelate.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class IPPanel extends JPanel {
	
	private Dimension blockSize = new Dimension(5, 5);
	
	private BufferedImage image;
	
	private static final boolean SPECTRUM = false;
	
	private boolean shiftPressed = false;

	public IPPanel(String path) {
		image = getImageFromPath(path);
		if(image == null)
			return;
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setContentPane(this);
		frame.setVisible(true);
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SHIFT) shiftPressed = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SHIFT) shiftPressed = false;
				else {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_UP:
						blockSize.height++;
						if(!shiftPressed) break;
					case KeyEvent.VK_RIGHT:
						blockSize.width++;
						break;
					case KeyEvent.VK_DOWN:
						if(blockSize.height != 1) blockSize.height--;
						if(!shiftPressed) break;
					case KeyEvent.VK_LEFT:
						if(blockSize.width != 1) blockSize.width--;
						break;
					}
					renderImage(image);
				}
			}
			
		});
		
		this.setSize(image.getWidth(), image.getHeight());
		this.setBackground(Color.black);
		this.renderImage(image);
	}
	
	private BufferedImage getImageFromPath(String path) {
		File image = new File(path);
		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void renderImage(BufferedImage image) {
		Graphics g = this.getGraphics();
		super.paint(g);
		Dimension resolution = new Dimension(getWidth() / blockSize.width, getHeight() / blockSize.height);
		for(int x = 0; x < resolution.width; x++)
			for(int y = 0; y < resolution.height; y++) {
				Color[] colors = new Color[blockSize.width * blockSize.height];
				for(int x_ = 0; x_ < blockSize.width; x_++)
					for(int y_ = 0; y_ < blockSize.height; y_++)
						colors[x_ + y_ * blockSize.width] = new Color(image.getRGB(x * blockSize.width + x_, y * blockSize.height + y_));
				g.setColor(getAverageColor(colors));
				g.fillRect(x * blockSize.width, y * blockSize.height, blockSize.width, blockSize.height);
				if(SPECTRUM) {
					g.setColor(Color.getHSBColor((float) x / resolution.width * 0.5f + (float) y / resolution.height * 0.5f, 1f, 1f));
					g.drawRect(x * blockSize.width, y * blockSize.height, blockSize.width, blockSize.height);
				}
			}
	}
	
	private Color getAverageColor(Color[] colors) {
		int[] sums = {0, 0, 0};
		for(Color color : colors) {
			sums[0] += color.getRed();
			sums[1] += color.getGreen();
			sums[2] += color.getBlue();
		}
		return new Color(sums[0] / colors.length, sums[1] / colors.length, sums[2] / colors.length); 
	}
	
}
