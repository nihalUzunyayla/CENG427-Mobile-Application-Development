package com.example.pawrescue;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class PetModalDetails extends Dialog {

    private ImageView modalImageViewPet;
    private TextView modalTextViewName,modalTextViewType,modalTextViewAge,modalTextViewGender,modalTextViewState;

    private Button btnAdopt;
    public PetModalDetails(@NonNull Context context) {

        super(context);
    }

    public void initialize() {
        setContentView(R.layout.pet_modal_details);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EDE7F6")));

        modalImageViewPet = (ImageView) findViewById(R.id.modalImageViewPet);
        modalTextViewName = (TextView) findViewById(R.id.modalTextViewName);
        modalTextViewType = (TextView)findViewById(R.id.modalTextViewType);
        modalTextViewAge = (TextView)findViewById(R.id.modalTextViewAge);
        modalTextViewGender = (TextView)findViewById(R.id.modalTextViewGender);
        modalTextViewState = (TextView)findViewById(R.id.modalTextViewState);
        btnAdopt = findViewById(R.id.btn_adopt);

        btnAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                showAdoptionConfirmationDialog();
            }
        });
    }
    private void showAdoptionConfirmationDialog() {
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customView);

        TextView alertBoldTextView = customView.findViewById(R.id.alertBoldTextView);
        TextView alertNormalTextView = customView.findViewById(R.id.alertNormalTextView);

        String boldText =  getContext().getString(R.string.alertDiagramBoldText);
        String normalText = getContext().getString(R.string.alertDiagramAddressText);

        alertBoldTextView.setText(boldText);
        alertNormalTextView.setText(normalText);

        builder.setPositiveButton(getContext().getString(R.string.ok), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setPetDetails(String name, String type, int age, String gender, String state, Bitmap image) {

            modalTextViewName.setText(String.valueOf(name));
            modalTextViewType.setText(String.valueOf(type));
            modalTextViewAge.setText(String.valueOf(age));
            modalTextViewGender.setText(String.valueOf(gender));
            modalTextViewState.setText(String.valueOf(state));
            modalImageViewPet.setImageBitmap(image);

    }
}
