import processing.core.*; 
import processing.xml.*; 

import java.util.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class AsteroidsGame extends PApplet {

//Tony Zeng, Asteroids


Spaceship SS;
ArrayList<Asteroid> Asteroids;
ArrayList<PowerUp> PowerUps = new ArrayList<PowerUp>();
ArrayList <Bullet> Bullets = new ArrayList<Bullet>();
ArrayList <scoreText> scoreTexts = new ArrayList<scoreText>();
ArrayList <HomingMissileB> Missiles=new ArrayList<HomingMissileB>();
Menu M = new Menu();
Explosions explode;
int lvl, aM, aL;
int combo, score;
double timer;
boolean lvlOn;
boolean gameOver;
boolean gameStart;
PFont font1;

public void setup() {
  size(650,650);
  smooth();
  font1 = loadFont("FranklinGothic-HeavyItalic-64.vlw");
  lvlOn = true;
  gameOver = false;
  gameStart = false;
  score = 0;
  combo = 0;
  timer = 0;
  lvl = 1;
  aM = 0;
  aL = 1;
  Asteroids = new ArrayList <Asteroid>();
  for(int i = 0;i < 1;i++) {
    Asteroids.add(new Asteroid((int)Math.random()*width,(int)Math.random()*height,Asteroid.L,1));
  }
  SS = new Spaceship();
  //PowerUps.add(new Shield());
}

public void draw() {
  background(0);
  if(gameOver){
    gameOver();
    Reset r = new Reset();
    r.click();
    r.show();
  }
  else if(!gameStart){
    M.click();
    M.show();
  }
  else if(gameStart){
    textFont(font1,16);
  PowerUpRoll();
  if(Asteroids.size() == 0) {
    lvlOn = false;
  }
  gamePlay();
  gameDisplay();
  if(lvlOn) {
    CollisionAB();
    if(!SS.getShieldOn() && !SS.getPlowOn()) {
      for(int i=0;i<Asteroids.size();i++) {
        if(checkCollisionS(Asteroids.get(i))){
          explode = new Explosions(SS.getX(),SS.getY());
          gameOver = true;
        }
      }
    }
    //PowerUps
    for(int j=0;j<PowerUps.size();j++) {
      if(checkCollisionS(PowerUps.get(j))) {
        if(PowerUps.get(j).getStage() == 1) {
          PowerUps.get(j).collision();
        }
      }
      //Shield Stage 2 & 3 Collision
      if(PowerUps.get(j) instanceof Shield) {
        if(PowerUps.get(j).getStage() == 2 || PowerUps.get(j).getStage() == 3) {
          for(int i = 0;i<Asteroids.size();i++) {
            if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),PowerUps.get(j).getX(),PowerUps.get(j).getY()) < Asteroids.get(i).getRadius()+PowerUps.get(j).getRadius()) {
              Asteroids.get(i).collision();
              PowerUps.get(j).collision();
              if(Asteroids.size() == i) {
                break;
              }
            }
          }
          if(PowerUps.get(j).getStage() == 3) {
            //println(PowerUps.get(j).getRadius());
            if(PowerUps.get(j).getRadius() >= 65) {
              //PowerUps.remove(j);
              PowerUps.get(j).setHit(true);
              
            }
          }
        }
      }
      //Plow Stage 2 Collision
      if(PowerUps.get(j) instanceof Plow) {
        if(PowerUps.get(j).getStage() == 2) {
          for(int i = 0;i<Asteroids.size();i++) {
            if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),(int)(PowerUps.get(j).getX()+20*Math.cos(SS.getDRadians())),PowerUps.get(j).getY()) < Asteroids.get(i).getRadius()) {
              Asteroids.get(i).collision();
            }
            else if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),(int)(PowerUps.get(j).getX()+10*Math.cos(SS.getDRadians())),(int)(PowerUps.get(j).getY()+20*Math.sin(SS.getDRadians()))) < Asteroids.get(i).getRadius()) {
              Asteroids.get(i).collision();
            }
            else if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),(int)(PowerUps.get(j).getX()+10*Math.cos(SS.getDRadians())),(int)(PowerUps.get(j).getY()-20*Math.sin(SS.getDRadians()))) < Asteroids.get(i).getRadius()) {
              Asteroids.get(i).collision();
            }
          }
        }
      }
      //Homing Missile Stage 2
      if(PowerUps.get(j) instanceof HomingMissile) {
        if(PowerUps.get(j).getStage() == 2) {
          if(Missiles.size() != 0) {
            for(int i = 0;i<Asteroids.size();i++) {
              for(int mI=0;mI<Missiles.size();mI++) {
                if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),Missiles.get(mI).getX(),Missiles.get(mI).getY()) < Asteroids.get(i).getRadius()+5) {
                  if(Asteroids.get(i).getRadius() == Asteroid.M) {
                    Asteroids.get(i).setScore(150);
                  }
                  if(Asteroids.get(i).getRadius() == Asteroid.S) {
                    Asteroids.get(i).setScore(200);
                  }
                  Asteroids.get(i).collision();
                  Missiles.get(mI).collision();
                }
              }
            }
          }
        }
      }
    }
  }
  else {
    levelSettings();
  }
  remove();
  }
}

public void keyPressed() {
  //SHIP CONTROLS
  if(key == CODED) {
    if(keyCode == LEFT) {
      SS.setLeftBoolean(true);
    }
    if(keyCode == RIGHT) {
      SS.setRightBoolean(true);
    }
    if(keyCode == UP) {
      SS.setUpBoolean(true);
    }
  }
}

