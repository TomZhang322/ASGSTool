package com.adcc.tool.msgCenter;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.widgets.Display;

import com.adcc.tool.msgCenter.view.ASGSMainShell;

public class APP {

	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("./conf/log4j.properties");
			Display display = Display.getDefault();
			ASGSMainShell shell = new ASGSMainShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
