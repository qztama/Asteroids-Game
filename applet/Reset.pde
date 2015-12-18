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

