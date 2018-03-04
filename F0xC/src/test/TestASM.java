package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.util.Image16;
import pusty.f0xC.util.KernelKey;
import pusty.f0xC.util.Pal16;
import pusty.f0xC.util.TileMap16;
import static pusty.f0xC.b16.InterUtil.*;
import static pusty.f0xC.b16.InterBuffer.*;

public class TestASM {

	static Pal16 pal = new Pal16("tmp.png"); //Palette for the following images
	
	static Image16 pL = new Image16("pL0.png"); //default left position
	static Image16 pL0 = new Image16("pL1.png"); //run left 0
	static Image16 pL1= new Image16("pL2.png"); // run left 1
	static Image16 pLF = new Image16("pL3.png"); //in air left
	static Image16 pR = new Image16("pR0.png"); //default right position
	static Image16 pR0 = new Image16("pR1.png"); //run right 0
	static Image16 pR1 = new Image16("pR2.png"); //run right 1
	static Image16 pRF = new Image16("pR3.png"); //in air right

	static Pointer playerImage = new Pointer(); //pointer to the player image
	

	
	static int keyPressed = 0; //key index 
	static boolean keyState = false; //boolean if pressed or not pressed
	
	static int velX = 0; //velocity X
	static int velY = 0; //velocity y
	
	static int lastVel = 0; //last direction (for display)
	
	static final int xOffset = 35; //x offset for the image on screen
	static final int yOffset = 20; //y offset for the image on screen
	static int playerX = xOffset; //playerPostionX
	static int playerY = yOffset; //playerPositionY
	
	static boolean isMoving = false; //is moving
	static int inMoving = 0; //moving timer
	
	static boolean jump = false; //is jumping
	static boolean onGround = false; //is on ground
	static int inJump = 0; //in air timer
	
    static TileMap16 tileWorld_ = new TileMap16("map.png"); //load tilemap
    static int[] tileWorld = null; //save it to a int array
    static final int tileWorldOffset = 3; //offset in the array for the sizex and sizey
    static Image16 ground = new Image16("ground.png"); //ground image
    static Pointer tile = new Pointer("tile_0"); //pointer to the current tile
    static Image16 tile_0 = new Image16("tile0.png"); //tile 0 image
    static Image16 tile_1 = new Image16("tile1.png"); //tile 1 image
    static Image16 tile_2 = new Image16("tile2.png"); //tile 2 image
    static Image16 tile_3 = new Image16("tile3.png"); //tile 3 image
    static Image16 tile_4 = new Image16("tile4.png"); //tile 4 image
    static Image16 tile_5 = new Image16("tile5.png"); //tile 5 image
    static int worldSizeX = 0; //world size x
    static int worldSizeY = 0; //world size y

	
    public static void mmain() //main class
    {
    	initGraphics();
    	loadPal(pal);
    	initWorld();
    	while(true) {
    		if(jump) {
    			velY=-2;
    			if(inJump<5)
    				velY=-1;
    			inJump--;
    			if(inJump<=0)
    				jump = false;
    			onGround=false;
    		}else
    			velY=1;
    		
    	//	keyPressed = InterUtil.checkKey();
    		
    		keyHandle();
    		handleVel();
    		showDebug();
    		
    		if(velX!=0)
    			lastVel=velX;
   
    		if(lastVel==1) {
    			if(!onGround)
    				playerImage.set(pRF);
    			else if(isMoving) {
    				if(inMoving >= 10)
    					playerImage.set(pR0);
    				else
    					playerImage.set(pR1);
    			}else
    				playerImage.set(pR);
    		}else {
    			if(!onGround)
    				playerImage.set(pLF);
    			else if(isMoving) {
    				if(inMoving >= 10)
    					playerImage.set(pL0);
    				else
    					playerImage.set(pL1);
    			}else
    				playerImage.set(pL);
    		}
    		
    		
    			drawWorld();
    			if(playerY > yOffset)
    				drawImage(playerImage.getValue(), xOffset, playerY);
    			else
    				drawImage(playerImage.getValue(), xOffset, yOffset);
    		
	    	flip();
	    	resetBuffer();
	    	sleep(1000/30);
    	}
    	//testFunc();
    	//halt();
    }
    //static int[] tileWorld = new int[worldSizeX * 7];
    public static void initWorld() {
    	worldSizeX = tileWorld[1];
    	worldSizeY = tileWorld[2];
    }
    public static void drawWorld() {
    	int wOff = (playerY-yOffset);
    	if(playerY > yOffset)
    		wOff = 0;
    	if(wOff < 4)
    		for(int i=0;i<20;i++)
    			drawImage(ground, i*4, 48- wOff);
    	for(int x=0;x<worldSizeX;x++) 
    		for(int y=0;y<worldSizeY;y++) {
    			if(tileWorld[y*worldSizeX+x+tileWorldOffset]!=0) {
    				int id = tileWorld[y*worldSizeX+x+tileWorldOffset];
    				if(id == 1)
    					tile.set(tile_0);
    				else if(id == 2)
    					tile.set(tile_1);
    				else if(id == 3)
    					tile.set(tile_2);
    				else if(id == 4)
    					tile.set(tile_3);
    				else if(id == 5)
    					tile.set(tile_4);
    				else if(id == 6)
    					tile.set(tile_5);
    				drawImage(tile.getValue(), x*8 - (playerX-xOffset),(y-worldSizeY+6) * 8 - wOff);
    			}
    	}
    }
    public static boolean collisionWorld(int tempX, int tempY) {
    	for(int x=0;x<worldSizeX;x++) 
    		for(int y=0;y<worldSizeY;y++) {
    			if(tileWorld[y*worldSizeX+x+tileWorldOffset]==0)continue;
    			if(tempX+9>=x*8 && tempX<=x*8+7 && tempY+15>=(y-worldSizeY+6)*8 && tempY<=(y-worldSizeY+6)*8+7)
    				return true;
    		}
    	return false;
    	
    }

	public static void handleVel() {
    	int tempX = playerX + velX;
    	if(tempX>=0 && !collisionWorld(tempX,playerY)) {
    		playerX=tempX;
    		inMoving++;
    	}
		if(inMoving>=20)
			inMoving=0;
    	int tempY = playerY + velY;
    	if(tempY+15 < 48 && !collisionWorld(playerX,tempY)) {
    		playerY=tempY;
    		onGround = false;
    	}else if(velY>0)
    		onGround = true;
    }
    
    public static void showDebug() {
    	resetCursor();
    	//8;15:1
    	printInt(tileWorld[6*worldSizeX+35+tileWorldOffset]);
    }
    public static void keyHandle() {
    	if(keyPressed==0x1E) { //a
    		if(keyState) {
    			velX=-1;
    			isMoving = true;
    		}else {
    			velX=0;
    			isMoving = false;
    		}
    	}else if(keyPressed==0x20) { //d
    		if(keyState) {
    			velX=1;
    			isMoving = true;
    		}else {
    			velX=0;
    			isMoving = false;
    		}
    	}else if(keyPressed==0x39){ //space
    		if(keyState && onGround) {
    			jump=true;
    			onGround=false;
    			inJump=15;
    		}
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/TestASM.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.addImpl(new KernelKey("keyPressed","keyState", "keyHandle"));
    	CompilerF0xC.addNoMask("keyHandle");
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.setCutter(true);
    	CompilerF0xC.main(ar);
    	//CompilerF0xC.forge.print("");
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createBIN();
	    	CompilerF0xC.createKER();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
