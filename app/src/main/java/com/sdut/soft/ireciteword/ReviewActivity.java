package com.sdut.soft.ireciteword;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.reviewWord.WordOptions;
import com.sdut.soft.ireciteword.reviewWord.WordReciteService;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity{
    List<Integer> list = Arrays.asList(R.id.btn_c0, R.id.btn_c1, R.id.btn_c2, R.id.btn_c3);
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_c0)
    Button btnC0;
    @BindView(R.id.btn_c1)
    Button btnC1;
    @BindView(R.id.btn_c2)
    Button btnC2;
    @BindView(R.id.btn_c3)
    Button btnC3;
    WordReciteService service ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        initView();
    }
    void initView(){
        service = new WordReciteService(this);
        setOptions();
    }

    private void setOptions() {
        WordOptions options = service.getNext();
        if( null == options ) {
            new AlertDialog.Builder(this)
                    .setTitle("下一步")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("是否退出复习")
                    .setPositiveButton(R.string.review_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.review_more, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
                            initView();
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

            return;
        }
        List<Word> words = options.getOptions();
        tvHint.setText(options.wordMark());
        btnC0.setText(words.get(0).getKey());
        btnC1.setText(words.get(1).getKey());
        btnC2.setText(words.get(2).getKey());
        btnC3.setText(words.get(3).getKey());
    }

    public void save() {
        UserService userService = new UserService(ReviewActivity.this);
        User user = userService.currentUser();
        user.setRvindex(user.getRvindex()+user.getPerday());
        userService.commitProgress(user);
        setResult(Const.REVIEW_FLAG);
        ReviewActivity.this.finish();
    }
    @OnClick(value = {R.id.btn_c0, R.id.btn_c1, R.id.btn_c2, R.id.btn_c3})
    public void choose(View v) {
        Integer choose = list.indexOf(v.getId());
        service.choose(choose);
        int cnt = service.getCnt();
        setOptions();
    }

}

