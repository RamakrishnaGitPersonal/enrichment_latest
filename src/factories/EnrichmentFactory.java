package factories;

import db.interfaces.EnrchConverter;
import db.utility.XmlConverter;

public class EnrichmentFactory {

    public EnrchConverter getConverter(String type){
        if (type==null) return  null;
        if (type.equals("XmlConverter")){
            return new XmlConverter();
        }
        return null;

    }
}
