package unsw.enrolment;

import java.util.List;
import java.util.*;
import java.util.Comparator;

public class Anonymous {
    List<Student> students;

    public Anonymous(List<Student> students) {
        this.students = students;
    }

    public List<Student> sort(List<Student> students) {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                if (s1.getProgram() < s2.getProgram()) {
                    return -1;
                } else if (s1.getProgram() > s2.getProgram()) {
                    return 1;
                }

                if (s1.getStreams().length < s2.getStreams().length) {
                    return -1;
                } else if (s1.getEnrolments().size() > s2.getEnrolments().size()) {
                    return 1;
                }

                if (s1.getName().compareTo(s2.getName()) < 0) {
                    return -1;
                } else if (s1.getName().compareTo(s2.getName()) > 0) {
                    return 1;
                }

                if (Integer.parseInt(s1.getZid()) < Integer.parseInt(s2.getZid())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return students;
    }

    public boolean equals(List<Student> sort, List<Student> answer) {
        for (int i = 0; i < sort.size(); i++) {
            if ((sort.get(i).getName()).compareTo(answer.get(i).getName()) != 0) {
                return false;
            }
        }
        return true;
    }
}
