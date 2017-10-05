#include<stdio.h>
#include<string.h>

void preprocessing(char *needle,int *shiftValue)
{
	int i;
	for(i = 0; i < 256 ;i++)
	{
		shiftValue[i] = strlen(needle);
	}
	for(i = 0;i<strlen(needle)-1;i++)
	{
		shiftValue[needle[i]] = strlen(needle) -i -1;
	}
}

int searchInWord(char *word,char *needle,int *shiftValue)
{
	int i,skipWord = 0;
	int timeMatch = 0;
	while (strlen(word) - skipWord >= strlen(needle))
	{
		i = strlen(needle) - 1;
		while (word[skipWord + i] == needle[i])
		{
			if(i == 0)
			{
				//printf("Found %d\n",skipWord);
				timeMatch++;
			}
			i--;
		}
		skipWord += shiftValue[word[skipWord+strlen(needle) - 1]];
	}
	return timeMatch;
}

int main()
{
	char buffer[255];
	char myString[255];
	char pattern[255];
	int chrShiftValue[256];
	int i;
	int timeMatch;
	printf("Your Word : ");
	fgets (buffer, sizeof(buffer), stdin);
	sscanf(buffer,"%s",myString);
	printf("Needle : ");
	fgets (buffer, sizeof(buffer), stdin);
	sscanf(buffer,"%s",pattern);
	
	preprocessing(pattern,chrShiftValue);
	
	timeMatch = searchInWord(myString,pattern,chrShiftValue);
	
	printf("%d time match\n",timeMatch);
	
	return 0;
}
