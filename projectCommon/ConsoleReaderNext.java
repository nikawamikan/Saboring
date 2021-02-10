package projectCommon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * 個人的に今までの中で一番使い勝手の良いConsole入力系クラス です。<br>
 * 文字列入力の際、途中で終了させる際に終了ワードをBoolean型で出力する事もできます。<br>
 * その際、文字列またはInt型のデータをクラスフィールドに保持しておくので<br>
 * toStringメソッド、toIntメソッドにて取り出す事が可能です。<br>
 * また、AutoCloseableをいんぷりめんつしてるのでTryWithResource可能<br>
 * 変数記述省略の為にgetIntやgetStringメソッドでもフィールド変数を書き換える仕様に変更しました。<br>
 * 終了ワードを利用して終了した場合はフィールドのStringの変数を変更しないように変更しました。(再利用する場合を考慮)<br>
 * Stringのコンスタントプール問題の解釈を間違っていた為。new演算子を利用したString作成の記述を破棄<br>
 * (Stringをnewした場合ヒープ領域およびコンスタントプール両方のメモリに保存される構造になっているらしい)
 *
 * @version 0.1.1 Bate
 * @author Kamishiro
 * @param str String型変数、toStringメソッドで呼び出し可能。
 * @param i   Int型変数、toIntメソッドで呼び出し可能。
 *
 */

public class ConsoleReaderNext implements AutoCloseable {

	// 基本入力をOverrideしながら記述を省略していますのでprivateで十分なのです
	private BufferedReader br;
	private boolean debug;
	private String quitWord;

	// 一時変数的なものです。
	protected String str = "";
	protected int i = 0;

