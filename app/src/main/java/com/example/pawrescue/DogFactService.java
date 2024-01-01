package com.example.pawrescue;

import retrofit2.Call;
import retrofit2.http.GET;
public interface DogFactService {
    @GET("facts/random?animal_type=dog&amount=1")
    Call<PetFact> getRandomDogFact();
}