public void keyReleased() {
  //SHIP CONTROLS
  if(key == CODED) {
    if(keyCode == UP) {
      SS.setUpBoolean(false);
    }
    if(keyCode == RIGHT) {
      SS.setRightBoolean(false);
    }
    if(keyCode == LEFT) {
      SS.setLeftBoolean(false);
    }
  }
  //SHOOTS BULLETS
  if(key == ' ') {
    if(SS.getMagazine() > 0) {
      SS.addBullet();
    }
  }
}

public void gameDisplay() {
  textAlign(0);
  noFill();
  rect(455,height-21,100,15);
  if(combo !=0) {
    fill(255);
    rect(455,height-21,(int)((2-timer)*50),15);
    fill(0);
    text("Combo x"+combo,475,height-9);
  }
  fill(255);
  //println(combo);
  //println(timer);
  text("Score: "+score, 0,15);
  text("Level: "+lvl,10,height-5);
  text("Magazine: "+SS.getMagazine(),100,height-5);
  text("Speed: "+SS.getSpeed(),220,height-5);
}

//CHECK COLLISIONS---------------------------------------------------------------------------------------------------------------------------------------------------------------
public void CollisionAB() {
  if(Bullets.size() != 0) {
    for(int i = 0;i<Asteroids.size();i++) {
      for(int j=0;j<Bullets.size();j++) {
        if(dist(Asteroids.get(i).getX(),Asteroids.get(i).getY(),Bullets.get(j).getX(),Bullets.get(j).getY()) < Asteroids.get(i).getRadius()+5) {
          if(Asteroids.get(i).getRadius() == Asteroid.L) {
            Asteroids.get(i).collision();
            Bullets.get(j).collision();
          }
          else if(Asteroids.get(i).getRadius() == Asteroid.M) {
            Asteroids.get(i).setScore(150);
            Asteroids.get(i).collision();
            Bullets.get(j).collision();
          }
          else if(Asteroids.get(i).getRadius() == Asteroid.S) {
            Asteroids.get(i).setScore(200);
            Asteroids.get(i).collision();
            Bullets.get(j).collision();
          }
          break;
          /*if(Asteroids.size() == i) {
           break;
           }*/
        }
      }
    }
  }
}

public boolean checkCollisionS(Floater F) {//CHECKS COLLISION B/T SPACESHIP & FLOATER
  if(dist((int)(SS.getX()+6*Math.cos(SS.getPointDirection()*Math.PI/180)),(int)(SS.getY()+6*Math.sin(SS.getPointDirection()*Math.PI/180)),F.getX(),F.getY()) < F.getRadius()+8) {
    println("Body");
    return true;
  }
  if(dist((int)(SS.getX()-12*Math.cos(SS.getDRadians())),(int)(SS.getY()-12*Math.sin(SS.getDRadians())),F.getX(),F.getY()) < F.getRadius()+4) {
    println("Back");
    return true;
  }
  if(dist((int)(SS.getX()-12*Math.cos(SS.getDRadians())+29*Math.sin(SS.getDRadians())),(int)(SS.getY()-12*Math.sin(SS.getDRadians())+29*Math.cos(SS.getDRadians())),F.getX(),F.getY()) < F.getRadius()+1) {
    println("R-Wing");
    return true;
  }
  if(dist((int)(SS.getX()-12*Math.cos(SS.getDRadians())-29*Math.sin(SS.getDRadians())),(int)(SS.getY()-12*Math.sin(SS.getDRadians())-29*Math.cos(SS.getDRadians())),F.getX(),F.getY()) < F.getRadius()+1) {
    println("L-Wing");
    return true;
  }
  return false;
}




//LEVELS-------------------------------------------------------------------------------------------------------------------------------------------------------------
public void levelSettings() {
  lvl++;
  SS.addMagazine();
  if(aM %3 == 0) {
    aM = 0;
    aL++;
  }
  if(lvl%2 == 0) {
    aM++;
  }
  else {
    aL++;
  }
  for(int i = 0;i<aL;i++) {
    Asteroids.add(new Asteroid((int)Math.random()*width,(int)Math.random()*height,Asteroid.L,1));
  }
  for(int i = 0;i<aM;i++) {
    Asteroids.add(new Asteroid((int)Math.random()*width,(int)Math.random()*height,Asteroid.M,Asteroid.mSpM));
  }
  lvlOn = true;
}


public void gamePlay() {
  comboTimer();
  SS.BulletTime();
  SS.move();
  SS.show();
  for(int i=0;i<Asteroids.size();i++) {
    Asteroids.get(i).move();
    Asteroids.get(i).show();
  } 
  for(int i = 0; i<PowerUps.size();i++) {
    PowerUps.get(i).move();
    PowerUps.get(i).show();
  }
  for(int i = 0;i<scoreTexts.size();i++) {
    scoreTexts.get(i).move();
    scoreTexts.get(i).show();
  }
}

public void comboTimer() {
  if(timer <2 && combo!=0) {
    timer+=1/60.0f;
  }
  if(timer >=2) {
    combo = 0;
  }
}

//REMOVE-------------------------------------------------------------------------------------------------------------------------------------------------------------------
public void remove() {
  for(int i = 0; i<Asteroids.size(); i++) {
    if(Asteroids.get(i).getHit()) {
      Asteroids.remove(i);
    }
  }
  for(int i = 0; i<Bullets.size(); i++) {
    if(Bullets.get(i).getHit()) {
      Bullets.remove(i);
    }
  }
  for(int i = 0; i<PowerUps.size(); i++) {
    if(PowerUps.get(i).getHit()) {
      PowerUps.remove(i);
    }
  }
  for(int i = 0; i<Missiles.size(); i++) {
    if(Missiles.get(i).getHit()) {
      Missiles.remove(i);
    }
  }
}

