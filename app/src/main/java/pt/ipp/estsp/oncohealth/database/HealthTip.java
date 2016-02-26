package pt.ipp.estsp.oncohealth.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Health tips to be displayed on landing screen and in their own page when clicked
 * @author Victor
 * @version 0.1
 * @since 0.1
 */

public class HealthTip implements java.io.Serializable{
    /** Id of the health tip */
    private long id;
    /** Name of the health tip. Shouldn't be longer that 3/4 words */
    private String name;
    /** A short text containing the core of the tip. Shouldn't be longer that 2/3 short phrases */
    private String shortText;
    /** A text containing some additional details to the tip. May contain some scientific information. */
    private String fullText;
    /** An image containing some illustration related to the tip. Must have fixed size (250x250px)
     * and is expected to have transparent background. */
    private byte[] image;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    /**
     * Returns the image in the {@link Bitmap} format
     * @return image
     */
    public Bitmap getImage() {
        return getImgFromBytes(image);
    }

    /**
     * Sets the image to the given one. Must be a 250x250px .png image
     * @param image The bitmap image
     */
    public void setImage(Bitmap image) {
        this.image = getBytesFromImg(image);
    }

    //TODO move this to utils class
    private static Bitmap getImgFromBytes(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private static byte[] getBytesFromImg(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
