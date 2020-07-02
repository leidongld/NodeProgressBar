package com.openld.nodeprogressbar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openld.nodeprogressbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: lllddd
 * created on: 2020/7/1 21:10
 * description:
 */
public class NodeProgressBar extends View {
    // 自定义View宽度
    private int mWidth;
    // 自定义View高度
    private int mHeight;

    // 节点个数
    private int mNodeCount;
    // 节点宽度
    private int mNodeWidth;
    // 节点高度
    private int mNodeHeight;
    // 未到达节点的资源id
    private int mNodeUnreached;
    // 已经到达节点的资源id
    private int mNodeReached;
    // 已完成节点的资源id
    private int mNodeFinished;
    // 失败节点的资源id
    private int mNodeFailed;
    // 节点大小比例，用于处理成功/失败节点比到达/未到达节点大的情况
    private float mNodeRatio;

    // 上方文字是否生效
    private boolean mTopTxtEnable;
    // 上方文字大小
    private int mTopTxtSize;
    // 上方文字颜色
    private int mTopTxtColor;
    // 上方文字距离节点的距离
    private int mTopTxtGap;
    // 上方文字的样式
    private int mTopTxtStyle;

    // 下方文字是否生效
    private boolean mBottomTxtEnable;
    // 下方文字的大小
    private int mBottomTxtSize;
    // 下方文字的颜色
    private int mBottomTxtColor;
    // 下方文字距离节点的距离
    private int mBottomTxtGap;
    // 下方文字的样式
    private int mBottomTxtStyle;

    // 下方提示文字的颜色（失败的节点）
    private int mBottomWarnTxtColor;
    // 相仿提示文字的样式（失败的节点使用）
    private int mBottomWarnTxtStyle;

    // 连接线的宽度
    private int mLineWidth;
    // 已到达的连接线颜色
    private int mReachedLineColor;
    // 未到达的连接线的颜色
    private int mUnreachedLineColor;

    // 节点区域横向宽度
    private int mRegionWidth;

    // 上方文字画笔
    private Paint mPaintTopTxt;
    // 底部文字画笔
    private Paint mPaintBottomTxt;
    // 底部提示文字的画笔
    private Paint mPaintBottomWarnTxt;
    // 节点画笔
    private Paint mPaintNode;
    // 未到达连线画笔
    private Paint mPaintUnreachedLine;
    // 已到达连线画笔
    private Paint mPaintReachedLine;

    // 未到达节点Bitmap
    private Bitmap mNodeUnreachedBitmap;
    // 已到达节点Bitmap
    private Bitmap mNodeReachedBitmap;
    // 失败节点Bitmap
    private Bitmap mNodeFailedBitmap;
    // 已完成节点Bitmap
    private Bitmap mNodeFinishedBitmap;

    // 上方文字的中心坐标列表
    private List<Location> mTopTxtLocationList;
    // 中间节点的中心文字坐标列表
    private List<Location> mNodeLocationList;
    // 下方文字的中心坐标列表
    private List<Location> mBottomTxtLocationList;

    private List<Node> mNodeList = new ArrayList<>();

    public NodeProgressBar(Context context) {
        this(context, null);
    }

