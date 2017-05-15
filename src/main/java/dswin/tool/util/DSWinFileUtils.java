package dswin.tool.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import dswin.tool.exception.DSWinToolException;
import dswin.tool.message.DSWinToolMessage;

final class DSWinFileUtils {

    private static final int BUFFER_SIZE = 1024;

    private DSWinFileUtils() {
    }

    public static void copyFile(final File src, final File dist) {
        copyFile(src, dist, false);
    }

    public static void copyFile(final File src, final File dist, final boolean override) {

        if (!src.exists()) {
            final String message = DSWinToolMessage.F0001.getMessage(src);
            throw new DSWinToolException(message);
        }

        if (override) {
            deleteFile(dist);
        }

        try {

            final File parent = dist.getParentFile();
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    final String message = DSWinToolMessage.F0002.getMessage(parent);
                    throw new DSWinToolException(message);
                }
            }

            if (!dist.createNewFile()) {
                final String message = DSWinToolMessage.F0002.getMessage(dist);
                throw new DSWinToolException(message);
            }

        } catch (final IOException e) {
            final String message = DSWinToolMessage.F0004.getMessage(dist);
            throw new DSWinToolException(message);
        }

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(src));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dist))) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();

        } catch (final IOException e) {
            final String message = DSWinToolMessage.F0004.getMessage(dist);
            throw new DSWinToolException(message);
        }
    }

    public static void deleteFile(final File file) {

        if (file.exists()) {
            if (!file.delete()) {
                final String message = DSWinToolMessage.F0003.getMessage(file);
                throw new DSWinToolException(message);
            }
        }
    }
}
