package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.BaseRVOnlyWithPicAdapter;
import com.twinkle.htwinkle.adapter.DetailCommAdapter;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.dialog.DialogInShowBigPic;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Collection;
import com.twinkle.htwinkle.entity.Comment;
import com.twinkle.htwinkle.entity.Focus;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.entity.Praise;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Bmob;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class DetailPostActivity extends BaseActivity implements Bmob.BmobAddPostCommentListener,
        Bmob.BmobPostCommentIncrementListener, Bmob.BmobGetOnePostListener, Bmob.BmobAddPraiseListener,
        Bmob.BmobSearchUserPraiseListener, Bmob.BmobDisablePraiseListener, Bmob.BmobUserFocusListener,
        Bmob.BmobDisableUserFocusListener, Bmob.BmobUserCollectionListener, Bmob.BmobUserDisableCollectionListener,
        Bmob.BmobSearchUserAboutTListener, Bmob.BmobGetCommListByPostListener, Bmob.BmobCommListener {

    private DialogInShowBigPic dialogInShowBigPic;

    private Collection collection;

    private Focus focus;

    private boolean ispraise = false;

    private boolean iscollect = false;

    private boolean isfocus = false;

    private boolean ismepost = false;

    private Post onepost;

    private boolean isrefresh = false;

    private boolean hasUrl = false;

    private MyDialog myDialog;

    private int current = 0;

    private List<Comment> list;

    private DetailCommAdapter adapter;

    private SmartImageView index_cr_iv_header;

    private TextView index_cr_tv_username;

    private TextView index_cr_tv_time;

    private TextView index_cr_tv_lv;

    private TextView detail_tv_text;

    private SmartImageView detail_siv_pic;

    private RecyclerView detail_rv_pic;

    private TextView index_cr_iv_types;

    private TextView index_item_tv_praise;

    private TextView index_item_tv_comment;

    private ImageButton index_item_ib_praise;

    private TextView detail_tv_place;

    private TextView detail_tv_title;

    private TextView detail_tv_newsdate;

    @ViewInject(value = R.id.detail_rv_comment)
    private RecyclerView detail_rv_comment;

    @ViewInject(value = R.id.detail_srl)
    private SwipeRefreshLayout detail_srl;

    @ViewInject(value = R.id.detail_user_comment_siv_logo)
    private SmartImageView detail_user_comment_siv_logo;


    @ViewInject(value = R.id.detail_user_comment_et_text)
    private EditText detail_user_comment_et_text;


    private void onPraiseClick() {
        if (ispraise) {
            Toast.makeText(DetailPostActivity.this, R.string.you_have_praise, Toast.LENGTH_SHORT).show();
        } else {
            Bmob.INSTANCE.setBmobAddPraiseListener(this);
            Bmob.INSTANCE.BmobAddPraise(new Praise(onepost, BmobUser.getCurrentUser(User.class)));
        }
    }

    @Event(value = R.id.detail_user_comment_siv_logo)
    private void onLogoClick(View view) {
        sendComment();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_detail_post;
    }

    @Override
    public Activity setActivity() {
        return DetailPostActivity.this;
    }

    @Override
    public void initView() {

        detail_srl.setRefreshing(true);
        detail_srl.setColorSchemeColors(Color.rgb(47, 223, 189));
        detail_srl.setOnRefreshListener(this::refresh);

        x.image().bind(detail_user_comment_siv_logo, BmobUser.getCurrentUser(User.class).getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
        detail_user_comment_et_text.addTextChangedListener(textWatcher);

        detail_rv_comment.setLayoutManager(new LinearLayoutManager(DetailPostActivity.this));
        adapter = new DetailCommAdapter(R.layout.item_detail_comment, list);
        adapter.addHeaderView(addHeader1());
        detail_rv_comment.setAdapter(adapter);

        refreshPostData();

        index_item_ib_praise.setEnabled(false);


        Bmob.INSTANCE.setBmobSearchUserPraiseListener(this);
        Bmob.INSTANCE.BmobSearchUserPraise(new Praise(onepost, BmobUser.getCurrentUser(User.class)));

        Bmob.INSTANCE.setBmobSearchUserAboutTListener(this);
        Bmob.INSTANCE.BmobSearchUserFocus(new Focus(onepost.getAuthor(), BmobUser.getCurrentUser(User.class)));
        Bmob.INSTANCE.BmobSearchUserCollect(new Collection(onepost, BmobUser.getCurrentUser(User.class)));

        Bmob.INSTANCE.setBmobGetCommListByPostListener(this);
        Bmob.INSTANCE.BmobGetCommListByPost(onepost, current);


    }

    private View addHeader1() {
        View view = getLayoutInflater().inflate(R.layout.activity_detail_content_layout, detail_rv_comment, false);
        index_cr_iv_header = view.findViewById(R.id.index_cr_iv_header);
        index_cr_tv_username = view.findViewById(R.id.index_cr_tv_username);
        index_cr_tv_time = view.findViewById(R.id.index_cr_tv_time);
        index_cr_tv_lv = view.findViewById(R.id.index_cr_tv_lv);
        detail_tv_text = view.findViewById(R.id.detail_tv_text);
        detail_siv_pic = view.findViewById(R.id.detail_siv_pic);
        detail_rv_pic = view.findViewById(R.id.detail_rv_pic);
        index_cr_iv_types = view.findViewById(R.id.index_cr_iv_types);
        index_item_tv_praise = view.findViewById(R.id.index_item_tv_praise);
        index_item_tv_comment = view.findViewById(R.id.index_item_tv_comment);
        index_item_ib_praise = view.findViewById(R.id.index_item_ib_praise);
        detail_tv_place = view.findViewById(R.id.detail_tv_place);
        detail_tv_newsdate = view.findViewById(R.id.detail_tv_newsdate);
        detail_tv_title = view.findViewById(R.id.detail_tv_title);

        index_cr_iv_header.setOnClickListener((e) -> onStartUserRoom(onepost.getAuthor()));

        index_cr_tv_username.setOnClickListener((e) -> onStartUserRoom(onepost.getAuthor()));

        index_item_ib_praise.setOnClickListener((e) -> onPraiseClick());

        detail_siv_pic.setOnClickListener((e) -> {
            if (onepost.getPic() != null && onepost.getPic().size() > 0) {
                onShowDialog(onepost.getPic().get(0));
            }
        });

        return view;

    }


    private void refresh() {

        current = 0;
        isrefresh = true;
        adapter.setEnableLoadMore(false);
        detail_srl.setRefreshing(true);
        Bmob.INSTANCE.BmobGetOnePost(new Post(onepost.getObjectId()));
        Bmob.INSTANCE.BmobGetCommListByPost(onepost, current);

    }

    private void onStartUserRoom(User user) {

        Intent intent = new Intent(DetailPostActivity.this, UserRoomActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);


    }


    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitle(R.string.title_content);

        dialogInShowBigPic = new DialogInShowBigPic(DetailPostActivity.this);

        onepost = (Post) getIntent().getSerializableExtra("post");

        Bmob.INSTANCE.setBmobGetOnePostListener(this);
        Bmob.INSTANCE.BmobGetOnePost(new Post(onepost.getObjectId()));

        Bmob.INSTANCE.updateUserLv(10);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detialmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_collection:
                setCollection();
                return true;
            case R.id.menu_focus:
                setFocus();
                return true;
            case R.id.menu_praise:
                setPraise();
                return true;
            case R.id.menu_look:
                onOpenBrowser();
                return true;
            case R.id.menu_delete:
                Bmob.INSTANCE.BmobCommDelete(onepost);
                Bmob.INSTANCE.setBmobCommListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onOpenBrowser() {

        Uri uri = Uri.parse(onepost.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    private void setFocus() {
        if (isfocus) {

            if (focus == null) return;

            Bmob.INSTANCE.setBmobDisableUserFocusListener(this);
            Bmob.INSTANCE.BmobDisableUserFocus(focus);
        } else {
            Bmob.INSTANCE.setBmobUserFocusListener(this);
            Bmob.INSTANCE.BmobUserFocus(new Focus(onepost.getAuthor(), BmobUser.getCurrentUser(User.class)));

        }
    }

    private void setCollection() {

        if (iscollect) {
            if (collection == null) return;

            Bmob.INSTANCE.setBmobUserDisableCollectionListener(this);
            Bmob.INSTANCE.BmobUserDisableCollection(collection);
        } else {
            Bmob.INSTANCE.setBmobUserCollectionListener(this);
            Bmob.INSTANCE.BmobUserCollection(new Collection(onepost, BmobUser.getCurrentUser(User.class)));

        }
    }

    private void setPraise() {
        if (ispraise) {
            Bmob.INSTANCE.setBmobDisablePraiseListener(this);
            Bmob.INSTANCE.BmobDisablePraise(new Praise(onepost, BmobUser.getCurrentUser(User.class)));
        } else {
            Bmob.INSTANCE.setBmobAddPraiseListener(this);
            Bmob.INSTANCE.BmobAddPraise(new Praise(onepost, BmobUser.getCurrentUser(User.class)));
        }

    }


    private void refreshPostData() {

        hasUrl = !TextUtils.isEmpty(onepost.getUrl());

        invalidateOptionsMenu();

        ismepost = (onepost.getAuthor().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId()));

        invalidateOptionsMenu();


        x.image().bind(index_cr_iv_header, onepost.getAuthor().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        if (!TextUtils.isEmpty(onepost.getTitle())) {
            detail_tv_title.setText(onepost.getTitle());
        }

        if (!TextUtils.isEmpty(onepost.getNewsdate())) {
            detail_tv_newsdate.setText(onepost.getNewsdate());
        }

        index_cr_tv_username.setText(onepost.getAuthor().getNickName());

        index_cr_tv_time.setText(onepost.getCreatedAt());


        index_cr_tv_lv.setText(Utils.INSTANCE.setUserLvByString(onepost.getAuthor()));

        detail_tv_text.setText(Utils.INSTANCE.convertHtmlText(onepost.getContent()));

        index_cr_iv_types.setText(Utils.INSTANCE.getPostTopicInString(onepost));

        detail_tv_place.setText(onepost.getPlace() == null ? "" : onepost.getPlace());

        switch (onepost.getTypes()) {
            case 1:
                detail_rv_pic.setVisibility(View.GONE);
                detail_siv_pic.setVisibility(View.GONE);
                break;
            case 2:
                if (onepost.getPic() != null && onepost.getPic().size() > 1) {
                    detail_siv_pic.setVisibility(View.GONE);
                    detail_rv_pic.setLayoutManager(new GridLayoutManager(DetailPostActivity.this, 3));
                    BaseRVOnlyWithPicAdapter adapter = new BaseRVOnlyWithPicAdapter(R.layout.base_imageview, onepost.getPic());
                    adapter.setOnItemChildClickListener((adapter1, view, position) -> onShowDialog(onepost.getPic().get(position)));
                    detail_rv_pic.setAdapter(adapter);
                }
                break;
            case 3:
                if (onepost.getPic() != null && onepost.getPic().size() == 1) {
                    detail_rv_pic.setVisibility(View.GONE);
                    detail_siv_pic.setImageUrl(onepost.getPic().get(0), R.drawable.logo);
                }
                break;
        }

        if (onepost.getCommentnum() != null && onepost.getCommentnum() > 0) {
            index_item_tv_comment.setText(String.valueOf(onepost.getCommentnum()));
        }

        if (onepost.getLikenum() != null && onepost.getLikenum() > 0) {
            index_item_tv_praise.setText(String.valueOf(onepost.getLikenum()));
        }
    }

    private void onShowDialog(String url) {
        dialogInShowBigPic.setUrl(url);
        dialogInShowBigPic.onShow();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(detail_user_comment_et_text.getText().toString())) {
                x.image().bind(detail_user_comment_siv_logo, BmobUser.getCurrentUser(User.class).getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
            } else {
                detail_user_comment_siv_logo.setImageDrawable(getDrawable(R.drawable.ic_send_black_36dp));
            }
        }
    };


    private void sendComment() {
        if (TextUtils.isEmpty(detail_user_comment_et_text.getText().toString())) {
            detail_user_comment_et_text.setFocusable(true);
            detail_user_comment_et_text.setError(getString(R.string.please_input_content));
            return;
        }

        Comment comment = new Comment();
        comment.setAuthor(BmobUser.getCurrentUser(User.class));
        comment.setContent(detail_user_comment_et_text.getText().toString());
        comment.setPost(onepost);

        myDialog = new MyDialog(DetailPostActivity.this, R.style.AlertDialog);
        myDialog.show();

        Bmob.INSTANCE.setBmobAddPostCommentListener(this);
        Bmob.INSTANCE.BmobAddPostComment(comment);

    }


    @Override
    public void onAddPostCommentListenerSuccess() {
        myDialog.dismiss();
        detail_user_comment_et_text.getText().clear();

        Bmob.INSTANCE.setBmobPostCommentIncrementListener(this);

        Post post = new Post();
        post.setObjectId(onepost.getObjectId());
        post.increment("commentnum");

        Bmob.INSTANCE.BmobPostCommentIncrement(post);

        Bmob.INSTANCE.updateUserLv(10);

    }

    @Override
    public void onAddPostCommentListenerFailure(String text) {
        myDialog.dismiss();
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobPostCommentIncrementSuccess() {
        Toast.makeText(DetailPostActivity.this, R.string.send_comment_success, Toast.LENGTH_SHORT).show();
        refresh();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onBmobPostCommentIncrementFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobGetOnePostSuccess(Post post) {

        detail_srl.setRefreshing(false);
        onepost = post;
        refreshPostData();

    }

    @Override
    public void onBmobGetOnePostFailure(String text) {
        detail_srl.setRefreshing(false);
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobAddPraiseSuccess() {
        Toast.makeText(DetailPostActivity.this, R.string.praise_success, Toast.LENGTH_SHORT).show();
        index_item_ib_praise.setBackgroundResource(R.drawable.ispraise);
        index_item_tv_praise.setText(Utils.INSTANCE.setPraiseFromIntToString(index_item_tv_praise, +1));
        ispraise = true;

    }

    @Override
    public void onBmobAddPraiseFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (ispraise) {
            menu.findItem(R.id.menu_praise).setTitle(R.string.menu_praise_cancel);
        } else {
            menu.findItem(R.id.menu_praise).setTitle(R.string.menu_praise);
        }

        if (iscollect) {
            menu.findItem(R.id.menu_collection).setTitle(R.string.menu_collection_cancel);
        } else {
            menu.findItem(R.id.menu_collection).setTitle(R.string.menu_collection);
        }

        if (isfocus) {
            menu.findItem(R.id.menu_focus).setTitle(R.string.menu_focus_cancel);
        } else {
            menu.findItem(R.id.menu_focus).setTitle(R.string.menu_focus);
        }

        if (hasUrl) {
            menu.findItem(R.id.menu_look).setVisible(true);
        } else {
            menu.findItem(R.id.menu_look).setVisible(false);
        }

        if (ismepost) {
            menu.findItem(R.id.menu_delete).setVisible(true);
        } else {
            menu.findItem(R.id.menu_delete).setVisible(false);
        }

        return true;
    }

    @Override
    public void onBmobSearchUserPraiseSuccess(int size) {

        if (size > 0) {
            ispraise = true;
            invalidateOptionsMenu();
            index_item_ib_praise.setEnabled(true);
            index_item_ib_praise.setBackgroundResource(R.drawable.ispraise);

        } else {
            index_item_ib_praise.setEnabled(true);
            index_item_ib_praise.setBackgroundResource(R.drawable.praise);
        }
    }

    @Override
    public void onBmobSearchUserPraiseFailure(String text) {
        index_item_ib_praise.setEnabled(true);
        index_item_ib_praise.setBackgroundResource(R.drawable.praise);
    }

    @Override
    public void onBmobDisablePraiseSuccess() {
        ispraise = false;
        invalidateOptionsMenu();
        index_item_ib_praise.setEnabled(true);
        index_item_tv_praise.setText(Utils.INSTANCE.setPraiseFromIntToString(index_item_tv_praise, -1));
        index_item_ib_praise.setBackgroundResource(R.drawable.praise);

    }

    @Override
    public void onBmobDisablePraiseFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobUserCollectionSuccess(Collection collection) {
        Toast.makeText(DetailPostActivity.this, R.string.collection_success, Toast.LENGTH_SHORT).show();
        iscollect = true;
        this.collection = collection;
        invalidateOptionsMenu();
    }

    @Override
    public void onBmobUserCollectionFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobUserDisableCollectionSuccess() {
        Toast.makeText(DetailPostActivity.this, R.string.collection_cancel, Toast.LENGTH_SHORT).show();
        iscollect = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onBmobUserDisableCollectionFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobUserFocusSuccess(Focus focus) {
        isfocus = true;
        invalidateOptionsMenu();
        this.focus = focus;
        Toast.makeText(DetailPostActivity.this, R.string.focus_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobUserFocusFaiure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobDisableUserFocusSuccess() {
        isfocus = false;
        invalidateOptionsMenu();
        Toast.makeText(DetailPostActivity.this, R.string.focus_cancel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobDisableUserFocusFailure(String text) {
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public <T extends BmobObject> void onBmobSearchUserAboutTSuccess(T t, List<T> list) {

        if (t instanceof Collection) {
            if (list != null && list.size() > 0) {
                iscollect = true;
                collection = (Collection) list.get(0);
                invalidateOptionsMenu();
            }
        } else if (t instanceof Focus) {
            if (list != null && list.size() > 0) {
                isfocus = true;
                focus = (Focus) list.get(0);
                invalidateOptionsMenu();
            }
        }


    }

    @Override
    public <T extends BmobObject> void onBmobSearchUserAboutTFailure(T t, String text) {

        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBmobGetCommListByPostSuccess(List<Comment> list) {
        if (this.list == null || this.list.size() == 0) {
            this.list = list;
            adapter.setNewData(list);
        } else {
            if (isrefresh) {
                adapter.replaceData(list);
                isrefresh = false;
            } else {
                adapter.addData(list);
            }

        }

        detail_srl.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        current++;

        if (list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void onBmobGetCommListByPostFailure(String text) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobCommSuccess(int flag, Object o) {
        if (flag == 3) {
            Toast.makeText(DetailPostActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
            this.finish();

        }

    }

    @Override
    public void onBmobCommFailure(int flag, Object o, String text) {

        if (flag == 3) {
            Toast.makeText(DetailPostActivity.this, text, Toast.LENGTH_SHORT).show();
        }


    }
}
