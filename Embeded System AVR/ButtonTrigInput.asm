		.include  "m8def.inc"
	
		LDI R16,LOW(RAMEND)
		OUT SPL,R16
		LDI R16,HIGH(RAMEND)
		OUT SPH,R16

		ldi R16,0xff
		out DDRD,R16
		ldi R16,0x00
		out DDRD,R16

		LDI R18,0xFF

main: 	IN R17,PINC
		ADD R17,R18
		breq ligth

		ldi R19,0x00
		Out PORTD,R19
		rjmp main


ligth:	ldi R19,0xAA
		Out PORTD,R19
		rjmp main
