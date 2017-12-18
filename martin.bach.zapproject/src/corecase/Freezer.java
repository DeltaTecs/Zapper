package corecase;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Freezer {

	private static boolean frozen = false;

	private static boolean[] keys_CrlF_pressed = new boolean[] { false, false };

	public static void init() {
		MainZap.getFrame().addKeyListener(KEY_LISTENER);
	}

	private static void toggleFreeze() {
		frozen = !frozen;
		MainZap.getCollisionLoop().setPaused(frozen);
		MainZap.getMainLoop().setPaused(frozen);
	}

	private static final KeyListener KEY_LISTENER = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {

			if (keys_CrlF_pressed[0] && keys_CrlF_pressed[1])
				// alle gedrückt
				toggleFreeze();

			// Einen losgelassen?
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				keys_CrlF_pressed[0] = false;
			if (e.getKeyCode() == KeyEvent.VK_F)
				keys_CrlF_pressed[1] = false;

		}

		@Override
		public void keyPressed(KeyEvent e) {

			// Einen gedrückt?
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				keys_CrlF_pressed[0] = true;
			if (e.getKeyCode() == KeyEvent.VK_F)
				keys_CrlF_pressed[1] = true;

		}
	};

}
