package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.dialog.PaymentFailDialog;
import com.partypeople.www.partypeople.dialog.PaymentResultDialog;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.NumberUtil;

public class ParticipateActivity extends AppCompatActivity {
    Party party;
    ListView listView;
    RewordViewAdapter mAdapter;
    TextView textView, textReword;
    EditText editName, editTel, editPrice;
    CheckBox checkBox;
    LinearLayout layout;
    RelativeLayout relativeLayout;
    int price;
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
                if(editName.getText().toString().equals("")) {
                    Toast.makeText(ParticipateActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editTel.getText().toString().length() < 8) {
                    Toast.makeText(ParticipateActivity.this, "전화번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkBox.isChecked()) {
                    Toast.makeText(ParticipateActivity.this, "이용약관에 동의해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(party.amount_custom && price < party.amount_method.get(0).price) {
                    Toast.makeText(ParticipateActivity.this, "결제 금액은 최소금액보다 커야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(price > 5000000) {
                    Toast.makeText(ParticipateActivity.this, "결제 금액은 500만원 미만이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ParticipateActivity.this, PaymentActivity.class);
                intent.putExtra("party", party);
                intent.putExtra("selected", selected);
                intent.putExtra("price", String.valueOf(price));
                intent.putExtra("tel", editTel.getText().toString());
                intent.putExtra("name", editName.getText().toString());
                startActivityForResult(intent, Constants.REQUEST_CODE_PAYMENT);
            }
        });

        layout = (LinearLayout)findViewById(R.id.root_layout);
        TextView payTitle = (TextView)findViewById(R.id.text_pay_title);
        if(party.amount_method.size()==1 && !party.amount_custom) {
            payTitle.setText("고정금액");
        } else if(party.amount_method.size()==1 && party.amount_custom) {
            payTitle.setText("최소금액");
        } else if(party.amount_method.size()>1) {
            payTitle.setText("포함사항별 선택");
        }

        listView = (ListView)findViewById(R.id.listView);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(0, true);

        textView = (TextView)findViewById(R.id.text_payment);
        price = party.amount_method.get(0).price;
        textView.setText(NumberUtil.getInstance().changeToPriceForm(price));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                price = party.amount_method.get(position).price;
                textView.setText(NumberUtil.getInstance().changeToPriceForm(price));
                selected = position;
            }
        });

        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        textReword = (TextView)findViewById(R.id.text_reword);
        editPrice = (EditText)findViewById(R.id.edit_price);
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editPrice.getText().toString().equals("")) {
                    price = 0;
                } else {
                    price = Integer.parseInt(editPrice.getText().toString());
                }
                textView.setText(NumberUtil.getInstance().changeToPriceForm(price));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        checkBox = (CheckBox)findViewById(R.id.checkBox);
        editName = (EditText)findViewById(R.id.edit_name);
        editName.setText(PropertyManager.getInstance().getUser().name);
        editTel = (EditText)findViewById(R.id.edit_tel);
        editTel.setText(String.format("%d", (long)PropertyManager.getInstance().getUser().tel));

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
        if(party.amount_custom) {
            relativeLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            editPrice.setText(party.amount_method.get(0).price+"");
            textReword.setText(party.amount_method.get(0).title);
        } else {
            relativeLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            for (int i = 0; i < party.amount_method.size(); i++) {
                mAdapter.add(party.amount_method.get(i));
            }
        }

        setHeight(1000 * party.amount_method.size());
        if(party.amount_custom) {
            ViewTreeObserver vto = relativeLayout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = relativeLayout.getMeasuredHeight();
                    setHeight(height);
                }
            });
        } else {
            ViewTreeObserver vto = listView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = listView.getMeasuredHeight();
                    setHeight(height);
                }
            });
        }
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
            String response = data.getStringExtra("response");
            if(result) {
                setResult(Constants.RESULT_CODE_PARTICIPATE);
                PaymentResultDialog dialog = new PaymentResultDialog(ParticipateActivity.this, response, getSupportFragmentManager());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
                PaymentFailDialog dialog = new PaymentFailDialog(ParticipateActivity.this, response, getSupportFragmentManager());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height=height;
        layout.setLayoutParams(params);
    }
}
