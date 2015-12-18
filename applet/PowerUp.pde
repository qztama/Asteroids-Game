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
        o-=155/120.0;
        if(o <= 100) {
          WB = false;
        }
      }
      else {
        o+=155/120.0;
        if(o >= 255) {
          WB = true;
        }
      }
    }
    if(myStage == 3) {
      effectR = effectR + 1.0/2;
      o-=255/140.0;
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
    xCorners[1] = (int)(20*Math.cos(Math.PI/6.0));
    yCorners[1] = -(int)(20*Math.sin(Math.PI/6.0));
    xCorners[2] = -(int)(20*Math.cos(Math.PI/6.0));
    yCorners[2] = -(int)(20*Math.sin(Math.PI/6.0));

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
      myTimer+=1/60.0;
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
      s1myTheta+=360.0/corners;
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
        println(mI*(360/nMissiles)/180.0*Math.PI);
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
    myDirectionX = speedlength*Math.cos(myPointDirection*Math.PI/180.0);
    //println(myDirectionX);
    myDirectionY = speedlength*Math.sin(myPointDirection*Math.PI/180.0);
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

