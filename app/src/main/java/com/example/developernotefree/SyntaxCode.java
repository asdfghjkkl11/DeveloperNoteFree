package com.example.developernotefree;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * SyntaxCode. paint color at edittext.
 * it customized from Syntax-View-Android
 * orginal is made by Badranh.
 * https://github.com/Badranh/Syntax-View-Android
 */
public class SyntaxCode {
    private EditText code;
    private SyntaxHighlighter keywords = new SyntaxHighlighter(
            Pattern.compile(
                    "\\b(include|package|transient|strictfp|void|char|short|int|long|double|float|const|static|volatile|byte|boolean|class|interface|native|private|protected|public|final|abstract|synchronized|enum|instanceof|assert|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|extends|implements|import|true|false|null)\\b"));
    private SyntaxHighlighter annotations = new SyntaxHighlighter(
            Pattern.compile(
                    "@Override|@Callsuper|@Nullable|@Suppress|@SuppressLint|super"));
    private SyntaxHighlighter numbers = new SyntaxHighlighter(
            Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)")
    );
    private SyntaxHighlighter special = new SyntaxHighlighter(
            Pattern.compile("[;#]")
    );
    private SyntaxHighlighter printStatments = new SyntaxHighlighter(
            Pattern.compile("\"(.+?)\"")
    );
    private SyntaxHighlighter[] schemes = {keywords, numbers, special, printStatments, annotations};
    public SyntaxCode(Context context,EditText editText) {
        code=editText;
        initialize(context, "#2b2b2b", "#cc7832", "#4a85a3", "#cc7832", "#6a8759","#1932f3");
    }
    public SyntaxCode(Context context) {
        initialize(context, "#2b2b2b", "#cc7832", "#4a85a3", "#cc7832", "#6a8759","#1932f3");
    }
    public SyntaxCode(Context context, AttributeSet attrs) {
        initialize(context, "#2b2b2b", "#cc7832", "#4a85a3", "#cc7832", "#6a8759","#1932f3");
    }
    public SyntaxCode(Context context, String BackgroundColor, String keywordsColor, String NumberColor, String specialCharColors, String printStatmentsColor, String annotationsColor) {
        initialize(context, BackgroundColor, keywordsColor, NumberColor, specialCharColors, printStatmentsColor, annotationsColor);
    }
    private void initialize(Context context, String BackgroundColor, String keywordsColor, String NumberColor, String specialCharColors, String printStatmentsColor, String annotationsColor) {
        setBgColor(BackgroundColor);
        setKeywordsColor(keywordsColor);
        setNumbersColor(NumberColor);
        setSpecialCharsColor(specialCharColors);
        setPrintStatmentsColor(printStatmentsColor);
        setAnnotationsColor(annotationsColor);
    }
    public void paint(Editable s){
        removeSpans(s, ForegroundColorSpan.class);
        for (SyntaxHighlighter scheme : schemes) {
            for (Matcher m = scheme.pattern.matcher(s); m.find(); ) {
                s.setSpan(new ForegroundColorSpan(scheme.color),
                        m.start(),
                        m.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    //remove old highlighting
    public void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
        CharacterStyle[] spans = e.getSpans(0, e.length(), type);
        for (CharacterStyle span : spans) {
            e.removeSpan(span);
        }
    }
    public void checkColor(String color) throws Error {
        color = color.trim();
        if (!color.contains("#")) {
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if (TextUtils.isEmpty(color)) {
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if (color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
    }
    //the user will be able to change color of the view as he wishes
    public void setBgColor(String color) {
        checkColor(color);
        code.setBackgroundColor(Color.parseColor(color));
    }

    public void setKeywordsColor(String color) {
        checkColor(color);
        keywords.setColor(color);
    }

    public void setNumbersColor(String color) {
        checkColor(color);
        numbers.setColor(color);
    }

    public void setSpecialCharsColor(String color) {
        checkColor(color);
        special.setColor(color);
    }

    public void setCodeTextColor(String color) {
        checkColor(color);
        code.setTextColor(Color.parseColor(color));
    }

    public void setAnnotationsColor(String color) {
        checkColor(color);
        annotations.setColor(color);
    }

    public void setPrintStatmentsColor(String color) {
        checkColor(color);
        printStatments.setColor(color);
    }
    public void setFont(Typeface tf) {
        code.setTypeface(tf);
    }
    private boolean checkValidity(String s) {
        Stack stackCheck = new Stack();
        char[] valid = s.toCharArray();
        for (int i = 0; i < valid.length; i++) {
            if (valid[i] == '{' || valid[i] == '(') {
                stackCheck.push((valid[i]));
            }
            if (valid[i] == '}' || valid[i] == ')') {
                if (stackCheck.empty()) {
                    return false;
                } else {
                    if (!matchPair((char) stackCheck.peek(), valid[i])) {
                        return false;
                    }
                    stackCheck.pop();
                }
            }
        }
        if (stackCheck.size() == 1) {
            return false;
        }
        return true;
    }

    private boolean matchPair(char c1, char c2) {
        if (c1 == '(' && c2 == ')')
            return true;
        else if (c1 == '{' && c2 == '}')
            return true;
        else if (c1 == '[' && c2 == ']')
            return true;
        else
            return false;
    }

    public boolean checkMyCode() {
        return checkValidity(code.getText().toString());
    }
}
