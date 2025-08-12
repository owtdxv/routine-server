package com.codruwh.routine.common;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class TextNormalizer {

    private static final Pattern COMBINING_MARKS = Pattern.compile("\\p{M}+");
    private static final Pattern NOISE_CHARS = Pattern.compile("[\\s\\(\\)\\[\\]\\{\\}\\-_/.,]+");
    private static final int MAX_INPUT_LENGTH = 2000;

    private TextNormalizer() {}

    public static String normForFoodName(String input) {
        if (input == null) return "";
        String s = truncate(input, MAX_INPUT_LENGTH);

        // 1) NFD 분해
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        // 2) 결합기호 제거(악센트 등)
        s = COMBINING_MARKS.matcher(s).replaceAll("");
        // 3) 소문자
        s = s.toLowerCase(Locale.ROOT);
        // 4) 공백/특수문자 제거
        s = NOISE_CHARS.matcher(s).replaceAll("");
        // 5) ★ NFC 재조합(중요: DB 값과 동일한 정규화 형태로 맞춤)
        s = Normalizer.normalize(s, Normalizer.Form.NFC);

        return s;
    }

    public static boolean equalsNormalized(String a, String b) {
        return Objects.equals(normForFoodName(a), normForFoodName(b));
    }

    private static String truncate(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }
}
