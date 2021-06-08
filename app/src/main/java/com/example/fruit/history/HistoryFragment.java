package com.example.fruit.history;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;

//public class HistoryFragment extends Fragment {
//
//    private WebView mHistoryRes;
//    private String mURL;
//    private MainActivity mActivity;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.history_fragment, container, false);
//
//        return view;
//    }
//}

public class HistoryFragment extends Fragment {

    private String[] data = {"肖申克的救赎", "这个杀手不太冷", "霸王别姬", "泰坦尼克号", "瓦力",
            "三傻大闹宝莱坞", "放牛班的春天", "千与千寻", "鬼子来了", "星际穿越"};

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data
//        );
//        ListView listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//    }
}

