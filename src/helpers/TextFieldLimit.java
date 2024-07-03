package helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextFieldLimit extends PlainDocument {
    private int limit;

    public TextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if (str == null) return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, a);
        }
    }
}
