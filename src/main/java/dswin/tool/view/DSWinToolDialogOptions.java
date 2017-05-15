package dswin.tool.view;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

enum DSWinToolDialogOptions {

    OK,

    CANCEL;

    public static List<DSWinToolDialogOptions> getQuestionOptions() {
        return new ArrayList<>(OK, CANCEL);
    }
}
