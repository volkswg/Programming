import java.util.Comparator;


public class Student {
	private String id;
	private String name;
	private String last;
	private double gpa;

	public Student(String id, String name, String last, double gpa) {
		this.id = id;
		this.name = name;
		this.last = last;
		this.gpa = gpa;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLast() {
		return last;
	}

	public double getGpa() {
		return gpa;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	@Override
	public String toString() {
		String ret = this.id + "\t" + this.name + " " + this.last + " \t" + this.gpa;
		return ret;
	}

	public static void sort(Student[] students, Comparator<Student> sComparator, boolean asc) {
		quicksort(students, 0, students.length - 1, sComparator,asc);
	}

	private static void quicksort(Student[] students, int left, int right, Comparator<Student> sComparator,boolean asc) {
		if (left < right) {
			int p = partition(students, left, right, sComparator,asc);
			quicksort(students, left, p - 1, sComparator,asc);
			quicksort(students, p + 1, right, sComparator,asc);
		}
	}

	private static int partition(Student[] students, int left, int right, Comparator<Student> sComparator,boolean asc) {
		int firstPointer = left;
		int lastPointer = right - 1;
		Student pivot = students[right];
		while (true) {
			while ((asc && sComparator.compare(students[firstPointer], pivot) < 0)
					||(!asc && sComparator.compare(students[firstPointer], pivot) > 0)) {
				firstPointer++;
			}

			while (lastPointer > 0 && ((asc && sComparator.compare(students[lastPointer], pivot) > 0)
					|| (!asc && sComparator.compare(students[lastPointer], pivot) < 0))) {
				lastPointer--;
			}

			if (firstPointer >= lastPointer) {
				break;
			} else {
				swap(students, firstPointer, lastPointer);
			}
		}
		swap(students, firstPointer, right);
		return firstPointer;
	}

	private static void swap(Student[] student, int i, int j) {
		Student studentTmp = student[i];
		student[i] = student[j];
		student[j] = studentTmp;
	}
}
