#include<stdio.h>
#include<stdlib.h>
#include<mpi.h>


int main(int argc, char *argv[])
{
	///MPIInit=============================
	int pid, nproc;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &nproc);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	///====================================
	int row, column, numAllData;
	int mywork, remainWork;
	int i,k;
	int *materialPoint = NULL;
	int *materialTemp = NULL;
	int newHeatSum;
	int *scount, *displac;
	int *gRecv, *gDisplac;
	int *localMatPoint;
	int *localTemp;
	int *matpointCalculated = NULL;

	///test
	int timeTransfer;

	if (pid == 0)
	{
		timeTransfer = 1;
		FILE *heatFile;
		char *fileHeatSmall = "C:\\Users\\DELL\\Desktop\\KMUTT\\CPE374 - Paralell Computing\\Homework\\Homework 3 heat\\heat_small.txt";
		heatFile = fopen(fileHeatSmall, "r");
		///read row and collumn
		fscanf(heatFile, "%d %d", &row, &column);
		///necessary(?)
		numAllData = row*column;

		///declare heap array
		materialPoint = (int*)calloc(numAllData, sizeof(int));
		materialTemp = (int*)calloc(numAllData, sizeof(int));
		matpointCalculated = (int*)calloc(numAllData, sizeof(int));

		///read data in file (and transpose)
		for (i = 0; i < numAllData; i++)
		{
			fscanf(heatFile, "%d", &materialPoint[i]);
			materialTemp[i] = materialPoint[i];
		}
		fclose(heatFile);
	}
	MPI_Bcast(&column, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&row, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&timeTransfer, 1, MPI_INT, 0, MPI_COMM_WORLD);
	//printf("==========hello from pid %d==========================\n", pid);
	scount = (int*)calloc(nproc, sizeof(int));
	displac = (int*)calloc(nproc, sizeof(int));
	gRecv = (int*)calloc(nproc, sizeof(int));
	gDisplac = (int*)calloc(nproc, sizeof(int));

	remainWork = (row - 2) % nproc;
	for (i = 0; i < nproc; i++)
	{
		mywork = (row - 2) / nproc;
		scount[i] = mywork;
		if (i < remainWork)
		{
			if (i%nproc == i)
			{
				scount[i]++;
			}
		}
		scount[i] = (scount[i] + 2)*column;
		if (i == 0)
		{
			displac[i] = 0;
		}
		else
		{
			displac[i] = ((scount[i - 1]) + displac[i - 1]) - (column * 2);
		}
		if (i >= row - 2)
		{
			scount[i] = 0;
			displac[i] = 0;
		}
	}
	for (i = 0; i < nproc; i++)
	{
		gRecv[i] = scount[i] - (column * 2);

		if (i == 0)
		{
			gDisplac[i] = column;
		}
		else
		{
			gDisplac[i] = gRecv[i - 1] + gDisplac[i - 1];
		}
		if (i >= row - 2)
		{
			gRecv[i] = 0;
			gDisplac[i] = 0;
		}
	}
	localMatPoint = (int*)calloc(scount[pid], sizeof(int));
	localTemp = (int*)calloc(scount[pid], sizeof(int));
	MPI_Scatterv(materialPoint, scount, displac, MPI_INT, localMatPoint, scount[pid], MPI_INT, 0, MPI_COMM_WORLD);
	for (i = 0; i < scount[pid]; i++)
	{
		localTemp[i] = localMatPoint[i];
	}
	for (k = 0; k < timeTransfer; k++)
	{
		for (i = column; i < scount[pid] - column; i++)
		{
			if (localMatPoint[i] != 255)
			{
				newHeatSum = localTemp[i - (column + 1)] + localTemp[i - (column)] + localTemp[i - (column - 1)] + localTemp[i - 1]
					+ localTemp[i] + localTemp[i + 1] + localTemp[i + (column + 1)] + localTemp[i + (column)] + localTemp[i + (column - 1)];
				newHeatSum /= 9;
				localMatPoint[i] = newHeatSum;
			}
		}
		for (i = 0; i < scount[pid]; i++)
		{
			localTemp[i] = localMatPoint[i];
		}
		///send Border=================================================
		if (pid != nproc - 1)
		{
			MPI_Send(&localMatPoint[scount[pid] - (2 * column)], column, MPI_INT, pid + 1, pid, MPI_COMM_WORLD);
		}
		if (pid != 0)
		{
			//MPI_Recv(&localTemp[0], column, MPI_INT, pid - 1, pid - 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			//MPI_Send(&localMatPoint[scount[pid] - (2 * column)], column, MPI_INT, pid - 1, pid, MPI_COMM_WORLD);
			MPI_Sendrecv(&localMatPoint[scount[pid] - (2 * column)], column, MPI_INT, pid - 1, pid, &localTemp[0], column, MPI_INT, pid - 1, pid - 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}
		if (pid != nproc - 1)
		{
			MPI_Recv(&localTemp[scount[pid] - column], column, MPI_INT, pid + 1, pid + 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}
		///send Border=================================================
	}
	MPI_Gatherv(&localMatPoint[column], gRecv[pid], MPI_INT, materialPoint, gRecv, gDisplac, MPI_INT, 0, MPI_COMM_WORLD);
	if (pid == 0)
	{
		for (i = 0; i < numAllData; i++)
		{
			if ((i) % column == 0)
				printf("\n");
			printf("%3d|", materialPoint[i]);
		}
	}

	free(materialTemp);
	free(materialPoint);
	MPI_Finalize();
	return 0;
}
