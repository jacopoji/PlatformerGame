package com.jacopo.game;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends Frame{

	Image img = GameUtil.getImage("images/kid_jump.png");
	
	//Player newPlayer = new Player(100,100,10,10,Color.BLUE);
	Player newPlayer = new Player(img,100,100);
	GameObject ground = new GameObject(0,Constants.FRAME_HEIGHT-100,Constants.FRAME_WIDTH,100);
	static List<GameObject> platforms = new ArrayList<GameObject>();
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		
		//platforms.add(ground);
		for(int i = 0;i<10;i++) {
			GameObject platform1 = new GameObject(200+i*80,300-i*30,100,30);
			platforms.add(platform1);
		}
		frame.LaunchFrame();
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
		boolean fallOff = true;
		for(GameObject o:platforms)
		{
			o.drawSelf(g);
			if(IntersectBorder(newPlayer.getRectBoundary(), o.getRectBoundary()) == 't') {
				newPlayer.isFalling = false;
				newPlayer.isJumping = false;
				newPlayer.jumpCount = 0;
				newPlayer.accumulatedJump = 0;
				if(o.y < newPlayer.y+newPlayer.height) { //falling through boundary check
					newPlayer.y = o.y-newPlayer.height;
				}
				fallOff = false;
			}
			if(IntersectBorder(newPlayer.getRectBoundary(), o.getRectBoundary()) == 'l') {
				newPlayer.velocityX = 0;
				newPlayer.x = o.x - newPlayer.width;
				System.out.println("Stopping left");
			}
			if(IntersectBorder(newPlayer.getRectBoundary(), o.getRectBoundary()) == 'r') {
				newPlayer.velocityX = 0;
				newPlayer.x = o.x+o.width;
				System.out.println("Stopping right");
			}
			if(IntersectBorder(newPlayer.getRectBoundary(), o.getRectBoundary()) == 'b') {
				newPlayer.isJumping = false;
				newPlayer.accumulatedJump = 0;
				newPlayer.isFalling = true;
				System.out.println("Stopping bottom");
			}
		}
		if(fallOff && ! newPlayer.isJumping)
			newPlayer.isFalling = true;
		newPlayer.drawSelf(g);
		ground.drawSelf(g);
		if(ground.getRectBoundary().intersects(newPlayer.getRectBoundary())) {
			newPlayer.isFalling = false;
			newPlayer.isJumping = false;
			newPlayer.jumpCount = 0;
			newPlayer.accumulatedJump = 0;
			if(ground.y < newPlayer.y+newPlayer.height) { //falling through boundary check
				newPlayer.y = ground.y-newPlayer.height;
			}
			fallOff = false;
		}
		
		
		
		//System.out.println("I'm re-drawing");
	}
	
	class PaintThread extends Thread {
        public void run(){
            while(true){
        		repaint();
                try {
                    Thread.sleep(20); //1s = 1000ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }   
            }
    	
        }
    }  
	
	public void LaunchFrame() {
		setTitle("Platformer");
		
		setVisible(true);
		
		setSize(Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT);
		
		setLocation(100,100);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				newPlayer.keyEventsOff(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_H) {
					for(GameObject o:platforms) {
						o.drawBoundary = true;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_G) {
					for(GameObject o:platforms) {
						o.drawBoundary = false;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				else {
					newPlayer.keyEventsOn(e);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		new PaintThread().start();
	}
	
	public char IntersectBorder(Rectangle t, Rectangle r) {
		int tw = t.width;
		int th = t.height;
		int rw = r.width;
		int rh = r.height;
		
		int tx = t.x;
		int ty = t.y;
		int rx = r.x;
		int ry = r.y;
		
		tw += tx;
		th += ty;
		rw += rx;
		rh += ry;
		
		int yTolerance = (int)Constants.JUMP_SPEED;
		int xTolerance = (int)Constants.MAX_SPEED_X * 2;
		
		
		if(rw > tx && (tw >= rx && tw < rx + xTolerance) && th > ry && rh > ty) return 'l';
		if(rw > tx && tw > rx && th > ry && (rh >= ty && rh < ty + yTolerance)) return 'b';
		if((rw >= tx && rw < tx + xTolerance) && tw > rx && th > ry && rh > ty) return 'r';
		if(rw > tx && tw > rx && (th >= ry && th < ry + yTolerance) && rh > ty) return 't';
		
		return 'n';
	}
	
	private Image offScreenImage = null;
	
	public void update(Graphics g) {
	    if(offScreenImage == null)
	        offScreenImage = this.createImage(Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT);
	     
	    Graphics gOff = offScreenImage.getGraphics();
	    paint(gOff);
	    g.drawImage(offScreenImage, 0, 0, null);
	}  
}