public void PowerUpRoll(){
  double rollS, rollP, rollHM;
  rollS = Math.random();
  rollHM = Math.random();
  rollP = Math.random();
  if(rollS < (1+.13f*(lvl-1))*.0006f){
    PowerUps.add(new Shield());
  }
  if(rollHM < (1+.16f*(lvl-1))*.0002f){
    PowerUps.add(new HomingMissile());
  }
  if(rollP < (1+.15f*(lvl-1))*.0005f){
    PowerUps.add(new Plow());
  }
}

public void gameOver(){
  comboTimer();
  SS.BulletTime();
  //SS.show();
  if(explode.getExpTime() !=0){
  explode.show();
  }
  for(int i=0;i<Asteroids.size();i++) {
    Asteroids.get(i).show();
  } 
  for(int i = 0; i<PowerUps.size();i++) {
    PowerUps.get(i).show();
  }
  for(int i = 0;i<Missiles.size();i++){
    Missiles.get(i).show();
  }
  textAlign(CENTER,CENTER);
  fill(255);
  text("Your Final Score Is " + score +"\nGameover", width/2,height/2);
}

class Asteroid extends Floater
{
  private double rotSpeed,myTheta,myRadius;
  private final static int L = 40;
  private final static int M = 20;
  private final static int S = 10;
  private final static double mSpM = 1.5f;//speed multipliers
  private final static double sSpM = 2;
  private int myScore;
  public Asteroid(int x, int y, int r, double speedM){
    corners = (int)(Math.random()*3)+6;
    xCorners = new int[corners];
    yCorners = new int[corners];
    rotSpeed = Math.random()*10-5;
    myRadius = r;
    myTheta = 0;
    for(int i = 0;i<corners;i++){
      xCorners[i] = (int)(myRadius*Math.cos(myTheta));
      yCorners[i] = (int)(myRadius*Math.sin(myTheta));
      myTheta += ((int)(Math.random()*20) + (360/corners-8))*Math.PI/180;
    }
    myCenterX = x; 
    myCenterY = y;
    myColor = color(255);
    myDirectionX = speedM*(Math.random()*4*(1+.15f*lvl)-2*(1+.15f*lvl)); //varies w/ lvl
    myDirectionY = speedM*(Math.random()*4*(1+.15f*lvl)-2*(1+.15f*lvl));
    myPointDirection = (Math.random()*20)-10;
    myScore = 100;
    Hit = false;
  }
  
  public void setScore(int s){
    myScore = s;
  }
  
  public void colorOpt(){
    noFill();
    stroke(myColor);
  }
  
  public void Rotation(){
    myPointDirection+=rotSpeed;
  }
  
  public int getRadius(){
    return (int)myRadius;
  }
  
  public void collision(){
    if(myRadius == L){
      Asteroids.add(new Asteroid((int)myCenterX,(int)myCenterY,M,mSpM));
      Asteroids.add(new Asteroid((int)myCenterX,(int)myCenterY,M,mSpM));
      //Asteroids.remove(Asteroids.indexOf(this));
    }
    else if(myRadius == M){
      Asteroids.add(new Asteroid((int)myCenterX,(int)myCenterY,S,sSpM));
      Asteroids.add(new Asteroid((int)myCenterX,(int)myCenterY,S,sSpM));
      //Asteroids.remove(Asteroids.indexOf(this));
    }
    else if(myRadius == S){
      //Asteroids.remove(Asteroids.indexOf(this));
    }
    Hit = true;
    //Score
    combo++;
    if(combo != 0){
    score+=myScore*combo;
    scoreTexts.add(new scoreText(this, combo*myScore));
    }
    else{
    score+=myScore;
    scoreTexts.add(new scoreText(this, myScore));
    }
    timer = 0;
  }
}
class Bullet extends Floater{
  Bullet(Floater SS){
    myColor = color(255);
    myPointDirection = SS.getPointDirection();
    myCenterX = SS.getX();
    myCenterY = SS.getY();
    myDirectionX = SS.getDirectionX()+10*Math.cos(myPointDirection*Math.PI/180);
    myDirectionY = SS.getDirectionY()+10*Math.sin(myPointDirection*Math.PI/180);
    Hit = false;
  }
  public void Rotation(){
  }
  public void colorOpt(){
    noFill();
    stroke(myColor);
  }
  public void show(){
    colorOpt();
    ellipse((int)myCenterX,(int)myCenterY,10,10);
  }
  
  public void collision(){
    //Bullets.remove(Bullets.indexOf(this));
    SS.addMagazine();
    Hit = true;
  }
  
  public void move ()
  {
    //Spaceship Input Movement
    Rotation();
    //Moves the floater towards the coordinates
    //myDirectionX and myDirectionY

    //move the floater in the current direction of travel
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;
  }
}
class Explosions
{
  ArrayList <Particle> Bang = new ArrayList <Particle>();
  int expTime;
  Explosions(double x, double y)
  {
    for(int i=0;i<40;i++)
    {
      Bang.add(new Particle(x,y));
    }
    expTime = 120;
  }
  public void show()
  {
    for(int i = 0;i<Bang.size();i++)
    {
      Bang.get(i).move();
      Bang.get(i).show();
    } 
    expTime--;
  }
  public int getExpTime(){
    return expTime;
  }
  public void remove(int i)
  {
    if(expTime == 0){
    //nExplosions.remove(i);
    }
  } 
}

