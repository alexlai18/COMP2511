package staff;


public class StaffTest {
    public void printStaffDetails(Object staff) {
        System.out.println(staff.toString());
    }

    public static void main(String[] args) {
        StaffMember staff = new StaffMember("Alex Lai", 10000, "2022-06-02", "2022-06-03");
        Lecturer lecturer = new Lecturer("CSE", "A", "Alex Lai", 10000, "2022-06-02", "2022-06-03");
        lecturer.setName("Alexander Lai");

        StaffTest test = new StaffTest();
        test.printStaffDetails(staff);
        test.printStaffDetails(lecturer);

        // Checking if the staff is the lecturer then changing it
        System.out.println(staff.equals(lecturer));
        lecturer.setName("Alex Lai");
        System.out.println(staff.equals(lecturer));
    }
}