package com.explorer.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenSearch {

private static final String BASE_PATH = "/transactionsLogUtility/";
	
	static Pattern p = Pattern.compile("\\d+");
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Map<String, Map<String, List<String>>> exhaustiveList = createExhaustiveList();
		BufferedReader reader1 = new BufferedReader(new FileReader("/client-search.txt"));
		Map<String, String> searchClients = getHashMap(reader1);
		
		System.out.println("Search started");
		int searchCount = 0 ; 
		int foundCount = 0 ; 
		for(String seachClient : searchClients.keySet()){
			searchCount ++;
			boolean found = searchForClient(exhaustiveList, seachClient);
			if(found){
				foundCount ++;
				System.out.println(seachClient);
			}
		}
		
		System.out.println("Search End");
		System.out.println("Search Result" );
		System.out.println("Total Ids:"+searchCount);
		System.out.println("Total hits:"+foundCount);

	}
	// found == true
	public static boolean searchForClient(Map<String, Map<String, List<String>>> exhaustiveList , String ClientId){
		for(String file : exhaustiveList.keySet()){
			Map<String, List<String>> clientsInAFile = exhaustiveList.get(file);
			if(clientsInAFile.get(ClientId) !=null)
				return true;
		}
		return false;
	}
	
	public static Map<String, String> getHashMap(BufferedReader reader) throws Exception{
		Map<String, String> clientIds = new HashMap<String, String>();
		String line = "";
		while ((line = reader.readLine()) != null) {
			List<String> tokens = Arrays.asList(line.split(","));
			String clientId = tokens.get(0).replaceAll("\\s","");
			clientIds.put(clientId, clientId);
		}
		return clientIds;
	}
	
	public static Map<String, Map<String, List<String>>> createExhaustiveList() throws Exception{
		
		int i =0;
		int totalNoOfDays = 23; 
		//int i = 2;
		
		Map<String, Map<String, List<String>>> exhaustiveList = new HashMap<String, Map<String, List<String>>>();
		while(i < totalNoOfDays ){
			String datePostfix = getDateString(i);
			List<String> logfiles = new ArrayList<String>(Arrays.asList(
					"p01trans.log"+datePostfix+"/p01trans.log", 
					"p02trans.log"+datePostfix+"/p02trans.log",
					"reg-p01trans.log"+datePostfix+"/reg-p01trans.log",
					"reg-p02trans.log"+datePostfix+"/reg-p02trans.log"));

            for(String logfile: logfiles){
            	Map<String, List<String>> clientIds = new HashMap<String, List<String>>();
            	exhaustiveList.put(logfile, clientIds);
            	findClientIdsForADayInAFile(clientIds, logfile);
            }	
            
            HashSet<String> inADay = new HashSet<String>();
            Map<String, List<String>> file1 = exhaustiveList.get(logfiles.get(0));
            inADay.addAll(file1.keySet());
            Map<String, List<String>> file2 = exhaustiveList.get(logfiles.get(1));
            inADay.addAll(file2.keySet());
            Map<String, List<String>> file3 = exhaustiveList.get(logfiles.get(2));
            inADay.addAll(file3.keySet());
            Map<String, List<String>> file4 = exhaustiveList.get(logfiles.get(3));
            inADay.addAll(file4.keySet());
            System.out.println("In a day "+inADay.size());
            
			for (String s : inADay) {
				//System.out.println(s);
			}
		
            
            i = i +1;
		}
		
		return exhaustiveList;
		
	}
	
	public static void findClientIdsForADayInAFile(Map<String, List<String>> clientIds, String logfile) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(BASE_PATH+"/"+logfile));
		String line;
		while ((line = reader.readLine()) != null) {
			addClientIds(clientIds, line);
		}
		System.out.println("logfile:"+logfile +" Total ClientIds:"+clientIds.size());
	}
	
	
	private static String getDateString(int i) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		Date startDate = dateFormat.parse("10202016");
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.DATE, i);
		String fileName =  dateFormat.format(cal.getTime());
		return fileName;
	}
	
	
	public static void addClientIds(Map<String, List<String>> clientIds,String line) {
		Matcher m = p.matcher(line);
		while (m.find()) {
			String result = m.group();
			if (result.length() == 9) {
				if (clientIds.get(result) == null) {
					clientIds.put(result, new ArrayList<String>());
				}
				//clientIds.get(result).add(line);
			}
		}
	}

}
