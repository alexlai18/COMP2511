package staff;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    // Initialising attributes for class StaffMember
    private String name;
    private int salary;
    private String start;
    private String end;

    /** 
     * Constructor for a staff member of subclass lecturer
     * @param name
     * @param salary
     * @param start
     * @param end
    */
    public StaffMember(String name, int salary, String start, String end) {
        this.name = name;
        this.salary = salary;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return name + " with salary " + salary + " worked from " + start + " to " + end;
    }

    public String getName() {
        return this.name;
    }

    public int getSalary() {
        return this.salary;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object staff) {
        StaffMember curr_staff = (StaffMember) staff;
        return curr_staff.name.equals(this.name) && (curr_staff.salary == this.salary) 
                && curr_staff.start.equals(this.start) && curr_staff.end.equals(this.end);
    }
}