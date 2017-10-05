
.data
	inWord: 	.space 255
	inNeedle: 	.space 255
	askWord:  	.asciiz "Enter your string : "
	askNeedle: 	.asciiz "Enter your pattern : "
	pWord:  	.asciiz "Your word : "
	pNeedle: 	.asciiz "Your pattern : "
	enterPrint: .asciiz "\n"
	strMatchprt: .asciiz " Times(s) Match"
	shiftVal: 	.word 256
	lenNeedle:	.word 1
	lenWord:	.word 1
	
.text

main:
	#input word
	la 		$a0,askWord	#Load address of [askWord] 
	li 		$v0,4		#print string
	syscall
	li 		$v0,8		#Function read string
	la 		$a0,inWord	#load address of [inWord]
	li 		$a1,255 	#set array of char
	move	$s0,$a0		#save address [inWord] to $s0
	syscall
	
	#input needle
	la 		$a0,askNeedle	#Load address of [askNeedle] 
	li 		$v0,4		#function print string
	syscall
	li 		$v0,8		#Function read string
	la 		$a0,inNeedle#load address of [inNeedle]
	li 		$a1,255 	#set array of char
	move	$s1,$a0		#save address [inNeedle] to $s1
	syscall
	
	# #print input
	# la		$a0,pWord	#load address of [pWord]
	# li		$v0,4		#print string
	# syscall
	

	# #la		$a0,buffer
	# move	$a0,$s0		#load address of [pWord]
	# li 		$v0,4		#print string
	# syscall	
	
	# #acess char in string
	# la $t2, pWord 		#load address of[pWord]
	# lb $a0, 0($t2)		#load byte of [pword] offset 1 pword[0]
	# li $v0, 1			#print char
	# syscall
	
	# #print \n --------------
	# la 		$a0,enterPrint
	# li 		$v0,4
	# syscall
	# #print \n --------------
	
	# la		$a0,pNeedle	#load address of [pNeedle]
	# li		$v0,4		#print string
	# syscall
	# #la		$a0,buffer
	# move	$a0,$s1		#load address of [word]
	# li 		$v0,4		#print string
	# syscall	
	
preprocess: 
	la		$t0,inNeedle
	jal		strlen		#call string lenght function
	sw		$v1,lenNeedle	#save strLen in lenNeedle
	li		$t9,256 	#$9 is i (255 is for all ascii)
	la 		$t0 , shiftVal	#load address of shiftVal[0]
loopSetAll:
	
	sw		$v1,($t0)			#shiftVal[i] = strlen(needle)
	addi	$t0,$t0,4			#address next arr
	addi	$t9,$t9,-1			#i--
	
	beq 	$t9,$zero,setNeedle	#if $t9 == 0 --> setNeedle
	j		loopSetAll
setNeedle:	
	addi 	$t9,$zero,0 		#$t9 = 0;i = 0
	la 		$t0 , inNeedle 		#&inNeedle
	addi	$t8,$v1,-1			#$t8 = strlen(needle)-1
	
loopsetSllNeedle:
	la		$t2 , shiftVal			#load address of shiftVal[0]
	beq 	$t9,$t8,endllopSllval	#check $t9 == $t8
	lb		$t1,0($t0)				#load asciival
	
	sll 	$t3,$t1,2				#$t3 = asciival *4
	add		$t5,$t3,$t2 			#$t5 address of shiftVal[inWord[i]]
	
	sub		$t4,$v1,$t9				
	addi	$t4,$t4,-1			#$t4 = strLen(needle)-i-1
	
	sw		$t4,($t5)			#shiftVal[i] = $s4
	
	addi	$t0,$t0,1			#move to next char
	addi	$t9,$t9,1			#i++
	
	j		loopsetSllNeedle
endllopSllval:
	j		searchFunc

strlen:
	li      $v1, 0                  #$v1;lenght = 0
strlenwhile:  	
	lb      $t1, 0($t0)             #$t1 = inNeedle[i];i=0
	addi 	$t1,$t1,-10				# calculate for \n (asciiz = 10)
	ble     $t1,$zero, endwh        # if char == NULL --> endwh
    addi    $v1, $v1, 1             # lenght++
    addi   	$t0, $t0, 1             # i++
    j       strlenwhile				#loop
endwh:
    jr      $ra 					#return to $ra

