package pt.ipp.estsp.oncohealth.database;

/**
 * Created by Victor on 02/03/2016.
 */
public class Routine {
    /** Id of the health tip */
    private long id;
    /** The number of times the routine should be repeated */
    private int repetitions;
    /** The time in milliseconds between each repetition*/
    private int interval;
    /** The image to be displayed in the animation */
    private byte[] image;
    /** A short description of the routine, expected to have 1/2 short phrases. */
    private String description;
    /** True if the routine was already completed by the user, false otherwise */
    private boolean isDone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}
