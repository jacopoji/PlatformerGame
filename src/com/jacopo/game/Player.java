package com.jacopo.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Player extends GameObject{
	double velocityX;
	double velocityY;
	boolean facing_right = true;
	boolean isJumping;
	boolean isFalling;
	int jumpCount = 0;
	double accumulatedJump=0;
	boolean spacePressed = false;
	boolean[] keyPressed = new boolean[200];
	boolean pause = false;
	boolean pPressed = false;
	public Player(double x, double y, int width, int height, Color color) {
		super(x, y, width, height, color);
		this.velocityX = 0;
		this.velocityY = 0;
		this.isJumping = false;
		this.isFalling = true;
	}
	
	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.velocityX = 0;
		this.velocityY = 0;
		this.isJumping = false;
		this.isFalling = true;
	}
	
	public Player(Image img,double x, double y) {
		super(img,x, y);
		this.velocityX = 0;
		this.velocityY = 0;
		this.isJumping = false;
		this.isFalling = true;
	}
	
	public void keyEventsOn(KeyEvent e) {
		keyPressed[e.getKeyCode()] = true;
		//System.out.println("Key"+e.getKeyCode()+ "pressed");
	}
	
	public void keyEventsOff(KeyEvent e) {
		keyPressed[e.getKeyCode()] = false;
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.spacePressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_P) {
			this.pPressed = false;
		}
		//System.out.println("Key"+e.getKeyCode() +"released");
	}
	
	@Override
	public void drawSelf(Graphics g) {
		if(this.facing_right)
			if(img != null) {
				g.drawImage(img, (int)x, (int)y, null);
			}
			else {
				g.setColor(color);
				g.fillRect((int)x, (int)y, width, height);
			}
		else
			if(img != null) {
				g.drawImage(img, (int)x+this.width, (int)y, -this.width,this.height,null);
			}
			else {
				g.setColor(color);
				g.fillRect((int)x, (int)y, width, height);
			}
		
		if(this.drawBoundary) {
			g.setColor(Color.RED);
			g.drawRect((int)this.x, (int)this.y, this.width, this.height);
		}

		if(keyPressed[KeyEvent.VK_P]) {
			if(!this.pPressed) {
				this.pause = !this.pause;
				this.pPressed = true;
			}
		}
		
//		if(keyPressed[KeyEvent.VK_UP])
//			this.y -= 3;
//		if(keyPressed[KeyEvent.VK_DOWN])
//			this.y += 3;
		if(!this.pause) {
			if(keyPressed[KeyEvent.VK_LEFT]) {
				this.facing_right = false;
				if(this.velocityX > -Constants.MAX_SPEED_X)
					this.velocityX -= 2;
			}
			if(keyPressed[KeyEvent.VK_RIGHT]) {
				this.facing_right = true;
				if(this.velocityX < Constants.MAX_SPEED_X)
					this.velocityX += 2;
			}
			if(!keyPressed[KeyEvent.VK_RIGHT] && !keyPressed[KeyEvent.VK_LEFT]){
				this.velocityX = this.velocityX * (1-Constants.FRICTION);
			}
			if(keyPressed[KeyEvent.VK_H]) {
				this.drawBoundary = true;//!this.drawBoundary;
			}
			if(keyPressed[KeyEvent.VK_G]) {
				this.drawBoundary = false;
			}
			
			/**
			 * Player object can jump when it's not falling or not falling but not double jumped
			 */
			if(keyPressed[KeyEvent.VK_SPACE]) {
				if(!this.spacePressed) {
					this.spacePressed = true;
					if(this.jumpCount < 2) {
						this.isJumping = true;
						this.isFalling = false;
						this.jumpCount += 1;
						this.accumulatedJump = 0;
					}
				}
			}
			else {
				this.isJumping = false;
			}
			if(this.isJumping){
				if(this.accumulatedJump < Constants.MAX_JUMP) {
					this.y-= Constants.JUMP_SPEED;
					this.accumulatedJump +=Constants.JUMP_SPEED;
				}
				else {
					this.isFalling = true;
					this.isJumping = false;
				}
			}
			if(this.isFalling) {
				this.y += Constants.GRAVITY;
			}
			//Constantly updating coordinates
			this.x += this.velocityX;
			this.x = this.x<0?0:this.x;
			this.x = this.x>Constants.FRAME_WIDTH-this.width?Constants.FRAME_WIDTH-this.width:this.x;
		}
		
		
	}
	
}
