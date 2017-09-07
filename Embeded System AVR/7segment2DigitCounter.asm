.include  "m8def.inc"
.equ speed = 0x0A


	LDI 	R16,LOW(RAMEND)
	OUT 	SPL,R16
	LDI 	R16,HIGH(RAMEND)
	OUT 	SPH,R16
	

	ldi 	R16,0xff	
	out 	DDRD, R16 ;Set port Out D
	
	ldi 	R16,0xff
	out 	DDRB, R16 ;Set port Out B
	
	ldi 	R16,0x40 ;0
	sts 	$0060,R16
	ldi 	R16,0x79 ;1
	sts 	$0061,R16
	ldi 	R16,0x24 ;2
	sts 	$0062,R16
	ldi 	R16,0x30 ;3
	sts 	$0063,R16
	ldi 	R16,0x19 ;4
	sts 	$0064,R16
	ldi 	R16,0x12 ;5
	sts 	$0065,R16
	ldi 	R16,0x02 ;6
	sts 	$0066,R16
	ldi 	R16,0x78 ;7
	sts 	$0067,R16
	ldi 	R16,0x00 ;8
	sts 	$0068,R16
	ldi 	R16,0x10 ;9
	sts 	$0069,R16

	ldi		R20,speed	;count Speed R26

	ldi		R18,0x60	;LSB tmp
	ldi		R19,0x60	;MSB tmp

start:
	ldi		R17,0x02 ;set bit out

	mov		R26,R18
	ld		R23,X
	
	out		PORTD, R17	;Bit Out Port
	out		PORTB, R23	;Num Out Port
	rcall 	delay

	dec 	R17			;swicth Bit

	mov		R26,R19
	ld		R23,X
	
	out		PORTD, R17	;Bit Out Port
	out		PORTB, R23	;Num Out Port
	rcall 	delay
	dec		R20 		;count Speed dec
	breq	Lplus		;if countSpeed = 0 jump
	rjmp 	start

Lplus:
	inc 	R18
	cpi		R18,0x6A
	breq	Mplus
	rjmp	resetTimeCount
Mplus:
	ldi		R18,0x60
	inc 	R19
	cpi		R19,0x6A
	breq	resetAll
	rjmp	resetTimeCount



resetTimeCount :
	ldi		R20,speed ;plus Speed
	rjmp	start
resetAll:
	ldi		R18,0x60
	ldi		R19,0x60
	rjmp	resetTimeCount
	
delay : LDI R16,0xFF                      
again : nop	
	   	nop  
	   	nop  
	   	rcall delay2
	   	dec R16                 
	   	brne again              
	   	ret

delay2: LDI R21,0x20            
again2: nop   
		dec R21  
		brne again2      
		ret 
