class scoreText{
  private double myCenterX, myCenterY, timer, score, myO;
  scoreText(Floater A, double s){
    myCenterX = A.getX();
    myCenterY = A.getY();
    timer = 0;
    score = s;
    myO = 255.0;
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
    myO-=255/60.0;
  }
}
