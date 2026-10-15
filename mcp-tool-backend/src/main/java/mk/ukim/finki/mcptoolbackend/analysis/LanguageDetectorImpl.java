package mk.ukim.finki.mcptoolbackend.analysis;

import org.springframework.stereotype.Component;

@Component
public class LanguageDetectorImpl implements LanguageDetector{

    private static final String MK_LETTERS = "ѓќѕљњџјЃЌЅЉЊЏЈ";
    private static final double NO_MK_LETTERS_PENALTY = 0.5;

    @Override
    public double macedonianConfidence(String text) {
        if (text == null || text.isBlank())
            return 0.0;

        long letters = 0;
        long cyrillic = 0;

        for (char c : text.toCharArray()){
            if (Character.isLetter(c)){
                letters++;
                if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CYRILLIC){
                    cyrillic++;
                }
            }
        }

        if (letters == 0)
            return 0.0;

        double ratio = (double) cyrillic / letters;
        boolean hasMKLetters = text.chars().anyMatch(c -> MK_LETTERS.indexOf(c) >= 0);

        if (hasMKLetters)
            return ratio;
        else
            return ratio * NO_MK_LETTERS_PENALTY;
    }
}
