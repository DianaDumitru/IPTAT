package iptat.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import iptat.handlers.KeyBindingsHandler;
import iptat.handlers.MouseEventHandler;
import iptat.util.Observer;
import iptat.util.ObserverConstants;
import iptat.util.Polygon2D;
import iptat.util.Subject;

public class DrawingBoard extends JPanel implements Subject, Observer {
	
	private List<Observer> observers;
	
	private Polygon2D polygon;
	private Point2D.Double cursorPosition;
	
	public DrawingBoard() {
		super.setBackground(Color.WHITE);
		
		observers = new ArrayList<Observer>();
		setPolygon(new Polygon2D());
		
		MouseEventHandler mouseHandler = new MouseEventHandler(this);
		super.addMouseListener(mouseHandler);
		super.addMouseMotionListener(mouseHandler);
		KeyBindingsHandler.init(this);
		
		cursorPosition = new Point2D.Double(0, 0);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g2 = (Graphics2D) graphics;
		
		AffineTransform transformer = g2.getTransform();
		// set origin to center of the drawingboard
		transformer.translate(super.getWidth() / 2, super.getHeight() / 2);
		// flip Y-axis
		transformer.scale(1.0, -1.0); // flip Y axis
		
		g2.setTransform(transformer);
		
		// draw with sub-pixel precision
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		polygon.draw(g2);
	}
	
	public Polygon2D getPolygon() {
		return polygon;
	}
	
	public void setPolygon(Polygon2D polygon) {
		polygon.removeObserver(this);
		
		this.polygon = polygon;
		polygon.addObserver(this);
		
		update(ObserverConstants.DRAWBOARD_REPAINT);
	}

	@Override
	public void update(int state) {
		if (state == ObserverConstants.DRAWBOARD_REPAINT)
			repaint();
		
		notifyObservers(state);
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
	}

	@Override
	public void removeObserver(Observer ob) {
		int index = observers.indexOf(ob);
		if (index != -1)
			observers.remove(ob);
	}

	@Override
	public void notifyObservers(int state) {
		for (Observer ob: observers) 
			ob.update(state);
	}
	
	public Point2D.Double getCursorPosition() {
		return cursorPosition;
	}
	
	public void setCursorPosition(Point2D.Double point) {
		cursorPosition = point;
	}
}
