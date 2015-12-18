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
      double dAmount = .2;
      double dRadians =myPointDirection*(Math.PI/180);

      //change coordinates of direction of travel
      if(Math.abs(myDirectionX) > 10.0/Math.sqrt(2)) {
        if(myDirectionX > 0) {
          myDirectionX = 10.0/Math.sqrt(2);
        }
        else {
          myDirectionX = -10.0/Math.sqrt(2);
        }
      }
      else {
        myDirectionX += ((dAmount) * Math.cos(dRadians));
      }
      if(Math.abs(myDirectionY) > 10.0/Math.sqrt(2)) {
        if(myDirectionY > 0) {
          myDirectionY = 10.0/Math.sqrt(2);
        }
        else {
          myDirectionY = -10.0/Math.sqrt(2);
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

