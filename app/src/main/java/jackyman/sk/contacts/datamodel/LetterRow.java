package jackyman.sk.contacts.datamodel;

import jackyman.sk.contacts.interfaces.RowType;

/**
 * Created by jakubcervenak on 30/04/18.
 */

public class LetterRow implements RowType {
    private String letter;

    public LetterRow(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }
}
