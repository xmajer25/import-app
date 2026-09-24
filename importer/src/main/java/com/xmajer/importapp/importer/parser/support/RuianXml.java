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

    public static final QName MUNICIPALITY =
            new QName(VF, "Obec");

    public static final QName MUNICIPALITY_PART =
            new QName(VF, "CastObce");

    public static final QName MUNICIPALITY_CODE =
            new QName(OBI, "Kod");

    public static final QName MUNICIPALITY_NAME =
            new QName(OBI, "Nazev");

    public static final QName PART_CODE =
            new QName(COI, "Kod");

    public static final QName PART_NAME =
            new QName(COI, "Nazev");

    public static final QName PART_MUNICIPALITY =
            new QName(COI, "Obec");
}
