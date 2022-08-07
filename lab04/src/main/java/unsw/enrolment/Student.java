package unsw.enrolment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Student {

    private String zid;
    private ArrayList<Enrolment> enrolments = new ArrayList<Enrolment>();
    private String name;
    private int program;
    private String[] streams;

	public Student(String zid, String name, int program, String[] streams) {
        this.zid = zid;
        this.name = name;
        this.program = program;
        this.streams = streams;
    }

    public boolean isEnrolled(CourseOffering offering) {
        return enrolments.stream().anyMatch(enrolment -> enrolment.getOffering().equals(offering));
    }

    public void setGrade(Grade grade) {
        enrolments.stream().forEach(enrolment -> {
            if (enrolment.getOffering().equals(grade.getOffering())) {
                enrolment.setGrade(grade);
            }
        });

    }

    public void addEnrolment(Enrolment enrolment) {
        enrolments.add(enrolment);
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public boolean canEnrol(Course prereq) {
        return enrolments.stream().anyMatch(enrolment -> (enrolment.getCourse().equals(prereq) && enrolment.getGrade() != null && enrolment.hasPassedCourse()));
    }

    public int getProgram() {
        return this.program;
    }

    public String getName() {
        return this.name;
    }

    public String getZid() {
        return this.zid;
    }

    public String[] getStreams() {
        return streams;
    }
}
