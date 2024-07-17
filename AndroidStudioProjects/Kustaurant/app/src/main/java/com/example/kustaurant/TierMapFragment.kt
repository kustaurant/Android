package com.example.kustaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kustaurant.databinding.FragmentTierMapBinding

class TierMapFragment : Fragment() {

    private lateinit var binding: FragmentTierMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierMapBinding.inflate(inflater, container, false)
        //setupMap()
        return binding.root
    }

}