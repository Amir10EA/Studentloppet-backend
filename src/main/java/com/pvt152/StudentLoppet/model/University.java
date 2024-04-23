package com.pvt152.StudentLoppet.model;

public enum University {
    UPPSALA_UNIVERSITET("Uppsala universitet"),
    LUNDS_UNIVERSITET("Lunds universitet"),
    GÖTEBORGS_UNIVERSITET("Göteborgs universitet"),
    STOCKHOLMS_UNIVERSITET("Stockholms universitet"),
    UMEA_UNIVERSITET("Umeå universitet"),
    LINKOPINGS_UNIVERSITET("Linköpings universitet"),
    KAROLINSKA_INSTITUTET("Karolinska institutet"),
    KUNGLIGA_TEKNISKA_HOGSKOLAN("Kungliga Tekniska högskolan"),
    LULEA_TEKNISKA_UNIVERSITET("Luleå tekniska universitet"),
    KARLSTADS_UNIVERSITET("Karlstads universitet"),
    LINNEUNIVERSITETET("Linnéuniversitetet"),
    OREBRO_UNIVERSITET("Örebro universitet"),
    MITTUNIVERSITETET("Mittuniversitetet"),
    MALMO_UNIVERSITET("Malmö universitet"),
    MALARDALENS_UNIVERSITET("Mälardalens universitet"),
    SVERIGES_LANTBRUKSUNIVERSITET("Sveriges lantbruksuniversitet"),
    BLEKINGE_TEKNISKA_HOGSKOLA("Blekinge tekniska högskola"),
    GYMNASTIK_OCH_IDROTTSHOGSKOLAN("Gymnastik- och idrottshögskolan"),
    HOGSKOLAN_I_BORAS("Högskolan i Borås"),
    HOGSKOLAN_I_HALMSTAD("Högskolan i Halmstad"),
    HOGSKOLAN_I_SKOVDE("Högskolan i Skövde"),
    HOGSKOLAN_KRISTIANSTAD("Högskolan Kristianstad"),
    HOGSKOLAN_VAST("Högskolan Väst"),
    SODERTORNS_HOGSKOLA("Södertörns högskola"),
    HOGSKOLAN_I_GAVLE("Högskolan i Gävle"),
    HOGSKOLAN_DALARNA("Högskolan Dalarna"),
    FORSVARSHOGSKOLAN("Försvarshögskolan"),
    STOCKHOLMS_KONSTNARLIGA_HOGSKOLA("Stockholms konstnärliga högskola"),
    CHALMERS_TEKNISKA_HOGSKOLA("Chalmers tekniska högskola"),
    HANDELSHOGSKOLAN_I_STOCKHOLM("Handelshögskolan i Stockholm"),
    HOGSKOLAN_I_JONKOPING("Högskolan i Jönköping"),
    MARIE_CEDERSCHIOLD_HOGSKOLA("Marie Cederschiöld högskola"),
    ENSKILDA_HOGSKOLAN_STOCKHOLM("Enskilda Högskolan Stockholm"),
    SOPHIAHEMMET_HOGSKOLA("Sophiahemmet Högskola"),
    BECKMANS_DESIGNHOGSKOLA("Beckmans designhögskola"),
    GAMMELKROPPA_SKOGSSKOLA("Gammelkroppa skogsskola"),
    JOHANNELUNDS_TEOLOGISKA_HOGSKOLA("Johannelunds teologiska högskola"),
    NEWMANINSTITUTET("Newmaninstitutet"),
    RODA_KORSETS_HOGSKOLA("Röda korsets högskola"),
    STOCKHOLMS_MUSIKPEDAGOGISKA_INSTITUT("Stockholms Musikpedagogiska Institut"),
    OREBRO_TEOLOGISKA_HOGSKOLA("Örebro teologiska högskola"),
    NORDISKA_HOGSKOLAN_FOR_FOLKHALSOVETENSKAP("Nordiska högskolan för folkhälsovetenskap"),
    WORLD_MARITIME_UNIVERSITY("World Maritime University"),
    KONSTFACK("Konstfack"),
    KUNGLIGA_KONSTHOGSKOLAN("Kungliga Konsthögskolan"),
    kUNGL_MUSIKHÖGSKOLAN_STOCKHOLM("Kungl. Musikhögskolan i Stockholm");

    private final String displayName;

    University(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}