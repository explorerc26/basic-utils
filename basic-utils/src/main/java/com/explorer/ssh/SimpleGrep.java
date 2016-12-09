package com.explorer.ssh;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;



public class SimpleGrep{

private static String SFTP_HOST = "host";
private static int SFTP_PORT = 22;
private static String SFTP_USER = "user";
private static String SFTP_PASS = "encodedone";
private static String SFTP_WORKING_DIR = "dir";

    public static void main(String []args){
    	
    	
    		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    		Calendar cal =  new GregorianCalendar(2015,9,8);
    		Calendar endDate = Calendar.getInstance();
    		
    		while(endDate.after(cal)){
    			String fileName =  dateFormat.format(cal.getTime());
    			System.out.println(fileName);
    			cal.add(Calendar.DATE, 1);
    		}
    		
    		
       

    	if(true){
    		return;
    	}
    	
//    	String clientId = 
		System.out.println("Copying files ....");
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		JSch jsch = null;
		try{
		    jsch = new JSch();
			session = jsch.getSession(SFTP_USER,SFTP_HOST , SFTP_PORT);
			session.setPassword(new String(Base64.decodeBase64(SFTP_PASS)));
			session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
			java.util.Properties config = new java.util.Properties();
			// bypass host key checking for that particular SSH login
			config.put("StrictHostKeyChecking", "no");
	
			// start a session
			session.setConfig(config);
			session.connect();
	
			// start a sftp channel
			channel = session.openChannel("exec");
		    ChannelExec ce = (ChannelExec) channel;

		    ce.setCommand("grep key locan/SystemOut*.log | grep SM_USER");
		    ce.setErrStream(System.err);

		    ce.connect();

		    BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
		    String line;
		    Pattern pattern = Pattern.compile(".*SM_USERDN=\\[(?<T>.*)\\], nylclientid.*");
		    while ((line = reader.readLine()) != null) {
		    	Matcher matcher = pattern.matcher(line);
		    	if(matcher.matches()){
		    		System.out.println(line);
		    		System.out.println("Matcher : "+matcher.group("T"));
		    	}

		    }

		    ce.disconnect();
		    session.disconnect();

		    System.out.println("Exit code: " + ce.getExitStatus());
		} catch(Exception ex){
			
		}

//		copyFile(channelSftp, inputFile1, inputFile1, outputDir);
//		copyFile(channelSftp, inputFile2, inputFile2, outputDir);
//
//		if (channelSftp != null)
//			channelSftp.disconnect();
//		if (channel != null)
//			channel.disconnect();
//		if (session != null)
//			session.disconnect();
		

		System.out.println("Uncompressing files ....");

    }

}