class Particle extends Floater
{
  double dRadians;
  int radius, o;
  Particle(double x, double y)
  {
    corners = 2;
    radius = (int)(Math.random()*15)+5;
    myPointDirection = (int)(Math.random()*360);
    xCorners = new int [corners];
    yCorners = new int [corners];
    dRadians = myPointDirection*Math.PI/180.0f;
    xCorners[0] = (int)(radius*Math.cos(Math.PI - dRadians));
    yCorners[0] = (int)(radius*Math.sin(Math.PI - dRadians));
    xCorners[1] = (int)(radius*Math.cos(dRadians));
    yCorners[1] = (int)(radius*Math.sin(dRadians));
    myColor = color(255);
    myCenterX = x;
    myCenterY = y;
    myDirectionX = radius/4.0f*Math.cos(dRadians);
    myDirectionY = radius/4.0f*Math.sin(dRadians);
    o = 255;
    /*println(yCorners[0]);
     println(xCorners[0]);
     println(yCorners[1]);
     println(xCorners[1]);*/
  }
  //useless functions
  public void Rotation(){}
  public void collision(){}
  public void colorOpt(){};
  //
  public void setX(int x) {myCenterX = x;}
  public int getX() {return (int)myCenterX;}
  public void setY(int y) {myCenterY = y;}
  public int getY() {return (int)myCenterY;}
  public void setDirectionX(double x) {myDirectionX = x;}
  public double getDirectionX() {return myDirectionX;}
  public void setDirectionY(double y) {myDirectionY = y;}
  public double getDirectionY() {return myDirectionY;}
  public void setPointDirection(int degrees) {myPointDirection = degrees;}
  public double getPointDirection() {return myPointDirection;}

  public void show ()
  {  
    //Draws the floater at the current position

    fill(255,255,255,o);
    stroke(255,255,255,o);
    //convert degrees to radians for sin and cos     
    double dRadians = myPointDirection*(Math.PI/180);

    int xRotatedTranslated, yRotatedTranslated;
    beginShape();

    //rotate and translate the coordinates of the floater using current direction	
    for(int nI = 0; nI < corners; nI++)
    {
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY); 
      vertex(xRotatedTranslated,yRotatedTranslated);
    }
    endShape(CLOSE);
    o-=5;
  }
  
  public void move(){
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;
  } 
}

abstract class Floater
{
  protected int[] xCorners;
  protected int[] yCorners;
  protected int corners;  //the number of corners, a triangular floater has 3
  protected int myColor;
  protected double myCenterX, myCenterY; //holds center coordinates
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel
  protected double myPointDirection; //holds current direction the ship is pointing in degrees
  protected boolean Hit;

  public void setHit(boolean h){Hit = h;}
  public boolean getHit(){return Hit;}
  public void setX(int x) {myCenterX = x;}
  public int getX() {return (int)myCenterX;}
  public void setY(int y) {myCenterY = y;}
  public int getY() {return (int)myCenterY;}
  public void setDirectionX(double x) {myDirectionX = x;}
  public double getDirectionX() {return myDirectionX;}
  public void setDirectionY(double y) {myDirectionY = y;}
  public double getDirectionY() {return myDirectionY;}
  public void setPointDirection(int degrees) {myPointDirection = degrees;}
  public double getPointDirection() {return myPointDirection;}

  abstract public void Rotation();
  abstract public void colorOpt();
  abstract public void collision();
  public int getRadius(){return 1;}

  public void accelerate (double dAmount)
  {  
    //Accelerates the floater in the direction it is pointing
    //(myPointDirection)

    //convert the current direction the floater is pointing to radians
    double dRadians =myPointDirection*(Math.PI/180);

    //change coordinates of direction of travel
    myDirectionX += ((dAmount) * Math.cos(dRadians));
    myDirectionY += ((dAmount) * Math.sin(dRadians));
  }
  public void rotate (int nDegreesOfRotation)
  {  
    //rotates the floater by a given number of degrees
    myPointDirection+=nDegreesOfRotation;
  }
  public void move ()
  {
    //Spaceship Input Movement
    Rotation();
    //Moves the floater towards the coordinates
    //myDirectionX and myDirectionY

    //move the floater in the current direction of travel
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;

    //wrap around screen
    if(myCenterX >width) {
      myCenterX = 0;
    }
    else if (myCenterX<0) {
      myCenterX = width;
    }
    if(myCenterY >height) {
      myCenterY = 0;
    }
    else if (myCenterY < 0) {
      myCenterY = height;
    }
  }
  public void show ()
  {  
    //Draws the floater at the current position

    colorOpt();
    //convert degrees to radians for sin and cos     
    double dRadians = myPointDirection*(Math.PI/180);

    int xRotatedTranslated, yRotatedTranslated;
    beginShape();

    //rotate and translate the coordinates of the floater using current direction	
    for(int nI = 0; nI < corners; nI++)
    {
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY); 
      vertex(xRotatedTranslated,yRotatedTranslated);
    }
    endShape(CLOSE);
  }
}

class Menu {
  ArrayList <PowerUp> Powers; 
  public Menu() {
    Powers = new ArrayList<PowerUp>();
    Powers.add(new Shield());
    Powers.add(new Plow());
    Powers.add(new HomingMissile());
    for(int i = 0;i<Powers.size();i++) {
      Powers.get(i).setDirectionX(0);
      Powers.get(i).setDirectionY(0);
      Powers.get(i).setX(100);
      Powers.get(i).setY(300+50*i);
      println(300+50*i);
    }
  }

