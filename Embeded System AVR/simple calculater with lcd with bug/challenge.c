#include <avr/io.h>
#include <util/delay.h>

#define KEY_PRT PORTB
#define KEY_DDR DDRB
#define KEY_PIN PINB
#define LCD_PRT PORTD
#define LCD_DDR DDRD
#define LCD_PIN PIND
#define LCD_RS 2
#define LCD_EN 3

#define CHECK_PRT PORTC

unsigned char keypad[4][4] = {'1','4','7','*',
							'2','5','8','0',
							'3','6','9','#',
							'+','-','=','C'};

							
unsigned int x = 1;
unsigned int y = 1;

unsigned int countForNum = 0;//for insert to firstNum
unsigned int countDigit = 0;
unsigned int firstNum[15]; //for firstNum
unsigned int numF;	//first Number Calculated
unsigned int numS;	//seccond Number Calculated
unsigned int secNumflag = 0;
unsigned int equalFlag = 0;

//test
unsigned int caltedf = 0;
unsigned int calteds = 0;
unsigned int i = 0;


void lcdCommand(unsigned char cmnd)
{
     LCD_PRT = (LCD_PRT & 0x0F) | (cmnd & 0xF0);
     LCD_PRT &= ~(1<<LCD_RS);
     _delay_us(1);
     LCD_PRT |= (1<<LCD_EN);
     _delay_us(1);
     LCD_PRT &= ~ (1<<LCD_EN);

     _delay_us(20);

     LCD_PRT =(LCD_PRT & 0x0F) | (cmnd <<4);
     LCD_PRT |= (1<<LCD_EN);
     _delay_us(1);
     LCD_PRT &=~ (1<<LCD_EN);
}
void lcdData(unsigned char d)
{
     LCD_PRT = (LCD_PRT & 0x0F) | (d&0xF0);
     LCD_PRT |= (1<<LCD_RS);
     _delay_us(1);
     LCD_PRT |= (1<<LCD_EN);
     _delay_us(1);
     LCD_PRT &= ~(1<<LCD_EN);
     
     LCD_PRT= (LCD_PRT & 0x0F) | (d << 4);
     LCD_PRT |= (1<<LCD_EN);
     _delay_us(1);
     LCD_PRT &= ~(1<<LCD_EN);

}
void lcd_init_my()
{
     LCD_DDR = 0xFF;

     LCD_PRT &=~(1<<LCD_EN);
     _delay_us(2000);
     lcdCommand(0x33);
     _delay_us(100);
     lcdCommand(0x32);
     _delay_us(100);
     lcdCommand(0x28);
     _delay_us(100);
     lcdCommand(0x0e);
     _delay_us(100);
     lcdCommand(0x01);
     _delay_us(2000);
     lcdCommand(0x06);
     _delay_us(100);
}

void lcd_gotoxy(unsigned char x,unsigned char y)
{
     unsigned char firstCharAdr[] = {0x80,0xC0,0x94,0xD4};
     lcdCommand(firstCharAdr[y-1]+x-1);
     _delay_us(100);
}
void lcd_print(char *str)
{
     unsigned char i =0;
     while(str[i]!=0)
     {
      lcdData(str[i]);
      i++;
     }
}

int numPow(unsigned int number,unsigned int power)
{
	unsigned int i;
	unsigned int sum = 1;
	for(i = 0;i<power;i++)
	{
		sum *= number;
	}
	return sum;
}
void clearFunc()
{
	x = 1;
	countForNum = 0;
	secNumflag = 0;
	countDigit = 0;
	numF = 0;
	numS = 0;
	caltedf = 0;
	calteds = 0;
	equalFlag = 0;
	lcdCommand(0x01);
	_delay_us(2000);
}

//incleasing position of cursor to next or check for enter next line
void lcd_position()
{
	x++;
	if (x>16)
	{
		x = 1;
		y++	;
		if(y>2)
		{
			y = 1;
			lcdCommand(0x01);
			_delay_us(2000);			
		}	
	}
	lcd_gotoxy(x,y);
	
}	
							
