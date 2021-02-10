package projectCommon;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.function.Function;

/**
 * ファイル内を全て読み込むためのテンプレート系クラスです。<br>
 * ファイルドメインやエンコード形式を間違っている場合は例外で終了します。<br>
 * 実行時はStartメソッドを使用します。<br>
 * 使用の際は引数にラムダ式で(String型 -> { //実際の処理内容// と<br>
 * ファイル内の読み込みを終了させる場合falseを返し、続行する場合はtrueを返すように使用します。})
 *
 * @author Kamishiro
 * @version 0.2beta
 */

public class FileReaderCore {

	/**
	 * ファイルドメインとエンコード形式を指定してください。<br>
	 * 内部にtoStringメソッドを持つファイルドメイン形式であればたぶん実装可能と思われます。
	 * 
	 * @param file
	 * @param encode
	 */
	public FileReaderCore(Object file, String encode) {
		this.file = file.toString();
		this.encode = encode;
	}

	/** ファイルドメイン */
	private String file;
	/** エンコード形式 */
	private String encode;

	/**
	 * ファイル内を読み込む為のテンプレート系メソッドです<br>
	 * 引数に実際の処理をラムダ式で記述して使用します。
	 *
	 * @param textLine readLine()から出力した文字列の引数を基準とした処理を記述する。
	 * @throws UnsupportedClassVersionError
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void start(Function<String, Boolean> textLine) {

		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode))) {
			while (ifNull(fileReader.readLine(), textLine))
				;
		} catch (UnsupportedEncodingException e) {
			System.err.println("エンコード形式が存在していません!!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("ファイルが存在していません!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("インアウトに問題が生じています!!");
			e.printStackTrace();
		}
	}

	/**
	 * StartメソッドでreadLine()で読み込んだ文字列がNullかどうかを判定してNullでない場合は<br>
	 * ラムダ式で実装した処理で処理を開始します。
	 * 
	 * @param lineString readLine()で読み込んだString型テキスト
	 * @param textLine   関数型インターフェースで定義したMethod
	 * @return 文字列がない場合はFalse、それ以外は関数型インターフェースで定義した処理で決定する。
	 */
	private boolean ifNull(String lineString, Function<String, Boolean> textLine) {
		if (lineString == null)
			return false;
		return textLine.apply(lineString);
	}
}
