#include<mpi.h>
#include<stdio.h>
#include<stdlib.h>

int main(int argc, char *argv[])
{
	double startTime = 0, EndTime = 0;
	int pid, nproc;
	MPI_Status status;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &nproc);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	startTime = MPI_Wtime();

	FILE *matrixA = NULL;
	FILE *matrixB = NULL;
	FILE *matrixC = NULL;

	int rowA, columnA, allDataA, rowB , columnB, allDataB ;
	int checkRemainA, checkRemainB;
	//int *arrMatrixA = NULL, *arrMatrixB = NULL, *arrMatrixALocal = NULL, *arrMatrixBLocal = NULL, *arrMatrixC = NULL,*arrMatrixCLocal = NULL;
	double *arrMatrixA = NULL, *arrMatrixB = NULL, *arrMatrixALocal = NULL, *arrMatrixBLocal = NULL, *arrMatrixC = NULL, *arrMatrixCLocal = NULL;
	///scatterVDeclareVar
	int *scountA, *displsA, *scountB, *displsB;
	///rearrange matrixB
	int ib = 0,countColumnReaded = 0,tmpIB = 0;
	///Set ScatterV parameter
	scountA = (int*)calloc(nproc, sizeof(int));
	displsA = (int*)calloc(nproc, sizeof(int));
	scountB = (int*)calloc(nproc, sizeof(int));
	displsB = (int*)calloc(nproc, sizeof(int));

	///initialize nessesary variable
	if (pid == 0)
	{
		/*char *fileA = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Smallest\\A_smallTest.txt",
			*fileB = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Smallest\\B_smallTest.txt";*/

		//char *fileA = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Small\\matrix1.txt",
		//	*fileB = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Small\\matrix2.txt",
		//	*fileC = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Small\\out.txt";

		char *fileA = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Medium\\matrix1.txt",
			*fileB = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Medium\\matrix2.txt",
			*fileC = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\MatrixSet\\MatrixSet\\Medium\\out.txt";


		matrixA = fopen(fileA, "r");
		matrixB = fopen(fileB, "r");
		matrixC = fopen(fileC, "wt");

		fscanf(matrixA, "%d %d", &rowA, &columnA);
		fscanf(matrixB, "%d %d", &rowB, &columnB);

		MPI_Bcast(&rowA, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&rowB, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&columnA, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&columnB, 1, MPI_INT, 0, MPI_COMM_WORLD);

		allDataA = rowA*columnA;
		allDataB = rowB*columnB;

		if (columnA != rowB)
		{
			printf("Error can't operate");
		}
		else
		{
			///start scan data
			//arrMatrixA = (int*)calloc(allDataA, sizeof(int));
			//arrMatrixB = (int*)calloc(allDataB, sizeof(int));
			arrMatrixA = (double*)calloc(allDataA, sizeof(double));
			arrMatrixB = (double*)calloc(allDataB, sizeof(double));

			for (int i = 0; i < allDataA; i++)
			{
				if (i % columnA == 0)printf("");
				fscanf(matrixA, "%lf", &arrMatrixA[i]);
				//printf("%.2lf ", arrMatrixA[i]);
			}

			tmpIB = ib;
			while (!feof(matrixB))
			{
				fscanf(matrixB, "%lf", &arrMatrixB[ib]);
				ib += rowB;
				countColumnReaded++;
				if (countColumnReaded == columnB)
				{
					tmpIB++;
					ib = tmpIB;
					countColumnReaded = 0;
				}
			}

			fclose(matrixA);
			fclose(matrixB);
			//arrMatrixC = (int*)calloc(rowA*columnB, sizeof(int));
			arrMatrixC = (double*)calloc(rowA*columnB, sizeof(double));

		}
	}

	if (pid != 0)
	{
		MPI_Bcast(&rowA, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&rowB, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&columnA, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&columnB, 1, MPI_INT, 0, MPI_COMM_WORLD);
		allDataA = rowA*columnA;
		allDataB = rowB*columnB;
	}
	
	checkRemainA = (rowA % nproc)*columnA;
	///set sendCount , Displacement
	for (int i = 0; i < nproc; i++)
	{
		displsA[i] = ((rowA / nproc)*columnA) * i;
		scountA[i] = (rowA / nproc)*columnA;
		scountB[i] = allDataB;
		displsB[i] = 0;
		if (checkRemainA != 0 )
		{
			scountA[nproc-1] += checkRemainA;
		}
	}

	int *gsRecv, *gDispls;
	gsRecv = (int*)calloc(nproc, sizeof(int));
	gDispls = (int*)calloc(nproc, sizeof(int));
	for (int i = 0; i<nproc; i++)
	{
		gsRecv[i] = (scountA[i] / columnA)*columnB;
		gDispls[i] = ((scountA[pid] / columnA)*columnB)*i;
	}
	///set sendCount , Displacement
	//arrMatrixALocal = (int*)calloc(scountA[pid], sizeof(int));
	//arrMatrixBLocal = (int*)calloc(scountB[pid], sizeof(int));
	arrMatrixALocal = (double*)calloc(scountA[pid], sizeof(double));
	arrMatrixBLocal = (double*)calloc(scountB[pid], sizeof(double));
	MPI_Scatterv(arrMatrixA, scountA, displsA, MPI_DOUBLE, arrMatrixALocal, scountA[pid], MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Scatterv(arrMatrixB, scountB, displsB, MPI_DOUBLE, arrMatrixBLocal, allDataB, MPI_DOUBLE, 0, MPI_COMM_WORLD);

	//printf("From pid #%d\n:A = %d B = %d\n==========================================================\n", pid, scountA[pid],allDataB);
	int dataCountinC = 0;
	//printf("From pid #%d:A=%d:B=%d \n==========================================================\n", pid, scountA[pid], scountB[pid]);
	for (int i = 0; i < scountA[pid]; i++)
	{
		//if (i%columnA == 0)printf("\n");
		//printf("%d ", arrMatrixALocal[i]);
	}
	//printf("\n-----------------\n");
	for (int i = 0; i < allDataB; i++)
	{
		//if (i%rowB == 0)printf("\n");
		//printf("%d ", arrMatrixBLocal[i]);
	}
	//printf("\n==========================================================\n");
	dataCountinC = (allDataB / columnA)*(scountA[pid] / columnA);
	arrMatrixCLocal = (double*)calloc(dataCountinC, sizeof(double));
	//printf("------>%d", (scountA[pid] / columnA));
	for (int i = 0; i < dataCountinC; i++)
	{
		//arrMatrixCLocal[i] = i;
	}
	//
	int tempOutput = 0;
	int offsetB = 0,offsetA = 0,writetoarr = 0;
	for (int h = 0; h < (scountA[pid] / columnA); h++)
	{
		//printf("\n");
		for (int i = 0; i < columnB; i++)
		{
			//printf(" ");

			for (int k = 0; k < columnA; k++)
			{
				tempOutput += (arrMatrixALocal[k + offsetA] * arrMatrixBLocal[k + offsetB]);
			}
			arrMatrixCLocal[writetoarr] = tempOutput;
			writetoarr++;
			//printf("%d ", tempOutput);
			tempOutput = 0;
			offsetB += columnA;
		}
		offsetB = 0;
		offsetA += columnA;
	}

	MPI_Gatherv(arrMatrixCLocal, dataCountinC, MPI_DOUBLE, arrMatrixC, gsRecv, gDispls, MPI_DOUBLE, 0, MPI_COMM_WORLD);
	for (int i = 0; i < dataCountinC; i++)
	{
		//if (i % 15 == 0)printf("\n");
		//printf("%d ", arrMatrixCLocal[i]);
	}
	//printf("\n\n");
	if (pid == 0)
	{
		fprintf(matrixC, "%d %d", rowA, columnB);
		for (int i = 0; i < rowA*columnB; i++)
		{
			if (((i % columnB) == 0))
			{
				
				fprintf(matrixC,"\n");
			}
			fprintf(matrixC, "%.2lf ", arrMatrixC[i]);
		}
		fclose(matrixC);
	EndTime = MPI_Wtime();
	printf("Timings : %lf Sec", EndTime - startTime);
	}
	MPI_Finalize();
	return 0;
}