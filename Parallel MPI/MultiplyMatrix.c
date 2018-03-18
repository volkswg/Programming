#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
	
	int pid, nproc;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &nproc);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	double startTime = 0;
	double EndTime = 0;
	startTime = MPI_Wtime();
	///File pointer Declare
	FILE *matrixFileA = NULL;
	FILE *matrixFileB = NULL;
	FILE *matrixFileC = NULL;
	
	///matrix size var
	int rowA, columnA, rowB, columnB;

	///array of data
	double *arrMatrixA = NULL;
	double *arrMatrixB = NULL;
	double *arrMatrixC = NULL;
	///array of data in each process
	double *arrMatrixALocal = NULL;
	double *arrMatrixCLocal = NULL;
	int dataNumA, dataNumB;
	
	///string contain the directory of file 
	char *fileA = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\Medium\\matrix1.txt";
	char *fileB = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\Medium\\matrix2.txt";
	char *fileC = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\Medium\\out2.txt";

	///General Propose var
	int h, i, k;
	int countColumn;
	int tmpIB;
	int *scountA = NULL;
	int *displsA = NULL;
	int checkRemainRowA;
	int rowReceive;
	int *gsRecv = NULL;
	int *gDispls = NULL;
	int numDataInC;

	double tempOutput;
	int offsetB;
	int offsetA;
	int writetoarr;

	int dataNumC;
	///Set ScatterV parameter
	scountA = (int*)calloc(nproc, sizeof(int));
	displsA = (int*)calloc(nproc, sizeof(int));
	///variable for gatherv
	gsRecv = (int*)calloc(nproc, sizeof(int));
	gDispls = (int*)calloc(nproc, sizeof(int));
	///Master read File
	if (pid == 0)
	{
		//matrixFileA = fopen(argv[1], "r");
		//matrixFileB = fopen(argv[2], "r");
		//matrixFileC = fopen(argv[3], "w");
		matrixFileA = fopen(fileA, "r");
		matrixFileB = fopen(fileB, "r");
		matrixFileC = fopen(fileC, "w");
		fscanf(matrixFileA, "%d %d", &rowA, &columnA);
		fscanf(matrixFileB, "%d %d", &rowB, &columnB);
	}
	MPI_Bcast(&rowA, 1, MPI_INT, 0, MPI_COMM_WORLD);
	//MPI_Bcast(&rowB, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&columnA, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&columnB, 1, MPI_INT, 0, MPI_COMM_WORLD);

	dataNumA = rowA * columnA;
	dataNumB = columnA * columnB;
	arrMatrixA = (double*)calloc(dataNumA, sizeof(double));
	arrMatrixB = (double*)calloc(dataNumB, sizeof(double));
	if (pid == 0) {
		///Read data From file matrix A
		for (i = 0; i < dataNumA; i++)
		{
			///pirnt Check matrix A
			fscanf(matrixFileA, "%lf", &arrMatrixA[i]);
			//if (i % columnA == 0)printf("\n");
			//printf("%.2lf ", arrMatrixA[i]);
		}
		fclose(matrixFileA);

		//printf("\n");
		///Read data From file matrix B
		i = 0;
		tmpIB = i;
		countColumn = 0;
		for (k = 0; k < dataNumB; k++)
		{
			fscanf(matrixFileB, "%lf", &arrMatrixB[i]);
			//printf("%.2lf", arrMatrixB[i]);
			i = i + columnA;
			countColumn++;
			if (countColumn == columnB)
			{
				tmpIB++;
				i = tmpIB;
				countColumn = 0;
			}
		}
		arrMatrixC = (double*)calloc(rowA*columnB, sizeof(double));
		fclose(matrixFileB);
	}
	///End Master Read File==================================================

	dataNumA = rowA*columnA;
	dataNumB = columnA*columnB;
	//arrMatrixB = (double*)calloc(dataNumB, sizeof(double));

	///Set Scatter Value
	checkRemainRowA = rowA % nproc;
	//printf("remain %d\n", checkRemainRowA);
	///all Row Recieve has Set====================
	///Set number of element will Send
	for (i = 0; i < nproc; i++)
	{
		rowReceive = rowA / nproc;
		scountA[i] = rowReceive;
		if (i < checkRemainRowA)
		{
			if (i%nproc == i)
			{
				scountA[i]++;
			}
		}
		scountA[i] = scountA[i] * columnA;

		if (i != 0) {
			displsA[i] = scountA[i - 1] + displsA[i - 1];
		}
		else {
			displsA[i] = 0;
		}
		//printf("from  Proc #%d  displs %d \t recieve %d\n", pid, displsA[i], scountA[i]);
	}

	for (i = 0; i<nproc; i++)
	{
		gsRecv[i] = (scountA[i] / columnA)*columnB;
		//gDispls[i] = ((scountA[pid] / columnA)*columnB)*i;
		if (i != 0) {
			gDispls[i] =  gsRecv[i - 1] + gDispls[i - 1];
		}
		else {
			gDispls[i] = 0;
		}
		//printf("from PID#%d [%d]gRecv : %d %d\n", pid,i, gDispls[i],gsRecv[i]);
	}

	///reassign dataNumB
	dataNumB = columnA * columnB;
	///calculate element of array C
	numDataInC = (dataNumB / columnA)*(scountA[pid] / columnA);
	//printf("CountData = %d\n", numDataInC);
	///Ready to scatterV
	arrMatrixALocal = (double*)calloc(scountA[pid], sizeof(double));
	arrMatrixCLocal = (double*)calloc(numDataInC, sizeof(double));

	MPI_Scatterv(arrMatrixA, scountA, displsA, MPI_DOUBLE, arrMatrixALocal, scountA[pid], MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Bcast(&arrMatrixB[0], dataNumB, MPI_DOUBLE, 0, MPI_COMM_WORLD);


	//printf("From pid #%d:\nA = %d\n==========================================================", pid, scountA[pid]);
	/*for (i = 0; i < scountA[pid]; i++)
	{
	if (i%columnA == 0)printf("\n");
	printf("%.2lf ", arrMatrixALocal[i]);
	}
	printf("\n-----------------\n");*/
	/*for (i = 0; i < dataNumB; i++)
	{
	if (i% rowB == 0)printf("\n");
	printf("%.2lf ", arrMatrixB[i]);
	}
	printf("\n==========================================================\n");*/
	tempOutput = 0;
	offsetB = 0;
	offsetA = 0;
	writetoarr = 0;
	for (h = 0; h < (scountA[pid] / columnA); h++)
	{
		//printf("\n");
		for (i = 0; i < columnB; i++)
		{
			//printf(" ");

			for (k = 0; k < columnA; k++)
			{
				tempOutput = tempOutput + (arrMatrixALocal[k + offsetA] * arrMatrixB[k + offsetB]);
			}
			arrMatrixCLocal[writetoarr] = tempOutput;
			//printf("%.2lf ", arrMatrixCLocal[writetoarr]);
			writetoarr++;
			tempOutput = 0;
			offsetB = offsetB + columnA;
		}
		offsetB = 0;
		offsetA = offsetA + columnA;
	}
	MPI_Gatherv(arrMatrixCLocal, numDataInC, MPI_DOUBLE, arrMatrixC, gsRecv, gDispls, MPI_DOUBLE, 0, MPI_COMM_WORLD);
	dataNumC = rowA * columnB;
	if (pid == 0)
	{
		fprintf(matrixFileC, "%d %d\n", rowA, columnB);
		for (i = 0; i < dataNumC; i++)
		{

			fprintf(matrixFileC, "%.2f", arrMatrixC[i]);
			if ((i + 1) % (columnB) == 0)
			{
				fprintf(matrixFileC, "\n");
			}
			else {
				fprintf(matrixFileC, " ");
			}
		}
		fclose(matrixFileC);
	}
		EndTime = MPI_Wtime();
		printf("proc %d Timings : %lf Sec",pid,EndTime - startTime);
	MPI_Finalize();
	return 0;
}