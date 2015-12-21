package iptat.handlers;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import iptat.gui.DrawingBoard;
import iptat.util.Polygon2D;

public class KeyBindingsHandler {
	
	private KeyBindingsHandler() {
	}
	
	public static void init(DrawingBoard drawingBoard) {
		InputMap inputMap = drawingBoard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = drawingBoard.getActionMap();
		
		Action reset = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				drawingBoard.setPolygon(new Polygon2D());
				drawingBoard.repaint();
			}
		};
		
		inputMap.put(KeyStroke.getKeyStroke("R"), "reset");
		actionMap.put("reset", reset);
		
		Action undo = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				drawingBoard.getPolygon().removeLast();
				drawingBoard.repaint();
			}
		};
		
		inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo");
		actionMap.put("undo", undo);
		
		Action redo = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				drawingBoard.getPolygon().restoreLast();
				drawingBoard.repaint();
			}
		};
		
		inputMap.put(KeyStroke.getKeyStroke("control X"), "redo");
		actionMap.put("redo", redo);
	}
}
