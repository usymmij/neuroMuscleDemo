/* 
  * 2                       - data for shift registers
  * 3                       - clock for shift registers
  * 4                       - latch for shift registers
  * 
  * 5                       - button? The code by Backyard brains claims there is a 
  *                           here but i can't find it
  * 
  * 7, 8, 9, 10, 11, 12     - extra DIO pins
  * D13                     - pin connected to colour LEDS in series
  * 
  * A0, A1, A2, A3, A4, A5  - EMG inputs
  * 
  * By usymmij June 2019
*/

#include <Servo.h>

//int AllPorts[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};//uncomment for debugging
#define ColouredElectrodeLedsPort 13 //the single port for all the colored leds
//under the electrode ports

#define SR_DATA 2
#define SR_CLOCK 3
#define SR_LATCH 4

int loopCount = 0;

int ExtraDigitalPorts[] = {7, 8, 9, 10, 11, 12}; //extra ports sticking out on the end

bool ServoConnected = false;
Servo servo;
int ServoPort;

int ElectrodePorts[] = {0, 1, 2, 3, 4, 5}; //electrode wires
int EMGData[6];

// [EMG][LED(green x2 - yellow x2 - red x2)]
int LEDData[6][6];

String SerialRead;
byte inByte;

int EMGLEDMIN = 0;
int EMGLEDMAX = 800;

void setup()
{
  //begin comms
  Serial.begin(9600);
  //light up the colored LEDS
  pinMode(ColouredElectrodeLedsPort, OUTPUT);
  digitalWrite(ColouredElectrodeLedsPort, HIGH);
  //pinmode for shift registers
  pinMode(SR_CLOCK, OUTPUT);
  pinMode(SR_LATCH, OUTPUT);
  pinMode(SR_DATA, OUTPUT);

  digitalWrite(SR_LATCH,LOW);
  digitalWrite(SR_CLOCK, LOW);
  digitalWrite(SR_DATA, LOW);
}

void read()
{

  for (int i = 0; i < 6; i++)
  {
    EMGData[i] = readAnalog(i);
  }
}

void output()
{

  for (int i = 0; i < 6; i++)
  {
    Serial.print(F("EMG"));
    Serial.print(i);
    Serial.print(EMGData[i]);
  }
  //Serial.println();
}

void input()
{

  if (Serial.available())
  {
    inByte = Serial.read();
    /**
     * byte codes:
     * 0 - 9: set minimum
     * 10 - 19: set maximum
     * 100 - 155: add servo
     *        second digit points to which EMG port it will represent
     *        third digit is the port the servo is connected to(0 = 7, 1 = 8... )
     * 199: remove servo
     **/
    if(inByte >= 0 && inByte < 10) {
      EMGLEDMIN = map(inByte, 0, 9, 0, 300);
    } else if (inByte >= 10 && inByte < 20) {
      EMGLEDMAX = map(inByte, 10, 19, 500, 1000);
    } else if (inByte >= 100 && inByte < 156)
    {
      int digTwo = inByte / 10;
      ServoConnected = true;
      servo.attach(inByte % digTwo);
      ServoPort = ExtraDigitalPorts[digTwo - 10];
    }
    else if (inByte == 199)
    {
      ServoConnected = false;
    }
  }
}

void DIOPartA()
{
  digitalWrite(SR_LATCH, LOW); // turn off latch
  for (int i = 0; i < 4; i++)
  {
    // 6 * 6 LEDs, but there are 5 8-bit registers so 40 bits of space and 4 bit to clear
    digitalWrite(SR_DATA, LOW);
    digitalWrite(SR_CLOCK, HIGH);
    digitalWrite(SR_CLOCK, LOW);
  }
  DIOWriteLED(0);
}

void DIOWriteLED(int i)
{

  for (int j = 0; j < 6; j++)
  {
    if (LEDData[5 - i][5 - j] == 1)
    {
      digitalWrite(SR_DATA, HIGH);
    }
    else
    {
      digitalWrite(SR_DATA, LOW);
    }
    digitalWrite(SR_CLOCK, HIGH);
    digitalWrite(SR_CLOCK, LOW);
  }
}

void DIOPartG()
{

  digitalWrite(SR_LATCH, HIGH); // start the rising edge to output to LEDs
  digitalWrite(SR_LATCH, LOW);
  if (ServoConnected)
  {
    servo.write(map(EMGData[ServoPort], 0, 1024, 105, 190));
  }
}

void loop()
{
  //reads and ouputs each cycle, but DIO and input is split up

  /*
  DIOParts:
  A:load shift registers part 1 (status leds(off), LED set 1)
  B:load shift registers part 2 (2)
  C:3
  D:4
  E:5
  F:6
  G:open shift register latch and Servo output
  */
  switch (loopCount)
  {
  case 0:
    loopCount++;

    read();
    output();
    ledStatusCheck();
    DIOPartA();

    break;

  case 1:       
    DIOWriteLED(1); 
                    

    loopCount++;
    break;

  case 2:
    read();         
    output();       
    DIOWriteLED(2); 
    input();        
                  
    loopCount++;
    break;

  case 3:     
    DIOWriteLED(3); 
                    

    loopCount++;
    break;

  case 4:
    read();         
    output();       
    DIOWriteLED(4); 
    loopCount++;
    break;

  case 5:       
    DIOWriteLED(5); 
    loopCount++;
    break;

  case 6:  
    DIOPartG(); 

    loopCount = 0;
    break;
  }
}

int readAnalog(int port)
{
  if (port == 0)
  {
    return analogRead(A0);
  }
  else if (port == 1)
  {
    return analogRead(A1);
  }
  else if (port == 2)
  {
    return analogRead(A2);
  }
  else if (port == 3)
  {
    return analogRead(A3);
  }
  else if (port == 4)
  {
    return analogRead(A4);
  }
  else if (port == 5)
  {
    return analogRead(A5);
  }
}

void ledStatusCheck()
{

  for (int i = 0; i < 6; i++) {
    // 1024 / 8 = 128
    LEDData[i][5] = constrain(EMGData[i], EMGLEDMIN, EMGLEDMAX);
    //0 - 6 where it is equal to the number of LEDS on
    LEDData[i][5] = map(LEDData[i][5], EMGLEDMIN, EMGLEDMAX, 0, 6);
    for (int j = 0; j < 6; j++)
    {
      if (j < LEDData[i][5] - 1)
      {
        LEDData[i][j] = 1;
      }
      else
      {
        LEDData[i][j] = 0;
      }
    }
  }
}
