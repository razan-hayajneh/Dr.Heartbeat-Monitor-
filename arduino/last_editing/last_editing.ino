#include <SoftwareSerial.h>
#define DEBUG true
#include <LiquidCrystal.h>
#include <stdlib.h>
LiquidCrystal lcd(12,11,6,5,4,3);
char incomingByte;  // incoming data
int  LED = 13;      // LED pin
int BPM_Last=0;
//Variables
float temp;
int hum;
String tempC;
int error;
int pulsePin = 0;                 // Pulse Sensor purple wire connected to analog pin 0
int blinkPin = 13;                // pin to blink led at each beat
int fadePin = 5;
int fadeRate = 0;
// Volatile Variables, used in the interrupt service routine!
volatile int BPM;                   // int that holds raw Analog in 0. updated every 2mS
volatile int Signal;                // holds the incoming raw data
volatile int IBI = 600;             // int that holds the time interval between beats! Must be seeded! 
volatile boolean Pulse = false;     // "True" when heartbeat is detected. "False" when not a "live beat". 
volatile boolean QS = false;        // becomes true when Arduino finds a beat.
// Regards Serial OutPut  -- Set This Up to your needs
static boolean serialVisual = true;   // Set to 'false' by Default.  Re-set to 'true' to see Arduino Serial Monitor ASCII Visual Pulse 
volatile int rate[10];                    // array to hold last ten IBI values
volatile unsigned long sampleCounter = 0;          // used to determine pulse timing
volatile unsigned long lastBeatTime = 0;           // used to find IBI
volatile int P =512;                      // used to find peak in pulse wave, seeded
volatile int T = 512;                     // used to find trough in pulse wave, seeded
volatile int thresh = 525;                // used to find instant moment of heart beat, seeded
volatile int amp = 100;                   // used to hold amplitude of pulse waveform, seeded
volatile boolean firstBeat = true;        // used to seed rate array so we startup with reasonable BPM
volatile boolean secondBeat = false;      // used to seed rate array so we startup with reasonable BPM
void setup()
{
  lcd.begin(16, 2);
  lcd.print("Dr.Heart Monitor");
  delay(100);
  pinMode(LED, OUTPUT);
  lcd.setCursor(0,1);
  lcd.print("Connecting...");
  Serial.begin(9600); //or use default 115200.
  if(Serial.available() > 0){
     lcd.setCursor(0,1);
     lcd.print("Connected    ");
    }
  delay(5000);
  interruptSetup(); 
}

void loop(){  
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Dr.Heart Monitor");
  send_BPM();
  lcd.setCursor(0, 1);
  lcd.print("BPM = ");
  lcd.print(BPM);
  delay(5000); 
}
void send_BPM(){
  if(BPM!=BPM_Last) {
       digitalWrite(LED, HIGH); // if receive, switch LED on
       Serial.print(BPM);     
       BPM_Last=BPM;
    }
   else
    {
       digitalWrite(LED, LOW);  // if not receive, switch LED Off 
    }
  }
void interruptSetup(){
  TCCR2A = 0x02;    
  TCCR2B = 0x06;     
  OCR2A = 0X7C;     
  TIMSK2 = 0x02;     
  sei();                  
} 

ISR(TIMER2_COMPA_vect){                      
  cli();                                      
  Signal = analogRead(pulsePin);              
  sampleCounter += 2;                         
  int N = sampleCounter - lastBeatTime;      
  if(Signal < thresh && N > (IBI/5)*3){     
    if (Signal < T){                        
      T = Signal;                            
    }
  }

  if(Signal > thresh && Signal > P){        
    P = Signal;                             
  }                                        
  if (N > 250){                                  
    if ( (Signal > thresh) && (Pulse == false) && (N > (IBI/5)*3) ){        
      Pulse = true;                              
      digitalWrite(blinkPin,HIGH);                
      IBI = sampleCounter - lastBeatTime;         
      lastBeatTime = sampleCounter;              
      if(secondBeat){                        
        secondBeat = false;                  
        for(int i=0; i<=9; i++){             
          rate[i] = IBI;                      
        }
      }

      if(firstBeat){                         
        firstBeat = false;                  
        secondBeat = true;                  
        sei();                               
        return;                              
      }   
      word runningTotal = 0;                
      for(int i=0; i<=8; i++){               
        rate[i] = rate[i+1];                  
        runningTotal += rate[i];            
      }

      rate[9] = IBI;                         
      runningTotal += rate[9];               
      runningTotal /= 10;                    
      BPM = 60000/runningTotal;              
      QS = true;                              
    }                       
  }

  if (Signal < thresh && Pulse == true){   
    digitalWrite(blinkPin,LOW);            
    Pulse = false;                         
    amp = P - T;                          
    thresh = amp/2 + T;                    
    P = thresh;                           
    T = thresh;
  }

  if (N > 2500){                          
    thresh = 512;                          
    P = 512;                               
    T = 512;                               
    lastBeatTime = sampleCounter;                 
    firstBeat = true;                     
    secondBeat = false;                   
  }
  sei();     
}// end isr
