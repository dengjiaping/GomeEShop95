package com.gome.ecmall.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.http.HttpException;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.gome.eshopnew.R;

/**
 * 异常管理
 */
public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static boolean Debug = true;// 是否保存错误日志

	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_XML = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_OO = 0x08;
	public final static byte TYPE_NULL = 0x09;
	public final static byte TYPE_INDEX = 0x10;
	public final static byte TYPE_FORMATE = 0x11;

	private byte type;
	private int code;

	private AppException() {
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (Debug) {
			this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error,
					this.getCode());
			Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error,
					Toast.LENGTH_SHORT).show();
			break;
		case TYPE_SOCKET:
			Toast.makeText(ctx, R.string.socket_exception_error,
					Toast.LENGTH_SHORT).show();
			break;
		case TYPE_NETWORK:
			Toast.makeText(ctx, R.string.network_not_connected,
					Toast.LENGTH_SHORT).show();
			break;
		case TYPE_XML:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT)
					.show();
			break;
		case TYPE_IO:
			Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT)
					.show();
			break;
		case TYPE_RUN:
			Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT)
					.show();
			break;

		case TYPE_OO:
			Toast.makeText(ctx, "内存溢出", Toast.LENGTH_SHORT).show();
			break;
		case TYPE_NULL:
			Toast.makeText(ctx, "空指针异常", Toast.LENGTH_SHORT).show();
			break;
		case TYPE_INDEX:
			Toast.makeText(ctx, "数组越界", Toast.LENGTH_SHORT).show();
			break;
		case TYPE_FORMATE:
			Toast.makeText(ctx, "数据类型转换异常", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	/**
	 * 提示友好的错误LOG
	 * 
	 * @param ctx
	 */
	public void makeMessage() {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			String err = String.format("网络异常，错误码：%d", this.getCode());
			this.println(err);
			break;
		case TYPE_HTTP_ERROR:
			this.println("网络异常，请求超时");
			break;
		case TYPE_SOCKET:
			this.println("网络异常，读取数据超时");
			break;
		case TYPE_NETWORK:
			this.println("网络连接失败，请检查网络设置");
			break;
		case TYPE_XML:
			this.println("数据解析异常");
			break;
		case TYPE_IO:
			this.println("文件流异常");
			break;
		case TYPE_RUN:
			this.println("应用程序运行时异常");
			break;
		case TYPE_OO:
			this.println("内存溢出");
			break;
		case TYPE_NULL:
			this.println("空指针异常");
			break;
		case TYPE_INDEX:
			this.println("数组越界");
			break;
		case TYPE_FORMATE:
			this.println("数据类型转换异常");
			break;

		}
	}

	/**
	 * 打印错误
	 * 
	 * @param message
	 */
	private void println(String message) {
		// System.out.println(message);
		BDebug.d("exception", message);
	}

	/**
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/GomeLog/Log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * 普通异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException normal(Exception e) {
		if (e instanceof NullPointerException) {
			return new AppException(TYPE_NULL, 0, e);
		} else if (e instanceof IndexOutOfBoundsException) {
			return new AppException(TYPE_INDEX, 0, e);
		} else if (e instanceof NumberFormatException) {
			return new AppException(TYPE_FORMATE, 0, e);
		} else if (e instanceof ArrayIndexOutOfBoundsException) {
			return new AppException(TYPE_FORMATE, 0, e);
		}
		return new AppException();
	}

	/**
	 * IO异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new AppException(TYPE_IO, 0, e);
		}
		return run(e);
	}

	/**
	 * XML异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}

	/**
	 * 网络相关的异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof HttpException) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	/**
	 * HTTP异常
	 * 
	 * @param code
	 *            http返回码
	 * @return
	 */
	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}

	/**
	 * HTTP异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0, e);
	}

	/**
	 * SOCKET异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0, e);
	}

	/**
	 * 运行时异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static AppException getAppExceptionHandler() {
		return new AppException();
	}

}
