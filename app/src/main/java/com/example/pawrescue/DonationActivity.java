package com.example.pawrescue;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonationActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextPhone, editTextCard, editTextName, editTextCVV, editTextExpiration;
    private Button buttonDonate;
    private String confirmationCode;
    private Snackbar snackbar;
    private boolean snackbarShown = false;
    private boolean snackbarActionClicked = false;
    private CountDownTimer countdownTimer;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCard = findViewById(R.id.editTextCard);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextExpiration = findViewById(R.id.editTextExpiration);

        editTextExpiration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidDate(inputText)) {
                    editTextExpiration.setError(getString(R.string.valid_date));
                } else {
                    editTextExpiration.setError(null);
                }
            }
        });

        editTextCVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidCVV(inputText)) {
                    editTextCVV.setError(getString(R.string.valid_cvv));
                } else {
                    editTextCVV.setError(null);
                }
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidPhoneNumber(inputText)) {
                    editTextPhone.setError(getString(R.string.valid_phone));
                } else {
                    editTextPhone.setError(null);
                }
            }
        });

        editTextCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidCardNumber(inputText)) {
                    editTextCard.setError(getString(R.string.valid_card_number));
                } else {
                    editTextCard.setError(null);
                }
            }
        });

        buttonDonate = findViewById(R.id.buttonDonate);
        buttonDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    // Reset the flag before sending a new confirmation code
                    snackbarActionClicked = false;
                    sendConfirmationCode();
                    showConfirmationDialog();
                } else {
                    Toast.makeText(DonationActivity.this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean validateFields() {
        String amount = editTextAmount.getText().toString();
        String phoneNumber = editTextPhone.getText().toString();
        String card = editTextCard.getText().toString();
        String cvv = editTextCVV.getText().toString();
        String expiration = editTextExpiration.getText().toString();

        if (!isValidPhoneNumber(phoneNumber)) {
            editTextPhone.setError(getString(R.string.valid_phone));
            return false;
        } else {
            editTextPhone.setError(null);
        }

        if (!isValidCardNumber(card)) {
            editTextCard.setError(getString(R.string.valid_card_number));
            return false;
        } else {
            editTextCard.setError(null);
        }

        if (!isValidDate(expiration)) {
            editTextExpiration.setError(getString(R.string.valid_date));
            return false;
        } else {
            editTextExpiration.setError(null);
        }

        if (!isValidCVV(cvv)) {
            editTextCVV.setError(getString(R.string.valid_cvv));
            return false;
        } else {
            editTextCVV.setError(null);
        }

        return !amount.isEmpty() && !card.isEmpty() && !cvv.isEmpty() && !expiration.isEmpty();
    }

    private String generateRandomCode() {
        Random random = new Random();
        int confirmationCode = 1000 + random.nextInt(9000);
        return String.valueOf(confirmationCode);
    }

    private void sendConfirmationCode() {
        confirmationCode = generateRandomCode();

        if (countdownTimer != null) {
            countdownTimer.cancel(); // Cancel the previous countdown timer
        }

        final long durationMillis = 60000; // 60 seconds
        countdownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                String remainingTime = String.format(Locale.getDefault(), getString(R.string.time) + "%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
                if (!snackbarActionClicked) {
                    showConfirmationSnackbar(remainingTime);
                } else {
                    dismissSnackbar();
                }
            }

            @Override
            public void onFinish() {
                dismissSnackbar();
                Toast.makeText(DonationActivity.this, getString(R.string.time_up), Toast.LENGTH_SHORT).show();
                snackbarShown = false; // Reset the flag when the timer finishes

                // Automatically close the dialog when the timer finishes
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }.start();
    }

    private void showConfirmationSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);

        if (!snackbarShown) { // Snackbar zaten gösteriliyorsa yeni bir tane oluşturma
            snackbar = Snackbar.make(rootView, getString(R.string.code) + confirmationCode + " - " + message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            snackbarShown = false;
                            snackbarActionClicked=true;
                        }
                    });

            snackbar.show();
            snackbarShown = true; // Snackbar gösterildiğinde boolean'ı güncelle
        } else {
            snackbar.setText(getString(R.string.code) + confirmationCode + " - " + message);
        }
    }

    private void dismissSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            snackbarShown = false; // Kapatıldığında boolean'ı sıfırla
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirmation_title));
        builder.setMessage(getString(R.string.confirmation));

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.approve), (dialog, which) -> {
            String userEnteredCode = input.getText().toString();
            checkConfirmationCode(userEnteredCode);
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            builder.setCancelable(true);
        });

        builder.setCancelable(false); // Kullanıcı dışarı tıklayarak dialogu kapatamasın
        dialog = builder.create();
        dialog.show();

        // Sol altta göstermek için işlem yapıyoruz
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setGravity(Gravity.START | Gravity.BOTTOM);
    }

    private void checkConfirmationCode(String userEnteredCode) {
        if (userEnteredCode.equals(confirmationCode)) {
            dismissSnackbar();
            Toast.makeText(DonationActivity.this, getString(R.string.thank), Toast.LENGTH_SHORT).show();
            finish(); // Ekranı kapat
        } else {
            Toast.makeText(DonationActivity.this, getString(R.string.wrong_code), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(String date) {
        String regex = "^(0[1-9]|1[0-2])/(202[4-9]|203[0-5])$";// MM/YYYY formatı
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    private boolean isValidCVV(String cvv) {
        String regex = "\\d{3}"; // 3 haneli olacak
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cvv);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[1-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidCardNumber(String cardNumber) {
        String regex = "^\\d{16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardNumber);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
