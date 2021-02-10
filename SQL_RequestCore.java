package projectCommon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLへリクエストを送信する際の抽象クラスです。<br>
 * コンストラクタからSQLのURLとUSER名とPASSWORDを設定して使用します。<br>
 * 使用時にはstartメソッドを使用しますが、マルチスレッティングのそれではありません。<br>
 * 実際の処理を記述する抽象メソッドのほかにSQLExceptionの例外処理を行う抽象メソッド、<br>
 * 終了する際に使用するResultSetなどをclose処理する際のFinallyの実際の処理を行うための抽象メソッドがあります。
 *
 * @author Kamishiro
 *
 */
public abstract class SQL_RequestCore{

	private String url;
	private String user;
	private String password;

	/**
	 * url,user,passwordの情報を定義してメソッドを使用可能な状態にします。
	 * 引数なしのコンストラクタは存在しません。
	 * @param url SQLサーバーのURL
	 * @param user SQLを使用する際のユーザー名
	 * @param password SQLを使用する際のユーザー名に付随するパスワード
	 */
	public SQL_RequestCore(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/** startメソッドで実際の処理を記述する */
	abstract protected void query(Statement st) throws SQLException;

	/** startメソッドのexceptionの実際の処理を記述する */
	abstract protected void isException(SQLException e);

	/** startメソッドのfinallyの実際の処理を記述する */
	abstract protected void isFinally();

	/**
	 * 始動用メソッドです。ここには基本的に処理に処理の記述は行えません。
	 */
	public void start() {
		try (Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement()) {
			query(st);
		} catch (SQLException e) {
			isException(e);
		} finally {
			isFinally();
		}
	}
}
