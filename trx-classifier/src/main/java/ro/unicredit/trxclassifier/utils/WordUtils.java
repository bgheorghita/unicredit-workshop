package ro.unicredit.trxclassifier.utils;

public class WordUtils {
    public static boolean matchWords(String value, String token) {
        if(value.equals(token)) {
            return true;
        }
        String cleanValue = value.replaceAll("[\\s-,.*]", "").toLowerCase();
        String cleanToken = token.replaceAll("[\\s-,.*]", "").toLowerCase();
        if(cleanValue.equals(cleanToken))
            return true;
        return false;
    }
}
