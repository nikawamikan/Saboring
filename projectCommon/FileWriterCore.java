package projectCommon;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

/**
 * fileをwriterするときに基本的な情報を定義しているクラスです。<br>
 * 継承したりなんだりすることで記述をある程度省略する事が可能です。<br>
 * 
 * @author Kamishiro
 *
 */

public class FileWriterCore {

	public FileWriterCore(Object file, String encode, boolean append) {
		this.encode = encode;
		this.file = file.toString();
		this.append = append;
	}

	private String file;
	private String encode;
	private boolean append;

	/**
	 * テキストファイルに記述するまでをテンプレート化したファイルライター
	 * 
	 * @param writer
	 */
	public void start(Consumer<BufferedWriter> writer){
		try (BufferedWriter fileWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, append),
						encode))) {
			writer.accept(fileWriter);
		} catch (UnsupportedEncodingException e) {
			System.err.println("指定したエンコードは見つかりません");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("指定したファイルは見つかりません");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("重大なエラーが発生しているかもしれません");
			e.printStackTrace();
		}
	}
}
