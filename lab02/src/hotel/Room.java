package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public abstract class Room {
    
    /**
     * Checks if the room is not booked out during the given time.
     * If so, creates a booking for the room at that time.
     * @param arrival
     * @param departure
     * @return The booking object if the booking succeeded, null if failed
     */
    abstract Booking book(LocalDate arrival, LocalDate departure);

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ],
     *  "type": the type of the room (standard, ensuite, penthouse)
     * }
     */
    abstract JSONObject toJSON();

    /**
     * Prints a welcome message to the guest staying in the room.
     */
    abstract void printWelcomeMessage();

}