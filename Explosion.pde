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
  void show()
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
  void remove(int i)
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
    dRadians = myPointDirection*Math.PI/180.0;
    xCorners[0] = (int)(radius*Math.cos(Math.PI - dRadians));
    yCorners[0] = (int)(radius*Math.sin(Math.PI - dRadians));
    xCorners[1] = (int)(radius*Math.cos(dRadians));
    yCorners[1] = (int)(radius*Math.sin(dRadians));
    myColor = color(255);
    myCenterX = x;
    myCenterY = y;
    myDirectionX = radius/4.0*Math.cos(dRadians);
    myDirectionY = radius/4.0*Math.sin(dRadians);
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

