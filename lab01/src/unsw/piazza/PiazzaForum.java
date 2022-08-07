package unsw.piazza;

import java.util.ArrayList;
import java.util.List;

/**
 * A Piazza Forum 
 * 
 * @author Your Name
 */
public class PiazzaForum {
    // Storing attributes as fields
    private String className;

    // Creating a total list of threads in this forum
    List<Thread> all_threads = new ArrayList<Thread>();

    /**
     * Initialises the new PiazzaForum with the given group name
     */
    public PiazzaForum(String className) {
        this.className = className;
    }

    /**
     * @return The name of the forum
     */
    public String getName() {
        return this.className;
    }

    /**
     * Sets the name of the group of the Forum
     * @param name
     */
    public void setName(String name) {
        this.className = name;
    }

    /**
     * Returns a list of Threads in the Forum, in the order that they were published
     */
    public List<Thread> getThreads() {
        return all_threads;
    }

    /**
     * Creates a new thread with the given title and adds it to the Forum.
     * The content is provided to allow you to create the first Post.
     * Threads are stored in the order that they are published.
     * Returns the new Thread object
     * @param title
     * @param content
     */
    public Thread publish(String title, String content) {
        Thread new_thread = new Thread(title, content);

        String[] new_tags = new String[256];
        new_thread.setTags(new_tags);

        all_threads.add(new_thread);
        return new_thread;
    }

    /**
     * Searches all forum Threads for any that contain the given tag.
     * Returns a list of all matching Thread objects in the order that they were published.
     * @param tag
     * @return
     */
    public List<Thread> searchByTag(String tag) {
        List<Thread> tagged_threads = new ArrayList<Thread>();

        for (Thread t : all_threads) {
            List<String> tags = t.getTags();
            if (tags.contains(tag)) {
                tagged_threads.add(t);
            }
        }
        return tagged_threads;
    }

}