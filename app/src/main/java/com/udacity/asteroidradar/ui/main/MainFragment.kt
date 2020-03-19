package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var navController: NavController
    lateinit var adapter: AsteroidAdapter
    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.statusLoadingWheel.visibility = View.VISIBLE
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)


        adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { data ->
            val bundle = bundleOf("selectedAsteroid" to data)
            navController.navigate(R.id.detailFragment, bundle)
        })


        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.asteroidRecycler.layoutManager = layoutManager


        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            binding.statusLoadingWheel.visibility = View.GONE
            it.let {
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })


        viewModel.imageOfDay.observe(viewLifecycleOwner, Observer {
            it.let {
                Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
                binding.picture = it
            }
        })


        binding.asteroidRecycler.adapter = adapter
        binding.viewModel = viewModel


    }
}
