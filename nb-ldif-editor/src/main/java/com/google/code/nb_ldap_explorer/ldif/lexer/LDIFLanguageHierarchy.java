
package com.google.code.nb_ldap_explorer.ldif.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.google.code.nb_ldap_explorer.ldif.javacclexer.LDIF_ParserConstants.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


public class LDIFLanguageHierarchy extends LanguageHierarchy<LDIFTokenId>{
    private static List<LDIFTokenId> tokens;
    private static Map<Integer,LDIFTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<LDIFTokenId>asList(new LDIFTokenId[] {
          new LDIFTokenId("EOF", "whitespace", EOF),
          new LDIFTokenId("ALPHA", "character", ALPHA),
          new LDIFTokenId("DIGIT", "character", DIGIT),
          new LDIFTokenId("UTF8_1", "character", UTF8_1),
          new LDIFTokenId("UTF8_2", "character", UTF8_2),
          new LDIFTokenId("UTF8_3", "character", UTF8_3),
          new LDIFTokenId("UTF8_4", "character", UTF8_4),
          new LDIFTokenId("UTF8_5", "character", UTF8_5),
          new LDIFTokenId("UTF8_6", "character", UTF8_6),
          new LDIFTokenId("SAFE_CHAR", "character", SAFE_CHAR),
          new LDIFTokenId("SAFE_INIT_CHAR", "character", SAFE_INIT_CHAR),
          new LDIFTokenId("SAFE_STRING", "character", SAFE_STRING),
          new LDIFTokenId("UTF8_CHAR", "character", UTF8_CHAR),
          new LDIFTokenId("UTF8_STRING", "character", UTF8_STRING),
          new LDIFTokenId("BASE64_CHAR", "character", BASE64_CHAR),
          new LDIFTokenId("BASE64_STRING", "character", BASE64_STRING),
          new LDIFTokenId("BASE64_UTF8_STRING", "character", BASE64_UTF8_STRING),
          new LDIFTokenId("DNVALUESEP", "seperator", DNVALUESEP),
          new LDIFTokenId("DNNAMECOMPONENTSEP", "seperator", DNNAMECOMPONENTSEP),
          new LDIFTokenId("NCATTRIBUTETYPEVALUESEP", "seperator", NCATTRIBUTETYPEVALUESEP),
          new LDIFTokenId("LDIF_COMMENT", "comment", LDIF_COMMENT),
          new LDIFTokenId("EOL", "whitespace", EOL),
          new LDIFTokenId("SPACE", "whitespace", SPACE),
          new LDIFTokenId("VERSION", "keyword", VERSION),
          new LDIFTokenId("DN_START", "keyword", DN_START),
          new LDIFTokenId("CHANGETYPE", "keyword", CHANGETYPE),
          new LDIFTokenId("CONTROL", "keyword", CONTROL),
          new LDIFTokenId("CONTROL_CRITICALITY", "value", CONTROL_CRITICALITY),
          new LDIFTokenId("ADD", "keyword", ADD),
          new LDIFTokenId("MODIFY", "keyword", MODIFY),
          new LDIFTokenId("DELETE", "keyword", DELETE),
          new LDIFTokenId("REPLACE", "keyword", REPLACE),
          new LDIFTokenId("MODRDN", "keyword", MODRDN),
          new LDIFTokenId("NEWRDN", "keyword", NEWRDN),
          new LDIFTokenId("DELETEOLDRDN", "keyword", DELETEOLDRDN),
          new LDIFTokenId("NEWSUPERIOR", "keyword", NEWSUPERIOR),
          new LDIFTokenId("DN64_START", "keyword", DN64_START),
          new LDIFTokenId("ATTVAL_SEP", "seperator", ATTVAL_SEP),
          new LDIFTokenId("ATTVAL_SEP_64", "seperator", ATTVAL_SEP_64),
          new LDIFTokenId("ATTVAL_SEP_URL", "seperator", ATTVAL_SEP_URL),
          new LDIFTokenId("END_OF_MOD", "keyword", END_OF_MOD),
          new LDIFTokenId("OPTION_SEP", "seperator", OPTION_SEP),
          new LDIFTokenId("ATTRIBUTE_TYPE_STRING", "attribute", ATTRIBUTE_TYPE_STRING),
          new LDIFTokenId("LDAP_OID", "attribute", LDAP_OID),
          new LDIFTokenId("DN", "value", DN),
          new LDIFTokenId("VERSION_NUM", "value", VERSION_NUM),
          new LDIFTokenId("DELETEOLDRDN_YESNO", "value", DELETEOLDRDN_YESNO),
          new LDIFTokenId("ATTVALUE", "value", ATTVALUE),
          new LDIFTokenId("BASE64_ATTVALUE", "value", BASE64_ATTVALUE),
          new LDIFTokenId("URL", "value", URL),
          new LDIFTokenId("DN64_VALUE", "value", DN64_VALUE),
          new LDIFTokenId("UNKNOWN", "unknown", DN64_VALUE+1),
        });
        idToToken = new HashMap<Integer, LDIFTokenId>();
        for(LDIFTokenId token: tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized LDIFTokenId getToken(int id) {
        if(idToToken == null) init();
        return idToToken.get(id);
    }

    @Override
    protected Collection<LDIFTokenId> createTokenIds() {
        if(tokens == null)
            init();
        return tokens;
    }

    @Override
    protected Lexer<LDIFTokenId> createLexer(LexerRestartInfo<LDIFTokenId> lri) {
        return new LDIFLexer(lri);
    }

    @Override
    protected String mimeType() {
        return "text/ldif";
    }

}
