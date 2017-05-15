package dswin.tool.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

/**
 * <p>
 * Derby Stallion for Windows のセーブデータのバックアップユーティリティクラスです。
 * </p>
 * このユーティリティクラスはセーブデータのバックアップ、リストア、バックアップファイルの削除機能を提供します。
 * 
 * @author Hiroyuki Sakai
 */
public final class DSWinToolBackupUtils {

    private static final String TIMESTAMP = "yyyyMMddHHmmssSSS";

    private DSWinToolBackupUtils() {
    }

    /**
     * <p>
     * バックアップファイルを作成します。
     * </p>
     * 指定されたセーブデータのバックアップファイルを作成します。
     * 
     * @param saveFileName
     *            セーブデータのファイル名
     */
    public static void createBackupFile(final String saveFileName) {

        final String saveLocation = DSWinToolDirecotry.getSaveLocation();
        final File saveFile = new File(saveLocation, saveFileName);

        final String backupFileName = generateBackupFileName(saveFileName);
        final String backupLocation = DSWinToolDirecotry.getBackupLocation();
        final File backupFile = new File(backupLocation, backupFileName);

        DSWinFileUtils.copyFile(saveFile, backupFile);
    }

    private static String generateBackupFileName(final String saveFileName) {

        final SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP);
        final Calendar calendar = Calendar.getInstance();
        final String timestamp = format.format(calendar.getTime());

        final StringBuilder builder = new StringBuilder(saveFileName);
        builder.append("-");
        builder.append(timestamp);
        builder.append(".bk");

        return builder.toString();
    }

    /**
     * <p>
     * バックアップファイルを削除します。
     * </p>
     * 指定されたファイル名のバックアップファイルを削除します。
     * 
     * @param backupFileName
     *            バックアップファイル名
     */
    public static void deleteBackupFile(final String backupFileName) {

        final String backupLocation = DSWinToolDirecotry.getBackupLocation();
        final File backupFile = new File(backupLocation, backupFileName);

        DSWinFileUtils.deleteFile(backupFile);
    }

    /**
     * <p>
     * バックアップファイルのリストアを行います。
     * </p>
     * {@code backupFileName} で指定されたバックアップファイルを {@code saveFileName} のセーブデータにリストアします。
     * 
     * @param backupFileName
     *            バックアップファイル名
     * @param saveFileName
     *            セーブデータのファイル名
     */
    public static void restore(final String backupFileName, final String saveFileName) {

        final String saveLocation = DSWinToolDirecotry.getSaveLocation();
        final File saveFile = new File(saveLocation, saveFileName);

        final String backupLocation = DSWinToolDirecotry.getBackupLocation();
        final File backupFile = new File(backupLocation, backupFileName);

        DSWinFileUtils.copyFile(backupFile, saveFile, true);
    }

    /**
     * <p>
     * バックアップファイル名の一覧を取得します。
     * </p>
     * {@code backup.location} に格納されているバックアップファイル名の一覧を返却します。
     * 
     * @return バックアップファイル名の一覧
     */
    public static List<String> getBackupFileNames() {

        final String backupLocation = DSWinToolDirecotry.getBackupLocation();
        final File backupDirectory = new File(backupLocation);
        final File[] backupFiles = backupDirectory.listFiles(new ExtensionFilter("bk"));

        final ArrayList<String> backupFileNames = new ArrayList<>();
        if (backupFiles != null) {
            for (final File backupFile : backupFiles) {
                backupFileNames.add(backupFile.getName());
            }
        }

        return backupFileNames;
    }

    /**
     * <p>
     * セーブデータのファイル名の一覧を取得します。
     * </p>
     * {@code save.location} に格納されているセーブデータのファイル名の一覧を返却します。
     * 
     * @return セーブデータのファイル名の一覧
     */
    public static List<String> getSaveFileNames() {

        final String saveLocation = DSWinToolDirecotry.getSaveLocation();
        final File saveDirectory = new File(saveLocation);
        final File[] saveFiles = saveDirectory.listFiles(new ExtensionFilter("dsf"));

        final ArrayList<String> saveFileNames = new ArrayList<>();
        if (saveFiles != null) {
            for (final File saveFile : saveFiles) {
                saveFileNames.add(saveFile.getName());
            }
        }

        return saveFileNames;
    }

    private static class ExtensionFilter implements FilenameFilter {

        private final String extension;

        public ExtensionFilter(final String extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(final File dir, final String name) {
            return name.endsWith("." + extension);
        }
    }
}
