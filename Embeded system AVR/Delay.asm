delay2: LDI R17,0xFF    // Consume  1 clock cycle
again2: nop   			// Consume  1 clock cycle
		dec R17   		// Consume  1 clock cycle
		brne again2     // Consume  1 (not jump) or 2 (jump) clock cycles
		ret   			// Consume  4 clock cycles



Total Clock cycles = 1+[1*(1+1+1)+255*(1+1+2)]+4 = 1028 Clock cycles

 or 1028 * 1/10^(-6) = 1.028 miliseconds @ 1MHz operation
