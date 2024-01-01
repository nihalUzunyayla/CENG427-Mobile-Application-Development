package com.example.pawrescue;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CatFactService {
    @GET("facts/random?animal_type=cat&amount=1")
    Call<PetFact> getRandomCatFact();
}