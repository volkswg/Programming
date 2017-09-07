	 	.include  "m8def.inc"


		ldi R16,0xff
		out DDRD,R16
	
		ldi R18,0x01
left:	out PORTD,R18
		rcall delay	
		rol R18
		brcs right
		rjmp left
right:	out PORTD,R18
		rcall delay
		ror R18
		brcs left
		rjmp right

delay : LDI R16,0xFF                      
again : nop	
	   	nop  
	   	nop  
	   	rcall delay2
	   	rcall delay2
	   	dec R16                 
	   	brne again              
	   	ret

delay2: LDI R17,0xFF               
again2: nop   
		dec R17   
		brne again2      
		ret                 
