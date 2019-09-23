package helper;

import db.utility.DbConnector;
import exceptions.NoFormateFoundException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class EnrichHelper {
public  static Set<String> formatsSet=null;

    public static Map<String, String> createInterfaceMap(Properties prop) {
        formatsSet=new HashSet<>();
        Map<String,String> formateIntMap=new HashMap<>();
    String[] formatStrArr = new String[2];
    for(String fInt:prop.getProperty("formats").split("\\|\\|"))
    {
        formatStrArr = fInt.split("=");
        // formate is value
        String value = formatStrArr[0];
        //interfaces are keys
        String[] interfacArr = formatStrArr[1].split(",");
        for (String interfac : interfacArr) {
            formateIntMap.put(interfac, value);
        }
        formatsSet.add(value);
    }
            return formateIntMap;
}

    public static Map<String, String> createCommonConstantsMap(Properties prop) {
        Map<String,String> commonConstantsMap= new HashMap<>();
        String commonConstants=prop.getProperty("common-constans");
        for (String ct:commonConstants.split(",")){
            String[] keyValArr=ct.split(":");
            commonConstantsMap.put(keyValArr[0],keyValArr[1]); //key=version  value=1.1
        }
        return commonConstantsMap;
    }

    /*
    * Map<formateName,FormateSpecificTafsStr>
    * */
    public static Map<String, String> createFormatSpecificTagsMap(Properties prop) throws NoFormateFoundException {
        Map<String,String> formatSpecificTagsMap=new HashMap<>();
        if (formatsSet==null || formatsSet.size()==0){throw new NoFormateFoundException("Formate Configuration missing"); }
        String formatSpecificTagsStr="";
        for (String fName:formatsSet){
            formatSpecificTagsStr=prop.getProperty(fName.trim());
            if (formatsSet==null){throw new NoFormateFoundException("Formate Configuration missing"); }
            else {
                formatSpecificTagsMap.put(fName,formatSpecificTagsStr);
            }
        }
        return formatSpecificTagsMap;
    }



    public static ResultSet fetchTableEnrichNewData(){
        ResultSet resultSet=null;
        try {
            Connection con= DbConnector.getDBObject();
            Statement statement=con.createStatement();
            resultSet=statement.executeQuery("select * from sysco_enrichment_status_data where status='New'");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
}
