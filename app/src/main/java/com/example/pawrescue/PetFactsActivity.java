package com.example.pawrescue;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetFactsActivity extends AppCompatActivity {
    private TextView tvFactCat;
    private TextView tvFactDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_facts);

        tvFactCat = findViewById(R.id.tvFactCat);
        tvFactDog = findViewById(R.id.tvFactDog);
        Button btnGiveFact = findViewById(R.id.btnGiveFact);

        btnGiveFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCatFact();
                fetchDogFact();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
    private void fetchCatFact() {
        CatFactService catFactService = RetrofitClient.getRetrofitInstance().create(CatFactService.class);

        Call<PetFact> call = catFactService.getRandomCatFact();
        call.enqueue(new Callback<PetFact>() {
            @Override
            public void onResponse(Call<PetFact> call, Response<PetFact> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String catFactText = response.body().getText();

                    if (isAlphanumericWithSpacesAndPunctuation(catFactText)) {
                        tvFactCat.setText(getString(R.string.cat) + catFactText);
                    } else {
                        fetchCatFact();
                    }
                }
            }

            @Override
            public void onFailure(Call<PetFact> call, Throwable t) {
                Toast.makeText(PetFactsActivity.this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDogFact() {
        DogFactService dogFactService = RetrofitClient.getRetrofitInstance().create(DogFactService.class);

        Call<PetFact> call = dogFactService.getRandomDogFact();
        call.enqueue(new Callback<PetFact>() {
            @Override
            public void onResponse(Call<PetFact> call, Response<PetFact> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String dogFactText = response.body().getText();

                    if (isAlphanumericWithSpacesAndPunctuation(dogFactText)) {
                        tvFactDog.setText(getString(R.string.dog) + dogFactText);
                    } else {
                        fetchDogFact();
                    }
                }
            }

            @Override
            public void onFailure(Call<PetFact> call, Throwable t) {
                Toast.makeText(PetFactsActivity.this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isAlphanumericWithSpacesAndPunctuation(String text) {
        return text.matches("^[a-zA-Z\\s\\p{Punct}]+$");
    }

}
