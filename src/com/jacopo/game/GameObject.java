package com.jacopo.game;
/**
 * Game object is either an image or rectangular blocks
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class GameObject {
	Image img = null;
	double x;
	double y;
	int width;
	int height;
	Color color;
	boolean drawBoundary = false;
	
	public GameObject(double x, double y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public GameObject(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = Color.BLACK;
	}
	public GameObject(Image img,double x, double y) {
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}
	
	public GameObject(){
		
	}
	
	public void drawSelf(Graphics g) {
		if(img != null) {
			g.drawImage(img, (int)x, (int)y, null);
		}
		else {
			g.setColor(color);
			g.fillRect((int)x, (int)y, width, height);
		}
		if(this.drawBoundary) {
			g.setColor(Color.RED);
			g.drawRect((int)this.x, (int)this.y, this.width, this.height);
		}
	}
	
	public Rectangle getRectBoundary() {
		return new Rectangle((int)x,(int)y,width,height);
	}
}
