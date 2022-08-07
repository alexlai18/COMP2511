package staff;

public class Lecturer extends StaffMember {

    private String field;
    private String status;

    /** 
     * Constructor for a staff member of subclass lecturer
     * @param field
     * @param status
    */

    public Lecturer(String field, String status, String name, int salary, String start, String end) {
        super(name, salary, start, end);
        this.field = field;
        this.status = status;
    }

    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String toString() {
        return field + " " + status + " " + super.toString();
    }

    @Override
    public boolean equals(Object staff) {
        Lecturer curr_lec = (Lecturer) staff;
        return super.equals(staff) && curr_lec.status.equals(this.status) && curr_lec.field.equals(this.field);
    }
    
}
