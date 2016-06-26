package xyz.vinesh.creditcardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vineshraju on 24/6/16.
 */
public class CreditCardView extends CardView {

    TextView number, name, expiry;
    ImageView logo;
    Context context;

    CardTypes cardTypes;


    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initFromXML(attrs);
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initFromXML(attrs);
    }

    public CreditCardView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initFromXML(AttributeSet attrs) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CreditCardView,
                0, 0);

        try {

            String sName = attributes.getString(R.styleable.CreditCardView_cardHolderName) != null ? attributes.getString(R.styleable.CreditCardView_cardHolderName) : "XXXXXX XXXX";
            String sNumber = attributes.getString(R.styleable.CreditCardView_cardNumber) != null ? attributes.getString(R.styleable.CreditCardView_cardNumber) : "XXXXXXXXXXXXXXXX";
            String sExpiry = attributes.getString(R.styleable.CreditCardView_expiry) != null ? attributes.getString(R.styleable.CreditCardView_expiry) : "MM/YY";
            String sCvv = attributes.getString(R.styleable.CreditCardView_cvv) != null ? attributes.getString(R.styleable.CreditCardView_cvv) : "XXX";

            int color = attributes.getColor(R.styleable.CreditCardView_cardColor, Color.WHITE);

            number.setText(introduceGaps(sNumber));
            name.setText(sName);
            expiry.setText(sExpiry);

            refreshLogo(sNumber);

        } finally {
            attributes.recycle();
        }
    }

    public void setCardTypes(CardTypes cardTypes) {
        this.cardTypes = cardTypes;
    }

    private void initView() {
        cardTypes = new CardTypes();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.credit_card_view, this, true);

        number = (TextView) view.findViewById(R.id.tvCardNumber);
        name = (TextView) view.findViewById(R.id.tvName);
        expiry = (TextView) view.findViewById(R.id.tvExpiry);
        logo = (ImageView) view.findViewById(R.id.ivLogo);

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshLogo(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void refreshLogo(CharSequence s) {
        boolean matched = false;
        ArrayList<CardTypes.PatternResourcePairs> cardTypes = CreditCardView.this.cardTypes.getCardTypes();
        for (CardTypes.PatternResourcePairs cardType : cardTypes) {
            if (cardType.matches(s.toString())) {
                matched = true;
                logo.setImageResource(cardType.getLogoResource());
                break;
            }
            if (!matched) logo.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public String getExpiry() {
        return expiry.getText().toString();
    }

    public void setExpiry(String expiry) {
        this.expiry.setText(expiry);
    }

    public ImageView getLogo() {
        return logo;
    }

    public void setLogo(ImageView logo) {
        this.logo = logo;
    }

    public String getName() {
        return name.getText().toString();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getNumber() {
        return number.getText().toString();
    }

    public void setNumber(String number) {
        this.number.setText(introduceGaps(number));
    }

    private String introduceGaps(String nonGappedNumber) {
        int count = 0;
        String gappedNumber = "";
        for (char c : nonGappedNumber.toCharArray()) {
            if (count < 4) {
                gappedNumber += c;
                count++;
            } else {
                gappedNumber = gappedNumber + " " + c;
                count = 1;
            }
        }
        return gappedNumber;
    }
}