searchFunc:
	la		$t0,inWord
	jal		strlen		#call string lenght function
	sw		$v1,lenWord	#save strLen in lenWord
	
	la		$t0,lenWord
	lw		$s7,($t0)		#s7 = lenWord
	la		$t1,lenNeedle
	lw		$s6,($t1)		#$s6 = lenNeedle
	addi 	$t8,$zero,0		#$t8 = skipword 
	addi 	$v1,$zero,0 	#$v1 = matchfound 
whileSearch1:
	sub		$t3,$s7,$t8		#$t3 = strlen(word) - skipWord
	addi	$t9,$s6,-1		#$t9 = i = strlen(needle)-1
	blt		$t3,$s6,endwhileS1
	
whileSearch2:
	la		$t0,inWord	
	add  	$t7,$t8,$t9		#$t7 = skipWord+i
	la		$t4,inNeedle	
	
	
	add		$t0,$t0,$t7		#$t0 &inWord[skipWord + i]
	add 	$t4,$t9,$t4		#$t4 &inNeedle[i]
	
	lb		$t1,0($t0)		#$t1 = inWord[skipWord + i]
	lb		$t2,0($t4)		#$t2 = inNeedle[i]
	
	# #check inWord[skipWord + i] == inNeedle[i]===========
	# move	$a0,$t1
	# li		$v0,11
	# syscall
	
	# move	$a0,$t2
	# li		$v0,11
	# syscall
	# #print \n --------------
	# la 		$a0,enterPrint
	# li 		$v0,4
	# syscall
	# #print \n --------------
	# #=====================================================
	
	bne		$t1,$t2,endwhileS2	#inWord[skipWord + i] != inNeedle(i) -> endwhileS2

	bne 	$t9,$zero,minusI	#if	$t9 != 0 -> minusI ; $t9 is i
	addi	$v1,$v1,1			#else $v1++ ; matchtime++
	
minusI:
	addi	$t9,$t9,-1				#i--
	blt		$t9,$zero,endwhileS2	#if i < 0 -->endwhileS2
	j		whileSearch2
	
endwhileS2:
	add		$t5,$t8,$s6		#$t5 = skipWord + strLen(needle)
	addi	$t5,$t5,-1		#$t5 = skipWord + strLen(needle)-1

	la		$t0,inWord		#load address inWord[0]
	
	
	add		$t0,$t0,$t5		#$t0 = &word[skipWord+strlen(needle) - 1]
	lb		$s1,0($t0)		#$s1 = word[skipWord+strlen(needle) - 1]
	
	# #show mismatch Character =============
	# move	$a0,$s1
	# li		$v0,11
	# syscall
	# #print \n --------------
	# la 		$a0,enterPrint
	# li 		$v0,4
	# syscall
	# #print \n --------------
	# #=====================================
	
	la		$t0,shiftVal
	sll		$s1,$s1,2	
	
	add		$t0,$s1,$t0		#$t0 = &shiftValue[word[skipWord+strlen(needle) - 1]]
	lw		$t1,0($t0)		#$t1 = shiftValue[word[skipWord+strlen(needle) - 1]]
	add		$t8,$t8,$t1		#skipWord += shiftValue[word[skipWord+strlen(needle) - 1]];
	
	j		whileSearch1
endwhileS1:
	
		#print \n --------------
	la 		$a0,enterPrint
	li 		$v0,4
	syscall
	#print \n --------------
	move	$a0,$v1
	li		$v0,1
	syscall
	
	la		$a0,strMatchprt
	li		$v0,4
	syscall
end:
	li	$v0,10
	syscall 
	
test:
	li		$t9,256
	la 		$t0 , shiftVal
	
	la		$t5,lenNeedle
	lw		$t6,($t5)
	move 	$a0,$t6
	li		$v0,1
	syscall
	
forloopCheck:
	lw		$t1,($t0)
	
	move 	$a0,$t0
	li		$v0,1
	syscall
	
	move 	$a0,$t1
	li		$v0,1
	syscall
	
	#print \n --------------
	la 		$a0,enterPrint
	li 		$v0,4
	syscall
	#print \n --------------
	
	
	addi 	$t0,$t0,4
	addi	$t9,$t9,-1
	beq 	$t9,$zero,end
	
	j 	forloopCheck