package jp.kyuuki.rensou.android.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.EditText;

public final class Utils {

    private Utils() {}
    
    // EditText に入力できなくする「スペースのみ」のパターン
    private final static Pattern ONLY_SPACE_PATTERN = Pattern.compile("[\\s　]*");

    // EditText に入力を許可されていない「スペースのみ」文字列かどうか？
    // true: 禁止されている文字列, false: 許可されている文字列
    public static boolean isForbiddenOnlySpace(String s) {
        Matcher matcher = ONLY_SPACE_PATTERN.matcher(s);
        return matcher.matches();
    }

    // EditText のスペースのみの入力をなかったことにする
    public static void checkWhiteSpace(EditText t) {
        // 禁止されている文字列だったら、空白に戻す。
        if (isForbiddenOnlySpace(t.getText().toString()) == true) {
            t.setText("");
        }
    }
}
