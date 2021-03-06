package corecase;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import battle.stage.StageIntro;
import battle.stage.StageManager;
import gui.Frame;
import io.TextureBuffer;
import lib.ClickListener;
import lib.ClickableObject;
import lib.PaintingTask;

public abstract class Tutorial {

	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_TUTORIAL_SCREEN);

	private static boolean visible = false;

	public static void show() {

		visible = true;
		MainZap.getStaticLayer().addTask(PAINTINGTASK);

		final ClickableObject table = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
		table.setVisible(true);
		table.addClickListener(new ClickListener() {

			@Override
			public void release(int dx, int dy) {
				MainZap.getStaticLayer().removeTask(PAINTINGTASK);
				MainZap.getFrame().removeClickable(table);
				new StageIntro(StageManager.getActiveStage()).register();
				MainZap.getMainLoop().setPaused(false);
				visible = false;
			}

			@Override
			public void press(int dx, int dy) {
			}
		});

		MainZap.getFrame().addClickable(table);
		MainZap.getMainLoop().setPaused(true);

	}

	private static final PaintingTask PAINTINGTASK = new PaintingTask() {

		@Override
		public void paint(Graphics2D g) {

			g.drawImage(TEXTURE, 0, 0, Frame.SIZE, Frame.SIZE, null);

		}
	};

	public static boolean isVisible() {
		return visible;
	}

}
