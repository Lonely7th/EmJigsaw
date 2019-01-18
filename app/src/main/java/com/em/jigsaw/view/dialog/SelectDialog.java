package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SelectDialogAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Time ： 2018/12/17 0017 .
 * Author ： JN Zhang .
 * Description ：选择弹窗 .
 */
public class SelectDialog extends Dialog {
    private Context context;
    private ListView listView;
    private Display display;
    private OnSelectListener onSelectListener = null;

    private List<String> mlist = new ArrayList<>();

    public SelectDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SelectDialog(Context context, List<String> mlist, final OnSelectListener onSelectListener) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        this.mlist.addAll(mlist);
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
        listView = findViewById(R.id.list_view);
        SelectDialogAdapter selectDialogAdapter = new SelectDialogAdapter(mlist, context);
        listView.setAdapter(selectDialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onSelectListener != null){
                    onSelectListener.onItemSelect(view,position,id);
                }
                dismiss();
            }
        });
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

//        getWindow().setGravity(Gravity.BOTTOM);
//
//        WindowManager windowManager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        display = windowManager.getDefaultDisplay();
//        findViewById(R.id.bg_dialog_select).setLayoutParams(new FrameLayout.LayoutParams((int) (display
//                .getWidth() * 1.0), LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public interface OnSelectListener{
        void onItemSelect(View view, int position, long id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
