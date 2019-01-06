import java.util.Comparator;

public class StudentDB {
	// ArrayList<Student> students;
	private Student[] students;
	private int studentCount;

	public static Comparator<Student> studentIDComparator = new Comparator<Student>() {
		public int compare(Student student1, Student student2) {
			String student1Val = student1.getId().toLowerCase();
			String student2Val = student2.getId().toLowerCase();

			return student1Val.compareTo(student2Val);
		}
	};
	public static Comparator<Student> studentNameComparator = new Comparator<Student>() {
		public int compare(Student student1, Student student2) {
			String student1Val = student1.getName().toLowerCase();
			String student2Val = student2.getName().toLowerCase();

			return student1Val.compareTo(student2Val);
		}
	};
	public static Comparator<Student> studentLastNameComparator = new Comparator<Student>() {
		public int compare(Student student1, Student student2) {
			String student1Val = student1.getLast().toLowerCase();
			String student2Val = student2.getLast().toLowerCase();

			return student1Val.compareTo(student2Val);
		}
	};
	public static Comparator<Student> studentGPAComparator = new Comparator<Student>() {
		public int compare(Student student1, Student student2) {
			double student1Val = student1.getGpa();
			double student2Val = student2.getGpa();
			double diff = student1Val - student2Val;
			int retVal = 0;
			if (diff > 0)
				retVal = 1;
			else if (diff < 0)
				retVal = -1;
			else
				retVal = 0;

			return retVal;
		}
	};

	public StudentDB() {
		this(20);
	}

	public StudentDB(int studentNum) {
		this.students = new Student[100];
		this.studentCount = 0;
		this.addStudent("58070501005", "Ad", "Bd", 0.50);
		this.addStudent("58070501001", "Ab", "Bb", 1.60);
		this.addStudent("58070501002", "Ae", "Be", 3.40);
		this.addStudent("58070501001", "Ac", "Bc", 4.50);
		this.addStudent("58070501004", "Ag", "Bg", 4.10);
		this.addStudent("58070501003", "Ah", "Bh", 2.30);
	}

	public void addStudent(String id, String name, String lName, double gpa) {
		this.students[this.studentCount++] = new Student(id, name, lName, gpa);
	}

	public void delStudent() {
		if (this.studentCount > 0) {
			this.studentCount--;
		}

	}

	public int getStudentNum() {
		return this.studentCount;
	}

	public String getAllData() {
		String retVal = "";
		for (int i = 0; i < getStudentNum(); i++) {
			retVal += students[i].getId() + "\t" + students[i].getName() + " " + students[i].getLast() + " \t"
					+ students[i].getGpa() + "\n";
		}
		return retVal;
	}

	public void sortByID(boolean asc) {
		quicksort(this.students, 0, this.studentCount - 1, studentIDComparator, asc);
	}

	public void sortByName(boolean asc) {
		quicksort(this.students, 0, this.studentCount - 1, studentNameComparator, asc);
	}

	public void sortByLName(boolean asc) {
		quicksort(this.students, 0, this.studentCount - 1, studentLastNameComparator, asc);
	}

	public void sortByGPA(boolean asc) {
		quicksort(this.students, 0, this.studentCount - 1, studentGPAComparator, asc);
	}

	private void quicksort(Student[] students, int left, int right, Comparator<Student> sComparator, boolean asc) {
		if (left < right) {
			int p = partition(students, left, right, sComparator, asc);
			quicksort(students, left, p - 1, sComparator, asc);
			quicksort(students, p + 1, right, sComparator, asc);
		}
	}

	private int partition(Student[] students, int left, int right, Comparator<Student> sComparator, boolean asc) {
		int firstPointer = left;
		int lastPointer = right - 1;
		Student pivot = students[right];
		while (true) {
			while ((asc && sComparator.compare(students[firstPointer], pivot) < 0)
					|| (!asc && sComparator.compare(students[firstPointer], pivot) > 0)) {
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

	private void swap(Student[] student, int i, int j) {
		Student studentTmp = student[i];
		student[i] = student[j];
		student[j] = studentTmp;
	}

}
