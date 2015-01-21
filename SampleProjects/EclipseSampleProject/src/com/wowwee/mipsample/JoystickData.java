package com.wowwee.mipsample;

import android.graphics.Point;

public class JoystickData {
	public enum TYPE
	{
		LEFT,
		RIGHT
	}
	
	public final static int INVALID_POINTER_ID = -1;
	
	public final TYPE type;
	public int pointerId;
	public Point startPoint;
	public Point dragPoint;
	public float maxJoystickValue;
	
	public JoystickData(TYPE type)
	{
		this.type = type;
		pointerId = INVALID_POINTER_ID;
		
		startPoint = new Point();
		dragPoint = new Point();
	}
	
	public void setStartPoint(int x, int y)
	{
		startPoint.x = x;
		startPoint.y = y;
		
		setDragPoint(x, y);
	}
	
	public void setDragPoint(int x, int y)
	{
		dragPoint.x = x;
		dragPoint.y = y;
	}
	
	public void setMaxJoystickValue(float max) {
		maxJoystickValue = max;
	}
	
	public float[] getMoveVector()
	{
		float dx = (dragPoint.x - startPoint.x) / maxJoystickValue;
		float dy = (dragPoint.y - startPoint.y) / maxJoystickValue;
		
		return new float[] { Math.max(-1, Math.min(1, dx)), Math.max(-1, Math.min(1, dy)) };
	}
	
	public void reset()
	{
		pointerId = INVALID_POINTER_ID;
		dragPoint.x = 0;
		dragPoint.y = 0;
		startPoint.x = 0;
		startPoint.y = 0;
	}
}
