package com.stackroute.datamunger;
import javax.swing.*;
import java.util.*;
/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {
		String s= queryString;
		String []s1=s.split(" ");
		for(int i=0;i<s1.length;i++)
		{
			s1[i]=s1[i].toLowerCase();
		}
		return s1;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {

		String []words=queryString.split(" ");
		int i=0;
		while(!(words[i].equalsIgnoreCase("from"))){
			i++;
		}
		i++;
		return words[i];
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
	    String re="( where | group by | order by )";
		if(queryString.contains(" where ")||queryString.contains(" group by ")||queryString.contains(" order by ")) {
		    String[]baseQuery=queryString.split(re,0);
            baseQuery[0] = baseQuery[0].trim();
            return baseQuery[0];
        }
		else
		    return queryString;


	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {
        String[] words=queryString.split(" ");
        String[] fields=words[1].split(",");
		return fields;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {
        String[] str;
        if(queryString.contains("where"))
        {
            str=queryString.toLowerCase().split("where");
        }
        else
            return null;

        String re="(order by | group by)";
        String []result;

        if(str[1].contains("order by")|| str[1].contains("group by"))
        {
            result=str[1].split(re,0);
            return result[0].trim();
        }
        else
        {
            return str[1].trim();
        }

	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {
	    String str=getConditionsPartQuery(queryString);
	    String []result;
        if(str==null)
            return null;
	    else if(str.contains(" and ")||str.contains(" or ")||str.contains(" not ")) {
            String re = "( and | or | not )";
             String[]s=result = str.split(re, 0);
             return result;
        }
        else
            return new String[]{str};

	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
        queryString=queryString.toLowerCase();
        if (!(queryString.contains("where")))
            return null;
        String[] s1= queryString.split("where");
        int []flag={0,0,0};
        if(s1[1].contains(" and "))
            flag[0]=1;
        if(s1[1].contains(" or "))
            flag[1]=1;
        if(s1[1].contains(" not "))
            flag[2]=1;

        int l=flag[0]+flag[1]+flag[2];


        String[] result=new String[l];

        Arrays.fill(result,null);
        int count=0;
        if(flag[0]==1)
        {
            result[count]="and";
            count++;
        }

        if(flag[1]==1)
        {
            result[count]="or";
            count++;
        }

        if(flag[2]==1)
        {
            result[count]="not";
        }
        System.out.println(Arrays.toString(result));

		return result;
	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {

	    if(!(queryString.contains("order by")))
	        return null;

	    String []s1= queryString.split("order by");
	    String []s2=s1[1].split(",");
	    for(int i=0;i<s2.length;i++)
        {
            s2[i]=s2[i].trim();

        }

	    int conditions=s2.length;

	    String[]result=new String[conditions];
	    Arrays.fill(result, null);

	    for(int i=0;i<conditions;i++){

            if(s2[i].contains(" "))
            {
                result[i]=s2[i].substring(0,s2[i].indexOf(" "));
            }
            else
                result[i]=s2[i];
        }

		return result;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {

	    if(queryString.contains("group by")) {
            String[] s = queryString.split(" group by ");
            if(s[1].contains(" having ")||s[1].contains(" order by "))
            {
                String re="( having | order by )";
                String []result=s[1].split("re,0");
                result[0]=result[0].trim();
                String [] str=result[0].split(",");
                return str;
            }
            else
                return s[1].trim().split(",");
        }
	    else
	    return null;
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {
	    String[]str1=queryString.split(" from");
	    String[]str2=str1[0].split("select ");
	    str2[1]=str2[1].trim();
	    String[] result=str2[1].split(",");
	    ArrayList<String> list=new ArrayList<String>();
	    int count=0;
	    for(int i=0;i<result.length;i++)
        {
            if(result[i].startsWith("count")||result[i].startsWith("max")||result[i].startsWith("min")||result[i].startsWith("sum")||result[i].startsWith("avg")){
                list.add(result[i]);
                count++;
            }
        }

	    if(count==0)
	        return null;

		String [] res=list.toArray(new String[list.size()]);
	    return res;
	}

}