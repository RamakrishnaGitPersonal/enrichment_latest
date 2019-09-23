package db.utility;

import db.interfaces.EnrchConverter;
import exceptions.NoFormateFoundException;
import helper.EnrichHelper;
import main.EnrchMain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

public class XmlConverter implements EnrchConverter {


    @Override
    public String convert(){
String xmlData="";
String subXml1="";//messageHeader
        String subXml2="";//messageRequestBody
        String subXml3="";//RequestHeader
        String subXml4="";//retrievPODetailsRequest
        try {
          ResultSet resultSet= EnrichHelper.fetchTableEnrichNewData();
            while (resultSet.next()){
                xmlData="<message>";
                subXml1="";
                subXml2="<messageRequestBody>";
                subXml3="";
                 subXml4="";
                String[] eMathodArr=resultSet.getString("enrich_method").split("=");
            String fName= EnrchMain.formateIntMap.get(eMathodArr[1].trim());
            if (fName==null)throw new NoFormateFoundException("Formate="+eMathodArr[1]+" not configured");
            else {
                 //EnrchMain.commonConstantsMap;
                String[] formatSpecificXmlTagsArr=EnrchMain.fromatSpecificTagsMap.get(fName).split(",");
              //split based on =
                String [] subFNameArr=formatSpecificXmlTagsArr[0].split("=");

                String [] messageTagColumnArr=subFNameArr[1].split("\\|\\|");

            //Start Preparing MessageHeader
                subXml1+="<"+subFNameArr[0]+">";

                //add constants
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Instant instant = timestamp.toInstant();

                    subXml1 +="<version>"+ EnrchMain.commonConstantsMap.get("version")+"</version>";
                    subXml1 +="<messageTimestamp>"+instant+"</messageTimestamp>";
                    subXml1 +="<sourceSystem>"+ EnrchMain.commonConstantsMap.get("sourceSystem")+"</sourceSystem>";

                for (String tc:messageTagColumnArr){
                    String tcArr[]=tc.split(":");
                if (tcArr[1].equals(""))
                {
                    subXml1+="</"+tcArr[0]+">";
                }else {
                    subXml1+="<"+tcArr[0]+">"+resultSet.getString(tcArr[1])+"</"+tcArr[0]+">";
                }
                }
                subXml1+="</"+subFNameArr[0]+">";
                //System.out.println(subXml1);
                //End Preparing MessageHeader


               // String [] reqHeadTagColumnArr=subFNameArr[1].split("\\|\\|");
                //Start Preparing RequestHeader
                String[] reqHeadNameArr=formatSpecificXmlTagsArr[1].split("=");
                String [] reqHeaderTagColumnArr=reqHeadNameArr[1].split("\\|\\|");
                subXml3+="<"+reqHeadNameArr[0]+">";
                for (String tc:reqHeaderTagColumnArr){
                    String tcArr[]=tc.split(":");
                    String tag=tcArr[0];
                    String column=tcArr[1];
               //     subXml1+="<"+tcArr[0]+">"+resultSet.getString(tcArr[1])+"</"+tcArr[0]+">";
                    if (column.equals("\"\""))
                    {
                        subXml3+="<"+tag+"/>";
                    }else {
                        subXml3+="<"+tag+">"+resultSet.getString(column)+"</"+tag+">";
                    }
                }
                subXml3+="</"+reqHeadNameArr[0]+">";
                //System.out.println(subXml3);
                //End Preparing RequestHeader

                //Start preparing subXml4

                String[] retrievPODetailsArr=formatSpecificXmlTagsArr[2].split("=");
                String [] retrievPODetailsTagColumnArr=retrievPODetailsArr[1].split("\\|\\|");
                subXml4+="<"+retrievPODetailsArr[0]+">";
                for (String tc:retrievPODetailsTagColumnArr){
                    String tcArr[]=tc.split(":");
                    String tag=tcArr[0];
                    String column=tcArr[1];
                    subXml4+="<"+tag+">"+resultSet.getString(column)+"</"+tag+">";
                }
                subXml4+="</"+retrievPODetailsArr[0]+">";

                //End preparing subXml4

            }
                subXml2+="<encrypted>"+EnrchMain.commonConstantsMap.get("encrypted")+"</encrypted>"+subXml3+subXml4+"</messageRequestBody>";
                xmlData+=subXml1+subXml2+"</message>";
                System.out.println("Complete XML:=>\n"+xmlData+"\n\n");
            }
        }catch (Exception e){
                e.printStackTrace();
        }

return xmlData;
    }
}
