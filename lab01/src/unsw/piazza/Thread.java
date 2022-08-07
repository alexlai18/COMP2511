package unsw.piazza;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * A thread in the Piazza forum.
 */
public class Thread {
    // Setting attributes as fields
    private String title;
    private List<String> all_posts;
    private String[] tags;

    // Creating a list of threads
    List<Thread> all_threads = new ArrayList<Thread>();

    /**
     * Creates a new thread with a title and an initial first post.
     * @param title
     * @param firstPost
     */
    public Thread(String title, String firstPost) {
        this.title = title;

        List <String> all_posts = new ArrayList<String>();
        all_posts.add(firstPost);
        this.all_posts = all_posts;

        String[] tags = new String[256];
        this.tags = tags;

        all_threads.add(this);
    }

    /**
     * @return The title of the thread
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return A SORTED list of tags
     */
    public List<String> getTags() {
        List<String> tag_list = new ArrayList<String>();
        for (String s : this.tags) {
            tag_list.add(s);
        }
        Collections.sort(tag_list);
        return tag_list;
    }

    /**
     * @return A list of posts in this thread, in the order that they were published
     */
    public List<String> getPosts() {
        return this.all_posts;
    }

    /**
     * Adds the given post object into the list of posts in the thread.
     * @param post
     */
    public void publishPost(String post) {
        this.all_posts.add(post);
    }

    /**
     * Allows the given user to replace the thread tags (list of strings)
     * @param tags
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
