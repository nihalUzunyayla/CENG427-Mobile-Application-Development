package com.example.pawrescue.ui.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pawrescue.AddPetActivity;
import com.example.pawrescue.PetFactsActivity;
import com.example.pawrescue.DonationActivity;
import com.example.pawrescue.EmergencyActivity;
import com.example.pawrescue.R;
import com.example.pawrescue.databinding.FragmentMainMenuBinding;

public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainMenuViewModel mainMenuViewModel =
                new ViewModelProvider(this).get(MainMenuViewModel.class);

        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewMainMenu;
        mainMenuViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        AppCompatButton addPetButton = (AppCompatButton) root.findViewById(R.id.buttonAddPet);
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddPetActivity.class));
            }
        });

        AppCompatButton btn_donate = (AppCompatButton) root.findViewById(R.id.btn_donate);
        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DonationActivity.class));
            }
        });

        AppCompatButton btn_emergency = (AppCompatButton) root.findViewById(R.id.btn_emergency);
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EmergencyActivity.class));
            }
        });

        AppCompatButton btn_fact = (AppCompatButton) root.findViewById(R.id.btn_fact);
        btn_fact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PetFactsActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}