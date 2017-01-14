package org.algoritica.mnist;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FROM: https://github.com/sina-masoud-ansari/MNIST
 *
 * <p>
 * Utility class for working with the MNIST database.
 * <p>
 * Provides methods for traversing the images and labels data files separately,
 * as well as simultaneously.
 * <p>
 * Provides also method for exporting an image by writing it as a PPM file.
 * <p>
 * Example usage:
 * <pre>
 *  MNISTManager m = new MNISTManager("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte");
 *  m.setCurrent(10); //index of the image that we are interested in
 *  int[][] image = m.readImage();
 *  System.out.println("Label:" + m.readLabel());
 *  MNISTManager.writeImageToPpm(image, "10.ppm");
 * </pre>
 */
public class MNISTManager {
    private MNISTImageFile images;
    private MNISTLabelFile labels;

    /**
     * Writes the given image in the given file using the PPM data format.
     *
     * @param image
     * @param ppmFileName
     * @throws IOException
     */
    public static void writeImageToPpm(int[][] image, String ppmFileName) throws IOException {
        BufferedWriter ppmOut = null;
        try {
            ppmOut = new BufferedWriter(new FileWriter(ppmFileName));

            int rows = image.length;
            int cols = image[0].length;
            ppmOut.write("P3\n");
            ppmOut.write("" + rows + " " + cols + " 255\n");
            for (int i = 0; i < rows; i++) {
                StringBuffer s = new StringBuffer();
                for (int j = 0; j < cols; j++) {
                    s.append(image[i][j] + " " + image[i][j] + " " + image[i][j] + "  ");
                }
                ppmOut.write(s.toString());
            }
        } finally {
            ppmOut.close();
        }

    }

    /**
     * Constructs an instance managing the two given data files. Supports
     * <code>NULL</code> value for one of the arguments in case reading only one
     * of the files (images and labels) is required.
     *
     * @param imagesFile
     *            Can be <code>NULL</code>. In that case all future operations
     *            using that file will fail.
     * @param labelsFile
     *            Can be <code>NULL</code>. In that case all future operations
     *            using that file will fail.
     * @throws IOException
     */
    public MNISTManager(String imagesFile, String labelsFile) throws IOException {
        if (imagesFile != null) {
            images = new MNISTImageFile(imagesFile, "r");
        }
        if (labelsFile != null) {
            labels = new MNISTLabelFile(labelsFile, "r");
        }
    }

    /**
     * Reads the current image.
     *
     * @return matrix
     * @throws IOException
     */
    public int[][] readImage() throws IOException {
        if (images == null) {
            throw new IllegalStateException("Images file not initialized.");
        }
        return images.readImage();
    }

    /**
     * Set the position to be read.
     *
     * @param index
     */
    public void setCurrent(int index) {
        images.setCurrentIndex(index);
        labels.setCurrentIndex(index);
    }

    /**
     * Reads the current label.
     *
     * @return int
     * @throws IOException
     */
    public int readLabel() throws IOException {
        if (labels == null) {
            throw new IllegalStateException("labels file not initialized.");
        }
        return labels.readLabel();
    }

    /**
     * Get the underlying images file as {@link MNISTImageFile}.
     *
     * @return {@link MNISTImageFile}.
     */
    public MNISTImageFile getImages() {
        return images;
    }

    /**
     * Get the underlying labels file as {@link MNISTLabelFile}.
     *
     * @return {@link MNISTLabelFile}.
     */
    public MNISTLabelFile getLabels() {
        return labels;
    }
}
