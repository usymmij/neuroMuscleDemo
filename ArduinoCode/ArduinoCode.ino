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
  * Download the desktop client here:
  * https://github.com/usymmij/neuroMuscleDemo
  * usymmij 2019
*/

#include <Servo.h>

//int AllPorts[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};//uncomment for debugging
#define ColouredElectrodeLedsPort 13 //the single port for all the colored leds
//under the electrode ports

#define SR_DATA 2
#define SR_CLOCK 3
#define SR_LATCH 4

#define DATA 4 //B00000100
#define CLOCK 8 //B00001000
#define LATCH 16 //B00010000

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
  PORTB |= B00100000;
  pinMode(12, OUTPUT);
  digitalWrite(12, HIGH);
  //pinmode for shift registers
  pinMode(SR_CLOCK, OUTPUT);
  pinMode(SR_LATCH, OUTPUT);
  pinMode(SR_DATA, OUTPUT);

  PORTD &= ~CLOCK;
  PORTD &= ~LATCH;
  PORTD &= ~DATA;
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
    Serial.print(i);
    int buffer[4];
    for(int j = 0; j < 4; j++) {
      buffer[j] = (EMGData[i] % 10);
      EMGData[i] /= 10;
    }
    for(int j = 0; j < 4; j++) {
      Serial.print(buffer[3-j]);
    }
    Serial.println();
  }
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
  PORTD &= ~LATCH; // turn off latch
  for (int i = 0; i < 4; i++)
  {
    // 6 * 6 LEDs, but there are 5 8-bit registers so 40 bits of space and 4 bit to clear
    PORTD &= ~DATA;
    PORTD |= CLOCK;
    PORTD &= ~CLOCK;
  }
  DIOWriteLED(0);
}

void DIOWriteLED(int i)
{

  for (int j = 0; j < 6; j++)
  {
    if (LEDData[5 - i][5 - j] == 1)
    {
      PORTD |= DATA;
    }
    else
    {
      PORTD &= ~DATA;
    }
    PORTD |= CLOCK;
    PORTD &= ~CLOCK;
  }
}

void DIOPartG()
{

  PORTD |= LATCH; // start the rising edge to output to LEDs
  PORTD &= ~LATCH;
  if (ServoConnected)
  {
    servo.write(map(EMGData[ServoPort], 0, 1024, 105, 190));
  }
}

void loop()
{
  //LED output is split between reads and writes

    read();
    ledStatusCheck();
    output();

    DIOPartA();
    DIOWriteLED(1);
    DIOWriteLED(2);
    DIOWriteLED(3);

    input();
    read();
    output();

    DIOWriteLED(4);
    DIOWriteLED(5);
    DIOPartG();
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
