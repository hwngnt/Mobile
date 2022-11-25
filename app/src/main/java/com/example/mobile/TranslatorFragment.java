package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobile.databinding.FragmentTranslatorBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


public class TranslatorFragment extends Fragment {
    FragmentTranslatorBinding binding;
    EditText input;
    TextView res;

    Translator translatorGerman;
    Translator translatorArabic;
    Translator translatorKorean;

    boolean check_german = false;
    boolean check_arabic = false;
    boolean check_korean = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTranslatorBinding.inflate(inflater,container,false);

        input = binding.inputText;
        res = binding.result;

        TranslatorOptions translatorOptionsGerman = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();
        TranslatorOptions translatorOptionsArabic = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();
        TranslatorOptions translatorOptionsKorean = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        translatorGerman = Translation.getClient(translatorOptionsGerman);
        translatorArabic = Translation.getClient(translatorOptionsArabic);
        translatorKorean = Translation.getClient(translatorOptionsKorean);
        binding.german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_german)
                translatorGerman.translate(input.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                res.setText(s);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                res.setText(e.toString());
                            }
                        });
                else {
                    check_german = true;
                    translatorGerman.downloadModelIfNeeded()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(),"Download Successful",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        binding.arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_arabic)
                translatorArabic.translate(input.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                res.setText(s);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                res.setText(e.toString());
                            }
                        });
                else{
                    check_arabic = true;
                    translatorArabic.downloadModelIfNeeded()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Download Successful",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



        binding.korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_korean)
                translatorKorean.translate(input.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                res.setText(s);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                res.setText(e.toString());
                            }
                        });
                else{
                    check_korean = true;
                    translatorKorean.downloadModelIfNeeded()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(),"Download Successful",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.mainFragment);
            }
        });


        return binding.getRoot();
    }
}