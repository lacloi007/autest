package tuanpv.autest;

import java.awt.EventQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tuanpv.autest.frame.FR0001;

public class AUTest {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(Constant.FILE_APPLICATION_CONTEXT);

		// start main frame
		EventQueue.invokeLater(() -> {
			FR0001 ex = new FR0001();
			ex.setVisible(true);
		});
	}
}
