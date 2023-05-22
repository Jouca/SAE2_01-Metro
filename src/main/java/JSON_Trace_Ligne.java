package main.java;

import java.util.List;

class JSON_Trace_Ligne {
    String type;
    List<Feature> features;
}

class Feature {
    String type;
    GeometryMultiLine geometry;
}

class GeometryMultiLine {
    List<List<Double>> coordinates;
    String type;
    Properties properties;
}

class Properties {
    Geo_point_2d geo_point_2d;
    String idrefliga;
    String idrefligc;
    String indice_lig;
    String res_com;
    String reseau;
    String mode;
    double fer;
    double train;
    double rer;
    double metro;
    double tramway;
    double navette;
    double val;
    String date_mes;
    double idf;
    String extcode;
    double shape_leng;
    String colourweb_hexa;
    String colourprint_cmjn;
    Picto picto;
    String picto_intermediaire;
    String picto_final;
}
