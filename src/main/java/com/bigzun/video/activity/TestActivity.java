package com.bigzun.video.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.model.ChannelYoutube;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by namnh40 on 12/23/2016.
 */

public class TestActivity extends BaseActivity {

    DatabaseReference mFilebaseDatabase;

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.edt_channel_id)
    EditText editId;
    @BindView(R.id.edt_channel_name)
    EditText editName;

    @OnClick(R.id.button_submit)
    public void onButtonSubmitClick() {
        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getBaseContext(), "Tên kênh không được để trống!");
        } else {
            Random random = new Random();
            String id = random.nextLong() + "abc";
            editId.setText(id);
            saveDB(id, name);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        ButterKnife.setDebug(BuildConfig.DEBUG);
        mFilebaseDatabase = FirebaseDatabase.getInstance().getReference();
        loadDB();
    }

    private void loadDB() {
        DatabaseReference dbRef = mFilebaseDatabase.child("channels").child("top_channels");
        FirebaseDatabase dbDB = dbRef.getDatabase();
        if (dbDB != null) {
            Log.e(TAG, "db: " + dbDB.toString());
        }
    }

    private void saveDB(String channel_Id, String channel_name) {
        DatabaseReference db = mFilebaseDatabase.child("channels").child("top_channels");
        String key = db.push().getKey();
//        mFilebaseDatabase.child("channels").push().setValue(new ChannelYoutube(channel_Id,channel_name));
//        String key = mFilebaseDatabase.child("channels").push().getKey();
        ChannelYoutube item = new ChannelYoutube(channel_Id, channel_name);
        Map<String, Object> valueChild = item.toMap();
        Map<String, Object> keyChild = new HashMap<>();
        keyChild.put(key, valueChild);
        db.updateChildren(keyChild);
        //db.setValue(keyChild);
    }

    private void loadDatabaseFromFilebase() {
        mFilebaseDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference channelDB = mFilebaseDatabase.child("channels");

        channelDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "addValueEventListener ----------------");
                ArrayList<ChannelYoutube> list = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                    list.add(item);
                }

                for (ChannelYoutube item : list) {
                    Log.e(TAG, "onDataChange item: " + item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        channelDB.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "addChildEventListener ----------------");
                        ArrayList<ChannelYoutube> list = new ArrayList<>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                            list.add(item);
                        }

                        for (ChannelYoutube item : list) {
                            Log.e(TAG, "onChildAdded item: " + item);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "addChildEventListener ----------------");
                        ArrayList<ChannelYoutube> list = new ArrayList<>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                            list.add(item);
                        }

                        for (ChannelYoutube item : list) {
                            Log.e(TAG, "onChildChanged item: " + item);
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "addChildEventListener ----------------");
                        ArrayList<ChannelYoutube> list = new ArrayList<>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                            list.add(item);
                        }

                        for (ChannelYoutube item : list) {
                            Log.e(TAG, "onChildRemoved item: " + item);
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "addChildEventListener ----------------");
                        ArrayList<ChannelYoutube> list = new ArrayList<>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                            list.add(item);
                        }

                        for (ChannelYoutube item : list) {
                            Log.e(TAG, "onChildMoved item: " + item);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        channelDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "addListenerForSingleValueEvent ----------------");
                ArrayList<ChannelYoutube> list = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    ChannelYoutube item = noteDataSnapshot.getValue(ChannelYoutube.class);
                    list.add(item);
                }

                for (ChannelYoutube item : list) {
                    Log.e(TAG, "onDataChange item: " + item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
