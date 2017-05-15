package dswin.tool;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.Window;

import dswin.tool.message.DSWinToolMessage;
import dswin.tool.view.DSWinToolFrame;

/**
 * <p>
 * Derby Stallion for Windows のセーブデータ管理ツールです。
 * </p>
 * このツールは Derby Stallion for Windows のセーブデータのバックアップ、バックアップデータのリストアを行う為のツールです。このツールは config/dswin.properties の
 * {@code save.location} からセーブデータを、 {@code backup.location} の設定値にセーブデータとバックアップデータの読み書きを行います。
 * 
 * @author Hiroyuki Sakai
 */
public final class DSWinTool implements Application {

    private static final String FONT_NAME = "メイリオ";

    private static final int FONT_STYLE = Font.PLAIN;

    private static final int FONT_SIZE = 11;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Window window = null;

    /**
     * メインメソッドです。
     * 
     * @param args
     *            引数
     */
    public static void main(final String[] args) {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("logging.properties")) {
            final LogManager logManager = LogManager.getLogManager();
            logManager.readConfiguration(inputStream);
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        DesktopApplicationContext.main(DSWinTool.class, args);
    }

    @Override
    public void startup(final Display display, final Map<String, String> properties) throws Exception {

        try {

            final Theme theme = Theme.getTheme();
            theme.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));

            final BXMLSerializer bxmlSerializer = new BXMLSerializer();
            window = (Window) bxmlSerializer.readObject(DSWinToolFrame.class, "DSWinToolFrame.bxml");
            window.open(display);

        } catch (final Throwable t) {
            logger.log(Level.SEVERE, t.getMessage(), t);
            final String message = DSWinToolMessage.F0005.getMessage();
            final Alert alert = new Alert(MessageType.ERROR, message, null, false);
            alert.open(display);
        }
    }

    @Override
    public boolean shutdown(final boolean optional) throws Exception {

        if (window != null) {
            window.close();
        }

        return false;
    }

    @Override
    public void suspend() throws Exception {
    }

    @Override
    public void resume() throws Exception {
    }
}