  public void show() {
    textAlign(CENTER,CENTER);
    //Title
    fill(255);
    textFont(font1,64);
    text("ASTEROIDS",width/2,4*height/16-10);
    //Controls
    //textAlign(0);
    textFont(font1,16);
    text("UP ARROW KEY: ACCEL\nRIGHT/LEFT ARROW KEY: TURN\nSPACE: SHOOT",width/2,height/4+75);
    //PowerUps
    for(int i = 0;i<Powers.size();i++) {
      Powers.get(i).move();
      Powers.get(i).show();
    }
    textAlign(0);
    text("Gives SpaceShip a shield that explodes on contact, creating a \nshockwave that breaks asteroids on contact. Duration: Indefinite", 130,300);
    text("Gives SpaceShip a plow that can be used to ram through asteroids, \nbreaking them on contact. Duration: 10 Seconds", 130,350);
    text("Unleashes a barrage of homing missiles depending on the number \nof asteroids on the field. Duration: Indefinite", 130,400);
    //Start
    textAlign(CENTER,CENTER);
    textFont(font1,32);
    stroke(255,255,255,150);
    fill(0);
    rect(width/2-75,3*height/4,150,50);
    fill(255,255,255,150);
    text("Start",width/2,3*height/4+25);
    if(mouseX > width/2 -75 && mouseX < width/2+75) {
      if(mouseY > 3*height/4 && mouseY < 3*height/4+50) {
        stroke(255,255,255);
        noFill();
        rect(width/2-75,3*height/4,150,50);
        fill(255,255,255);
        text("Start",width/2,3*height/4+25);
      }
    }
  }

  public void click() {
    if(mousePressed && (mouseButton == LEFT)){
    {
      if(mouseX > width/2 -75 && mouseX < width/2+75) {
        if(mouseY > 3*height/4 && mouseY < 3*height/4+50) {
          gameStart = true;
        }
      }
    }
    }
  }
}

abstract class PowerUp extends Floater {
  private final static int pR = 30;
  protected int myStage;

  public int getStage() {
    return myStage;
  }
  
  public void setStage(int s) {
    myStage = s;
  }
}
//Shield--------------------------------------------------------------------------------------------------------------------------------------
class Shield extends PowerUp {
  private double effectR;
  private double o;
  private boolean WB;
  Shield() {
    myColor = color(255);
    myCenterX = Math.random()*width; 
    myCenterY = Math.random()*height;
    myDirectionX = Math.random()*4 - 2; 
    myDirectionY = Math.random()*4 - 2;
    myStage = 1;
    effectR = 70;
    o = 255;
    WB = true;
    Hit = false;
  }
  public void show() {
    if(myStage == 1) {
      //
      textAlign(CENTER,CENTER);
      fill(255,255,255,(int)o);
      stroke(255,255,255,(int)o);
      ellipse((int)myCenterX,(int)myCenterY,PowerUp.pR,PowerUp.pR);
      //
      fill(0);
      text("S", (int)myCenterX,(int)myCenterY -1);
    }
    if(myStage == 2) {
      myCenterX = SS.getX()-4*Math.cos(SS.getDRadians());
      myCenterY = SS.getY()-4*Math.sin(SS.getDRadians());
      fill(255,255,255,(int)o);
      stroke(255,255,255,(int)o);
      ellipse((int)(myCenterX),(int)(myCenterY),70,70); //1
    }
    if(myStage == 3) {
      noFill();
      stroke(255,255,255,(int)o);
      ellipse((int)myCenterX,(int)myCenterY,(int)effectR,(int)effectR);
    }
  }
  public void move() {
    if(myStage == 1) {
      myCenterX += myDirectionX;
      myCenterY += myDirectionY;

      //wrap around screen
      if(myCenterX >width) {
        myCenterX = 0;
      }
      else if (myCenterX<0) {
        myCenterX = width;
      }
      if(myCenterY >height) {
        myCenterY = 0;
      }
      else if (myCenterY < 0) {
        myCenterY = height;
      }
      //Opacity
      if(WB) {
        o-=155/120.0f;
        if(o <= 100) {
          WB = false;
        }
      }
      else {
        o+=155/120.0f;
        if(o >= 255) {
          WB = true;
        }
      }
    }
    if(myStage == 3) {
      effectR = effectR + 1.0f/2;
      o-=255/140.0f;
    }
  }
  public void colorOpt() {
  }
  public void collision() {
    if(myStage == 1) {
      if(SS.getShieldOn()){
        Hit = true;
      }
      else{
        myStage++;
      }
      SS.setShieldOn(true);
      o = 100;
    }
    else if(myStage == 2) {
      myStage++;
      o = 255;
      SS.setShieldOn(false);
    }
  }
  public int getRadius() {
    if(myStage == 1) {
      return (int)(PowerUp.pR/2);
    }
    else if(myStage == 2) {
      return 35;                         //1
    }
    else {
      return (int)(effectR/2);
    }
  }

  public void Rotation() {
  }
}
//Plow-----------------------------------------------------------------------------------------------------------------------------------------
class Plow extends PowerUp
{
  private double myTimer;
  Plow() {
    corners = 3;
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = 0;
    yCorners[0] = 20;
    xCorners[1] = (int)(20*Math.cos(Math.PI/6.0f));
    yCorners[1] = -(int)(20*Math.sin(Math.PI/6.0f));
    xCorners[2] = -(int)(20*Math.cos(Math.PI/6.0f));
    yCorners[2] = -(int)(20*Math.sin(Math.PI/6.0f));

    myColor= color(255);
    myStage = 1;
    myCenterX = Math.random()*width; 
    myCenterY = Math.random()*height;
    myDirectionX = Math.random()*4 - 2; 
    myDirectionY = Math.random()*4 - 2;
    myPointDirection = 0;
    myTimer = 0;
    Hit = false;
  }
  
