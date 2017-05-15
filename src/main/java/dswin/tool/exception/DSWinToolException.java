package dswin.tool.exception;

/**
 * <p>
 * Derby Stallion for Windows のセーブデータ管理ツールの例外クラスです。
 * </p>
 * Derby Stallion for Windows のセーブデータ管理ツールで予期せぬ例外が発生した場合にスローされます。
 * 
 * @author Hiroyuki Sakai
 */
public class DSWinToolException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * この例外を構築します。
     * </p>
     * メッセージを元に構築します。
     * 
     * @param message
     *            メッセージ
     */
    public DSWinToolException(final String message) {
        super(message);
    }

    /**
     * <p>
     * この例外を構築します。
     * </p>
     * メッセージと原因となった例外を元に構築します。
     * 
     * @param message
     *            メッセージ
     * @param cause
     *            原因となった例外
     */
    public DSWinToolException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
