package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.List;

public class ParticipateActivity extends AppCompatActivity {
    Party party;
    ListView listView;
    RewordViewAdapter mAdapter;
    TextView textView;
    EditText editName, editTel;
    int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);

        party = (Party)getIntent().getSerializableExtra("party");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        Button btn = (Button)findViewById(R.id.btn_pay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().equals("")) {
                    Toast.makeText(ParticipateActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ParticipateActivity.this, PaymentActivity.class);
                intent.putExtra("party", party);
                intent.putExtra("selected", selected);
                intent.putExtra("tel", editTel.getText().toString());
                intent.putExtra("name", editName.getText().toString());
                startActivityForResult(intent, Constants.REQUEST_CODE_PAYMENT);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(0, true);

        textView = (TextView)findViewById(R.id.text_payment);
        textView.setText(party.amount_method.get(0).price + " 원");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(party.amount_method.get(position).price + " 원");
                selected = position;
            }
        });

        editName = (EditText)findViewById(R.id.edit_name);
        editName.setText(PropertyManager.getInstance().getUser().name);
        editTel = (EditText)findViewById(R.id.edit_tel);
        editTel.setText(Double.toString(PropertyManager.getInstance().getUser().tel));

        TextView textBtn = (TextView)findViewById(R.id.text_btn_tos);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipateActivity.this, NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_TOS);
                startActivity(intent);
            }
        });
        textBtn = (TextView)findViewById(R.id.text_btn_policy);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipateActivity.this, NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_POLICY);
                startActivity(intent);
            }
        });

        initData();
    }

    private void initData() {
        for(int i=0; i<party.amount_method.size(); i++) {
            mAdapter.add(party.amount_method.get(i));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int)Math.ceil(75 * party.amount_method.size() * getResources().getDisplayMetrics().density)
                + (int)Math.ceil(7 * party.amount_method.size()-1 * getResources().getDisplayMetrics().density);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE_PAYMENT && resultCode==Constants.RESULT_CODE_PAYMENT) {
            boolean result = data.getBooleanExtra("result", false);
            if(result) {
                Toast.makeText(ParticipateActivity.this, "결제 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ParticipateActivity.this, "결제 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
