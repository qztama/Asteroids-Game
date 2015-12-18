//Tony Zeng, Asteroids, Comp Science
//url: http://qizeng1.webs.com/AsteroidsBeta1.0/AsteroidGame.html

import java.util.*;
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

void setup() {
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

void draw() {
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

void keyPressed() {
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

void keyReleased() {
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

void gameDisplay() {
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
void CollisionAB() {
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

boolean checkCollisionS(Floater F) {//CHECKS COLLISION B/T SPACESHIP & FLOATER
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
void levelSettings() {
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


void gamePlay() {
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

void comboTimer() {
  if(timer <2 && combo!=0) {
    timer+=1/60.0;
  }
  if(timer >=2) {
    combo = 0;
  }
}

//REMOVE-------------------------------------------------------------------------------------------------------------------------------------------------------------------
void remove() {
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

void PowerUpRoll(){
  double rollS, rollP, rollHM;
  rollS = Math.random();
  rollHM = Math.random();
  rollP = Math.random();
  if(rollS < (1+.13*(lvl-1))*.0006){
    PowerUps.add(new Shield());
  }
  if(rollHM < (1+.16*(lvl-1))*.0002){
    PowerUps.add(new HomingMissile());
  }
  if(rollP < (1+.15*(lvl-1))*.0005){
    PowerUps.add(new Plow());
  }
}

void gameOver(){
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

