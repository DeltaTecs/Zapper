package gui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

import corecase.MainZap;
import io.SettingsInitReader;
import io.TextureBuffer;
import lib.ClickableObject;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int SIZE = 800;
	public static final int HALF_SCREEN_SIZE = (SIZE / 2);
	public static final int BUFFER_AMOUNT = 2;

	private BufferStrategy bufferStrategy;

	private ArrayList<ClickableObject> coreClickables = new ArrayList<ClickableObject>();

	public Frame() {

		super("Zapper Space Combat Simulator " + MainZap.VERSION);
		setIconImage(TextureBuffer.get(TextureBuffer.NAME_HEADIMAGE));
		setLayout(null);
		setResizable(MainZap.allowBiggerWindow);
		getContentPane().setPreferredSize(new Dimension(SIZE, SIZE));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		repaint();
		super.createBufferStrategy(BUFFER_AMOUNT);
		bufferStrategy = super.getBufferStrategy();
		setIgnoreRepaint(true);
		setLocationRelativeTo(null);
		getContentPane().addMouseListener(clickListener);
		getContentPane().addMouseMotionListener(motionListener);
	}

	private MouseMotionListener motionListener = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent e) {
			ArrayList<ClickableObject> coreClickablesBuffered = new ArrayList<ClickableObject>(coreClickables);
			for (ClickableObject co : coreClickablesBuffered) {
				co.callDrag((int) (e.getX() / MainZap.getScale()), (int) (e.getY() / MainZap.getScale()));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			ArrayList<ClickableObject> coreClickablesBuffered = new ArrayList<ClickableObject>(coreClickables);
			for (ClickableObject co : coreClickablesBuffered) {
				co.callMove((int) (e.getX() / MainZap.getScale()), (int) (e.getY() / MainZap.getScale()));
			}
		}

	};

	private MouseListener clickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			ArrayList<ClickableObject> coreClickablesBuffered = new ArrayList<ClickableObject>(coreClickables);
			for (ClickableObject co : coreClickablesBuffered) {
				co.callPress((int) (e.getX() / MainZap.getScale()), (int) (e.getY() / MainZap.getScale()));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			ArrayList<ClickableObject> coreClickablesBuffered = new ArrayList<ClickableObject>(coreClickables);
			for (ClickableObject co : coreClickablesBuffered) {
				co.callRelease((int) (e.getX() / MainZap.getScale()), (int) (e.getY() / MainZap.getScale()));
			}
		}

	};

	public void rescale(float scale) {
		setResizable(true);
		getContentPane().setPreferredSize(new Dimension((int) (SIZE * scale), (int) (SIZE * scale)));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void autoscale() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int sx = gd.getDisplayMode().getWidth();
		int sy = gd.getDisplayMode().getHeight();

		int smallerSize = sy;
		if (sx < sy)
			smallerSize = sx;
		
		int availableSize = smallerSize - 100;
		float scale = (float) availableSize / SIZE;
		MainZap.setScale(scale);
		SettingsInitReader.save();
	}

	public void addClickable(ClickableObject co) {
		coreClickables.add(co);
	}

	public void removeClickable(ClickableObject co) {
		coreClickables.remove(co);
	}

	public BufferStrategy getBufferStrategy() {
		return bufferStrategy;
	}

	public void clearClickables() {
		coreClickables.clear();
	}

}
