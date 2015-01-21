package com.wowwee.mipsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class JoystickDrawer {
	public Bitmap ringBitmap;
	public Bitmap joystickBitmap;
	
	public Rect ringRect;
	public Rect joystickRect;
	
	private float drawRatio = 1.0f;
	
	public JoystickDrawer(Bitmap ringBitmap, Bitmap joystickBitmap)
	{
		this.ringBitmap = ringBitmap;
		this.joystickBitmap = joystickBitmap;
		
		ringRect = new Rect();
		joystickRect = new Rect();
	}
	
	public void destroy() {
		ringBitmap = null;
		joystickBitmap = null;
	}
	
	public void setJoystickBitmap(Bitmap joystickBitmap) {
		this.joystickBitmap = joystickBitmap;
	}
	
	public void setDrawRatio(float drawRatio)
	{
		this.drawRatio = drawRatio;
		
		ringRect.right = (int) (ringBitmap.getWidth()*drawRatio);
		ringRect.bottom = (int) (ringBitmap.getHeight()*drawRatio);
		
		joystickRect.right = (int) (joystickBitmap.getWidth()*drawRatio);
		joystickRect.bottom = (int) (joystickBitmap.getHeight()*drawRatio);
	}
	
	public float getMaxJoystickValue() {
		return ((ringRect.width() - joystickRect.width()) / 2.0f);
	}
	
	public void drawJoystick(Canvas canvas, JoystickData data)
	{
		if (data.pointerId != JoystickData.INVALID_POINTER_ID)
		{
			float dragX = data.dragPoint.x - data.startPoint.x;
			float dragY = data.dragPoint.y - data.startPoint.y;
			
			//compute the drag bound 
			float dragFactor = (ringRect.width() - joystickRect.width()) / 2.0f / (float)Math.sqrt(dragX*dragX + dragY*dragY);
			if (dragFactor < 1.0f)
			{
				dragX *= dragFactor;
				dragY *= dragFactor;
			}
			dragX += data.startPoint.x;
			dragY += data.startPoint.y;
			
			//draw the outer ring
			Matrix ringMatrix = new Matrix();
			ringMatrix.postScale(drawRatio, drawRatio);
			ringMatrix.postTranslate(data.startPoint.x-ringRect.centerX(), data.startPoint.y-ringRect.centerY());
			canvas.drawBitmap(ringBitmap, ringMatrix, null);
			
			//draw the joystick
			Matrix joystickMatrix = new Matrix();
			joystickMatrix.postScale(drawRatio, drawRatio);
			joystickMatrix.postTranslate(dragX-joystickRect.centerX(), dragY-joystickRect.centerY());
			canvas.drawBitmap(joystickBitmap, joystickMatrix, null);
		}
	}
}
