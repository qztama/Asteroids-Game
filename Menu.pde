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

