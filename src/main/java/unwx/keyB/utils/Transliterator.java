package unwx.keyB.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Transliterator {

    public String transliterate(String s){
        if (isValidToTransliterate(s)){
            return _transliterate(s);
        }
        else throw new IllegalArgumentException();
    }


    private String _transliterate(String s) {
        String t = s.trim();
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
                'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с',
                'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы',
                'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е',
                'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О',
                'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
                'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c',
                'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1',
                '2', '3', '4', '5', '6', '7', '8', '9'};

        String[] abcLat = {"-", "a", "b", "v", "g", "d", "e", "e", "zh", "z",
                "i", "y", "k", "l", "m", "n", "o", "p", "r", "s",
                "t", "u", "f", "h", "ts", "ch", "sh", "sch", "",
                "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D",
                "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N",
                "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch",
                "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b",
                "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < t.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (t.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    private boolean isValidToTransliterate(@Nullable String s){
        if (s == null)
            return false;
        return isStringCorrect(s);
    }


    /**
     * @regex
     * "^([ a-zA-Zа-ёА-ЯЁїЇіІ]+)$"
     * looking for latin, cyrillic characters.
     * Does not allow characters of genus !@#$%... etc...
     */
    private boolean isStringCorrect(@NotNull String s) {
        Pattern pattern = Pattern.compile("^([ a-zA-Zа-ёА-ЯЁїЇіІ1-9]+)$");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

}
