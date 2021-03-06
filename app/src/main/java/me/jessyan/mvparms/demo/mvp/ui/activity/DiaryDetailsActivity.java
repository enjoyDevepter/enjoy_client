package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDiaryDetailsComponent;
import me.jessyan.mvparms.demo.di.module.DiaryDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.DiaryDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.DiaryDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.HiNestedScrollView;
import me.jessyan.mvparms.demo.mvp.ui.widget.MoneyView;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;
import me.jessyan.mvparms.demo.mvp.ui.widget.emoji.EmotionKeyboard;
import me.jessyan.mvparms.demo.mvp.ui.widget.emoji.EmotionLayout;
import me.jessyan.mvparms.demo.mvp.ui.widget.emoji.IEmotionSelectedListener;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiaryDetailsActivity extends BaseActivity<DiaryDetailsPresenter> implements DiaryDetailsContract.View, View.OnClickListener, TextView.OnEditorActionListener, NestedScrollView.OnScrollChangeListener {
    @BindView(R.id.title_layout)
    View titleLayoutV;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.share)
    View shareV;

    @BindView(R.id.headImage)
    ShapeImageView headImageIV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.follow)
    View followV;
    @BindView(R.id.publishDate)
    TextView publishDateTV;


    @BindView(R.id.goods_info)
    View goodsInfoV;
    @BindView(R.id.goods_image)
    ImageView goodsImageIV;
    @BindView(R.id.goods_name)
    TextView goodsNameTV;
    @BindView(R.id.goods_price)
    MoneyView goodsPriceTV;


    @BindView(R.id.diary_publishDate)
    TextView diaryPublishDateTV;
    @BindView(R.id.index)
    TextView indexTV;
    @BindView(R.id.left)
    ImageView leftIV;
    @BindView(R.id.right)
    ImageView rightIV;
    @BindView(R.id.intro)
    TextView introTV;

    @BindView(R.id.browse_layout)
    View shareNewV;
    @BindView(R.id.browse)
    TextView browseTV;
    @BindView(R.id.comment_layout)
    View commentV;
    @BindView(R.id.comment)
    TextView commentTV;
    @BindView(R.id.praise_layout)
    View praiseV;
    @BindView(R.id.isPraise)
    View isPraiseTV;
    @BindView(R.id.praise)
    TextView praiseTV;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.tabFloat)
    TabLayout tabFloatLayout;
    @BindView(R.id.commentRV)
    RecyclerView commentRV;
    @BindView(R.id.nestedScrollView)
    HiNestedScrollView nestedScrollView;

    @BindView(R.id.content)
    EditText commentET;
    @BindView(R.id.vote_layout)
    View voteLayout;
    @BindView(R.id.vote)
    View voteV;

    @BindView(R.id.elEmotion)
    EmotionLayout mElEmotion;
    @BindView(R.id.emoji_layout)
    View emojiV;
    @BindView(R.id.content_layout)
    View contentLayoutV;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    EmotionKeyboard mEmotionKeyboard;
    private boolean hasLoadedAllItems;
    private DiaryDetailsResponse response;

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerDiaryDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .diaryDetailsModule(new DiaryDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_diary_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("日记详情");
        backV.setOnClickListener(this);
        shareV.setOnClickListener(this);
        followV.setOnClickListener(this);
        goodsInfoV.setOnClickListener(this);
        praiseV.setOnClickListener(this);
        commentV.setOnClickListener(this);
        leftIV.setOnClickListener(this);
        rightIV.setOnClickListener(this);
        voteLayout.setOnClickListener(this);
        commentET.setOnEditorActionListener(this);
        ArmsUtils.configRecyclerView(commentRV, mLayoutManager);
        commentRV.setAdapter(mAdapter);
        tab.addTab(tab.newTab().setText("全部评论"));
        tabFloatLayout.addTab(tabFloatLayout.newTab().setText("全部评论"));
        commentRV.setNestedScrollingEnabled(false);
        commentRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    nestedScrollView.setNeedScroll(true);
                }
            }
        });
        nestedScrollView.setOnScrollChangeListener(this);

        initEmotionKeyboard();
        mElEmotion.attachEditText(commentET);
    }

    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToContent(contentLayoutV);
        mEmotionKeyboard.bindToEmotionButton(emojiV);
        mEmotionKeyboard.bindToEditText(commentET);
        mEmotionKeyboard.setEmotionLayout(mElEmotion);
        mElEmotion.setEmotionSelectedListener(new IEmotionSelectedListener() {
            @Override
            public void onEmojiSelected(String key) {
            }

            @Override
            public void onSend() {
                comment();
            }

        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setLoadedAllItems(boolean has) {
        hasLoadedAllItems = has;
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.share:
                showWX();
                break;
            case R.id.browse_layout:
                showNewWX();
                break;
            case R.id.follow:
                provideCache().put("memberId", response.getMember().getMemberId());
                mPresenter.follow(!followV.isSelected());
                break;
            case R.id.praise_layout:
                provideCache().put("diaryId", response.getDiary().getDiaryId());
                mPresenter.vote(voteV.isSelected() ? false : true);
                break;
            case R.id.goods_info:
                Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                intent.putExtra("goodsId", response.getGoods().getGoodsId());
                intent.putExtra("merchId", response.getGoods().getMerchId());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.vote_layout:
                provideCache().put("diaryId", response.getDiary().getDiaryId());
                mPresenter.vote(voteV.isSelected() ? false : true);
                break;
            case R.id.left:
            case R.id.right:
                if (null != response && response.getDiary().getImageList() != null && response.getDiary().getImageList().size() > 0) {
                    Intent intentOne = new Intent(getActivity(), ImageShowActivity.class);
                    String[] images = new String[response.getDiary().getImageList().size()];
                    for (int i = 0; i < response.getDiary().getImageList().size(); i++) {
                        images[i] = response.getDiary().getImageList().get(i);
                    }
                    intentOne.putExtra("images", images);
                    ArmsUtils.startActivity(intentOne);
                }
                break;
        }
    }


    private void showNewWX() {
        if (null != response && null != response.getDiary()) {
            Diary diary = response.getDiary();
            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
            if (cache.get(KEY_KEEP + "token") == null) {
                ArmsUtils.startActivity(LoginActivity.class);
                return;
            }
            UMWeb web = new UMWeb(diary.getShareUrl());
            web.setTitle(diary.getShareTitle());//标题
            web.setDescription(diary.getShareDesc());
            new ShareAction(this)
                    .withMedia(web)
                    .setCallback(shareListener)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .open();

        }
    }

    private void showWX() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (cache.get(KEY_KEEP + "token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        UMWeb web = new UMWeb(response.getShareUrl());
        web.setTitle(response.getShareTitle());//标题
        web.setDescription(response.getShareDesc());
        new ShareAction(this)
                .withMedia(web)
                .setCallback(shareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .open();
    }

    @Override
    public void comment(boolean success) {
        if (success) {
            commentET.setText("");
        }
    }

    @Override
    public void updateCommentUI(int count) {
        tab.setVisibility(count >= 0 ? View.VISIBLE : View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) commentRV.getLayoutParams();
        layoutParams.height = ArmsUtils.getDimens(getContext(), R.dimen.diary_comment_height) * count;
        commentRV.setLayoutParams(layoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void updateUI(DiaryDetailsResponse response) {
        this.response = response;
        nickNameTV.setText(response.getMember().getNickName());
        followV.setSelected("1".equals(response.getMember().getIsFollow()) ? true : false);
        publishDateTV.setText(response.getDiary().getPublishDate());
        goodsNameTV.setText(response.getGoods().getName());
        goodsPriceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
        diaryPublishDateTV.setText(response.getDiary().getPublishDate());
        indexTV.setText(response.getDiary().getTitle());
        introTV.setText(response.getDiary().getContent());
        browseTV.setText(String.valueOf(response.getDiary().getBrowse()));
        commentTV.setText(String.valueOf(response.getDiary().getComment()));
        isPraiseTV.setSelected("1".equals(response.getDiary().getIsPraise()) ? true : false);
        voteV.setSelected("1".equals(response.getDiary().getIsPraise()) ? true : false);
        praiseTV.setText(String.valueOf(response.getDiary().getPraise()));

        if ("1".equals(response.getDiary().getIsShare())) {
            shareNewV.setOnClickListener(this);
        } else {
            shareNewV.setVisibility(View.GONE);
        }


        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(response.getMember().getHeadImage())
                        .imageView(headImageIV)
                        .isCenterCrop(true)
                        .build());

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(response.getGoods().getImage())
                        .imageView(goodsImageIV)
                        .isCenterCrop(true)
                        .build());

        if (response.getDiary().getImageList() != null && response.getDiary().getImageList().size() > 0) {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url(response.getDiary().getImageList().get(0))
                            .imageView(leftIV)
                            .isCenterCrop(true)
                            .build());
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .isCenterCrop(true)
                            .imageView(leftIV)
                            .build());
        }
        if (response.getDiary().getImageList() != null && response.getDiary().getImageList().size() > 1) {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .isCenterCrop(true)
                            .placeholder(R.drawable.place_holder_img)
                            .url(response.getDiary().getImageList().get(1))
                            .imageView(rightIV)
                            .build());
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .isCenterCrop(true)
                            .imageView(rightIV)
                            .build());
        }
    }

    @Override
    public void updatefollowStatus(boolean follow) {
        followV.setSelected(follow);
    }

    @Override
    public void updateVoteStatus(boolean vote) {
        voteV.setSelected(vote);
        isPraiseTV.setSelected(vote);
        praiseTV.setText(vote ? String.valueOf(Integer.valueOf(praiseTV.getText().toString()) + 1) : String.valueOf(Integer.valueOf(praiseTV.getText().toString()) - 1));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                comment();
                return true;
        }
        return false;
    }

    private void comment() {
        String comment = commentET.getText().toString();
        if (ArmsUtils.isEmpty(comment)) {
            showMessage("请输入评论内容");
            return;
        }
        hintKeyBoard();
        provideCache().put("diaryId", response.getDiary().getDiaryId());
        provideCache().put("content", comment);
        mPresenter.comment();
    }

    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(commentRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int[] location = new int[2];
        tab.getLocationOnScreen(location);
        int[] titleLocation = new int[2];
        titleLayoutV.getLocationInWindow(titleLocation);
        int yPosition = location[1];
        if (yPosition < (titleLayoutV.getHeight() + titleLocation[1])) {
            tabFloatLayout.setVisibility(View.VISIBLE);
        } else {
            tabFloatLayout.setVisibility(View.GONE);
        }
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            if (!hasLoadedAllItems) {
                mPresenter.getDiaryComment(false);
            }
        }
    }
}