    public NodeProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NodeProgressBar);

        mNodeCount = ta.getInt(R.styleable.NodeProgressBar_nodeCount, 0);
        mNodeWidth = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_nodeWidth, 0);
        mNodeHeight = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_nodeHeight, 0);
        mNodeUnreached = ta.getResourceId(R.styleable.NodeProgressBar_nodeUnreached, R.drawable.node_unreached);
        mNodeReached = ta.getResourceId(R.styleable.NodeProgressBar_nodeReached, R.drawable.node_unreached);
        mNodeFinished = ta.getResourceId(R.styleable.NodeProgressBar_nodeFinished, R.drawable.node_unreached);
        mNodeFailed = ta.getResourceId(R.styleable.NodeProgressBar_nodeFailed, R.drawable.node_unreached);
        mNodeRatio = ta.getFloat(R.styleable.NodeProgressBar_nodeRatio, 1.0F);

        mTopTxtEnable = ta.getBoolean(R.styleable.NodeProgressBar_topTxtEnable, false);
        mTopTxtSize = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_topTxtSize, 0);
        mTopTxtColor = ta.getColor(R.styleable.NodeProgressBar_topTxtColor, Color.TRANSPARENT);
        mTopTxtGap = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_topTxtGap, 0);
        mTopTxtStyle = ta.getInteger(R.styleable.NodeProgressBar_topTxtStyle, TxtStyle.BOLD);

        mBottomTxtEnable = ta.getBoolean(R.styleable.NodeProgressBar_bottomTxtEnable, false);
        mBottomTxtSize = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_bottomTxtSize, 0);
        mBottomTxtColor = ta.getColor(R.styleable.NodeProgressBar_bottomTxtColor, Color.TRANSPARENT);
        mBottomTxtGap = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_bottomTxtGap, 0);
        mBottomTxtStyle = ta.getInteger(R.styleable.NodeProgressBar_bottomTxtStyle, TxtStyle.COMMON);

        mBottomWarnTxtColor = ta.getColor(R.styleable.NodeProgressBar_bottomWarnTxtColor, Color.TRANSPARENT);
        mBottomWarnTxtStyle = ta.getInteger(R.styleable.NodeProgressBar_bottomWarnTxtStyle, TxtStyle.COMMON);

        mLineWidth = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_lineWidth, 0);
        mReachedLineColor = ta.getColor(R.styleable.NodeProgressBar_reachedLineColor, Color.TRANSPARENT);
        mUnreachedLineColor = ta.getColor(R.styleable.NodeProgressBar_unreachedLineColor, Color.TRANSPARENT);

        mRegionWidth = ta.getDimensionPixelSize(R.styleable.NodeProgressBar_regionWidth, 0);

        ta.recycle();

        configBitmaps(context);
        configPaints();
    }

    /**
     * 配置画笔
     */
    private void configPaints() {
        // 上方文字画笔属性设置
        configTopTxtPaint();
        // 下方文字画笔属性设置
        configBottomTxtPaint();
        // 下方提示文字画笔属性设置
        configBottomWarnTxtPaint();
        // 节点画笔属性设置
        configNodePaint();
        // 未到达连接线画笔属性设置
        configUnreachedLinePaint();
        // 已到达连接线画笔属性设置
        configReachedLinePaint();
    }

    /**
     * 已到达连接线画笔属性设置
     */
    private void configReachedLinePaint() {
        mPaintReachedLine = new Paint();
        mPaintReachedLine.setColor(mReachedLineColor);
        mPaintReachedLine.setStrokeWidth(mLineWidth);
        mPaintReachedLine.setStyle(Paint.Style.FILL);
        mPaintReachedLine.setAntiAlias(true);
    }

    /**
     * 未到达连接线画笔属性设置
     */
    private void configUnreachedLinePaint() {
        mPaintUnreachedLine = new Paint();
        mPaintUnreachedLine.setColor(mUnreachedLineColor);
        mPaintUnreachedLine.setStrokeWidth(mLineWidth);
        mPaintUnreachedLine.setStyle(Paint.Style.FILL);
        mPaintUnreachedLine.setAntiAlias(true);
    }

    /**
     * 节点画笔属性设置
     */
    private void configNodePaint() {
        mPaintNode = new Paint();
        mPaintNode.setAntiAlias(true);
    }

    /**
     * 下方提示文字画笔属性设置
     */
    private void configBottomWarnTxtPaint() {
        mPaintBottomWarnTxt = new Paint();
        mPaintBottomWarnTxt.setTextSize(mBottomTxtSize);
        mPaintBottomWarnTxt.setColor(mBottomWarnTxtColor);
        mPaintBottomWarnTxt.setTextAlign(Paint.Align.CENTER);
        mPaintBottomWarnTxt.setAntiAlias(true);
        if (TxtStyle.COMMON == mBottomWarnTxtStyle) {
            mPaintBottomWarnTxt.setTypeface(Typeface.DEFAULT);
        } else if (TxtStyle.BOLD == mBottomWarnTxtStyle) {
            mPaintBottomWarnTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (TxtStyle.ITALIC == mBottomWarnTxtStyle) {
            mPaintBottomWarnTxt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
    }

    /**
     * 下方文字画笔属性设置
     */
    private void configBottomTxtPaint() {
        mPaintBottomTxt = new Paint();
        mPaintBottomTxt.setTextSize(mBottomTxtSize);
        mPaintBottomTxt.setColor(mBottomTxtColor);
        mPaintBottomTxt.setTextAlign(Paint.Align.CENTER);
        mPaintBottomTxt.setAntiAlias(true);
        if (TxtStyle.COMMON == mBottomTxtStyle) {
            mPaintBottomTxt.setTypeface(Typeface.DEFAULT);
        } else if (TxtStyle.BOLD == mBottomTxtStyle) {
            mPaintBottomTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (TxtStyle.ITALIC == mBottomTxtStyle) {
            mPaintBottomTxt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
    }

    /**
     * 上方文字画笔属性设置
     */
    private void configTopTxtPaint() {
        mPaintTopTxt = new Paint();
        mPaintTopTxt.setTextSize(mTopTxtSize);
        mPaintTopTxt.setColor(mTopTxtColor);
        mPaintTopTxt.setTextAlign(Paint.Align.CENTER);
        mPaintTopTxt.setAntiAlias(true);
        if (TxtStyle.COMMON == mTopTxtStyle) {
            mPaintTopTxt.setTypeface(Typeface.DEFAULT);
        } else if (TxtStyle.BOLD == mTopTxtStyle) {
            mPaintTopTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (TxtStyle.ITALIC == mTopTxtStyle) {
            mPaintTopTxt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
    }

    /**
     * 配置bitmap
     *
     * @param context
     */
    private void configBitmaps(Context context) {
        Resources resources = context.getResources();
        mNodeUnreachedBitmap = BitmapFactory.decodeResource(resources, R.drawable.node_unreached);
        mNodeReachedBitmap = BitmapFactory.decodeResource(resources, R.drawable.node_reached);
        mNodeFailedBitmap = BitmapFactory.decodeResource(resources, R.drawable.node_failed);
        mNodeFinishedBitmap = BitmapFactory.decodeResource(resources, R.drawable.node_finished);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        if (mTopTxtEnable && mBottomTxtEnable) {// 上下文字都展示
//            mHeight = mTopTxtSize + mTopTxtGap + mNodeHeight + mBottomTxtGap + mBottomTxtSize;
//        } else if (mTopTxtEnable) {// 仅上方文字展示
//            mHeight = mTopTxtSize + mTopTxtGap + mNodeHeight;
//        } else if (mBottomTxtEnable) {// 仅下方文字展示
//            mHeight = mNodeHeight + mBottomTxtGap + mBottomTxtSize;
//        } else {// 不展示上下文字
//            mHeight = mNodeHeight;
//        }
        // 上线各加1dp的余量，防止个别情况下展示不全
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mNodeCount <= 0 || mNodeList.size() != mNodeCount || mNodeList.isEmpty()) {
            return;
        }

        // 初始化位置列表
        initLocationLists();

        // 测量坐标
        measureLocations();

        // 绘制上方文字
        if (mTopTxtEnable) {
            for (int i = 0; i < mNodeCount; i++) {
                Node node = mNodeList.get(i);
                if (TextUtils.isEmpty(node.topTxt)) {
                    continue;
                }
                Paint.FontMetrics metrics = mPaintTopTxt.getFontMetrics();
                int x = mTopTxtLocationList.get(i).x;
                int y = (int) (mTopTxtLocationList.get(i).y + Math.abs(mPaintTopTxt.ascent() + mPaintTopTxt.descent() / 2));
                canvas.drawText(node.topTxt, x, y, mPaintTopTxt);
            }
        }

        // 绘制连线
        for (int i = 0; i < mNodeCount; i++) {
            Node node = mNodeList.get(i);

            if (i == mNodeCount - 1) {
                break;
            }

            int x1 = mNodeLocationList.get(i).x;
            int y1 = mNodeLocationList.get(i).y;
            int x2 = mNodeLocationList.get(i + 1).x;
            int y2 = mNodeLocationList.get(i + 1).y;
            if (Node.LineState.UNREACHED == node.nodeAfterLineState) {
                canvas.drawLine(x1, y1, x2, y2, mPaintUnreachedLine);
            } else if (Node.LineState.REACHED == node.nodeAfterLineState) {
                canvas.drawLine(x1, y1, x2, y2, mPaintReachedLine);
            } else {
                canvas.drawLine(x1, y1, x2, y2, mPaintUnreachedLine);
            }
        }

        // 绘制节点
        for (int i = 0; i < mNodeCount; i++) {
            Node node = mNodeList.get(i);

            int x = mNodeLocationList.get(i).x;
            int y = mNodeLocationList.get(i).y;
            if (Node.NodeState.UNREACHED == node.nodeState) {
                Rect rect = new Rect(0, 0, mNodeUnreachedBitmap.getWidth(), mNodeUnreachedBitmap.getHeight());
                RectF rectF = new RectF(x - mNodeRatio * mNodeWidth / 2, y - mNodeRatio * mNodeHeight / 2, x + mNodeRatio * mNodeWidth / 2, y + mNodeRatio * mNodeHeight / 2);
                canvas.drawBitmap(mNodeUnreachedBitmap, rect, rectF, mPaintNode);
            } else if (Node.NodeState.REACHED == node.nodeState) {
                Rect rect = new Rect(0, 0, mNodeUnreachedBitmap.getWidth(), mNodeUnreachedBitmap.getHeight());
                RectF rectF = new RectF(x - mNodeRatio * mNodeWidth / 2, y - mNodeRatio * mNodeHeight / 2, x + mNodeRatio * mNodeWidth / 2, y + mNodeRatio * mNodeHeight / 2);
                canvas.drawBitmap(mNodeReachedBitmap, rect, rectF, mPaintNode);
            } else if (Node.NodeState.FAILED == node.nodeState) {
                Rect rect = new Rect(0, 0, mNodeUnreachedBitmap.getWidth(), mNodeUnreachedBitmap.getHeight());
                RectF rectF = new RectF(x - 1.0F * mNodeWidth / 2, y - 1.0F * mNodeHeight / 2, x + 1.0F * mNodeWidth / 2, y + mNodeHeight / 2);
                canvas.drawBitmap(mNodeFailedBitmap, rect, rectF, mPaintNode);
            } else if (Node.NodeState.FINISHED == node.nodeState) {
                Rect rect = new Rect(0, 0, mNodeUnreachedBitmap.getWidth(), mNodeUnreachedBitmap.getHeight());
                RectF rectF = new RectF(x - 1.0F * mNodeWidth / 2, y - 1.0F * mNodeHeight / 2, x + 1.0F * mNodeWidth / 2, y + 1.0F * mNodeHeight / 2);
                canvas.drawBitmap(mNodeFinishedBitmap, rect, rectF, mPaintNode);
            }
        }

        // 绘制下方文字
        if (mBottomTxtEnable) {
            for (int i = 0; i < mNodeCount; i++) {
                Node node = mNodeList.get(i);
                if (TextUtils.isEmpty(node.bottomTxt)) {
                    continue;
                }
                int x = mBottomTxtLocationList.get(i).x;
                int y = (int) (mBottomTxtLocationList.get(i).y + Math.abs(mPaintBottomTxt.ascent() + mPaintBottomTxt.descent() / 2));
                if (Node.NodeState.FAILED != node.nodeState) {
                    canvas.drawText(node.bottomTxt, x, y, mPaintBottomTxt);
                } else {
                    canvas.drawText(node.bottomTxt, x, y, mPaintBottomWarnTxt);
                }
            }
        }
    }

    private void initLocationLists() {
        if (mTopTxtLocationList != null) {
            mTopTxtLocationList.clear();
        } else {
            mTopTxtLocationList = new ArrayList<>();
        }

        if (mNodeLocationList != null) {
            mNodeLocationList.clear();
        } else {
            mNodeLocationList = new ArrayList<>();
        }

        if (mBottomTxtLocationList != null) {
            mBottomTxtLocationList.clear();
        } else {
            mBottomTxtLocationList = new ArrayList<>();
        }
    }

    /**
     * 测量元素的中心坐标
     */
    private void measureLocations() {
        if (mNodeCount == 1) {
            // 上方文字的中心坐标
            if (mTopTxtEnable) {
                Location topTxtLoc = new Location();
                topTxtLoc.x = mWidth / 2;
                topTxtLoc.y = mTopTxtSize / 2;
                mTopTxtLocationList.add(topTxtLoc);
            }

            // 节点的中心坐标
            if (mTopTxtEnable) {
                Location nodeLoc = new Location();
                nodeLoc.x = mWidth / 2;
                nodeLoc.y = mTopTxtSize + mTopTxtGap + mNodeHeight / 2;
                mNodeLocationList.add(nodeLoc);
            } else {
                Location nodeLoc = new Location();
                nodeLoc.x = mWidth / 2;
                nodeLoc.y = mNodeHeight / 2;
                mNodeLocationList.add(nodeLoc);
            }

            // 下方文字的中心坐标
            if (mTopTxtEnable && mBottomTxtEnable) {
                Location bottomTxtLoc = new Location();
                bottomTxtLoc.x = mWidth / 2;
                bottomTxtLoc.y = mTopTxtSize + mTopTxtGap + mNodeHeight + mBottomTxtGap + mBottomTxtSize / 2;
                mBottomTxtLocationList.add(bottomTxtLoc);
            } else if (mBottomTxtEnable) {
                Location bottomTxtLoc = new Location();
                bottomTxtLoc.x = mWidth / 2;
                bottomTxtLoc.y = mNodeHeight + mBottomTxtGap + mBottomTxtSize / 2;
                mBottomTxtLocationList.add(bottomTxtLoc);
            }
            return;
        }

        int space = (mWidth - mRegionWidth * mNodeCount) / (mNodeCount - 1);
        for (int i = 0; i < mNodeCount; i++) {
            // 上方文字的中心坐标
            if (mTopTxtEnable) {
                Location topTxtLoc = new Location();
                topTxtLoc.x = mRegionWidth / 2 + i * space + i * mRegionWidth;
                topTxtLoc.y = mTopTxtSize / 2;
                mTopTxtLocationList.add(topTxtLoc);
            }

            // 节点的中心坐标
            if (mTopTxtEnable) {
                Location nodeLoc = new Location();
                nodeLoc.x = mRegionWidth / 2 + i * space + i * mRegionWidth;
                nodeLoc.y = mTopTxtSize + mTopTxtGap + mNodeHeight / 2;
                mNodeLocationList.add(nodeLoc);
            } else {
                Location nodeLoc = new Location();
                nodeLoc.x = mRegionWidth / 2 + i * space + i * mRegionWidth;
                nodeLoc.y = mNodeHeight / 2;
                mNodeLocationList.add(nodeLoc);
            }

            // 下方文字的中心坐标
            if (mTopTxtEnable && mBottomTxtEnable) {
                Location bottomTxtLoc = new Location();
                bottomTxtLoc.x = mRegionWidth / 2 + i * space + i * mRegionWidth;
                bottomTxtLoc.y = mTopTxtSize + mTopTxtGap + mNodeHeight + mBottomTxtGap + mBottomTxtSize / 2;
                mBottomTxtLocationList.add(bottomTxtLoc);
            } else if (mBottomTxtEnable) {
                Location bottomTxtLoc = new Location();
                bottomTxtLoc.x = mRegionWidth / 2 + i * space + i * mRegionWidth;
                bottomTxtLoc.y = mNodeHeight + mBottomTxtGap + mBottomTxtSize / 2;
                mBottomTxtLocationList.add(bottomTxtLoc);
            }
        }
    }

    /**
     * 上方文字是否生效
     *
     * @param mTopTxtEnable
     */
    public void setTopTxtEnable(boolean mTopTxtEnable) {
        this.mTopTxtEnable = mTopTxtEnable;
        invalidate();
    }

    /**
     * 下方文字是否生效
     *
     * @param mBottomTxtEnable
     */
    public void setBottomTxtEnable(boolean mBottomTxtEnable) {
        this.mBottomTxtEnable = mBottomTxtEnable;
        invalidate();
    }

    /**
     * 设置节点信息
     *
     * @param mNodeList
     */
    public void setNodeList(@NonNull List<Node> mNodeList) {
        this.mNodeList = mNodeList;
        this.mNodeCount = mNodeList.size();
        invalidate();
    }

    /**
     * 中心坐标
     */
    private static class Location {
        int x;
        int y;
    }

    /**
     * 节点对象
     */
    public static class Node {
        public interface NodeState {
            int UNREACHED = 1;
            int REACHED = 2;
            int FINISHED = 3;
            int FAILED = 4;
        }

        public interface LineState {
            int REACHED = 0;
            int UNREACHED = 1;
        }

        // 节点上方文字
        public String topTxt;
        // 节点下方文字
        public String bottomTxt;
        // 节点状态
        public int nodeState;
        // 节点后连线状态
        public int nodeAfterLineState;
    }

    /**
     * 字体
     */
    public interface TxtStyle {
        int COMMON = 0;
        int BOLD = 1;
        int ITALIC = 2;
    }
}
