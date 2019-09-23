package main;

import db.interfaces.EnrchConverter;
import db.utility.XmlConverter;
import factories.EnrichmentFactory;
import helper.EnrichHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnrchMain {
   public static Map<String,String> formateIntMap;
    public static Map<String,String> commonConstantsMap;
    public static Map<String,String> fromatSpecificTagsMap;


    public static void main(String arg[]){
        EnrchMain enrchMain=new EnrchMain();
        enrchMain.init();
        EnrichmentFactory efactory=new EnrichmentFactory();

        EnrchConverter converter =efactory.getConverter("XmlConverter");
        converter.convert();

    }

    private void init(){
        try {
            Properties prop=new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("resources/enrich_config.properties"));

            formateIntMap= EnrichHelper.createInterfaceMap(prop);
             commonConstantsMap=EnrichHelper.createCommonConstantsMap(prop);
             fromatSpecificTagsMap=EnrichHelper.createFormatSpecificTagsMap(prop);
            System.out.println("Initialization Successfully Completed, readyFor Action..!");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
