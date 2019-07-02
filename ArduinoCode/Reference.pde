import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

int samples=0;
float total=0;  
PFont theFont;
float target=100;

import processing.serial.*;
Serial myPort;
int oldX=0;
float oldY=0;
PrintWriter outData;
float[] filterdat=new float[5];
Minim       minim;
AudioOutput out;
Oscil       wave;

void setup() {
  size(500, 200);
  minim = new Minim(this);
 myPort = new Serial(this, "COM4", 9600);

  out = minim.getLineOut();


  wave = new Oscil( 440, 0.5f, Waves.SINE );

  wave.patch( out );
  wave.setFrequency( 5000 );
  theFont=loadFont("font.vlw");
  textFont(theFont, 30);

  outData=createWriter("results.csv");
  frameRate(100);
  background(0);
  for (int i=0; i < 5; i++) {
    filterdat[i]=0;
  }
}

float mafilter(float data) {
  double total=0;
  for (int i=2; i < 5; i++) {
    filterdat[i-1]=filterdat[i];
    total=total+filterdat[i];
  }
  total=total/3;
  filterdat[4]=data;
  return (float)total;
}

void draw() {
  background(0);
  fill(255);
    textFont(theFont, 30);
    String rawData="";
    while (rawData.indexOf("v") == -1) {
      if ( myPort.available() > 0) {
      rawData=rawData+ char(myPort.read());   
      }      
      }
      
      String[] splitup=rawData.replace("v","").split(",");
      float rvalue=mafilter(float(splitup[1]));
      float rvalue2=(float(splitup[1]));
//  float foo=random(0, 200);
//  float rvalue=mafilter(foo);
//  float rvalue2=foo;
//   rawData=""+rvalue;
 char status='0';
  if (keyPressed) {
    if (key == ' ') {
      samples=0;
      total=0;

    }
          if (key=='q') {
        outData.flush();
        outData.close();
        exit();
    }
  }
  if (samples < 10) {
    total=total+rvalue;
    samples++;
    text("Calibrating...", 10, 10);
          status='1';
  } else {
    target=(total/samples)+200;
    text("Current:"+rvalue2, 10, 30);
    text("        "+rvalue, 10, 60);
    text("Threshold:"+target, 10, 100);
          status='2';
    if (rvalue >= target) {
      wave.setAmplitude( 1 );
      fill(255,0,0);
      textFont(theFont,70);
      text("TARGET",10,150);
    }
    else {
      wave.setAmplitude( 0 );
    }
      
  }
  outData.print(rawData.replace("v","")+","+rvalue2+","+frameRate+","+status+","+target+"\n");
}