  public void setStage(int s){
    myStage = s;
    xCorners[0] = 20;
    yCorners[0] = 0;
    xCorners[1] = 10;
    yCorners[1] = 20;
    xCorners[2] = 10;
    yCorners[2] = -20;
  }

  public void colorOpt() {
    /*if(myStage == 1) {
      stroke(255);
    }
    else {
      fill(0);
      stroke(255);
    }*/
  }
  
  public void Rotation() {
    rotate(5);
  }

  public void show() {
    if(myStage == 1) {
      stroke(255);
      fill(255,255,255,100);
      super.show();
      fill(255);
      textAlign(CENTER,CENTER);
      text("P", (int)myCenterX, (int)(myCenterY-1));
      textAlign(0);
    }
    else {
      textAlign(0);
      noFill();
      rect(width/2-50,20,100,15);
      fill(255);
      rect(width/2-50,20,(int)((10-myTimer)*10),15);
      fill(255);
      text((int)(10-myTimer),width/2,32);
      myCenterX = SS.getX();
      myCenterY = SS.getY();
      myPointDirection = SS.getPointDirection();
      fill(0);
      stroke(255);
      super.show();
    }
  }
  public void move() {
    if(myStage == 1) {
      super.move();
    }
    if(myStage == 2) {
      myTimer+=1/60.0f;
      println(myTimer);
      if(myTimer >=10) {
        //PowerUps.remove(PowerUps.indexOf(this));
        Hit = true;
        SS.setPlowOn(false);
      }
    }
  }

  public int getRadius() {
    if(myStage == 1) {
      return (int)(PowerUp.pR/2);
    }
    return 0;
  }

  public void collision() {
    if(myStage == 1) {
      if(SS.getPlowOn()){
        for(int i =0;i<PowerUps.size();i++){
          if(PowerUps.get(i) instanceof Plow){
            PowerUps.get(i).setHit(true);
          }
        }
        Plow Refresh = new Plow();
        Refresh.setStage(2);
        PowerUps.add(Refresh);
      }
      else{
      myStage++;
      xCorners[0] = 20;
      yCorners[0] = 0;
      xCorners[1] = 10;
      yCorners[1] = 20;
      xCorners[2] = 10;
      yCorners[2] = -20;
      SS.setPlowOn(true);
      }
    }
  }
} 
//----------------------------------------------------------------------------------------------------------------------
class HomingMissile extends PowerUp {
  private int s1myTheta;
  HomingMissile() {
    s1myTheta = 0;
    myPointDirection = 90;
    
    corners = 5;
    xCorners = new int[corners];
    yCorners = new int[corners];
    for(int i = 0; i<corners; i++) {
      xCorners[i] = (int)(18*Math.cos(s1myTheta*Math.PI/180));
      yCorners[i] = (int)(18*Math.sin(s1myTheta*Math.PI/180));
      s1myTheta+=360.0f/corners;
    }
    
    myCenterX = Math.random()*width; 
    myCenterY = Math.random()*height;
    myDirectionX = Math.random()*4 - 2; 
    myDirectionY = Math.random()*4 - 2;
    myStage = 1;
  }
  public void show() {
    if(myStage == 1) {
      super.show();
      fill(255);
      textAlign(CENTER,CENTER);
      text("HM", (int)myCenterX, (int)myCenterY-2);
    }
    if(myStage == 2){
      for(int mI = 0; mI<Missiles.size();mI++){
        Missiles.get(mI).move();
        Missiles.get(mI).show();
      } 
      //println(Missiles.size());
      if(Missiles.size() == 0){
        Hit = true;
        //PowerUps.remove(PowerUps.indexOf(this));
    }
  }
  }
  
  public int getRadius() {
    if(myStage == 1) {
      return (int)(PowerUp.pR/2);
    }
    return 0;
  }

  public void Rotation() {
    if(myStage == 1){
    rotate(3);
    }
  }

  public void move() {
    if(myStage == 1) {
      super.move();
    }
    if(myStage == 2){
    }
  }
  
  public void colorOpt(){
    fill(0);
    stroke(255);
  }

  public void collision() {
    if(myStage == 1) {
      myStage++;
      int nMissiles = Asteroids.size();
      /*if(Asteroids.size()<5){
        nMissiles = Asteroids.size();
      }
      else{
        nMissiles = 5;
      }*/
      for(int mI=0;mI<nMissiles;mI++){
        if(mI == Asteroids.size()){
          break;
        }
        Missiles.add(new HomingMissileB((double)(mI*(360/nMissiles)),mI,myCenterX,myCenterY));
        println(mI*(360/nMissiles)/180.0f*Math.PI);
      }
    } 
    else if(myStage == 2){
      //PowerUps.remove(PowerUps.indexOf(this));
      Hit = true;
    }
  }
}
//HOMING MISSILE BULLET--------------------------------------------------------------------------------------------------------------------
class HomingMissileB extends Floater{
  private boolean tLeft, tRight;
  private int myAI;
  private Floater A;
  private int speedlength;
  private PVector Av, mySpeed;
  private double ATheta;
  boolean CCW, CW;
  int count;
  HomingMissileB(double pointDirection, int aI, double x, double y){
    myPointDirection = pointDirection;
    corners = 4;
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = -6;
    yCorners[0] = -5;
    xCorners[1] = 5;
    yCorners[1] = -5;
    xCorners[2] = 5;
    yCorners[2] = 5;
    xCorners[3] = -6;
    yCorners[3] = 5;
    myAI = aI;
    myCenterX = x;
    myCenterY = y;
    myDirectionX = 0;
    myDirectionY = 0;
    speedlength = 5;
    count = 0;
    Hit = false;
  }
  