	{
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * デフォルトのコンストラクタでは終了キーワード"q"でデバックメッセージありです。<br>
	 * 第一引数に終了ワード、第二引数にデバックをBoolean型でfalseにする事ができます。
	 */
	public ConsoleReaderNext() {
		this(true);
	}

	/**
	 * デフォルトのコンストラクタでは終了キーワード"q"でデバックメッセージありです。<br>
	 * 第一引数に終了ワード、第二引数にデバックをBoolean型でfalseにする事ができます。
	 *
	 * @param debug デフォルトではtrueです。表示しない場合はfalseを入力します。
	 */
	public ConsoleReaderNext(boolean debug) {
		this("q", debug);
	}

	/**
	 * デフォルトのコンストラクタでは終了キーワード"q"でデバックメッセージありです。<br>
	 * 第一引数に終了ワード、第二引数にデバックをBoolean型でfalseにする事ができます。
	 *
	 * @param quitWord 終了ワードです。デフォルトでは"q"です。
	 */
	public ConsoleReaderNext(String quitWord) {
		this(quitWord, true);
	}

	/**
	 * デフォルトのコンストラクタでは終了キーワード"q"でデバックメッセージありです。<br>
	 * 第一引数に終了ワード、第二引数にデバックをBoolean型でfalseにする事ができます。
	 *
	 * @param quitWord 終了ワードです。デフォルトでは"q"です。
	 * @param debug    デフォルトではtrueです。表示しない場合はfalseを入力します。
	 */
	public ConsoleReaderNext(String quitWord, boolean debug) {
		if (quitWord.equals(""))
			this.quitWord = "q";
		this.debug = debug;
		this.quitWord = quitWord;
		// 基本はデバックモードです。falseを入れてデバックモードから通常モードに設定します。
		if (debug) {
			System.out.println("// コンソール入力にSystem.inをラップしました //");
		}
	}

	/**
	 * 終了時にcloseする事をするためのメソッドです。<br>
	 * try(var cr = new ConsoleReaderNext()){}のように使用すればcloseメソッドを呼び出す必要はありません。
	 */
	@Override
	public void close() throws IOException {
		// とらいうぃずりそーすで使えるようにしてます。
		br.close();
		if (debug) {
			System.out.println("// コンソール入力をclose処理しました。 //");
		}
	}

	/**
	 * 格納されている文字列を出力します。<br>
	 *
	 * @return boolAnd系メソッドで入力された文字列
	 */
	@Override
	public String toString() {
		return this.str;
	}

	/**
	 * 格納されているInt型を出力します。<br>
	 *
	 * @return boolAnd系メソッドで入力されたint型数値
	 */
	public int toInt() {
		return this.i;
	}

	/**
	 * コンソールに入力された文字列を出力します。<br>
	 * 空文字列も返すので一部では不具合が生じる場合があります
	 *
	 * @return コンソールに入力された文字列
	 */
	public String getStringBasic() {
		try {
			System.out.print("-> ");
			return br.readLine();
		} catch (IOException e) {
			System.err.println("もしかしたら致命的なエラーが発生しているかもしれません...");
			System.err.println("入出力系エラーなので物理的な問題も考えられます");
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 入力を促すメッセージを表示してから、コンソールに入力された文字列を出力します。<br>
	 * 空文字列も返すので一部では不具合が生じる場合があります
	 *
	 * @param 入力を促すメッセージ
	 * @return コンソールに入力された文字列
	 */
	public String getStringBasic(String message) {
		System.out.print(message);
		return getStringBasic();
	}

	/**
	 * コンソールに入力された文字列を出力します。<br>
	 * 空文字列を許容しません。
	 *
	 * @return コンソールに入力された文字列
	 */
	public String getString() {
		for (; (str = getStringBasic()).equals("");)
			;
		return str;
	}

	/**
	 * 入力を促すメッセージを表示してから、コンソールに入力された文字列を出力します。<br>
	 * 空文字列を許容しません。
	 *
	 * @param 入力を促すメッセージ
	 * @return コンソールに入力された文字列
	 */
	public String getString(String message) {
		for (; (str = getStringBasic(message + System.lineSeparator())).equals("");)
			;
		return str;
	}

	/**
	 * 文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はフィールドに格納しているのでtoStringメソッドで取得します。<br>
	 * 空文字列も返すので一部では不具合が生じる場合があります
	 *
	 * @return 終了キーワードが入力された場合true
	 */
	public boolean boolAndStringBasic() {
		String str = getStringBasic();
		if (str.toLowerCase().equals(quitWord.toLowerCase())) {
			return true;
		}
		this.str = str;
		return false;
	}

	/**
	 * 入力を促すメッセージを表示してから、文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はフィールドに格納しているのでtoStringメソッドで取得します。<br>
	 * 空文字列も返すので一部では不具合が生じる場合があります
	 *
	 * @param 入力を促すメッセージ
	 * @return 終了キーワードが入力された場合true
	 */
	public boolean boolAndStringBasic(String message) {
		System.out.println(message + System.lineSeparator() + quitWord + "で終了します。");
		return boolAndStringBasic();
	}

	/**
	 * 文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はフィールドに格納しているのでtoStringメソッドで取得します。<br>
	 * 空文字列を許容しません。
	 *
	 * @return 終了キーワードが入力された場合true
	 */
	public boolean boolAndString() {
		return boolAndString("");
	}

	/**
	 * 入力を促すメッセージを表示してから、文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はフィールドに格納しているのでtoStringメソッドで取得します。<br>
	 * 空文字列を許容しません。
	 *
	 * @param 入力を促すメッセージ
	 * @return 終了キーワードが入力された場合true
	 */
	public boolean boolAndString(String message) {
		String str;
		do {
			System.out.println(message + System.lineSeparator() + quitWord + "で終了します。");
			str = getStringBasic();
		} while (str.equals(""));
		if (str.toLowerCase().equals(quitWord.toLowerCase()))
			return true;
		this.str = str;
		return false;
	}

	/**
	 * コンソールに入力された文字列をInt型に変換して返します。 数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @return 入力された数値
	 */
	public int getInt() {
		for (;;) {
			String str = getStringBasic("");
			try {
				return i = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				System.out.println("数字以外が入力されています。");
			}
		}
	}

	/**
	 * 入力を促すメッセージを表示してから、コンソールに入力された文字列をInt型に変換し、<br>
	 * 入力された文字列はInt型に変換されてから入力範囲値かを判定したのち、I返します。<br>
	 * 数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @param message 入力を促すメッセージ
	 * @param min     最小値
	 * @param max     最大値
	 * @return 入力された数値
	 */
	public int getInt(String message) {
		for (;;) {
			String str = getStringBasic(message + System.lineSeparator());
			try {
				return i = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				System.out.println("数字以外が入力されています。");
			}
		}
	}

	/**
	 * 入力を促すメッセージを表示してから、コンソールに入力された文字列をInt型に変換し、<br>
	 * 入力された文字列はInt型に変換されてから入力範囲値かを判定したのち、I返します。<br>
	 * 数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @param message 入力を促すメッセージ
	 * @param min     最小値
	 * @param max     最大値
	 * @return 入力された数値
	 */
	public int getInt(String message, int min, int max) {
		for (;;) {
			i = getInt(message);
			if (min <= i && max >= i) {
				return i;
			}
			System.out.println("入力された数値が範囲外です。");
		}
	}

	/**
	 * 文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はInt型に変換されてフィールドに格納しているのでtoIntメソッドで取得します。<br>
	 * 終了ワード以外で数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @return 終了ワードが入力された場合true
	 */

	public boolean boolAndInt() {
		return boolAndInt("");
	}

	/**
	 * 入力を促すメッセージを表示してから、文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はInt型に変換されてフィールドに格納しているのでtoIntメソッドで取得します。<br>
	 * 終了ワード以外で数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @param message 入力を促すメッセージ
	 * @return 終了ワードが入力された場合true
	 */
	public boolean boolAndInt(String message) {
		String str;
		for (;;) {
			str = getStringBasic(message + System.lineSeparator() + quitWord + "で終了します。" + System.lineSeparator());
			if (str.equals(quitWord))
				return true;
			try {
				i = Integer.parseInt(str);
				return false;
			} catch (NumberFormatException e) {
				System.out.println("数字以外が入力されています。");
			}
		}
	}

	/**
	 * 入力を促すメッセージを表示してから、文字列を入力してもらい、終了ワードが入力された時にtrueを返します。<br>
	 * 入力された文字列はInt型に変換されてから入力範囲値かを判定してから<br>
	 * フィールドに格納しているのでtoIntメソッドで取得します。<br>
	 * 終了ワード以外で数字以外が入力された際はもう一度入力してもらいます。
	 *
	 * @param message 入力を促すメッセージ
	 * @param min     最小値
	 * @param max     最大値
	 * @return 終了ワードが入力された場合true
	 */
	public boolean boolAndInt(String message, int min, int max) {
		for (;;) {
			if (boolAndInt(message)) {
				return true;
			}
			if (min <= i && max >= i) {
				return false;
			}
			System.out.println("入力された数値が範囲外です。");
		}
	}

	/**
	 * YまたはNの入力を検出してBoolean型を返す為のメソッドです。
	 *
	 * @param message 入力を促すメッセージ
	 * @return Yを入力した場合はTrueを返します。
	 */
	public boolean YorN(String message) {
		Pattern y = Pattern.compile("[Yy]");
		Pattern n = Pattern.compile("[Nn]");
		for (;;) {
			String str = getStringBasic(message + System.lineSeparator() + "[Y/n]");
			if (y.matcher(str).find()) {
				return true;
			} else if (n.matcher(str).find()) {
				return false;
			}
		}
	}
}
