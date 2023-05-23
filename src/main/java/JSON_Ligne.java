import com.google.gson.JsonObject;

import java.util.List;

class JSON_Ligne {
    String id_line;
    String name_line;
    String shortname_line;
    String transportmode;
    String transportsubmode;
    String type;
    String operatorref;
    String operatorname;
    String additionaloperators;
    String networkname;
    String colourweb_hexa;
    String textcolourweb_hexa;
    String colourprint_cmjn;
    String textcolourprint_hexa;
    String accessibility;
    String audiblesigns_available;
    String visualsigns_available;
    String id_groupoflines;
    String shortname_groupoflines;
    String notice_title;
    String notice_text;
    Picto picto;
    String valid_fromdate;
    String valid_todate;
    String status;
    String privatecode;
}

class Picto {
    boolean thumbnail;
    String filename;
    int width;
    String format;
    String etag;
    String mimetype;
    String id;
    String last_synchronized;
    int height;
}
