package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class World extends JPanel
{
 int hoehe;
 int breite;
 private final int level;
 private int plattformzeiger;
 Plattform[] plattformen;
 BufferedImage hintergrund;
 Image[][] plattformbilder;
 protected Rectangle[] hitbox;

 public World(int level, Graphics g)
 {
  this.level = level;
  paintComponent(g);
 }

 public void paintComponent(Graphics g)
 {
  super.paintComponent(g);

  try
  {
   hintergrund = ImageIO.read(new File("res/New_Background.png"));
   g.drawImage(hintergrund.getScaledInstance(1300, 900, Image.SCALE_DEFAULT),0,0,null);
  }
  catch(IOException ioe)
  {
   System.out.println("Kein Hintergrundbild gefunden!");
  }

  draw(g);
 }

 private void draw(Graphics g)
 {
  switch(level)
  {
   case 1 -> erzeugeLevel1(g);
   case 2 -> erzeugeLevel2(g);
  }
 }

 void erzeugeLevel1(Graphics g)
 {
  System.out.println("level");

  plattformen = new Plattform[3];
  plattformbilder = new Image[plattformen.length][100];
  hitbox = new Rectangle[plattformen.length];

  for(int i=0;i<hitbox.length;i++){

   hitbox[i] = new Rectangle();
  }



  plattformzeiger=0;

  for(; plattformzeiger<plattformen.length; plattformzeiger++)
  {
   plattformen[plattformzeiger] = new Plattform();
  }

  plattformzeiger = 0;

  erstellePlattform(0,0,7,5, 0,g);
  erstellePlattform(500,300,3,2,0,g);
  erstellePlattform(300,600,5,15,0,g);

  zeichnePlattformen(g);
 }

 void erzeugeLevel2(Graphics g)
 {
  plattformen = new Plattform[2];
  plattformbilder = new Image[plattformen.length][100];
  hitbox = new Rectangle[plattformen.length];

  for(int i=0;i<hitbox.length;i++){

   hitbox[i] = new Rectangle();
  }

  plattformzeiger = 0;

  for(; plattformzeiger<plattformen.length;plattformzeiger++)
  {
   plattformen[plattformzeiger] = new Plattform();
  }

  plattformzeiger = 0;
 }

 void setzeStart (int x, int y) {
  hoehe = y;
  breite = x;
 }
 void setzeEnde (int x, int y) {
  hoehe = y;
  breite = x;
 }
 public void drawHitbox(Graphics g) {

  g.setColor(Color.PINK);
  //objektiv bessere For-schleife
     for (Rectangle rectangle : hitbox) {
         g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
     }
 }

 public Rectangle[] getPlattformHitboxes() {

  Rectangle[] rectangles = new Rectangle[plattformen.length];
  System.arraycopy(hitbox, 0, rectangles, 0, rectangles.length);

  return rectangles;
 }

 public void zeichnePlattformen(Graphics g)
 {
  g.drawImage(hintergrund.getScaledInstance(1300, 900, Image.SCALE_DEFAULT),0,0,null);
  drawHitbox(g);


  for(int i=0;i<plattformen.length;i++)
  {
   for(int j=0;j<100;j++)
   {
    if(plattformbilder[i][j] != null)
    {
     g.drawImage(plattformbilder[i][j], plattformen[i].getPositions().get(j)[0], plattformen[i].getPositions().get(j)[1], null);
    }
    else
    {
     break;
    }
   }
  }
 }

 void erstellePlattform(int x, int y, int laenge, int hoehe, int schaedlich , Graphics g)
 {
  if(plattformzeiger>=plattformen.length)
  {
   System.out.println("Hat versucht, eine nicht vorhandene Plattform zu erstellen!");
   return;
  }

  hitbox[plattformzeiger].x = 16 + x;
  hitbox[plattformzeiger].y = 16 + y;
  hitbox[plattformzeiger].width = 38 * laenge;
  hitbox[plattformzeiger].height = 38 * hoehe;

  try
  {
   plattformen[plattformzeiger].erzeugePlattform(x,y,laenge,hoehe,schaedlich);
  }
  catch(IOException ioe)
  {
   System.out.println("Fehler mit den Bildern");
  }
  catch(NullPointerException npe)
  {
   System.out.println("Wir haben ein Problem: es ist null!");
  }

  ArrayList<int[]> positions = plattformen[plattformzeiger].getPositions();
  BufferedImage[] platformPictures = plattformen[plattformzeiger].getImages();

  int imageDimensions = platformPictures[0].getHeight() * 3;

  if(platformPictures.length != positions.size())
  {
   System.out.println("Anzahl an Plattformpositionen und Plattformbildern stimmt nicht überein!");
   return;
  }

  for(int i=0;i<positions.size();i++) {
   //g.drawImage(platformPictures[i].getScaledInstance(imageDimensions,imageDimensions,Image.SCALE_DEFAULT), positions.get(i)[0], positions.get(i)[1], null);
   plattformbilder[plattformzeiger][i] = platformPictures[i].getScaledInstance(imageDimensions, imageDimensions, Image.SCALE_DEFAULT);
  }
  plattformzeiger ++;
 }

 void loeschePlattform(int index)
 {
  if(index<0 || index>=plattformzeiger)
  {
   System.out.println("Angegebener Index " + index + " nicht gültig!");
   return;
  }

  for(int i=index;i<plattformen.length-1;i++)
  {
   plattformen[i] = plattformen[i+1];
  }
  plattformen[plattformen.length-1] = null;
  plattformzeiger --;
 }
}