int main(void)
{	
	int sum;
	unsigned char colloc,rowloc;
	char ans[12];
	int unsigned operatorCheck;
	KEY_DDR = 0xF0; //1111 0000
	KEY_PRT = 0xFF;
	DDRC = 0xFF;
	lcd_init_my();
	lcd_gotoxy(1,1);

    while(1)
    {
		//calculate first number==========================================
		if(secNumflag == 1 && caltedf == 0)
		{
			for(i =0;i<countDigit;i++)
			{
				numF += firstNum[i]*numPow(10,(countDigit-i)-1);
			}
			caltedf = 1;
			countForNum = 0;
			countDigit = 0;
		}
		//firstNum =======================================================
		//calculate seccond number==========================================
		if(equalFlag == 1 && calteds == 0)
		{
			for(i =0;i<countDigit;i++)
			{
				numS += firstNum[i]*numPow(10,(countDigit-i)-1);
			}
			calteds = 1;
			switch(operatorCheck){
				case 0:
					sum = numF + numS;
					break;
				case 1:
					sum = numF - numS;
					break;
			}
			clearFunc();
			sprintf(ans, "%d", sum);
			lcd_print(ans);
		}
		//seccondNum =======================================================
		
 		do //loop until release button
		{
			KEY_PRT &= 0x0F;
			colloc = (KEY_PIN & 0x0F);
		} while(colloc != 0x0F); 

		do //loop until click some button
		{
			_delay_ms(20);
			colloc = (KEY_PIN&0x0F);
		} while (colloc == 0x0F);

		while(1)
		{
			//test value for click button in row
			KEY_PRT = 0xEF;// 1110 1111
			colloc = (KEY_PIN & 0x0F);
			if(colloc != 0x0F)
			{
				rowloc =0;
				break;	
			}	
			//test value for click button in row			
			KEY_PRT = 0xDF;// 1101 1111
			colloc = (KEY_PIN & 0x0F);
			if (colloc != 0x0F)
			{
				rowloc = 1;
				break;
			}
			//test value for click button in row
			KEY_PRT = 0xBF;//1011 1111
			colloc = (KEY_PIN & 0x0F);
			if(colloc != 0x0F)
			{
				rowloc =2;
				break;
			}	
			KEY_PRT = 0x7F;// 0111 1111
			colloc = (KEY_PIN & 0x0F);
			if(colloc != 0x0F)
			{
				rowloc =3;
				break;	
			}			
		}	
		
		if(colloc ==0x0E)//check what collum user click 
		{
			if(rowloc == 3 && secNumflag == 0){//check for input one operator [+] 
				lcdData(keypad[rowloc][0]);//select data from array by row user click
				lcd_position();
				secNumflag = 1;
				operatorCheck = 0;
			}
			else if(rowloc != 3)
			{
				countDigit++;
			}
			switch(rowloc){
				case 2 :
					lcdData(keypad[rowloc][0]);// [3]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][0]-'0';
					countForNum++;
					//
					break;
				case 1 :
					lcdData(keypad[rowloc][0]);// [2]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][0]-'0';
					countForNum++;
					//
					break;
				case 0 :
					lcdData(keypad[rowloc][0]);// [1]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][0]-'0';
					countForNum++;
					//
					break;
			}
		}			
		else if(colloc == 0x0D)//check what collum user click 
		{
			if(rowloc == 3 && secNumflag == 0){//check for input one operator------[-]
				lcdData(keypad[rowloc][1]);//select data from array by row user click
				lcd_position();
				secNumflag = 1;
				operatorCheck = 1;
			}
			else if(rowloc != 3)
			{
				countDigit++;
			}
			switch(rowloc){
				case 2 :
					lcdData(keypad[rowloc][1]);// [6]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][1]-'0';
					countForNum++;
					//
					break;
				case 1 :
					lcdData(keypad[rowloc][1]);// [5]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][1]-'0';
					countForNum++;
					//
					break;
				case 0 :
					lcdData(keypad[rowloc][1]);// [4]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][1]-'0';
					countForNum++;
					//
					break;
			}
		}		
		else if(colloc == 0x0B)//check what collum user click 
		{

			if(rowloc == 3){// ---------------------[=]
				//lcdData(keypad[rowloc][2]);//select data from array by row user click
				lcd_position();
				equalFlag = 1;
			}
			else if(rowloc != 3)
			{
				countDigit++;
			}
			switch(rowloc){
				case 2 :
					lcdData(keypad[rowloc][2]);// [9]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][2]-'0';
					countForNum++;
					//
					break;
				case 1 :
					lcdData(keypad[rowloc][2]);// [8]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][2]-'0';
					countForNum++;
					//
					break;
				case 0 :
					lcdData(keypad[rowloc][2]);// [7]
					lcd_position();
					//
					firstNum[countForNum] = keypad[rowloc][2]-'0';
					countForNum++;
					//
					break;
			}
		}			
		else if(colloc == 0x07)//check what collum user click 
		{
			if(rowloc == 3)
			{
				clearFunc();
			}
			else if(rowloc == 1)
			{
				lcdData(keypad[rowloc][3]);//select data from array by row user click
				lcd_position();
				//
				firstNum[countForNum] = keypad[rowloc][3]-'0';
				countForNum++;
				countDigit++;
				//
			}
		}
    }
}
