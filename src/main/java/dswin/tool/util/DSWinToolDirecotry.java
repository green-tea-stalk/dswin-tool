package dswin.tool.util;

import java.util.ResourceBundle;

final class DSWinToolDirecotry {

    private static ResourceBundle bundle = ResourceBundle.getBundle("dswin");

    private DSWinToolDirecotry() {
    }

    public static String getSaveLocation() {
        return bundle.getString("save.location");
    }

    public static String getBackupLocation() {
        return bundle.getString("backup.location");
    }
}
