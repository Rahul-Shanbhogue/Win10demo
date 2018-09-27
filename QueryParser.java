package com.wipro.databaseEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class QueryParser {
	QueryParameter ob;
	ArrayList<String> queryList = new ArrayList<String>();
	ArrayList<String> arrBeforeWhere = new ArrayList<String>();
	ArrayList<String> arrAfterWhere = new ArrayList<String>();
	ArrayList<String> arrCondition = new ArrayList<String>();
	ArrayList<String> arrFields = new ArrayList<String>();
	int indexWhere;

	public enum logicalOp{
	and, or, not
	}
	
	public enum aggregate{
	max, min, avg, count, sum
	}
	
	public QueryParser(){
	ob = new QueryParameter();
	}
	
	public void getQueryList(){
	//prints all the tokens in the query string
	for(String a : queryList) 
	    System.out.println(a);
	}
	
	public void splittingString(String queryString){
		String[] arrOfStr = queryString.split(" ");
        for (String a : arrOfStr) 
            queryList.add(a);
	}
	
	public String getFileName(){
		//Getting the name of the file from the query string
        return queryList.get(queryList.indexOf("from")+1);
	}
	
	public String getBasePart(){
		//extracting the base part
		int indexFrom = queryList.indexOf("from");
		for(int i=0; i<=indexFrom+1; i++){
	        //System.out.print(queryList.get(i) + " ");
	        arrBeforeWhere.add(queryList.get(i));
	    }
		return arrBeforeWhere.toString();
	}
	
	public ArrayList<String> getFilters(){
	    //extracting the filter part
		indexWhere = queryList.indexOf("where");
		int indexGroup = queryList.indexOf("group");
		int indexOrder = queryList.indexOf("order");
		int stopWhere=-1;
		if(indexOrder>0 || indexGroup>0){
			stopWhere = (indexGroup<indexOrder)? indexGroup : indexOrder;
		}else{
			stopWhere = queryList.size();
		}
	    for(int i=indexWhere+1; i<stopWhere; i++){
	        //System.out.print(queryList.get(i) + " ");
	        arrAfterWhere.add(queryList.get(i));
	    }
	    return arrAfterWhere;
	}
	
	public ArrayList<String> logicalOperators(){
		//extracting logical operators from the query string 
        for (logicalOp s : logicalOp.values()){
        	if(arrAfterWhere.indexOf(s.toString())>=0){
        		arrCondition.add(arrAfterWhere.get(arrAfterWhere.indexOf(s.toString())));
        	}
        }
        return arrCondition;
	}
	
	public ArrayList<String> getFields(){
		//selected fields of the query string from the file
        String str = queryList.get(1);
        String[] fields = str.split(","); 
        return (ArrayList<String>) Arrays.asList(fields);
	}
	
	public ArrayList<String> getOrderBy(){
		//Extracting the order by field from the given string.
        if(queryList.contains("order") && queryList.get(queryList.indexOf("order")+1).equals("by")){
        	//System.out.println("contains order by");
        	int indexOrderBy = queryList.indexOf("order")+1;
        	String[] fields = queryList.get(indexOrderBy +1).split(",");
        	//System.out.println(queryList.get(indexOrderBy +1));
        	return (ArrayList<String>) Arrays.asList(fields);
        }
        return null;
	}
	
	public ArrayList<String> getGroupBy(){
		//Extracting the group by field from the given string.
        if(queryList.contains("group") && queryList.get(queryList.indexOf("group")+1).equals("by")){
        	//System.out.println("contains order by");
        	int indexGroupBy = queryList.indexOf("group")+1;
        	String[] fields = queryList.get(indexGroupBy +1).split(",");
        	//System.out.println(queryList.get(indexGroupBy +1));
        	return (ArrayList<String>) Arrays.asList(fields);
        }
        return null;
	}
	
	public ArrayList<String> getAggregateFields(){
		for(aggregate a : aggregate.values()){      	
        	String s = queryList.get(1);
            String[] s1 = s.split(","); 
            for(String str: s1){
            	if(str.startsWith(a.toString())){
         			//System.out.println(s2);
            		arrFields.add(str);
         		}
            }
        }
		return arrFields;
	} 
	
	//this function to parse the query string
	public QueryParameter parseQuery(String queryString){   
	        
	//obtaining query string from user
		System.out.println("Enter a SQL query");
	    Scanner sc = new Scanner(System.in);
	    ob.setQueryString(sc.nextLine());
	    
	    //parse the query string
	    splittingString(ob.getQueryString());
	    ob.setFile(getFileName());
	    ob.setFields(getFields());
	    ob.setBaseQuery(getBasePart());
	    
	    if(queryList.contains("where")){
	        ob.setWhereFilters(getFilters());
	        ob.setLogicalOperators(logicalOperators());
	        ob.setRestrictions(null);
	        
	    }else{
	    	//ob.setBaseQuery(ob.getQueryString());
	    }
	    
	    ob.setOrderBy(getOrderBy());
	    ob.setGroupBy(getGroupBy());
	    ob.setAggregateFields(getAggregateFields());
	    //construct the query parameter
	    sc.close();
        return ob;
	}
}

