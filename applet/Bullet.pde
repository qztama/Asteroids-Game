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