  public void show(){
    noFill();
    stroke(255);
    ellipse((int)(myCenterX + 5*Math.cos(myPointDirection*Math.PI/180)),(int)((myCenterY)+ 5*Math.sin(myPointDirection*Math.PI/180)),10,10);
    //println("ellipsecenter: "+(int)(5*Math.cos(myPointDirection*Math.PI/180)));
    super.show();
  }
  
  public void Rotation(){
  }
  
  public void collision(){
    //Missiles.remove(Missiles.indexOf(this));
    Hit = true;
  }
  
  public void move(){
    if(Asteroids.size() == 0){
      Hit = true;
    }
    else{
      if(myAI >= Asteroids.size()){
        myAI = (int)(Math.random()*Asteroids.size());
      }
    A = Asteroids.get(myAI);
    checkLocation();
    myDirectionX = speedlength*Math.cos(myPointDirection*Math.PI/180.0f);
    //println(myDirectionX);
    myDirectionY = speedlength*Math.sin(myPointDirection*Math.PI/180.0f);
    //println(myDirectionY); 
    super.move();
    }
  }
  
  public void colorOpt(){
    fill(0);
    stroke(255);
  }
  
  public void checkLocation(){
    Av = new PVector((float)(A.getX()-myCenterX), (float)(A.getY() - myCenterY));
    ATheta = Math.atan2(Av.y,Av.x) *180/Math.PI;
    //println(ATheta);
    //println(myPointDirection);
    if(myPointDirection>=360){
      myPointDirection-=360;
    }
    if(ATheta < 0){
      ATheta+=360;
    }
    if(myPointDirection<ATheta){
      myPointDirection+=5;
    }
    else if(myPointDirection>ATheta){
      myPointDirection-=5;
    }
  }
}

class Reset {
  
  public Reset(){
    Asteroids= new ArrayList <Asteroid>();
    PowerUps = new ArrayList<PowerUp>();
    Bullets = new ArrayList<Bullet>();
    scoreTexts = new ArrayList<scoreText>();
    Missiles=new ArrayList<HomingMissileB>();
  }

  public void show() {
    fill(0);
    stroke(255,255,255,150);
    rect(width/2-50,height/2+25,100,50);
    fill(255,255,255,150);
    text("Replay", width/2, height/2+50);
    if(mouseX > (width/2-50) && mouseX < (width/2+50)) {
      if(mouseY > height/2+25 && mouseY < height/2+75) {
        fill(0);
        stroke(255);
        rect(width/2-50,height/2+25,100,50);
        fill(255);
        text("Replay", width/2, height/2+50);
      }
    }
  }

  public void click() {
    if(mousePressed && (mouseButton == LEFT)) {
      {
        if(mouseX > (width/2-50) && mouseX < (width/2+50)) {
          if(mouseY > height/2+25 && mouseY < height/2+75) {
            lvlOn = true;
            gameOver = false;
            gameStart = true;
            score = 0;
            combo = 0;
            timer = 0;
            lvl = 1;
            aM = 0;
            aL = 1;
            Asteroids = new ArrayList <Asteroid>();
            for(int i = 0;i < 1;i++) {
              Asteroids.add(new Asteroid((int)Math.random()*width,(int)Math.random()*height,Asteroid.L,1));
            }
            SS = new Spaceship();
          }
        }
      }
    }
  }
}

class Spaceship extends Floater//Bullets, Ship, Ship Input,Ship Color
{

  protected int[] fxCorners;
  protected int[] fyCorners;
  protected int flameCorners;
  boolean upIsPressed, rightIsPressed, leftIsPressed;
  boolean shieldOn;
  boolean plowOn;
  int magazine;
  Spaceship()
  {
    corners = 9;  //the number of corners, a triangular floater has 3
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = 18;
    yCorners[0] = 0;
    xCorners[1] = -12;
    yCorners[1] = 30;
    xCorners[2] = 0;
    yCorners[2] = 6;
    xCorners[3] = -6;
    yCorners[3] = 0;
    xCorners[4] = -18;
    yCorners[4] = 12;
    xCorners[5] = -18;
    yCorners[5] = -12;
    xCorners[6] = -6;
    yCorners[6] = 0;
    xCorners[7] = 0;
    yCorners[7] = -6;
    xCorners[8] =-12;
    yCorners[8] = -30;
    flameCorners = 11;

    fyCorners = new int[flameCorners];
    fxCorners = new int[flameCorners];
    fxCorners[0] = -19;
    fyCorners[0] = 3;
    fxCorners[1] = -28;
    fyCorners[1] = 9;
    fxCorners[2] = -25;
    fyCorners[2] = 3;
    fxCorners[3] = -34;
    fyCorners[3] = 6;
    fxCorners[4] = -31;
    fyCorners[4] = 3;
    fxCorners[5] = -40;
    fyCorners[5] = 0;
    fxCorners[6] = -31;
    fyCorners[6] = -3;
    fxCorners[7] = -34;
    fyCorners[7] = -6;
    fxCorners[8] =-25;
    fyCorners[8] = -3;
    fxCorners[9] =-28;
    fyCorners[9] = -9;
    fxCorners[10] =-19;
    fyCorners[10] = -3;

    upIsPressed = false;
    rightIsPressed = false;
    leftIsPressed = false;
    myColor = color(255);
    myCenterX = width/2; 
    myCenterY = height/2; //holds center coordinates
    myDirectionX = 0; 
    myDirectionY = 0; //holds x and y coordinates of the vector for direction of travel
    myPointDirection = 0; //holds current direction the ship is pointing in degrees
    magazine = 5;

    shieldOn = false;
    plowOn = false;
    Hit = false;
  }
  public void Rotation() {
    if(upIsPressed) {
      double dAmount = .2f;
      double dRadians =myPointDirection*(Math.PI/180);

      //change coordinates of direction of travel
      if(Math.abs(myDirectionX) > 10.0f/Math.sqrt(2)) {
        if(myDirectionX > 0) {
          myDirectionX = 10.0f/Math.sqrt(2);
        }
        else {
          myDirectionX = -10.0f/Math.sqrt(2);
        }
      }
      else {
        myDirectionX += ((dAmount) * Math.cos(dRadians));
      }
      if(Math.abs(myDirectionY) > 10.0f/Math.sqrt(2)) {
        if(myDirectionY > 0) {
          myDirectionY = 10.0f/Math.sqrt(2);
        }
        else {
          myDirectionY = -10.0f/Math.sqrt(2);
        }
      }
      else {
        myDirectionY += ((dAmount) * Math.sin(dRadians));
      }
    }
    if(leftIsPressed) {
      rotate(-15);
    }
    if(rightIsPressed) {
      rotate(15);
    }
  }
  public double getSpeed() {
    return (Math.sqrt(myDirectionX*myDirectionX + myDirectionY*myDirectionY));
  }
  public void colorOpt() {
    noFill();
    strokeWeight(1);
    if(shieldOn || plowOn) {
      stroke(255,255,255,100);
    }
    else {
      stroke(myColor);
    }
  }
  //Shield
  public void setShieldOn(boolean b) {
    shieldOn = b;
  }

