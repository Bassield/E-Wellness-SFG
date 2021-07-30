package com.sfg.EWellnessSFG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.sfg.EWellnessSFG.adapter.MessageAdapter;
import com.sfg.EWellnessSFG.model.Message;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import EWellnessSFG.R;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CollectionReference MessageRef1;
    private CollectionReference MessageRef2;
    private MessageAdapter adapter;
    private TextInputEditText envoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
        MessageRef1 = FirebaseFirestore.getInstance().collection("chat").document(extras.getString("key1")).collection("message");
        MessageRef2 = FirebaseFirestore.getInstance().collection("chat").document(extras.getString("key2")).collection("message");
        envoyer = (TextInputEditText) findViewById(R.id.activity_mentor_chat_message_edit_text);
        Button btnEnvoyer = (Button) findViewById(R.id.activity_mentor_chat_send_button);
        setUpRecyclerView();
        btnEnvoyer.setOnClickListener(v -> {
            Message msg = new Message(envoyer.getText().toString(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            MessageRef1.document().set(msg);
            MessageRef2.document().set(msg);
            envoyer.setText("");
        });
    }

    private void setUpRecyclerView() {
        Query query = MessageRef1.orderBy("dateCreated");
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        adapter = new MessageAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.activity_mentor_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
