package cc.seedland.foundation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.WeakHashMap;

import cc.seedland.oa.foundation.R;

/**
 * author shibo
 * date 2018/4/23
 * description
 */
public class SLTextView extends AppCompatTextView {

    public final static int FONT_TYPE_EN_BOLD = 0;
    public final static int FONT_TYPE_EN_LIGHT = 1;
    public final static int FONT_TYPE_EN_REGULAR = 2;
    public final static int FONT_TYPE_EN_SECONDARY = 3;
    public final static int FONT_TYPE_ZH_PRIMARY = 4;
    public final static int FONT_TYPE_ZH_SECONDARY = 5;
    private static WeakHashMap<Integer, Typeface> sIconFontMap = new WeakHashMap<>();
    private int mFontType;

    public SLTextView(Context context) {
        this(context, null);
    }

    public SLTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SLTextView, defStyleAttr, 0);
            mFontType = a.getInteger(R.styleable.SLTextView_fontStyle, 0);
            a.recycle();
        }
    }

    public static Typeface getOrCreateTypeFace(Context context, int fontType) {
        if (null == sIconFontMap.get(fontType)) {
            try {
                Typeface iconFont = null;
                switch (fontType) {
                    case FONT_TYPE_EN_BOLD:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/DIN-Bold.otf");
                        break;
                    case FONT_TYPE_EN_LIGHT:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/DIN_Light.otf");
                        break;
                    case FONT_TYPE_EN_REGULAR:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/DIN_Regular.otf");
                        break;
                    case FONT_TYPE_EN_SECONDARY:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/IBMPlexMono-Regular.ttf");
                        break;
                    case FONT_TYPE_ZH_PRIMARY:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/TengXiang-W2.ttf");
                        break;
                    case FONT_TYPE_ZH_SECONDARY:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/DianHei.otf");
                        break;
                }
                if (null != iconFont) {
                    sIconFontMap.put(fontType, iconFont);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sIconFontMap.get(fontType);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setTypeface(getTypeface());
        this.setIncludeFontPadding(false);
    }

    @Override
    public Typeface getTypeface() {
        return getOrCreateTypeFace(getContext(), mFontType);
    }
}
