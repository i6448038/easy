package servlets;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bootstrapplugin.bean.EditorBean;

public class AbstractServer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String modification = req.getParameter("modification");
		this.addTheModifactionMessage(modification);
	}

	// 向文件中，添加布局信息。
	private void addTheModifactionMessage(String message) throws IOException {
		RandomAccessFile f = new RandomAccessFile(EditorBean.htmlFile
				.getLocation().toFile(), "rw");
		StringBuffer buffer = new StringBuffer();
		byte[] bfr = new byte[10];
		int i = 0;
		while ((i = f.read(bfr, 0, 10)) != -1) {
			buffer.append(new String(bfr, 0, i));
		}
		int positon = buffer.toString().indexOf("<body>") + 6;
		f.seek(positon);
		message = message + buffer.toString().substring(positon);
		f.write(("\n" + message).getBytes());
		f.close();
	}

}
