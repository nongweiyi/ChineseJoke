package com.example.chinesejoke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomTextView(Context context) {
		super(context);

	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		StringBuffer  buf=new StringBuffer();
		buf.append("    ");
		for(int index=0;index<text.hashCode();index++){
			if("\n".equals(text.charAt(index))){
				buf.append("    ");
			}
			buf.append(text.charAt(index));
		}
		
		super.setText(buf, type);
	}

	
}
