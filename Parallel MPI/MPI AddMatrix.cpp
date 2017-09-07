#include<mpi.h>
#include<stdio.h>
#include<stdlib.h>

int main(int argc, char *argv[])
{
		int rank, nproc;
		MPI_Status status;
		MPI_Init(&argc, &argv);
		MPI_Comm_size(MPI_COMM_WORLD, &nproc);
		MPI_Comm_rank(MPI_COMM_WORLD, &rank);
		int row, column;
		//declear dynamic array
		int **arrMatrixA;
		int **arrMatrixB;
		int **arrMatrixC;
		double StartTime, EndTime;
		FILE *matrixFileA;
		FILE *matrixFileB;
		FILE *matrixOut;
		char *filenameA = "C:\\Users\\DELL\\Desktop\\Add_Matrix_Dataset\\A_large.txt";
		char *filenameB = "C:\\Users\\DELL\\Desktop\\Add_Matrix_Dataset\\B_large.txt";
		char *filenameOut = "C:\\Users\\DELL\\Desktop\\Add_Matrix_Dataset\\Out_large.txt";

		//First pid = 0
		if (rank == 0)
		{
			matrixFileA = fopen(filenameA, "r");
			matrixFileB = fopen(filenameB, "r");

			fscanf(matrixFileA, "%d %d", &row, &column);
			fclose(matrixFileA);
			fclose(matrixFileB);
		}
		MPI_Bcast(&row, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(&column, 1, MPI_INT, 0, MPI_COMM_WORLD);
		//printf("hello from Proc #%d : %d %d\n", rank,row,column);

		//Dynamic Array
		arrMatrixA = (int**)calloc(row, sizeof(int*));
		arrMatrixB = (int**)calloc(row, sizeof(int*));
		arrMatrixC = (int**)calloc(row, sizeof(int*));
		for (int i = 0; i < row; i++) {
			arrMatrixA[i] = (int*)calloc(column, sizeof(int));
			arrMatrixB[i] = (int*)calloc(column, sizeof(int));
			arrMatrixC[i] = (int*)calloc(column, sizeof(int));
		}
		//seccond pid = 0
		if (rank == 0)
		{
			matrixFileA = fopen(filenameA, "r");
			matrixFileB = fopen(filenameB, "r");
			matrixOut = fopen(filenameOut, "w");

			if (matrixFileA)
				fscanf(matrixFileA, "%d %d", &row, &column);
			if (matrixFileB)
				fscanf(matrixFileB, "%d %d", &row, &column);

			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < column; j++)
				{
					fscanf(matrixFileA, "%d", &arrMatrixA[i][j]);
					fscanf(matrixFileB, "%d", &arrMatrixB[i][j]);
					//printf("%d ", arrMatrixA[i][j]);
				}
				//printf("\n");
			}
			StartTime = MPI_Wtime();
			for (int i = 0; i < row; i++)
			{
					MPI_Bcast(&arrMatrixA[i][0], column, MPI_INT, 0, MPI_COMM_WORLD);
					MPI_Bcast(&arrMatrixB[i][0], column, MPI_INT, 0, MPI_COMM_WORLD);
				//printf("\n");
			}
			fprintf(matrixOut, "%d %d\n", row, column);
			for (int i = rank; i < row; i = i+nproc)
			{
				//printf("from pid #%d, : %d |", rank, i);
				for (int j = 0; j < column; j++)
				{
					arrMatrixC[i][j] = arrMatrixA[i][j] + arrMatrixB[i][j];
				}
				//printf("\n");
			}
			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < column; j++)
				{
					if (i%nproc != 0) {
						MPI_Recv(&arrMatrixC[i][j], 1, MPI_INT, i%nproc, i%nproc, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			
						/*if (j != 0)
							fprintf(matrixOut, " %d", arrMatrixC[i][j]);
						else
						{
							fprintf(matrixOut, "%d", arrMatrixC[i][j]);
						}
					}
					else
					{
						if (j != 0)
						fprintf(matrixOut, " %d", arrMatrixC[i][j]);
						else
						{
							fprintf(matrixOut, "%d", arrMatrixC[i][j]);
						}*/
					}
					//printf("%d ", arrMatrixC[i][j]);
				}
				//printf("\n");
				//fprintf(matrixOut, "\n");
			}

			EndTime = MPI_Wtime();
			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < column; j++)
				{
					if (j != 0)
						fprintf(matrixOut, " %d", arrMatrixC[i][j]);
					else
					{
						fprintf(matrixOut, "%d", arrMatrixC[i][j]);
					}
					//printf("%d ", arrMatrixC[i][j]);
				}
				//printf("\n");
				fprintf(matrixOut, "\n");
			}

			fclose(matrixOut);
			printf("Write Complete");
			printf("Timings : %f Sec", EndTime - StartTime);
		}
		if (rank != 0)
		{
			for (int i = 0; i < row; i++)
			{
				MPI_Bcast(&arrMatrixA[i][0], column, MPI_INT, 0, MPI_COMM_WORLD);
				MPI_Bcast(&arrMatrixB[i][0], column, MPI_INT, 0, MPI_COMM_WORLD);
					//printf("%d ", arrMatrixA[i][j]);
				//printf("\n");
			}
			int buffAns;
			for (int i = rank; i < row; i = i + nproc)
			{
				//printf("from pid #%d, : %d |", rank, i);
				for (int j = 0; j < column; j++)
				{
					buffAns = arrMatrixA[i][j] + arrMatrixB[i][j];
					//printf("%d ", buffAns);
					MPI_Send(&buffAns, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
				}
				//printf("\n");
			}
		}
		MPI_Finalize();
		return 0;
}