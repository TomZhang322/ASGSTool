package com.adcc.tool.msgCenter.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.adcc.tool.msgCenter.util.LogUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.adcc.tool.msgCenter.AppConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ASGSMainShell extends Shell {
	private ASGSMainShell mShell;
	private Text Msg_text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
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

	/**
	 * Create the shell.
	 * @param display
	 */
	public ASGSMainShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		mShell = this;
		
		Group group_1 = new Group(this, SWT.NONE);
		group_1.setBounds(10, 10, 459, 624);
		
		Label lblMsg = new Label(group_1, SWT.NONE);
		lblMsg.setBounds(10, 10, 61, 17);
		lblMsg.setText("样例报文 :");
		
		Msg_text = new Text(group_1, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		Msg_text.setBounds(20, 33, 418, 534);
		
		Button btnSend = new Button(group_1, SWT.NONE);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String xmlStr = Msg_text.getText().trim();
				if(!Strings.isNullOrEmpty(xmlStr)){
			        CloseableHttpClient client = null;
			        CloseableHttpResponse response = null;
					try {
						HttpPost httpPost = new HttpPost(AppConfiguration.getInstance().getAOMIPAdapterUrl());
						RequestConfig requestConfig = RequestConfig.custom()
						        .setConnectTimeout(5000)
						        .setSocketTimeout(5000)
						        .setConnectionRequestTimeout(120000).build();
						httpPost.setConfig(requestConfig);
						List<BasicNameValuePair> list = Lists.newArrayListWithExpectedSize(1);
						list.add(new BasicNameValuePair("message",xmlStr));
						httpPost.setEntity(new UrlEncodedFormEntity(list));
						client = HttpClients.createDefault();
						response = client.execute(httpPost);
	                    if(response.getStatusLine().getStatusCode() == 200){
	                        InputStream is = response.getEntity().getContent();
	                        Map<String, Object> m = new ObjectMapper().readValue(getString(is),Map.class);
	                        String strResult = m.get("result").toString();
	                        if("SUCCESS".equals(strResult)){
								LogUtil.info("Send success message:"+xmlStr);
	                        }
	                        
	            			MessageBox mb = new MessageBox(mShell, SWT.ICON_WORKING | SWT.OK);
	            			mb.setText("系统提示");
	            			mb.setMessage("报文发送成功，请查表核对数据！\r\n");
	            			mb.open();
	                    } else {
							LogUtil.error("send error :" + response.getStatusLine().getStatusCode());
	            			MessageBox mb = new MessageBox(mShell, SWT.ICON_ERROR | SWT.OK);
	            			mb.setText("系统提示");
	            			mb.setMessage("访问接口失败，HTTP Error Code：" + response.getStatusLine().getStatusCode());
	            			mb.open();
	                    }
					} catch (Exception ex) {
						LogUtil.error("send error", ex);
            			MessageBox mb = new MessageBox(mShell, SWT.ICON_ERROR | SWT.OK);
            			mb.setText("系统提示");
            			mb.setMessage("发送失败，请查看日志" + ex.getMessage());
            			mb.open();
					} finally {
			            if(response != null){
			                try {
			                    response.close();
			                } catch (IOException ex) {
								LogUtil.error("close IO error", ex);
			                }
			            }
			            if(client != null){
			                try {
			                    client.close();
			                } catch (IOException ex) {
								LogUtil.error("close IO error", ex);
			                }
			            }
			        }
				}
			}
		});
		btnSend.setBounds(358, 587, 80, 27);
		btnSend.setText("确认发送");
		createContents();
	}
	
    /**
     * 获取字符串
     * @param is
     * @return
     * @throws java.io.IOException
     */
    private String getString(InputStream is) throws IOException {
        int intLength = -1;
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((intLength = is.read(buffer,0,buffer.length)) > -1){
            String strValue =  new String(buffer,0,intLength);
            sb.append(strValue);
        }
        if(is != null){
            is.close();
        }
        return sb.toString();
    }

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("二级网关模拟发报工具");
		setSize(506, 688);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
