package dswin.tool.view;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;

import dswin.tool.message.DSWinToolMessage;
import dswin.tool.util.DSWinToolBackupUtils;

/**
 * <p>
 * Derby Stallion for Windows のセーブデータ管理ツールの画面クラスです。
 * </p>
 * 
 * @author Hiroyuki Sakai
 */
public class DSWinToolFrame extends Frame implements Bindable {

    @BXML
    private PushButton backupButton;

    @BXML
    private ListView saveFileListView;

    @BXML
    private PushButton restoreButton;

    @BXML
    private ListView backupFileListView;

    @BXML
    private PushButton deleteButton;

    @Override
    public final void initialize(final Map<String, Object> namespace, final URL location, final Resources resources) {

        backupButton.getButtonPressListeners().add(new BackupButtonPressListener());
        saveFileListView.setListData(DSWinToolBackupUtils.getSaveFileNames());
        restoreButton.getButtonPressListeners().add(new RestoreButtonPressListener());
        backupFileListView.setListData(DSWinToolBackupUtils.getBackupFileNames());
        deleteButton.getButtonPressListeners().add(new DeleteButtonPressListener());
    }

    private class BackupButtonPressListener implements ButtonPressListener {

        private final Logger logger = Logger.getLogger(getClass().getName());

        @Override
        public void buttonPressed(final Button button) {

            try {

                final String saveFileName = (String) saveFileListView.getSelectedItem();
                if (saveFileName == null) {
                    final String message = DSWinToolMessage.E0001.getMessage();
                    Prompt.prompt(MessageType.ERROR, message, DSWinToolFrame.this);
                    return;
                }

                DSWinToolBackupUtils.createBackupFile(saveFileName);
                backupFileListView.setListData(DSWinToolBackupUtils.getBackupFileNames());

            } catch (final Throwable t) {
                logger.log(Level.SEVERE, t.getMessage(), t);
                final String message = DSWinToolMessage.F0005.getMessage();
                final Alert alert = new Alert(MessageType.ERROR, message, null, true);
                alert.open(DSWinToolFrame.this);
            }
        }
    }

    private class RestoreButtonPressListener implements ButtonPressListener {

        private final Logger logger = Logger.getLogger(getClass().getName());

        @Override
        public void buttonPressed(final Button button) {

            try {

                final String saveFileName = (String) saveFileListView.getSelectedItem();
                if (saveFileName == null) {
                    final String message = DSWinToolMessage.E0002.getMessage();
                    Prompt.prompt(MessageType.ERROR, message, DSWinToolFrame.this);
                    return;
                }

                final String backupFileName = (String) backupFileListView.getSelectedItem();
                if (backupFileName == null) {
                    final String message = DSWinToolMessage.E0003.getMessage();
                    Prompt.prompt(MessageType.ERROR, message, DSWinToolFrame.this);
                    return;
                }

                final String message = DSWinToolMessage.I0001.getMessage(backupFileName, saveFileName);
                final List<DSWinToolDialogOptions> options = DSWinToolDialogOptions.getQuestionOptions();
                final Alert alert = new Alert(MessageType.QUESTION, message, options, true);
                alert.setSelectedOption(DSWinToolDialogOptions.CANCEL);
                alert.open(DSWinToolFrame.this, new DialogCloseListener() {

                    private final Logger logger = Logger.getLogger(getClass().getName());

                    @Override
                    public void dialogClosed(final Dialog dialog, final boolean modal) {

                        try {

                            final Object selectedOption = alert.getSelectedOption();
                            if (DSWinToolDialogOptions.CANCEL.equals(selectedOption)) {
                                return;
                            }

                            DSWinToolBackupUtils.restore(backupFileName, saveFileName);
                            final String message = DSWinToolMessage.I0002.getMessage();
                            Prompt.prompt(MessageType.INFO, message, DSWinToolFrame.this);

                        } catch (final Throwable t) {
                            logger.log(Level.SEVERE, t.getMessage(), t);
                            final String message = DSWinToolMessage.F0005.getMessage();
                            final Alert alert = new Alert(MessageType.ERROR, message, null, true);
                            alert.open(DSWinToolFrame.this);
                        }
                    }
                });

            } catch (final Throwable t) {
                logger.log(Level.SEVERE, t.getMessage(), t);
                final String message = DSWinToolMessage.F0005.getMessage();
                final Alert alert = new Alert(MessageType.ERROR, message, null, true);
                alert.open(DSWinToolFrame.this);
            }
        }
    }

    private class DeleteButtonPressListener implements ButtonPressListener {

        private final Logger logger = Logger.getLogger(getClass().getName());

        @Override
        public void buttonPressed(final Button button) {

            try {

                final String fileName = (String) backupFileListView.getSelectedItem();
                if (fileName == null) {
                    final String message = DSWinToolMessage.E0004.getMessage();
                    Prompt.prompt(MessageType.ERROR, message, DSWinToolFrame.this);
                    return;
                }

                final String message = DSWinToolMessage.I0003.getMessage(fileName);
                final List<DSWinToolDialogOptions> options = DSWinToolDialogOptions.getQuestionOptions();
                final Alert alert = new Alert(MessageType.QUESTION, message, options, true);
                alert.setSelectedOption(DSWinToolDialogOptions.CANCEL);
                alert.open(DSWinToolFrame.this, new DialogCloseListener() {

                    @Override
                    public void dialogClosed(final Dialog dialog, final boolean modal) {

                        final Object selectedOption = alert.getSelectedOption();
                        if (DSWinToolDialogOptions.CANCEL.equals(selectedOption)) {
                            return;
                        }

                        DSWinToolBackupUtils.deleteBackupFile(fileName);
                        backupFileListView.setListData(DSWinToolBackupUtils.getBackupFileNames());
                    }
                });

            } catch (final Throwable t) {
                logger.log(Level.SEVERE, t.getMessage(), t);
                final String message = DSWinToolMessage.F0005.getMessage();
                final Alert alert = new Alert(MessageType.ERROR, message, null, true);
                alert.open(DSWinToolFrame.this);
            }
        }
    }
}
