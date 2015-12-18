class Asteroid extends Floater
{
  private double rotSpeed,myTheta,myRadius;
  private final static int L = 40;
  private final static int M = 20;
  private final static int S = 10;
  private final static double mSpM = 1.5;//speed multipliers
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
    myDirectionX = speedM*(Math.random()*4*(1+.15*lvl)-2*(1+.15*lvl)); //varies w/ lvl
    myDirectionY = speedM*(Math.random()*4*(1+.15*lvl)-2*(1+.15*lvl));
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
