package com.example.foodiesmailru.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.databinding.FragmentSearchBinding
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.menu.ProductsRVAdapter
import java.util.*

class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductsRVAdapter
    val searchResultList = mutableListOf<Product>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    private fun bind(){
        recyclerView = binding.recyclerView
        adapter = ProductsRVAdapter(model,-1,searchResultList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context,2)
        }
    }
    private fun setClickListeners(){
        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String): Boolean {
//                //todo(сделать поиск по свойствам и тэгам)
                searchResultList.clear()
                val searchInput = binding.searchBar.query
                searchInput.replace("\\s".toRegex(),"").lowercase(Locale.getDefault())
                for (product in model.products.value!!){
                    if (searchInput.contains(product.name.lowercase(Locale.getDefault())) or product.name.lowercase(Locale.getDefault()).contains(searchInput)){
                        searchResultList.add(product)
                    }
                    else if (product.description.lowercase(Locale.getDefault()).contains(searchInput))
                    {
                      searchResultList.add(product)
                    }
                }
                recyclerView.adapter!!.notifyDataSetChanged()
                binding.searchBar.clearFocus()
                return true
            }
        })
    }
}