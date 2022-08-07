package satellite;


public class Satellite {
    // Storing attributes as fields
    private String name;
    private int height;
    private double position;
    private double velocity;
    
    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double position, double velocity) {
        this.name = name;
        this.height = height;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * Getter for name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return this.position;
    }

    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return Math.toRadians(this.position);
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return this.velocity;
    }

    /**
     * Returns the angular velocity (radians per second) of the satellite
     */
    public double getAngularVelocity() {
        return (this.velocity / (this.height * 1000));
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for height
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Setter for velocity
     * @param velocity
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return (this.velocity / time);
    }

    public static void main(String[] args) {
        Satellite sat_a = new Satellite("Satellite A", 10000, 122, 55);

        Satellite sat_b = new Satellite("Satellite B", 5438, 0, 234000);

        Satellite sat_c = new Satellite("Satellite C", 9029, 284, 0);

        System.out.println("I am " + sat_a.getName() + " at position " + sat_a.getPositionDegrees() + " degrees, " 
                        + sat_a.getHeight() + " km above the centre of the earth and moving at a velocity of " 
                        + sat_a.getLinearVelocity() + " metres per second");
        sat_a.setHeight(9999);
        sat_b.setPosition(45);
        sat_c.setVelocity(36.5);
        
        System.out.println(sat_a.getPositionRadians());
        System.out.println(sat_b.getAngularVelocity());
        System.out.println(sat_c.getLinearVelocity() * 120);
    }
}