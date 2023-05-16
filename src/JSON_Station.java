import com.google.gson.JsonObject;

import java.util.List;

class JSON_Station {
    Geo_point_2d geo_point_2d;
    Geo_shape geo_shape;
    double codeunique;
    String nom_long;
    String label;
    double id_ref_lda;
    String nom_lda;
    double id_ref_zdl;
    String nom_zdl;
    String idrefliga;
    String idrefligc;
    String res_com;
    String mode;
    double fer;
    double train;
    double rer;
    double metro;
    double tramway;
    double navette;
    double val;
    String terfer;
    String tertrain;
    String terrer;
    String termetro;
    String tertramway;
    String ternavette;
    String terval;
    String exploitant;
    double principal;
    double idf;
    double x;
    double y;
}

class Geo_point_2d {
    double lon;
    double lat;
}

class Geo_shape {
    String type;
    Geometry geometry;
    JsonObject properties;
}

class Geometry {
    List<Double> coordinates;
    String type;
}