  public boolean getShieldOn() {
    return shieldOn;
  }

  //Plow
  public void setPlowOn(boolean b) {
    plowOn = b;
  }

  public boolean getPlowOn() {
    return plowOn;
  }
  //
  public void collision() {
  }

  public double getDRadians() {
    return PI/180*myPointDirection;
  }
  public void setUpBoolean(boolean b) {
    upIsPressed = b;
  }
  public void setLeftBoolean(boolean b) {
    leftIsPressed = b;
  }
  public void setRightBoolean(boolean b) {
    rightIsPressed = b;
  }

  public void show() {
    //noFill();
    //stroke(255,0,0);
    //ellipse((int)(myCenterX+6*Math.cos(SS.getPointDirection()*Math.PI/180)),(int)(myCenterY+6*Math.sin(SS.getPointDirection()*Math.PI/180)),16,16);
    colorOpt();
    if(magazine !=0) {
      ellipse((int)myCenterX,(int)myCenterY,10,10);
    }
    //convert degrees to radians for sin and cos     
    double dRadians = myPointDirection*(Math.PI/180);

    int xRotatedTranslated, yRotatedTranslated;
    beginShape();

    //rotate and translate the coordinates of the floater using current direction	
    for(int nI = 0; nI < corners; nI++)
    {
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY); 
      vertex(xRotatedTranslated,yRotatedTranslated);
    }
    endShape(CLOSE);

    if(upIsPressed == true)
    {
      fill(250,160,0);
      strokeWeight(3);
      stroke(255,0,0,150);
      beginShape();

      //rotate and translate the coordinates of the floater using current direction	
      for(int nI = 0; nI < flameCorners; nI++)
      {
        xRotatedTranslated = (int)((fxCorners[nI]* Math.cos(dRadians)) - (fyCorners[nI] * Math.sin(dRadians))+myCenterX);
        yRotatedTranslated = (int)((fxCorners[nI]* Math.sin(dRadians)) + (fyCorners[nI] * Math.cos(dRadians))+myCenterY); 
        vertex(xRotatedTranslated,yRotatedTranslated);
      }
      endShape(CLOSE);
    }
    fxCorners[1] = (int)((Math.random())*3)-28;
    fxCorners[3] = (int)((Math.random())*2)-34;
    fxCorners[5] = (int)((Math.random())*4)-40;
    fxCorners[7] = (int)((Math.random())*2)-34;
    fxCorners[9] = (int)((Math.random())*3)-28;
    strokeWeight(1);
  }

  public int getMagazine() {
    return magazine;
  }

  public void addMagazine() {
    magazine++;
  }

  public void addBullet() {
    Bullets.add(new Bullet(this));
    magazine--;
  }

  public void BulletTime() {
    for(int i =0;i<Bullets.size();i++) {
      Bullets.get(i).move();
      Bullets.get(i).show();
      if(Bullets.get(i).getX() < 0 || Bullets.get(i).getX() > width || Bullets.get(i).getY() < 0 || Bullets.get(i).getY() > height) {
        Bullets.remove(i);
        magazine++;
      }
    }
  }
}

class scoreText{
  private double myCenterX, myCenterY, timer, score, myO;
  scoreText(Floater A, double s){
    myCenterX = A.getX();
    myCenterY = A.getY();
    timer = 0;
    score = s;
    myO = 255.0f;
  }
  public void show(){
    if(myO <= 0){
      scoreTexts.remove(scoreTexts.indexOf(this));
    }
    textAlign(CENTER,CENTER);
    fill((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255),(int)myO);
    text((int)score,(int)myCenterX,(int)myCenterY);
    textAlign(0);
  }
  public void move(){
    myCenterY-=1;
    myO-=255/60.0f;
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#EBE9ED", "AsteroidsGame" });
  }
}
