package redoscheckr;

import extension.helpers.ConvertUtil;
import extension.helpers.HttpMessageWapper.ContentMimeType;
import extension.helpers.SmartCodec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author isayan
 */
public class MatchPattern {

    public enum Type {
        CLIENT_SIDE("Client Side"),
        SERVER_SIDE("Server Side");

        private String ident = "";

        Type(String ident) {
            this.ident = ident;
        }

        @Override
        public String toString() {
            return ident;
        }

    };

    private final Pattern pattern;

    private final ContentMimeType contentType;

    private final MatchPattern.Type type;

    public MatchPattern(Pattern pattern, ContentMimeType contentType, MatchPattern.Type type) {
        this.pattern = pattern;
        this.contentType = contentType;
        this.type = type;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public Matcher getMatcher(CharSequence value) {
        return this.pattern.matcher(value);
    }

    public ContentMimeType getContentType() {
        return this.contentType;
    }

    public MatchPattern.Type getType() {
        return this.type;
    }

    public static String toDecode(String value, ContentMimeType contentType) {
        String decodeValue = value;
        switch (contentType) {
            case JAVA_SCRIPT:
                decodeValue = ConvertUtil.decodeJsLangQuote(value, false);
                break;
            case JSON:
                decodeValue = ConvertUtil.decodeJsonLiteral(value, false);
                break;
            case HTML:
            case XML:
                decodeValue = SmartCodec.toHtmlDecode(value, SmartCodec.ENCODE_PATTERN_ALPHANUM);
                break;
        }
        return decodeValue;
    }

}
