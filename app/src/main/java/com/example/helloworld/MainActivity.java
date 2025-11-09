package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_UNREAD = "unread";

    private ListView listViewChats;
    private ArrayList<HashMap<String, Object>> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewChats = findViewById(R.id.listViewChats);

        chatList = new ArrayList<>();
        populateChatList();

        String[] from = {KEY_AVATAR, KEY_NAME, KEY_TIME, KEY_MESSAGE, KEY_UNREAD};
        int[] to = {R.id.iv_avatar, R.id.tv_name, R.id.tv_time, R.id.tv_message_preview, R.id.tv_unread_count};

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                chatList,
                R.layout.chat_item,
                from,
                to
        );

        adapter.setViewBinder(new MyViewBinder());

        listViewChats.setAdapter(adapter);

        listViewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> chat = chatList.get(position);
                String name = (String) chat.get(KEY_NAME);

                Toast.makeText(MainActivity.this, "Відкриття чату з " + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateChatList() {
        HashMap<String, Object> chat1 = new HashMap<>();
        chat1.put(KEY_AVATAR, android.R.drawable.ic_dialog_info);
        chat1.put(KEY_NAME, "Олена Коваленко");
        chat1.put(KEY_TIME, "14:20");
        chat1.put(KEY_MESSAGE, "Привіт! Як справи?");
        chat1.put(KEY_UNREAD, 3);
        chatList.add(chat1);

        HashMap<String, Object> chat2 = new HashMap<>();
        chat2.put(KEY_AVATAR, android.R.drawable.ic_dialog_map);
        chat2.put(KEY_NAME, "Work Chat");
        chat2.put(KEY_TIME, "13:05");
        chat2.put(KEY_MESSAGE, "Дедлайн сьогодні о 18:00!");
        chat2.put(KEY_UNREAD, 0);
        chatList.add(chat2);

        HashMap<String, Object> chat3 = new HashMap<>();
        chat3.put(KEY_AVATAR, android.R.drawable.ic_dialog_email);
        chat3.put(KEY_NAME, "Мама");
        chat3.put(KEY_TIME, "12:15");
        chat3.put(KEY_MESSAGE, "Не забудь купити хліб.");
        chat3.put(KEY_UNREAD, 1);
        chatList.add(chat3);

        HashMap<String, Object> chat4 = new HashMap<>();
        chat4.put(KEY_AVATAR, android.R.drawable.ic_dialog_dialer);
        chat4.put(KEY_NAME, "Андрій (Доставка)");
        chat4.put(KEY_TIME, "Вчора");
        chat4.put(KEY_MESSAGE, "Буду у вас за 10 хвилин.");
        chat4.put(KEY_UNREAD, 0);
        chatList.add(chat4);

        HashMap<String, Object> chat5 = new HashMap<>();
        chat5.put(KEY_AVATAR, android.R.drawable.ic_menu_myplaces);
        chat5.put(KEY_NAME, "Друзі 🚀");
        chat5.put(KEY_TIME, "Вчора");
        chat5.put(KEY_MESSAGE, "Макс: Поїхали на вихідних на природу?");
        chat5.put(KEY_UNREAD, 12);
        chatList.add(chat5);
    }

    private class MyViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.tv_unread_count) {
                TextView unreadCountView = (TextView) view;
                int unreadCount = (Integer) data;

                if (unreadCount > 0) {
                    unreadCountView.setText(String.valueOf(unreadCount));
                    unreadCountView.setVisibility(View.VISIBLE);
                } else {
                    unreadCountView.setVisibility(View.GONE);
                }
                return true;
            }

            return false;
        }
    }
}