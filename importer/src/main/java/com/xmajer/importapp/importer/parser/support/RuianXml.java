package com.xmajer.importapp.importer.parser.support;

import javax.xml.namespace.QName;

public final class RuianXml {

    private RuianXml() {}

    public static final String VF =
            "urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1";

    public static final String OBI =
            "urn:cz:isvs:ruian:schemas:ObecIntTypy:v1";

    public static final String COI =
            "urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1";

    public static final String OKI =
            "urn:cz:isvs:ruian:schemas:OkresIntTypy:v1";

    public static final String PUI =
            "urn:cz:isvs:ruian:schemas:PouIntTypy:v1";

    public static final String COM =
            "urn:cz:isvs:ruian:schemas:ComTypy:v1";

    public static final String GML =
            "http://www.opengis.net/gml/3.2";

    public static final QName MUNICIPALITY =
            new QName(VF, "Obec");

    public static final QName MUNICIPALITY_PART =
            new QName(VF, "CastObce");

    public static final QName MUNICIPALITY_CODE =
            new QName(OBI, "Kod");

    public static final QName MUNICIPALITY_NAME =
            new QName(OBI, "Nazev");

    public static final QName MUNICIPALITY_STATUS_CODE =
            new QName(OBI, "StatusKod");

    public static final QName MUNICIPALITY_DISTRICT =
            new QName(OBI, "Okres");

    public static final QName MUNICIPALITY_POU =
            new QName(OBI, "Pou");

    public static final QName MUNICIPALITY_VALID_FROM =
            new QName(OBI, "PlatiOd");

    public static final QName MUNICIPALITY_TRANSACTION_ID =
            new QName(OBI, "IdTransakce");

    public static final QName MUNICIPALITY_GLOBAL_CHANGE_PROPOSAL_ID =
            new QName(OBI, "GlobalniIdNavrhuZmeny");

    public static final QName MUNICIPALITY_GRAMMATICAL_CASES =
            new QName(OBI, "MluvnickeCharakteristiky");

    public static final QName MUNICIPALITY_NUTS_LAU =
            new QName(OBI, "NutsLau");

    public static final QName PART_CODE =
            new QName(COI, "Kod");

    public static final QName PART_NAME =
            new QName(COI, "Nazev");

    public static final QName PART_MUNICIPALITY =
            new QName(COI, "Obec");

    public static final QName DISTRICT_CODE =
            new QName(OKI, "Kod");

    public static final QName POU_CODE =
            new QName(PUI, "Kod");

    public static final QName GRAMMATICAL_CASE_2 =
            new QName(COM, "Pad2");

    public static final QName GRAMMATICAL_CASE_3 =
            new QName(COM, "Pad3");

    public static final QName GRAMMATICAL_CASE_4 =
            new QName(COM, "Pad4");

    public static final QName GRAMMATICAL_CASE_6 =
            new QName(COM, "Pad6");

    public static final QName GRAMMATICAL_CASE_7 =
            new QName(COM, "Pad7");
}
