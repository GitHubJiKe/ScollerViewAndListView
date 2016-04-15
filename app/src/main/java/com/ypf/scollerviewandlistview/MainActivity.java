package com.ypf.scollerviewandlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListViewForScrollView listView;
    ArrayList<String> data;
    // 最后可见条目的索引
    private int lastVisibleIndex;
    // 设置一个最大的数据条数，超过即不再加载
    private int MaxDateNum;
    private ArrayAdapter adapter;
    ScrollView sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaxDateNum = 220; // 设置最大数据条数
        listView = (ListViewForScrollView) findViewById(R.id.listview);//在布局中一定要指定高度，要不然填充满父布局后，无法滑动
        sc = (ScrollView) findViewById(R.id.sc);
        sc.smoothScrollTo(0, 0);//使得scrollview处于视图的首位
        data = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            data.add("data" + i);
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastVisibleIndex == (adapter.getCount() - 1)) {
                    // 当滑到底部时自动加载
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int a = lastVisibleIndex + 10;
                            if (a < 220) {
                                for (int i = lastVisibleIndex; i <= a; i++) {
                                    data.add("data" + i);
                                    Log.d("TAG", "i = " + i);
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 计算最后可见条目的索引
                lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                // 所有的条目已经和最大条数相等，则移除底部的View
                if (totalItemCount >= MaxDateNum + 1) {
                    Toast.makeText(MainActivity.this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
